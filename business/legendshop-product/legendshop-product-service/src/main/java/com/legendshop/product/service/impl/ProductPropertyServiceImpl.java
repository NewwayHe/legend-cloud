/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.ProductPropertyAggBatchDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.ProductPropertyValueDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ProductProperty;
import com.legendshop.product.entity.ProductPropertyValue;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ProductPropertyQuery;
import com.legendshop.product.service.CategoryService;
import com.legendshop.product.service.ProductPropertyAggParamService;
import com.legendshop.product.service.ProductPropertyAggSpecificationService;
import com.legendshop.product.service.ProductPropertyService;
import com.legendshop.product.service.convert.ProductPropertyConverter;
import com.legendshop.product.service.convert.ProductPropertyValueConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 规格属性ServiceImpl.
 *
 * @author legendshop
 */
@Service
@Slf4j
public class ProductPropertyServiceImpl implements ProductPropertyService {

	@Autowired
	private ProductPropertyDao productPropertyDao;

	@Autowired
	private ProductPropertyValueDao propertyValueDao;

	@Autowired
	private ProductPropertyConverter propertyConverter;

	@Autowired
	private ProductPropertyValueConverter valueConverter;

	@Autowired
	private ProductPropertyAggSpecificationDao specificationDao;

	@Autowired
	private ProductPropertyAggParamDao paramDao;

	@Autowired
	private ProductPropertyAggDao aggDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	CategoryService categoryService;

	@Autowired
	private ProductPropertyAggCategoryDao aggCategoryDao;


	@Autowired
	private ProductPropertyValueDao productPropertyValueDao;

	@Autowired
	private ProductPropertyAggSpecificationService aggSpecificationService;

	@Autowired
	private ProductPropertyAggParamService aggParamService;

	public List<ProductProperty> getById(String propName) {
		return productPropertyDao.getProductProperty(propName);
	}

	@Override
	public ProductPropertyBO getDetailById(Long id) {
		ProductPropertyBO productPropertyBO = getById(id);
		List<ProductPropertyValue> byPropId = propertyValueDao.getByPropId(id);
		productPropertyBO.setProdPropList(valueConverter.convert2BoList(byPropId));
		productPropertyBO.setAggBO(aggDao.queryByProductId(id));
		return productPropertyBO;
	}

	@Override
	public ProductPropertyBO getById(Long id) {
		ProductProperty property = productPropertyDao.getById(id);
		if (property == null) {
			return null;
		}
		ProductPropertyBO productPropertyBO = propertyConverter.convert2BO(property);
		List<String> valueList = productPropertyDao.getPropertyValueList(id);
		if (valueList.size() > 0) {
			String s = Arrays.toString(valueList.stream().toArray());
			productPropertyBO.setProdPropStr(s.substring(1, s.length() - 1));
		}
		return productPropertyBO;
	}

	@Override
	public List<ProductPropertyBO> getById(List<Long> propIds, Long categoryId) {
		return propertyConverter.convert2BoList(productPropertyDao.getProductProperty(propIds, categoryId));
	}

	/**
	 * 根据类目id、商品id查询相关联的参数属性
	 */
	@Override
	public List<ProductPropertyBO> queryParamByCategoryId(Long categoryId, Long productId) {
		List<ProductPropertyBO> productParameterList = new ArrayList<>();
		//根据productId加载商品的所有参数值id
		if (ObjectUtil.isNotNull(productId)) {
			/*根据productId获取参数属性*/
			Product product = productDao.getById(productId);
			/*参数属性、自定义参数属性*/
			List<ProductPropertyDTO> systemParamList = JSONUtil.toList(JSONUtil.parseArray(product.getParameter()), ProductPropertyDTO.class);
			List<ProductPropertyDTO> userParamList = JSONUtil.toList(JSONUtil.parseArray(product.getUserParameter()), ProductPropertyDTO.class);
			List<ProductPropertyDTO> productParamList = systemParamList.stream().map(e -> {
				e.setSource(ProductPropertySourceEnum.SYSTEM.value());
				return e;
			}).collect(Collectors.toList());
			productParamList.addAll(userParamList.stream().map(e -> {
				e.setSource(ProductPropertySourceEnum.USER.value());
				return e;
			}).collect(Collectors.toList()));
			/*将ProductParamDTO对象转换成ProductPropertyBO对象*/
			for (ProductPropertyDTO productParamDTO : productParamList) {
				ProductPropertyBO productPropertyBO = new ProductPropertyBO();
				productPropertyBO.setId(productParamDTO.getId());
				productPropertyBO.setPropName(productParamDTO.getPropName());
				productPropertyBO.setSource(productParamDTO.getSource());
				List<ProductPropertyValueDTO> valueList = productParamDTO.getProdPropList();
				if (valueList != null && valueList.size() > 0) {
					for (ProductPropertyValueDTO s : valueList) {
						ProductPropertyValueBO productPropertyValueBO = new ProductPropertyValueBO();
						productPropertyValueBO.setId(s.getId());
						productPropertyValueBO.setPropId(productParamDTO.getId());
						productPropertyValueBO.setName(s.getName());
						/*设置select为true*/
						productPropertyValueBO.setSelectFlag(s.getSelectFlag());
						if (productPropertyBO.getProdPropList() == null) {
							productPropertyBO.setProdPropList(new ArrayList<>());
						}
						productPropertyBO.addProductPropertyValueList(productPropertyValueBO);
					}
				}
				productParameterList.add(productPropertyBO);
			}
			return productParameterList;
		}
		//根据类目id，查询所有的aggid,
		log.info("查询所有的aggid！date:{}", System.currentTimeMillis());
		Long aggId = aggCategoryDao.queryAggIdByCategoryId(categoryId);
		log.info("查询所有的aggid！date:{}", System.currentTimeMillis());
		if (ObjectUtil.isNotEmpty(aggId)) {
			//根据aggId,查询所有的值
			log.info("根据aggId！date:{}", System.currentTimeMillis());
			productParameterList = propertyConverter.convert2BoList(productPropertyDao.getParameterPropertyList(Collections.singletonList(aggId)));
			log.info("根据aggId！date:{}", System.currentTimeMillis());
		}
		//如果属性为空，递归查找父类的属性
		if (productParameterList == null) {
			Long parentId = categoryService.getById(categoryId).getParentId();
			log.info("递归查找父类的属性！date:{}", System.currentTimeMillis());
			if (parentId.compareTo(-1L) == 0) {
				return null;
			}
			return queryParamByCategoryId(parentId, productId);
		}

		List<Long> productIdList = productParameterList.stream().map(ProductPropertyBO::getId).collect(Collectors.toList());

		if (productIdList.size() > 0) {
			List<ProductPropertyValueBO> productPropertyValueBOS = valueConverter.convert2BoList(productPropertyValueDao.queryByPropId(productIdList));
			//将参数值列表放到Map中保存
			Map<Long, List<ProductPropertyValueBO>> valueMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				productPropertyValueBOS.forEach(item -> {
					if (e.compareTo(item.getPropId()) == 0) {
						List<ProductPropertyValueBO> list = new ArrayList<>();
						if (valueMap.get(e) != null) {
							list = valueMap.get(e);
						}
						list.add(item);
						valueMap.put(e, list);
					}
				});
			});

			productParameterList.forEach(item -> {
				item.getProdPropList().addAll(valueMap.get(item.getId()));
			});
		}
		log.info("productParameterList！date:{}", System.currentTimeMillis());
		return productParameterList;
	}

	@Override
	public List<ProductPropertyBO> queryParamByCategoryIds(List<Long> categoryId, Long productId) {
		List<ProductPropertyBO> productPropertyList = new ArrayList<>();
		for (Long aLong : categoryId) {
			List<ProductPropertyBO> productPropertyBOS = queryParamByCategoryId(aLong, productId);
			for (ProductPropertyBO productPropertyBO : productPropertyBOS) {
				productPropertyList.add(productPropertyBO);
			}

		}
		return productPropertyList;
	}

	/**
	 * 根据类目id、商品id查询相关联的规格属性
	 */
	@Override
	public List<ProductPropertyDTO> querySpecificationByCategoryId(Long categoryId, Long productId) {
		List<ProductPropertyDTO> specificationList = new ArrayList<>();
		if (ObjectUtil.isNotEmpty(productId)) {
			return JSONUtil.toList(JSONUtil.parseArray(productDao.getById(productId).getSpecification()), ProductPropertyDTO.class);
		}
		//根据类目id，查询所有的aggid,
		Long aggId = aggCategoryDao.queryAggIdByCategoryId(categoryId);
		//根据aggId,查询所有的值
		if (aggId != null) {
			specificationList = propertyConverter.to(productPropertyDao.getSpecificationPropertyList(Collections.singletonList(aggId)));
		}
		//如果属性为空，递归查找父类的属性
		if (specificationList == null) {
			Long parentId = categoryService.getById(categoryId).getParentId();
			if (parentId.compareTo(-1L) == 0) {
				return null;
			}
			return querySpecificationByCategoryId(parentId, productId);
		}

		List<Long> propertyIds = specificationList.stream().map(ProductPropertyDTO::getId).collect(Collectors.toList());
		if (propertyIds.size() > 0) {
			List<ProductPropertyValueDTO> productPropertyValueDTO = valueConverter.to(productPropertyValueDao.queryByPropId(propertyIds));
			//将参数值列表放到Map中保存
			Map<Long, List<ProductPropertyValueDTO>> valueMap = new HashMap<>(16);
			propertyIds.forEach(e -> {
				productPropertyValueDTO.forEach(item -> {
					if (e.compareTo(item.getPropId()) == 0) {
						List<ProductPropertyValueDTO> list = new ArrayList<>();
						if (valueMap.get(e) != null) {
							list = valueMap.get(e);
						}
						list.add(item);
						valueMap.put(e, list);
					}
				});
			});

			specificationList.forEach(item -> {
				//设置参数值
				List<ProductPropertyValueDTO> propList = valueMap.get(item.getId());
				if (propList != null) {
					item.setProdPropList(propList);
				}
			});
		}
		return specificationList;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		//属性名和属性值
		productPropertyDao.updateDeleteFlag(id, true);
		propertyValueDao.updateDeleteFlagByPropId(id, true);
	}

	@Override
	@Transactional
	public boolean save(ProductPropertyDTO productPropertyDTO) {
		Date date = new Date();
		//新建默认是正常状态
		productPropertyDTO.setDeleteFlag(false);
		productPropertyDTO.setCreateTime(date);
		Long propId = productPropertyDao.save(propertyConverter.from(productPropertyDTO));

		if (propId > 0 && productPropertyDTO.getProdPropList() != null) {
			//保存value
			productPropertyDTO.getProdPropList().forEach((s) -> {
				s.setPropId(propId);
				s.setDeleteFlag(false);//新建默认是正常状态
				s.setCreateTime(date);
			});
			propertyValueDao.save(valueConverter.from(productPropertyDTO.getProdPropList()));
		}

		//保存类目关联
		if (productPropertyDTO.getAggIdList() != null && productPropertyDTO.getAggIdList().size() > 0) {
			ProductPropertyAggBatchDTO dto = new ProductPropertyAggBatchDTO();
			dto.setAggIdlist(productPropertyDTO.getAggIdList());
			dto.setPropId(propId);
			//保存到规格、参数关联表
			if ("S".equals(productPropertyDTO.getAttributeType())) {
				aggSpecificationService.save(dto);
			} else {
				aggParamService.save(dto);
			}

		}
		return true;
	}

	@Override
	public List<ProductPropertyBO> queryByGroupId(Long id) {
		return propertyValueDao.queryByGroupId(id);
	}

	@Override
	public List<ProductPropertyBO> queryByGroupId(List<Long> groupIds) {
		return propertyValueDao.queryByGroupId(groupIds);
	}

	@Override
	public Long createId() {
		return propertyValueDao.createId();
	}

	@Override
	public void save(List<ProductPropertyDTO> propertyList) {
		productPropertyDao.save(propertyConverter.from(propertyList));
	}

	@Override
	public List<ProductPropertyBO> getDetailByIds(List<Long> productIdList) {
		//根据属性值idList,查询所有的值
		List<ProductPropertyBO> productPropertyBOList = propertyConverter.convert2BoList(productPropertyDao.queryAllByIds(productIdList));
		if (productIdList.size() > 0) {
			List<ProductPropertyValueBO> productPropertyValueBOS = valueConverter.convert2BoList(productPropertyValueDao.queryByPropId(productIdList));
			//将属性值列表放到Map中保存
			Map<Long, List<ProductPropertyValueBO>> valueMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				productPropertyValueBOS.forEach(item -> {
					if (e.compareTo(item.getPropId()) == 0) {
						List<ProductPropertyValueBO> list = new ArrayList<>();
						if (valueMap.get(e) != null) {
							list = valueMap.get(e);
						}
						list.add(item);
						valueMap.put(e, list);
					}
				});
			});

			productPropertyBOList.forEach(item -> {
				//设置属性值
				List<ProductPropertyValueBO> propList = valueMap.get(item.getId());
				if (propList != null) {
					item.setProdPropList(propList);
				}
			});
		}
		return productPropertyBOList;
	}

	@Override
	@Transactional
	public boolean update(ProductPropertyDTO productProperty) {
		Date date = new Date();
		ProductProperty property = propertyConverter.from(productProperty);
		ProductProperty originProductProperty = productPropertyDao.getById(productProperty.getId());
		BeanUtil.copyProperties(productProperty, originProductProperty, new CopyOptions().setIgnoreNullValue(Boolean.TRUE));
		originProductProperty.setUpdateTime(date);

		int update = productPropertyDao.update(originProductProperty);
		if (update > 0) {
			List<Long> originPropertyValueIds = propertyValueDao.getValueIdsByPropId(productProperty.getId());
			//保存value
			productProperty.getProdPropList().forEach((s) -> {
				//找出删除掉的属性值
				if (originPropertyValueIds.contains(s.getId())) {
					originPropertyValueIds.remove(s.getId());
				}
				if (s.getId() != null) {//修改
					ProductPropertyValue originValue = propertyValueDao.getById(s.getId());
					s.setShopId(originValue.getShopId());
					s.setUpdateTime(date);
					s.setPropId(originValue.getPropId());
					s.setCreateTime(originValue.getCreateTime());
					s.setDeleteFlag(false);//正常状态
					propertyValueDao.update(valueConverter.from(s));
				} else if (s.getId() == null)//新建
				{
					s.setPropId(productProperty.getId());
					s.setDeleteFlag(false);
					s.setCreateTime(date);
					propertyValueDao.save(valueConverter.from(s));
				}
			});
			//对于已经删除掉的属性值，修改deleteFlag
			if (originPropertyValueIds.size() > 0) {
				for (Long valueId : originPropertyValueIds) {
					propertyValueDao.updateDeleteFlag(valueId, true);
				}
			}
			//先删除所有类目关联，再保存
			aggSpecificationService.deleteByProductId(productProperty.getId());
			aggParamService.deleteByPropId(productProperty.getId());
			//保存类目关联
			if (productProperty.getAggIdList() != null && productProperty.getAggIdList().size() > 0) {
				ProductPropertyAggBatchDTO dto = new ProductPropertyAggBatchDTO();
				dto.setAggIdlist(productProperty.getAggIdList());
				dto.setPropId(productProperty.getId());
				//保存到规格、参数关联表
				if ("S".equals(productProperty.getAttributeType())) {
					aggSpecificationService.save(dto);
				} else {
					aggParamService.save(dto);
				}

			}
			return true;
		}
		return update > 0;
	}


	@Override
	public PageSupport<ProductPropertyBO> queryPage(ProductPropertyQuery productPropertyQuery) {
		PageSupport<ProductPropertyBO> page = propertyConverter.convert2BoPageList(productPropertyDao.queryProductPropertyPage(productPropertyQuery));

		//获取属性id集合(规格属性、参数属性，下同)
		List<Long> productIdList = page.getResultList().stream().map(ProductPropertyBO::getId).collect(Collectors.toList());
		if (productIdList.size() > 0) {
			//获取属性值
			List<ProductPropertyValueBO> propValueList = valueConverter.convert2BoList(propertyValueDao.queryByPropId(productIdList));

			//获取类目关联管理名
			List<ProductPropertyBO> propList = "S".equals(productPropertyQuery.getAttributeType()) ? specificationDao.getAggNameByPropId(productIdList) : paramDao.getAggNameByPropId(productIdList);

			//收集属性值
			Map<Long, StringBuffer> paramMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				propValueList.forEach(i -> {
					if (i.getPropId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramMap.get(e) != null) {
							buffer = paramMap.get(e);
						}
						paramMap.put(e, buffer.append(i.getName() + ","));
					}
				});
			});

			//收集类目关联管理名
			Map<Long, StringBuffer> aggMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				propList.forEach(i -> {
					if (i.getId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (aggMap.get(e) != null) {
							buffer = aggMap.get(e);
						}
						aggMap.put(e, buffer.append(i.getAggPropName() + ","));
					}
				});
			});

			//设置属性值,设置类目关联管理名
			page.getResultList().forEach(e -> {
				//设置属性值
				if (paramMap.get(e.getId()) != null) {
					String s = paramMap.get(e.getId()).toString();
					e.setProdPropStr(s.substring(0, s.length() - 1));
				}
				//设置类目关联管理名
				if (aggMap.get(e.getId()) != null) {
					String s = aggMap.get(e.getId()).toString();
					e.setPropertyAggStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return page;
	}

	@Override
	public List<String> queryAttachmentByUrl(String url) {
		return productPropertyDao.queryAttachmentByUrl(url);
	}
}
