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
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.ParamGroupDTO;
import com.legendshop.product.entity.ParamGroup;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ProductPropertyParamGroup;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ParamGroupQuery;
import com.legendshop.product.service.ParamGroupService;
import com.legendshop.product.service.convert.ParamGroupConverter;
import com.legendshop.product.service.convert.ProductPropertyValueConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 参数组列表ServiceImpl.
 *
 * @author legendshop
 */
@Service
public class ParamGroupServiceImpl implements ParamGroupService {

	@Autowired
	private ParamGroupDao ParamGroupDao;

	@Autowired
	private ProductPropertyAggParamGroupDao aggParamGroupDao;

	@Autowired
	private ProductPropertyValueDao propertyValueDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductPropertyAggParamDao aggParamDao;

	@Autowired
	private ProductPropertyParamGroupDao paramGroupDao;
	@Autowired
	private ParamGroupConverter paramGroupConverter;
	@Autowired
	private ProductPropertyValueConverter valueConverter;

	@Override
	public ParamGroupBO getById(Long id) {
		ParamGroupBO paramGroupBO = paramGroupConverter.convert2Bo(ParamGroupDao.getById(id));
		paramGroupBO.setParams(ParamGroupDao.getPropByGroupId(id));
		return paramGroupBO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteParamGroupById(Long id) {
		//先删除掉关联的参数
		paramGroupDao.deleteByGroupId(id);
		return ParamGroupDao.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveParamGroup(ParamGroupDTO paramGroupDTO) {
		Long paramGroupId = 0L;
		if (ObjectUtil.isNotNull(paramGroupDTO.getId())) {
			ParamGroupDao.update(paramGroupConverter.from(paramGroupDTO));
			paramGroupId = paramGroupDTO.getId();
		} else {
			paramGroupDTO.setCreateTime(new Date());
			paramGroupId = ParamGroupDao.save(paramGroupConverter.from(paramGroupDTO));
		}
		List<ProductPropertyParamGroup> list = new ArrayList<>();
		//默认排序排在最后
		int count = paramGroupDao.getCountByGroupId(paramGroupId);
		for (int i = 0; i < paramGroupDTO.getParamsIdList().size(); i++) {
			ProductPropertyParamGroup group = new ProductPropertyParamGroup(paramGroupId, paramGroupDTO.getParamsIdList().get(i), count + 1 + i);
			list.add(group);
		}
		paramGroupDao.save(list);
		return paramGroupId;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateParamGroup(ParamGroupDTO paramGroupDTO) {
		ParamGroup origin = ParamGroupDao.getById(paramGroupDTO.getId());
		BeanUtil.copyProperties(paramGroupDTO, origin, new CopyOptions().ignoreNullValue());
		int result = ParamGroupDao.update(origin);
		Long paramGroupId = paramGroupDTO.getId();
		//先清除掉，再保存
		paramGroupDao.deleteByGroupId(paramGroupId);
		if (paramGroupDTO.getParamsIdList().size() > 0) {
			List<ProductPropertyParamGroup> list = new ArrayList<>();
			for (int i = 0; i < paramGroupDTO.getParamsIdList().size(); i++) {
				ProductPropertyParamGroup group = new ProductPropertyParamGroup(paramGroupId, paramGroupDTO.getParamsIdList().get(i), 1 + i);
				list.add(group);
			}
			paramGroupDao.save(list);
		}

		return result;
	}

	@Override
	public PageSupport<ParamGroupBO> getParamGroupPage(ParamGroupQuery paramGroupQuery) {
		PageSupport<ParamGroupBO> pageSupport = paramGroupConverter.convert2BoPageList(ParamGroupDao.getParamGroupPage(paramGroupQuery));
		//获取参数组id集合
		List<Long> groupIdList = pageSupport.getResultList().stream().map(ParamGroupBO::getId).collect(Collectors.toList());
		if (groupIdList.size() > 0) {
			//获取类目关联管理名
			List<ProductPropertyAggBO> aggBOList = aggParamDao.queryAggNameByGroupId(groupIdList);
			//获取参数名
			List<ProductPropertyBO> aggList = paramGroupDao.queryPropertyNameByGroupId(groupIdList);

			//收集参数名
			Map<Long, StringBuffer> paramMap = new HashMap<>(16);
			groupIdList.forEach(e -> {
				aggList.forEach(i -> {
					if (i.getGroupId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (paramMap.get(e) != null) {
							buffer = paramMap.get(e);
						}
						paramMap.put(e, buffer.append(i.getPropName() + ","));
					}
				});
			});

			//收集类目关联管理名
			Map<Long, StringBuffer> aggMap = new HashMap<>(16);
			groupIdList.forEach(e -> {
				aggBOList.forEach(i -> {
					if (i.getGroupId().compareTo(e) == 0) {
						StringBuffer buffer = new StringBuffer();
						if (aggMap.get(e) != null) {
							buffer = aggMap.get(e);
						}
						aggMap.put(e, buffer.append(i.getName() + ","));
					}
				});
			});

			//设置参数值、类目关联管理名
			pageSupport.getResultList().forEach(e -> {
				//设置参数值
				if (paramMap.get(e.getId()) != null) {
					String s = paramMap.get(e.getId()).toString();
					e.setParamsStr(s.substring(0, s.length() - 1));
				}
				//设置类目关联管理名
				if (aggMap.get(e.getId()) != null) {
					String s = aggMap.get(e.getId()).toString();
					e.setPropertyAggStr(s.substring(0, s.length() - 1));
				}
			});
		}
		return pageSupport;
	}

	@Override
	public PageSupport<ProductPropertyBO> getParamAndValueById(ParamGroupQuery query) {
		PageSupport<ProductPropertyBO> page = aggParamDao.getParamAndValueById(query);

		//获取属性id集合
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

	@Override
	public List<ProductPropertyBO> getDetailByIds(ArrayList<Long> groupIds) {
		if (groupIds.size() == 0) {
			return new ArrayList<>();
		}
		/*根据参数组groupIds,查询所有的参数组集合*/
		List<ParamGroupBO> paramGroupList = paramGroupConverter.convert2BoList(ParamGroupDao.queryAllByIds(groupIds));
		/*根据参数组groupIds,查询所有的属性集合*/
		List<ProductPropertyBO> propertyList = paramGroupDao.queryPropertyNameByGroupId(groupIds);
		/*获取属性id集合*/
		List<Long> propIds = propertyList.stream().map(ProductPropertyBO::getId).collect(Collectors.toList());
		/*根据属性id集合,查询所有的属性值集合*/
		List<ProductPropertyValueBO> valueList = valueConverter.convert2BoList(propertyValueDao.queryByPropId(propIds));

		/*收集属性值*/
		propertyList.forEach(productPropertyBO -> valueList.forEach(productPropertyValueBO -> {
			if (productPropertyBO.getProdPropList() == null) {
				productPropertyBO.setProdPropList(new ArrayList<>());
			}
			productPropertyBO.addProductPropertyValueList(productPropertyValueBO);
		}));

		/*收集属性*/
		paramGroupList.forEach(paramGroupBO -> propertyList.forEach(productPropertyBO -> {
			productPropertyBO.setSource(ProductPropertySourceEnum.USER.value());
			if (paramGroupBO.getId().equals(productPropertyBO.getGroupId())) {
				productPropertyBO.setGroupName(paramGroupBO.getName());
			}
		}));

		return propertyList;
	}

	@Override
	public List<ProductPropertyBO> getDetailByCategoryId(long id, Long productId) {
		List<ProductPropertyBO> propertyList;
		if (ObjectUtil.isNotEmpty(productId)) {
			Product product = productDao.getById(productId);
			propertyList = JSONUtil.toList(JSONUtil.parseArray(product.getParamGroup()), ProductPropertyBO.class);
			propertyList.addAll(JSONUtil.toList(JSONUtil.parseArray(product.getUserParamGroup()), ProductPropertyBO.class));
			return propertyList;
		}
		/*根据类目id,查询所有的参数组集合*/
		List<ParamGroupBO> paramGroupList = aggParamGroupDao.queryByCategoryId(id);
		if (paramGroupList.size() == 0) {
			return new ArrayList<>();
		}
		/*根据参数组groupIds,查询所有的属性集合*/
		List<Long> groupIds = paramGroupList.stream().map(ParamGroupBO::getId).collect(Collectors.toList());
		propertyList = paramGroupDao.queryPropertyNameByGroupId(groupIds);
		/*获取属性id集合*/
		List<Long> propIds = propertyList.stream().map(ProductPropertyBO::getId).collect(Collectors.toList());
		/*根据属性id集合,查询所有的属性值集合*/
		List<ProductPropertyValueBO> valueList = valueConverter.convert2BoList(propertyValueDao.queryByPropId(propIds));

		/*收集属性值*/
		propertyList.forEach(productPropertyBO -> valueList.forEach(productPropertyValueBO -> {
			if (productPropertyBO.getProdPropList() == null) {
				productPropertyBO.setProdPropList(new ArrayList<>());
			}
			productPropertyBO.addProductPropertyValueList(productPropertyValueBO);
		}));

		/*设置参数组属性到属性里*/
		for (ParamGroupBO paramGroupBO : paramGroupList) {
			for (ProductPropertyBO productPropertyBO : propertyList) {
				if (productPropertyBO.getGroupId().compareTo(paramGroupBO.getId()) == 0) {
					productPropertyBO.setSource(paramGroupBO.getSource());
					productPropertyBO.setGroupName(paramGroupBO.getName());
				}
			}
		}

		return propertyList;
	}

	@Override
	public List<ParamGroupBO> allOnline() {
		return ParamGroupDao.queryAllOnline();
	}

}
