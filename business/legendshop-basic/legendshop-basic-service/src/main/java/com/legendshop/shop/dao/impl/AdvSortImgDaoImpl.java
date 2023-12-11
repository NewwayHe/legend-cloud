/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.shop.dao.AdvSortImgDao;
import com.legendshop.shop.entity.AdvSortImg;
import com.legendshop.shop.query.AdvSortImgQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * (AdvSortImg)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-07-09 15:28:04
 */
@Repository
public class AdvSortImgDaoImpl extends GenericDaoImpl<AdvSortImg, Long> implements AdvSortImgDao {

	@Override
	public List<AdvSortImg> queryByAdvSortId(Long sortId) {
		return queryByProperties(new LambdaEntityCriterion<>(AdvSortImg.class).eq(AdvSortImg::getCategoryId, sortId));
	}

	@Override
	public PageSupport<AdvSortImg> queryPageImg(AdvSortImgQuery advSortImgQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(AdvSortImg.class, advSortImgQuery.getPageSize(), advSortImgQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("status", advSortImgQuery.getStatus());
		map.put("categoryId", advSortImgQuery.getCategoryId());
		map.like("name", advSortImgQuery.getAdvImgName(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotEmpty(advSortImgQuery.getProp()) && ObjectUtil.isNotEmpty(advSortImgQuery.getOrder())) {
			map.put("orderBy", " order by " + advSortImgQuery.getProp() + " " + advSortImgQuery.getOrder());
		}
		query.setSqlAndParameter("AdvSortImg.page", map);
		return querySimplePage(query);
	}

	@Override
	public List<CategoryBO> getTopCategory() {
		return query(getSQL("AdvSortImg.getTopCategory"), CategoryBO.class);
	}

	@Override
	public void batchUpdateStatus(List<Long> ids, Integer status) {
		List<Object[]> args = new ArrayList<>();
		String sql = "update ls_adv_sort_img set status =? where id = ?";
		ids.forEach(id -> {
			args.add(new Object[]{status, id});
		});
		batchUpdate(sql, args);
	}

	@Override
	public List<AdvSortImg> queryByCategoryId(Long categoryId) {
		QueryMap map = new QueryMap();
		map.put("categoryId", categoryId);
		SQLOperation operation = this.getSQLAndParams("AdvSortImg.queryByCategoryId", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(AdvSortImg.class));
	}
}
