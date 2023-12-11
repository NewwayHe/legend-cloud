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
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.bo.ProductGroupConditionalBO;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.dao.ProductGroupDao;
import com.legendshop.product.dao.ProductGroupRelationDao;
import com.legendshop.product.dto.ProductGroupDTO;
import com.legendshop.product.entity.ProductGroup;
import com.legendshop.product.enums.ProductGroupTypeEnum;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;
import com.legendshop.product.service.ProductGroupService;
import com.legendshop.product.service.convert.ProductGroupConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ProductGroupServiceImpl.
 * 服务实现类
 *
 * @author legendshop
 */
@Slf4j
@Service
public class ProductGroupServiceImpl extends BaseServiceImpl<ProductGroupDTO, ProductGroupDao, ProductGroupConverter> implements ProductGroupService {

	/**
	 * 引用的Dao接口
	 */
	@Autowired
	private ProductGroupDao productGroupDao;

	@Autowired
	private ProductGroupConverter productGroupConverter;

	@Autowired
	private ProductGroupRelationDao relationDao;


	@Override
	public ProductGroupBO getProductGroup(Long id) {
		return productGroupConverter.convert2BO(productGroupDao.getById(id));
	}

	@Override
	public PageSupport<ProductGroupBO> queryProductGroupListPage(ProductGroupQuery productGroupQuery) {
		return productGroupConverter.convert2BoPageList(productGroupDao.queryProductGroupPage(productGroupQuery));
	}

	@Override
	public PageSupport<ProductGroupDTO> page(ProductGroupQuery productGroupQuery) {
		return productGroupConverter.page(productGroupDao.page(productGroupQuery));
	}

	@Override
	public PageSupport<ProductGroupRelationBO> queryProductList(ProductGroupRelationQuery relationQuery) {
		ProductGroup productGroup = productGroupDao.getById(relationQuery.getGroupId());
		if (null == productGroup) {
			return new PageSupport<>();
		}

		if (ObjectUtil.isEmpty(relationQuery.getSort())) {
			relationQuery.setSort(StrUtil.toUnderlineCase(productGroup.getSort()));
		}

		if (ProductGroupTypeEnum.SYSTEM.getValue().equals(productGroup.getType())) {
			//获取系统定义分组的排序条件
			String conditional = productGroup.getConditional();
			ProductGroupConditionalBO productGroupConditionalBO = JSONUtil.toBean(conditional, ProductGroupConditionalBO.class);
			relationQuery.setSort(StrUtil.toUnderlineCase(productGroupConditionalBO.getSortBy()));
			relationQuery.setDescending("desc".equals(productGroupConditionalBO.getDescending()));
			relationQuery.setGroupId(null);
		}
		return productGroupDao.queryProductList(relationQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delGroupAndRelation(Long id) {
		if (productGroupDao.deleteById(id) > 0) {
			relationDao.deleteProdGroupRelevanceByGroupId(id);
			log.info("删除商品分组：{}", id);
			return true;
		}
		return false;
	}


}
