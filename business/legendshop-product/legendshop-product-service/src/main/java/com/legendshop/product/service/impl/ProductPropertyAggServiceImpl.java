/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.*;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.ProductPropertyAggCategoryDTO;
import com.legendshop.product.dto.ProductPropertyAggDTO;
import com.legendshop.product.entity.*;
import com.legendshop.product.query.ProductPropertyAggQuery;
import com.legendshop.product.service.CategoryService;
import com.legendshop.product.service.ProductPropertyAggService;
import com.legendshop.product.service.ProductPropertyService;
import com.legendshop.product.service.convert.ProductPropertyAggConverter;
import com.legendshop.product.service.convert.ProductPropertyValueConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类目关联Service.
 *
 * @author legendshop
 */
@Service
@Slf4j
public class ProductPropertyAggServiceImpl implements ProductPropertyAggService {

	@Autowired
	private ProductPropertyAggDao productPropertyAggDao;

	@Autowired
	private ProductPropertyAggSpecificationDao aggSpecificationDao;

	@Autowired
	private ProductPropertyAggParamDao aggParamDao;

	@Autowired
	private ProductPropertyValueDao propertyValueDao;

	@Autowired
	private ProductPropertyAggParamGroupDao aggParamGroupDao;

	@Autowired
	private ProductPropertyAggBrandDao aggBrandDao;

	@Autowired
	private ProductPropertyAggCategoryDao aggCategoryDao;

	@Autowired
	private ProductPropertyAggConverter aggConverter;


	@Autowired
	private ProductPropertyService productPropertyService;


	@Autowired
	private ProductPropertyValueConverter valueConverter;

	@Autowired
	private CategoryService categoryService;

	@Override
	public ProductPropertyAggBO getById(Long id) {
		ProductPropertyAggBO productPropertyAggBO = aggConverter.convert2BO(productPropertyAggDao.getById(id));
		List<Long> idList = new ArrayList<>();
		idList.add(id);
		productPropertyAggBO.setCategoryList(aggCategoryDao.queryByAggId(idList));
		return productPropertyAggBO;
	}

	/**
	 * 查询规格属性
	 */
	@Override
	public PageSupport<ProductPropertyBO> getSpecificationById(ProductPropertyAggQuery query) {
		PageSupport<ProductPropertyBO> page = aggSpecificationDao.queryByPage(query);
		List<Long> productIdList = page.getResultList().stream().map(ProductPropertyBO::getId).collect(Collectors.toList());
		if (productIdList.size() > 0) {
			//获取规格值
			List<ProductPropertyValueBO> aggList = valueConverter.convert2BoList(propertyValueDao.queryByPropId(productIdList));
			//收集规格值
			Map<Long, StringBuffer> paramMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				aggList.forEach(i -> {
					if (i.getPropId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramMap.get(e) != null) {
							buffer = paramMap.get(e);
						}
						paramMap.put(e, buffer.append(i.getName() + ","));
					}
				});
			});
			//设置规格值
			page.getResultList().forEach(e -> {
				if (paramMap.get(e.getId()) != null) {
					String s = paramMap.get(e.getId()).toString();
					e.setProdPropStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return page;
	}

	/**
	 * 查询参数属性
	 */
	@Override
	public PageSupport<ProductPropertyBO> getParamById(ProductPropertyAggQuery query) {
		PageSupport<ProductPropertyBO> page = aggParamDao.queryByPage(query);
		List<Long> productIdList = page.getResultList().stream().map(ProductPropertyBO::getId).collect(Collectors.toList());
		if (productIdList.size() > 0) {
			//获取参数值
			List<ProductPropertyValueBO> aggList = valueConverter.convert2BoList(propertyValueDao.queryByPropId(productIdList));
			//收集参数值
			Map<Long, StringBuffer> paramMap = new HashMap<>(16);
			productIdList.forEach(e -> {
				aggList.forEach(i -> {
					if (i.getPropId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramMap.get(e) != null) {
							buffer = paramMap.get(e);
						}
						paramMap.put(e, buffer.append(i.getName() + ","));
					}
				});
			});
			//设置参数值
			page.getResultList().forEach(e -> {
				if (paramMap.get(e.getId()) != null) {
					String s = paramMap.get(e.getId()).toString();
					e.setProdPropStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return page;
	}

	/**
	 * 查询品牌
	 */
	@Override
	public PageSupport<BrandBO> getBrandById(ProductPropertyAggQuery query) {
		PageSupport<BrandBO> page = aggBrandDao.queryByPage(query);
		return page;
	}

	@Override
	public PageSupport<BrandBO> getBrandByIds(ProductPropertyAggQuery query) {
		ProductPropertyAggCategory byCategoryId = aggCategoryDao.getByCategoryId(query.getCategoryId());
		if (ObjectUtil.isEmpty(byCategoryId)) {
			log.info("没有关联品牌");
			return null;
		}
		query.setId(byCategoryId.getAggId());
		PageSupport<BrandBO> brandBOPageSupport = aggBrandDao.queryByPage(query);
		return brandBOPageSupport;
	}

	/**
	 * 查询参数组
	 */
	@Override
	public PageSupport<ProductPropertyAggParamGroupBO> getParamGroupById(ProductPropertyAggQuery query) {
		PageSupport<ProductPropertyAggParamGroupBO> page = aggParamGroupDao.queryByPage(query);
		List<Long> groupIds = page.getResultList().stream().map(ProductPropertyAggParamGroupBO::getId).collect(Collectors.toList());
		if (groupIds.size() > 0) {
			//查询参数组的参数属性
			List<ProductPropertyBO> propertyBOList = productPropertyService.queryByGroupId(groupIds);
			//将参数属性name放到Map中保存
			Map<Long, StringBuffer> paramsMap = new HashMap<>(16);
			groupIds.forEach(e -> {
				propertyBOList.forEach(item -> {
					if (item.getGroupId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramsMap.get(e) != null) {
							buffer = paramsMap.get(e);
						}
						paramsMap.put(e, buffer.append(item.getPropName() + ","));
					}
				});
			});
			//将参数属性name放到页面中
			page.getResultList().forEach(e -> {
				if (paramsMap.get(e.getId()) != null) {
					String s = paramsMap.get(e.getId()).toString();
					e.setPropertyStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return page;
	}

	@Override
	public List<ProductPropertyAggDTO> listAll() {
		return aggConverter.to(productPropertyAggDao.listAll());
	}

	@Override
	public ProductPropertyAggBO getDetailById(Long id) {
		ProductPropertyAggQuery query = new ProductPropertyAggQuery();
		query.setId(id);
		//查询全部，设置大小为300行
		query.setPageSize(300);
		//查询基本信息
		ProductPropertyAggBO productPropertyAggBO = getById(id);

		//设置规格属性
		productPropertyAggBO.setSpecificationList(getSpecificationById(query).getResultList());

		//设置参数属性
		productPropertyAggBO.setParamList(getParamById(query).getResultList());

		//设置品牌
		productPropertyAggBO.setBrandList(getBrandById(query).getResultList());

		//设置参数组属性
		productPropertyAggBO.setParamGroupList(getParamGroupById(query).getResultList());

		return productPropertyAggBO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteById(Long id) {
		//重置类目
		aggCategoryDao.deleteByAggId(id);
		//删除规格属性关联表
		aggSpecificationDao.deleteByAggId(id);
		//删除参数属性关联表
		aggParamDao.deleteByAggId(id);
		//删除参数组关联表
		aggParamGroupDao.deleteByAggId(id);
		//删除品牌关联表
		aggBrandDao.deleteByAggId(id);
		return productPropertyAggDao.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long save(ProductPropertyAggDTO propertyAggDTO) {
		if (ObjectUtil.isNotNull(propertyAggDTO.getId())) {
			update(propertyAggDTO);
			return propertyAggDTO.getId();
		}
		propertyAggDTO.setCreateTime(new Date());
		log.info("propertyAggDTO{}", propertyAggDTO);
		Long propertyAggId = productPropertyAggDao.save(aggConverter.from(propertyAggDTO));
		//保存类目关联表
		if (propertyAggDTO.getCategoryList() != null) {
			// 类目只能关联一个分组，所以这里删除
			aggCategoryDao.deleteByCategoryId(propertyAggDTO.getCategoryList().stream().map(ProductPropertyAggCategoryDTO::getCategoryId).collect(Collectors.toList()));
			List<ProductPropertyAggCategory> categoryList = propertyAggDTO.getCategoryList().stream()
					.map(e -> new ProductPropertyAggCategory(propertyAggId, e.getCategoryId(), e.getSeq()))
					.collect(Collectors.toList());
			aggCategoryDao.save(categoryList);
		}
		//保存规格属性关联表
		if (propertyAggDTO.getSpecificationList() != null) {
			List<ProductPropertyAggSpecification> specificationList = propertyAggDTO.getSpecificationList().stream()
					.map(e -> new ProductPropertyAggSpecification(propertyAggId, e.getPropId(), e.getSeq()))
					.collect(Collectors.toList());
			aggSpecificationDao.save(specificationList);
		}
		//保存参数属性关联表
		if (propertyAggDTO.getParamList() != null) {
			List<ProductPropertyAggParam> paramList = propertyAggDTO.getParamList().stream()
					.map(e -> new ProductPropertyAggParam(propertyAggId, e.getPropId(), e.getSeq()))
					.collect(Collectors.toList());
			aggParamDao.save(paramList);
		}
		//保存品牌关联表
		if (propertyAggDTO.getBrandIdList() != null) {
			List<ProductPropertyAggBrand> paramList = propertyAggDTO.getBrandIdList().stream()
					.map(e -> new ProductPropertyAggBrand(propertyAggId, e.getBrandId(), e.getSeq()))
					.collect(Collectors.toList());
			aggBrandDao.save(paramList);
		}
		//保存参数组关联表
		if (propertyAggDTO.getParamGroupList() != null) {
			List<ProductPropertyAggParamGroup> paramList = propertyAggDTO.getParamGroupList().stream()
					.map(e -> new ProductPropertyAggParamGroup(propertyAggId, e.getGroupId(), e.getSeq()))
					.collect(Collectors.toList());
			aggParamGroupDao.save(paramList);
		}
		return propertyAggId;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int update(ProductPropertyAggDTO propertyAggDTO) {
		ProductPropertyAgg orginAgg = productPropertyAggDao.getById(propertyAggDTO.getId());
		orginAgg.setName(propertyAggDTO.getName());
		orginAgg.setUpdateTime(new Date());
		productPropertyAggDao.update(orginAgg);
		//保存类目关联表，先重置，再修改
		aggCategoryDao.deleteByAggId(orginAgg.getId());
		if (propertyAggDTO.getCategoryList() != null) {
			// 类目只能关联一个分组，所以这里删除
			aggCategoryDao.deleteByCategoryId(propertyAggDTO.getCategoryList().stream().map(ProductPropertyAggCategoryDTO::getCategoryId).collect(Collectors.toList()));
			List<ProductPropertyAggCategory> categoryList = propertyAggDTO.getCategoryList().stream()
					.map(e -> new ProductPropertyAggCategory(orginAgg.getId(), e.getCategoryId(), e.getSeq()))
					.collect(Collectors.toList());
			aggCategoryDao.save(categoryList);
		}
		//保存规格属性关联表，先重置，再修改
		aggSpecificationDao.deleteByAggId(orginAgg.getId());
		if (propertyAggDTO.getSpecificationList() != null) {
			List<ProductPropertyAggSpecification> specificationList = propertyAggDTO.getSpecificationList().stream()
					.map(e -> new ProductPropertyAggSpecification(orginAgg.getId(), e.getPropId(), e.getSeq()))
					.collect(Collectors.toList());
			aggSpecificationDao.save(specificationList);
		}
		//保存参数属性关联表，先重置，再修改
		aggParamDao.deleteByAggId(orginAgg.getId());
		if (propertyAggDTO.getParamList() != null) {
			List<ProductPropertyAggParam> paramList = propertyAggDTO.getParamList().stream()
					.map(e -> new ProductPropertyAggParam(orginAgg.getId(), e.getPropId(), e.getSeq()))
					.collect(Collectors.toList());
			aggParamDao.save(paramList);
		}
		//保存品牌关联表，先重置，再修改
		aggBrandDao.deleteByAggId(orginAgg.getId());
		if (propertyAggDTO.getBrandIdList() != null) {
			List<ProductPropertyAggBrand> paramList = propertyAggDTO.getBrandIdList().stream()
					.map(e -> new ProductPropertyAggBrand(orginAgg.getId(), e.getBrandId(), e.getSeq()))
					.collect(Collectors.toList());
			aggBrandDao.save(paramList);
		}
		//保存参数组关联表，先重置，再修改
		aggParamGroupDao.deleteByAggId(orginAgg.getId());
		if (propertyAggDTO.getParamGroupList() != null) {
			List<ProductPropertyAggParamGroup> paramList = propertyAggDTO.getParamGroupList().stream()
					.map(e -> new ProductPropertyAggParamGroup(orginAgg.getId(), e.getGroupId(), e.getSeq()))
					.collect(Collectors.toList());
			aggParamGroupDao.save(paramList);
		}
		return 1;
	}

	@Override
	public PageSupport<ProductPropertyAggBO> querySimplePage(ProductPropertyAggQuery query) {
		return aggConverter.convert2BoPageList(productPropertyAggDao.querySimplePage(query));
	}

	@Override
	public PageSupport<ProductPropertyAggBO> queryPage(ProductPropertyAggQuery query) {
		PageSupport<ProductPropertyAggBO> pageList = aggConverter.convert2BoPageList(productPropertyAggDao.queryProductTypePage(query));
		List<Long> aggIdList = pageList.getResultList().stream().map(ProductPropertyAggBO::getId).collect(Collectors.toList());
		if (aggIdList.size() > 0) {
			//查询类目属性
			List<ProductPropertyAggCategoryBO> aggCategoryList = aggCategoryDao.queryByAggId(aggIdList);
			//查询规格属性
			List<ProductPropertyBO> specificationList = aggSpecificationDao.queryByAggId(aggIdList);
			//查询参数属性
			List<ProductPropertyBO> paramList = aggParamDao.queryByAggId(aggIdList);
			//查询品牌
			List<BrandBO> brandList = aggBrandDao.queryBrandByAggId(aggIdList);
			//查询参数组的参数属性
			List<ParamGroupBO> paramGroupList = aggParamGroupDao.queryByAggId(aggIdList);

			//将类目属性name放到Map中保存
			Map<Long, StringBuffer> categoryMap = new HashMap<>(16);
			aggIdList.forEach(e -> {
				aggCategoryList.forEach(item -> {
					if (e.compareTo(item.getAggId()) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (categoryMap.get(e) != null) {
							buffer = categoryMap.get(e);
						}
						categoryMap.put(e, buffer.append(item.getName() + ","));
					}
				});
			});
			//将规格属性name放到Map中保存
			Map<Long, StringBuffer> specificationMap = new HashMap<>(16);
			aggIdList.forEach(e -> {
				specificationList.forEach(item -> {
					if (e.compareTo(item.getAggId()) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (specificationMap.get(e) != null) {
							buffer = specificationMap.get(e);
						}
						specificationMap.put(e, buffer.append(item.getPropName() + ","));
					}
				});
			});
			//将参数属性name放到Map中保存
			Map<Long, StringBuffer> paramMap = new HashMap<>(16);
			aggIdList.forEach(e -> {
				paramList.forEach(item -> {
					if (e.compareTo(item.getAggId()) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramMap.get(e) != null) {
							buffer = paramMap.get(e);
						}
						paramMap.put(e, buffer.append(item.getPropName() + ","));
					}
				});
			});
			//将品牌name放到Map中保存
			Map<Long, StringBuffer> brandMap = new HashMap<>(16);
			aggIdList.forEach(e -> {
				brandList.forEach(item -> {
					if (e.compareTo(item.getAggId()) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (brandMap.get(e) != null) {
							buffer = brandMap.get(e);
						}
						brandMap.put(e, buffer.append(item.getBrandName() + ","));
					}
				});
			});
			//将参数组name放到Map中保存
			Map<Long, StringBuffer> paramGroupMap = new HashMap<>(16);
			aggIdList.forEach(e -> {
				paramGroupList.forEach(item -> {
					if (e.compareTo(item.getAggId()) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramGroupMap.get(e) != null) {
							buffer = paramGroupMap.get(e);
						}
						paramGroupMap.put(e, buffer.append(item.getName() + ","));
					}
				});
			});
			pageList.getResultList().forEach(item -> {
				//设置类目
				if (categoryMap.get(item.getId()) != null) {
					String s = categoryMap.get(item.getId()).toString();
					item.setCategoryStr(s.substring(0, s.length() - 1));
				}
				//设置规格属性
				if (specificationMap.get(item.getId()) != null) {
					String s = specificationMap.get(item.getId()).toString();
					item.setSpecificationStr(s.substring(0, s.length() - 1));
				}
				//设置参数属性
				if (paramMap.get(item.getId()) != null) {
					String s = paramMap.get(item.getId()).toString();
					item.setParamStr(s.substring(0, s.length() - 1));
				}
				//设置品牌
				if (brandMap.get(item.getId()) != null) {
					String s = brandMap.get(item.getId()).toString();
					item.setBrandStr(s.substring(0, s.length() - 1));
				}
				//设置参数组属性
				if (paramGroupMap.get(item.getId()) != null) {
					String s = paramGroupMap.get(item.getId()).toString();
					item.setParamGroupStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return pageList;
	}
}
