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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.service.SkuService;
import com.legendshop.product.service.convert.SkuConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单品库存ServiceImpl.
 *
 * @author legendshop
 */
@Service
public class SkuServiceImpl implements SkuService {


	@Autowired
	private SkuDao skuDao;

	@Autowired
	private SkuConverter skuConverter;

	@Autowired
	private CacheManagerUtil cacheManagerUtil;

	@Autowired
	private ProductDao productDao;

	@Override
	@Cacheable(value = "SkuBO", key = "#skuId")
	public SkuBO getSkuById(Long skuId) {
		return skuConverter.convert2BO(skuDao.getById(skuId));
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "Sku", key = "#skuDTO.id"),
			@CacheEvict(value = "SkuBO", key = "#skuDTO.id")
	})
	public int updateSku(SkuBO skuBO) {
		return skuDao.update(skuConverter.toEntity(skuBO));
	}

	@Override
	public List<SkuBO> getSkuByProduct(Long productId) {
		return skuConverter.convert2BoList(skuDao.getSkuByProductId(productId));
	}

	/**
	 * 获取属性和属性值
	 *
	 * @param activityProductDTO
	 * @return
	 */
	@Override
	public List<ProductPropertyDTO> getPropValListByProd(ActivityProductDTO activityProductDTO) {
		List<SkuBO> skuBOS = activityProductDTO.getSkuBOS();
		if (CollUtil.isEmpty(skuBOS)) {
			return null;
		} else {
			String skuType = activityProductDTO.getSkuType();
			Long activityId = activityProductDTO.getActivityId();
			SkuBO defaultSku = null;

			//普通商品则取得列表中第一个有库存的sku作为默认选中的sku
			if (ObjectUtil.isNull(skuType)) {
				if (ObjectUtil.isNotEmpty(activityProductDTO.getSkuId())) {
					defaultSku = skuBOS.stream().filter(e -> e.getId().equals(activityProductDTO.getSkuId())).findFirst().orElse(null);
				} else {
					for (SkuBO skuBO : skuBOS) {
						if (skuBO.getStocks() > 0) {
							defaultSku = skuBO;
							break;
						}
					}
				}
			}

			// 如果前端选中了sku，例如订单详情点击商品时跳转，只要存在就选中这个sku
			else if (ObjectUtil.isNotEmpty(activityProductDTO.getSkuId())) {
				defaultSku = skuBOS.stream().filter(e -> e.getId().equals(activityProductDTO.getSkuId()) && e.getSkuType().equals(skuType)).findFirst().orElse(null);
			}

			// 辉哥：如果为空，则选择第一个为默认选中的sku
			if (null == defaultSku) {
				defaultSku = skuBOS.stream().filter(e -> e.getStocks() > 0).findFirst().orElse(skuBOS.get(0));
			}
			//解析商品规格
			List<ProductPropertyDTO> productPropertyDTOList = JSONUtil.toList(JSONUtil.parseArray(activityProductDTO.getSpecification()), ProductPropertyDTO.class);
			//过滤掉selectFlag=false的属性值
			productPropertyDTOList.forEach(productPropertyDTO -> {
//				productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream().filter(ProductPropertyValueDTO::getSelectFlag).collect(Collectors.toList()));
				productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream().filter(e -> ObjectUtil.isNotNull(e.getSelectFlag()) && e.getSelectFlag()).collect(Collectors.toList()));
			});
			//过滤掉prodPropList为空的规格对象
			productPropertyDTOList = productPropertyDTOList.stream().filter(productPropertyDTO -> {
				return CollUtil.isNotEmpty(productPropertyDTO.getProdPropList());
			}).collect(Collectors.toList());
			//全部规格改为false,设为不选中状态
			productPropertyDTOList.forEach(productPropertyDTO -> {
				productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream()
						.map(productPropertyValueDTO -> {
							productPropertyValueDTO.setSelectFlag(false);
							return productPropertyValueDTO;
						}).collect(Collectors.toList()));
			});
			//将默认sku下的规格设为选中状态
			if (ObjectUtil.isNotEmpty(defaultSku)) {
				//取得sku规格字符串(k1:v1;k2:v2;k3:v3)中的每对key:value,并将属于默认sku规格的value存储起来
				List<Long> selectedVals = new ArrayList<Long>();
				for (SkuBO skuBO : skuBOS) {
					String properties = skuBO.getProperties();
					if (StrUtil.isNotBlank(properties)) {
						String[] skuStrs = properties.split(";");
						for (String skuStr : skuStrs) {
							String[] skuItems = skuStr.split(":");
							Long valueId = Long.valueOf(skuItems[1]);
							if (skuBO.getId().equals(defaultSku.getId())) {
								selectedVals.add(valueId);
							}
						}
					}
				}
				//存储起来的规格改为选中状态
				productPropertyDTOList.forEach(productPropertyDTO -> {
					productPropertyDTO.setProdPropList(productPropertyDTO.getProdPropList().stream()
							.map(productPropertyValueDTO -> {
								if (selectedVals.contains(productPropertyValueDTO.getId())) {
									productPropertyValueDTO.setSelectFlag(true);
								}
								return productPropertyValueDTO;
							}).collect(Collectors.toList()));
				});
			}
			return productPropertyDTOList;
		}
	}

	private List<KeyValueEntityDTO> splitJoinSku(String properties, String expr) {
		List<KeyValueEntityDTO> skuKVs = new ArrayList<KeyValueEntityDTO>();
		if (properties != null && properties != "") {
			String[] proValue = properties.split(expr);
			if (proValue != null && proValue.length > 0) {
				int length = proValue.length;
				for (int i = 0; i < length; i++) {
					String[] valeus = proValue[i].split(":");
					if (valeus != null && valeus.length > 1) {
						KeyValueEntityDTO kv = new KeyValueEntityDTO();
						kv.setKey(valeus[0]);
						kv.setValue(valeus[1]);
						skuKVs.add(kv);
					}
				}
			}
		}
		return skuKVs;
	}


	@Override
	public List<KeyValueEntityDTO> getSkuImg(String skuProp) {
		List<KeyValueEntityDTO> keyValueEntities = splitJoinSku(skuProp, ";");
		return keyValueEntities;
	}


	@Override
	public SkuBO getByProductIdSkuId(Long productId, Long skuId) {
		return skuConverter.convert2BO(skuDao.getByProductIdSkuId(productId, skuId));
	}

	/**
	 * 根据skuId 修改活动类型
	 *
	 * @param skuId
	 * @param skuType       修改为目标活动类型
	 * @param originSkuType 原来的活动类型
	 */
	@Override
	public int updateSkuTypeById(Long skuId, String skuType, String originSkuType) {
		return skuDao.updateSkuTypeById(skuId, skuType, originSkuType);
	}

	@Override
	public void batchUpdateSkuType(List<Long> skuIds, String skuType, String originalSkuType) {
		skuDao.batchUpdateSkuType(skuIds, skuType, originalSkuType);
	}

	@Override
	public int updateSkuTypeByProductId(Long productId, String skuType, String originSkuType) {
		return skuDao.updateSkuTypeById(productId, skuType, originSkuType);
	}

	@Override
	public int updateStocksBySkuId(Long skuId, Integer basketCount) {
		return skuDao.updateStocksBySkuId(skuId, basketCount);
	}

	@Override
	public List<SkuDTO> queryBySkuIds(List<Long> skuIds) {
		return skuConverter.to(skuDao.queryBySkuIds(skuIds));
	}

	@Override
	public List<SkuBO> querySkuByProductIdList(List<Long> productIdList) {
		return skuConverter.convert2BoList(skuDao.querySkuByProductIdList(productIdList));
	}

	@Override
	public void clearSkuCache(List<Long> skuIds) {
		if (CollUtil.isNotEmpty(skuIds)) {
			for (Long skuId : skuIds) {
				cacheManagerUtil.evictCache("SkuBO", skuId);
			}
		}
	}


	@Override
	public List<SkuBO> queryCouponSkuByShopId(List<Long> shopIds) {
		return skuDao.queryCouponSkuByShopId(shopIds);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean reduceStock(Long skuId, Integer stock) {
		Sku sku = skuDao.getById(skuId);
		//销售库存
		Integer skuStocks = sku.getStocks();
		if (ObjectUtil.isNull(skuStocks) || skuStocks == 0) {
			throw new BusinessException("库存不足");
		}
		if (skuStocks < stock) {
			throw new BusinessException("库存不足");
		}
		productDao.reduceStock(sku.getProductId(), stock);
		return skuDao.reduceStock(skuId, stock);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean addBackStock(Long skuId, Integer stock) {
		if (stock <= 0) {
			throw new BusinessException("库存必须大于0");
		}
		Sku sku = skuDao.getById(skuId);
		productDao.addBackStock(sku.getProductId(), stock);
		return skuDao.addBackStock(skuId, stock);
	}

	@Override
	public List<SkuBO> queryBOBySkuIds(List<Long> skuIds) {
		return skuDao.queryBOBySkuIds(skuIds);
	}

	@Override
	public Integer getSkuStock(Long skuId) {
		return skuDao.getSkuStock(skuId);
	}

	@Override
	public Integer getProductStock(Long productId) {
		return skuDao.getProductStock(productId);
	}

	@Override
	public List<SkuBO> querySkuBOByProductIdList(List<Long> productIdList) {
		return skuDao.querySkuBOByProductIdList(productIdList);
	}

	@Override
	public List<SkuBO> getSkuBOByProduct(Long productId) {
		return skuDao.getSkuBOByProductId(productId);
	}

	@Override
	public List<ProductDTO> querySkuCount(List<Long> productIdList) {
		return skuDao.querySkuCount(productIdList);
	}

	@Override
	public SkuBO getSkuByIdToCustom(Long skuId) {
		return skuDao.getSkuByIdToCustom(skuId);
	}

}
