/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.dao.ProductGroupRelationDao;
import com.legendshop.product.dto.ProductGroupRelationDTO;
import com.legendshop.product.entity.ProductGroupRelation;
import com.legendshop.product.service.ProductGroupRelationService;
import com.legendshop.product.service.convert.ProductGroupRelationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品-分组关联服务实现
 *
 * @author legendshop
 */
@Service
public class ProductGroupRelationServiceImpl extends BaseServiceImpl<ProductGroupRelationDTO, ProductGroupRelationDao, ProductGroupRelationConverter> implements ProductGroupRelationService {


	@Autowired
	private ProductGroupRelationDao productGroupRelationDao;


	@Override
	public List<Long> getProductIdListByGroupId(Long prodGroupId) {
		return productGroupRelationDao.getProductIdListByGroupId(prodGroupId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveRelation(String productIds, Long groupId) {
		//先删除原有关联数据，再重新插入数据
		productGroupRelationDao.deleteProdGroupRelevanceByGroupId(groupId);
		String[] productIdArr = productIds.split(",");
		List<ProductGroupRelation> list = new ArrayList<>();
		for (String productId : productIdArr) {
			ProductGroupRelation relation = new ProductGroupRelation();
			relation.setGroupId(groupId);
			relation.setProductId(Long.parseLong(productId));
			list.add(relation);
		}
		productGroupRelationDao.save(list);
		return true;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveRelation(String productIds, String groupIds) {
		String[] productIdArr = productIds.split(",");
		String[] groupIdArr = groupIds.split(",");
		for (String groupId : groupIdArr) {
			List<ProductGroupRelation> list = new ArrayList<>();
			for (String productId : productIdArr) {
				int num = productGroupRelationDao.getProductByGroupId(Long.parseLong(productId), Long.parseLong(groupId));
				if (num <= 0) {
					ProductGroupRelation relation = new ProductGroupRelation();
					relation.setProductId(Long.parseLong(productId));
					relation.setGroupId(Long.parseLong(groupId));
					list.add(relation);
				}
			}
			if (list.size() > 0) {
				productGroupRelationDao.save(list);
			}
		}
		return true;
	}

}
