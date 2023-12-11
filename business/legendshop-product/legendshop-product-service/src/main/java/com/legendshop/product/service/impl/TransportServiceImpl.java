/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.dto.CityDTO;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dao.TransportDao;
import com.legendshop.product.dto.*;
import com.legendshop.product.entity.Transport;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.enums.TransFreeTypeEnum;
import com.legendshop.product.enums.TransTypeEnum;
import com.legendshop.product.query.TransportQuery;
import com.legendshop.product.service.*;
import com.legendshop.product.service.convert.TransportConverter;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 运费模板ServiceImpl.
 *
 * @author legendshop
 */
@Service
public class TransportServiceImpl implements TransportService {

	@Autowired
	protected Validator validator;

	@Autowired
	private TransportDao transportDao;

	@Autowired
	private TransFeeService transFeeService;

	@Autowired
	private TransFreeService transFreeService;

	@Autowired
	private TransConstFeeService transConstFeeService;

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private TransportConverter transportConverter;

	@Autowired
	private LocationApi locationApi;

	@Resource(name = "transportFreePostageHandler")
	private AbstractTransportHandler transportHandler;

	@Autowired
	private ProductService productService;


	@Override
	public TransportBO getById(Long id) {
		return transportConverter.convert2BO(transportDao.getById(id));
	}

	@Override
	public Long getByName(String name, Long id) {
		return transportDao.getByName(name, id);
	}

	@Override
	public TransportBO getTransport(Long id) {
		TransportBO transportBO = transportConverter.convert2BO(transportDao.getById(id));
		if (null != transportBO) {
			TransportBO result = transportHandler.getWithItems(transportBO, id);

			List<LocationDTO> allCities = locationApi.loadCitys().getData();
			//parent就是省份ID,按省份进行分组
			Map<Long, List<LocationDTO>> citiesMap = allCities.stream().collect(Collectors.groupingBy(LocationDTO::getParentId));

			// 区域限售
			if (result.getFreeFlag()) {
				result.setLocationRestrictedSalesList(convert2TransSetting(result.getTransCityDTOList(), citiesMap));

			}
			// 固定运费
			else if (result.getTransType().equals(TransCityTypeEnum.CONSTANT_FREIGHT.value().toString())) {
				for (TransConstFeeDTO transConstFeeDTO : result.getTransConstFeeDTOList()) {
					transConstFeeDTO.setProvinceList(convert2TransSetting(transConstFeeDTO.getTransCityDTOList(), citiesMap));
				}
			} else {
				// 运费计算
				if (CollUtil.isNotEmpty(result.getTransFeeDTOList())) {
					for (TransFeeDTO transFeeDTO : result.getTransFeeDTOList()) {
						transFeeDTO.setProvinceList(convert2TransSetting(transFeeDTO.getTransCityDTOList(), citiesMap));
					}
				}

				// 条件包邮
				if (result.getConditionFreeFlag()) {
					for (TransFreeDTO transFreeDTO : result.getTransFreeDTOList()) {
						result.setTransFreeType(transFreeDTO.getType());
						transFreeDTO.setProvinceList(convert2TransSetting(transFreeDTO.getTransCityDTOList(), citiesMap));
						if (TransFreeTypeEnum.FULL_NUM.value().equals(transFreeDTO.getType())) {
							transFreeDTO.setNumber(BigDecimal.valueOf(transFreeDTO.getNum()));
						} else if (TransFreeTypeEnum.FULL_MONEY.value().equals(transFreeDTO.getType())) {
							transFreeDTO.setNumber(transFreeDTO.getPrice());
						}
					}
				}
			}

			return result;
		}
		return null;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveTransport(TransportDTO transportDTO) {
		Transport transport = transportConverter.from(transportDTO);
		transport.setRecDate(DateUtil.date());
		Long transportId = transportDao.saveTransprot(transport);
		transportDTO.setId(transportId);
		return handleTransport(transportDTO);
	}

	/**
	 * 处理运费模板运费计算、条件包邮这些
	 *
	 * @param transportDTO
	 * @return
	 */
	private Long handleTransport(TransportDTO transportDTO) {
		Long transportId = transportDTO.getId();
		//包邮
		if (transportDTO.getFreeFlag()) {
			List<Long> cityIds = new ArrayList<>();
			transportDTO.setTransCityDTOList(convert2TransCity(transportDTO.getLocationRestrictedSalesList()));
			transportDTO.getTransCityDTOList().forEach(c -> {
				c.setParentId(-1L);
				c.setTransId(transportId);
				c.setType(TransCityTypeEnum.AREA_LIMIT.value());
				cityIds.add(c.getCityId());
			});
			HashSet<Long> set = new HashSet<>(cityIds);
			if (cityIds.size() != set.size()) {
				throw new BusinessException("存在重复的可支持销售的地区!");
			}
			transCityService.batchAdd(transportDTO.getTransCityDTOList());
			return transportId;
		}
		//固定运费
		if (transportDTO.getTransType().equals(TransTypeEnum.CONSTANT.value())) {
			for (TransConstFeeDTO transConstFeeDTO : transportDTO.getTransConstFeeDTOList()) {
				transConstFeeDTO.setTransCityDTOList(convert2TransCity(transConstFeeDTO.getProvinceList()));
			}
			transConstFeeService.saveWithCityList(transportDTO.getTransConstFeeDTOList(), transportId);
			return transportId;
		}
		//运费计算方式
		for (TransFeeDTO transFeeDTO : transportDTO.getTransFeeDTOList()) {
			// 参数校验
			Set<ConstraintViolation<TransFeeDTO>> constraintViolations = validator.validate(transFeeDTO);
			for (ConstraintViolation<TransFeeDTO> valid : constraintViolations) {
				throw new BusinessException(valid.getMessage());
			}

			transFeeDTO.setTransCityDTOList(convert2TransCity(transFeeDTO.getProvinceList()));
			transFeeDTO.setCalFreightType(transportDTO.getTransType());
			transFeeDTO.setStatus(1);
		}
		transFeeService.saveWithCityList(transportDTO.getTransFeeDTOList(), transportId);

		//条件包邮
		if (transportDTO.getConditionFreeFlag()) {
			for (TransFreeDTO transFreeDTO : transportDTO.getTransFreeDTOList()) {
				transFreeDTO.setTransCityDTOList(convert2TransCity(transFreeDTO.getProvinceList()));
				transFreeDTO.setType(transportDTO.getTransFreeType());
				if (TransFreeTypeEnum.FULL_NUM.value().equals(transFreeDTO.getType())) {
					transFreeDTO.setNum(transFreeDTO.getNumber().intValue());
				} else if (TransFreeTypeEnum.FULL_MONEY.value().equals(transFreeDTO.getType())) {
					transFreeDTO.setPrice(transFreeDTO.getNumber());
				} else {
					throw new BusinessException("条件包邮类型不能为空");
				}
			}
			transFreeService.saveWithCityList(transportDTO.getTransFreeDTOList(), transportId);
		}
		return transportId;
	}


	@Override
	public int deleteTransport(Long id) {
		Long count = productService.getCountByTransId(id);
		if (count > 0) {
			throw new BusinessException("该运费模板被商品使用，不能删除");
		}
		delItems(id);
		return transportDao.deleteById(id);
	}

	private void delItems(Long id) {
		transConstFeeService.delByTransId(id);
		transCityService.delByTransId(id);
		transFreeService.delByTransId(id);
		transFeeService.delByTransId(id);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateTransport(TransportDTO transportDTO) {
		delItems(transportDTO.getId());
		transportDTO.setRecDate(DateUtil.date());
		int update = transportDao.updateTransport(transportConverter.from(transportDTO));
		handleTransport(transportDTO);
		return update;
	}


	/**
	 * todo 运费模板查询性能问题
	 *
	 * @param transportQuery
	 * @return
	 */
	@Override
	public PageSupport<TransportBO> queryTransportPage(TransportQuery transportQuery) {
		long t1 = System.currentTimeMillis();
		PageSupport<TransportBO> pageList = transportConverter.convert2BoPageList(transportDao.queryTransportPage(transportQuery));
		List<TransportBO> resultList = pageList.getResultList();

		//空的时候啥也不干就返回
		if (CollUtil.isEmpty(resultList)) {
			return pageList;
		}
		//包邮运费模板的ID列表
		List<Long> transportIds = resultList.stream().filter(e -> e.getFreeFlag()).map(e -> e.getId()).collect(Collectors.toList());
		List<TransCityDTO> cityLists = transCityService.getCityList(transportIds, -1L);


		List<LocationDTO> provinces = locationApi.loadProvinces().getData();
		Map<Long, LocationDTO> provincesMap = provinces.stream().collect(Collectors.toMap(LocationDTO::getId, v -> v, (p1, p2) -> p1));

		List<LocationDTO> allCities = locationApi.loadCitys().getData();
		//parent就是省份ID,按省份进行分组
		Map<Long, List<LocationDTO>> citiesMap = allCities.stream().collect(Collectors.groupingBy(LocationDTO::getParentId));


		if (cityLists != null) {
			//每个包邮的运费模板的城市列表
			Map<Long, List<TransCityDTO>> cityListByTransport = cityLists.stream().collect(Collectors.groupingBy(TransCityDTO::getTransId));
			resultList.forEach(r -> {
				if (r.getFreeFlag()) {
					//1. 包邮
					List<TransCityDTO> cityList = cityListByTransport.get(r.getId());
					Map<Long, List<TransCityDTO>> collect = cityList.stream().collect(Collectors.groupingBy(TransCityDTO::getProvinceId));

					if (provinces.size() != collect.size()) {
						//r.setArea("全国(除指定地区外)");  要指定选择的地区 newway

						//拼接地区名称
						StringBuffer sb = new StringBuffer();
						collect.forEach((k, transCityDTOS) -> {
							//List<LocationDTO> cities = locationApi.loadCityList(k).getData();
							//LocationDTO province = locationApi.get(k).getData();

							List<LocationDTO> cities = citiesMap.get(k);
							LocationDTO province = provincesMap.get(k);
							if (ObjectUtil.isNotEmpty(province)) {
								sb.append(province.getName());
							}
							if (cities.size() != transCityDTOS.size()) {
								sb.append("（");
								cities.forEach(c -> {
									transCityDTOS.forEach(city -> {
										if (c.getId().equals(city.getCityId())) {
											sb.append(c.getName());
											sb.append("、");
										}
									});
								});
								sb.setLength(sb.length() - 1);
								sb.append("）");
							} else {
								sb.append("（全省）");
							}
							sb.append("、");
						});
						sb.setLength(sb.length() - 1);

						r.setArea(sb.toString());

					} else {
						r.setArea("全国");
					}
					r.setLocationRestrictedSalesList(convert2TransSetting(r.getTransCityDTOList(), citiesMap));
					return;
				}
				//2. 固定运费
				if (r.getTransType().equals(TransCityTypeEnum.CONSTANT_FREIGHT.value().toString())) {
					List<TransConstFeeDTO> transConstFeeList = transConstFeeService.getListAreaByTransId(r.getId());
					for (TransConstFeeDTO transConstFeeDTO : transConstFeeList) {
						transConstFeeDTO.setProvinceList(convert2TransSetting(transConstFeeDTO.getTransCityDTOList(), citiesMap));
					}
					r.setTransConstFeeDTOList(transConstFeeList);
					return;
				}
				//3. 条件包邮
				if (r.getConditionFreeFlag()) {
					List<TransFreeDTO> transFreeList = transFreeService.getListAreaByTransId(r.getId());
					for (TransFreeDTO transFreeDTO : transFreeList) {

						//有性能问题，注意不要用循环来查询数据库 newway
						transFreeDTO.setProvinceList(convert2TransSetting(transFreeDTO.getTransCityDTOList(), citiesMap));
						r.setTransFreeType(transFreeDTO.getType());
						if (TransFreeTypeEnum.FULL_NUM.value().equals(transFreeDTO.getType())) {
							transFreeDTO.setNumber(BigDecimal.valueOf(transFreeDTO.getNum()));
						} else if (TransFreeTypeEnum.FULL_MONEY.value().equals(transFreeDTO.getType())) {
							transFreeDTO.setNumber(transFreeDTO.getPrice());
						}
					}
					r.setTransFreeDTOList(transFreeList);
				}

				//4. 加载费用
				List<TransFeeDTO> transFeeList = transFeeService.getListAreaByTransId(r.getId());
				for (TransFeeDTO transFeeDTO : transFeeList) {
					transFeeDTO.setProvinceList(convert2TransSetting(transFeeDTO.getTransCityDTOList(), citiesMap));
				}
				r.setTransFeeDTOList(transFeeList);
			});
		}

		return pageList;
	}


	@Override
	public PageSupport<TransportBO> queryTransportChoosePage(TransportQuery transportQuery) {
		PageSupport<TransportBO> pageList = transportConverter.convert2BoPageList(transportDao.queryTransportPage(transportQuery));
		List<TransportBO> resultList = pageList.getResultList();
		resultList.forEach(transportBO -> {
			transportBO.setType(getTransTypeName(transportBO));
		});
		return pageList;
	}

	private String getTransTypeName(TransportBO transportBO) {
		String type = "";
		if (transportBO.getFreeFlag()) {
			type = "包邮";
		} else if (transportBO.getTransType().equals(TransTypeEnum.CONSTANT.value())) {
			type = "固定运费";
		} else if (transportBO.getTransType().equals(TransTypeEnum.NUMBER.value())) {
			type = "按件计费";
		} else if (transportBO.getTransType().equals(TransTypeEnum.WEIGHT.value())) {
			type = "按重量计费";
		} else if (transportBO.getTransType().equals(TransTypeEnum.VOLUME.value())) {
			type = "按体积计费";
		} else {
			throw new BusinessException("未知的模板类型！");
		}
		return type;
	}

	private List<TransCityDTO> convert2TransCity(List<TransProvinceDTO> provinceList) {
		if (CollUtil.isNotEmpty(provinceList)) {
			List<TransCityDTO> result = new ArrayList<>(provinceList.size());
			for (TransProvinceDTO transProvinceDTO : provinceList) {
				for (CityDTO cityDTO : transProvinceDTO.getChildren()) {
					if (null == transProvinceDTO.getId() || null == cityDTO.getId() || null == transProvinceDTO.getSelectFlag()) {
						throw new BusinessException("地区列表不能为空");
					}
					TransCityDTO transCityDTO = new TransCityDTO();
					transCityDTO.setProvinceId(transProvinceDTO.getId());
					transCityDTO.setCityId(cityDTO.getId());
					transCityDTO.setSelectFlag(transProvinceDTO.getSelectFlag());
					result.add(transCityDTO);
				}
			}
			return result;
		} else {
			throw new BusinessException("地区列表不能为空");
		}
	}

	/**
	 * @param transCityList 包邮情况下的区域限售
	 * @param citiesMap     provinceId: 城市列表
	 * @return
	 */
	private List<TransProvinceDTO> convert2TransSetting(List<TransCityDTO> transCityList, Map<Long, List<LocationDTO>> citiesMap) {
		if (CollUtil.isNotEmpty(transCityList)) {
			List<TransProvinceDTO> result = new ArrayList<>();

			// 按省份分组
			Map<Long, List<TransCityDTO>> provinceMap = transCityList.stream().collect(Collectors.groupingBy(TransCityDTO::getProvinceId));
			provinceMap.forEach((provinceId, cityList) -> {
				TransProvinceDTO transProvinceDTO = new TransProvinceDTO();
				transProvinceDTO.setId(provinceId);
				transProvinceDTO.setSelectFlag(cityList.get(0).getSelectFlag());

				//获取当前省份下所有城市
				if (citiesMap != null) {
					List<LocationDTO> locationDTOS = citiesMap.get(provinceId);
					if (locationDTOS != null) {
						Map<Long, LocationDTO> locationDTOMap = locationDTOS.stream().collect(Collectors.toMap(LocationDTO::getId, a -> a, (k1, k2) -> k1));
						List<CityDTO> cityDTOList = new ArrayList<>();
						cityList.forEach(city -> {
							CityDTO cityDTO = new CityDTO();
							cityDTO.setId(city.getCityId());
							if (locationDTOMap.containsKey(city.getCityId())) {
								LocationDTO locationDTO = locationDTOMap.get(city.getCityId());
								cityDTO.setCode(locationDTO.getCode());
								cityDTO.setName(locationDTO.getName());
							}
							cityDTOList.add(cityDTO);
						});
						transProvinceDTO.setChildren(cityDTOList);
					}
				}
				result.add(transProvinceDTO);
			});

			return result;
		}
		return Collections.emptyList();
	}
}
