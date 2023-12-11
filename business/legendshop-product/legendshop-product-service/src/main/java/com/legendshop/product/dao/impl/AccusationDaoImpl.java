/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.IllegalProductBO;
import com.legendshop.product.dao.AccusationDao;
import com.legendshop.product.entity.Accusation;
import com.legendshop.product.enums.AccusationUserTypeEnum;
import com.legendshop.product.excel.IllegalExportDTO;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.query.ProductQuery;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 举报表Dao.
 *
 * @author legendshop
 */
@Repository
public class AccusationDaoImpl extends GenericDaoImpl<Accusation, Long> implements AccusationDao {

	/**
	 * 根据用户类型加载不同的数据，用户查用户表， 管理员查管理员的表
	 *
	 * @param id
	 * @return
	 */
	@Override
	public AccusationBO getAccusation(Long id) {
		Accusation accusation = this.getById(id);
		if (accusation == null) {
			return null;
		}

		if (AccusationUserTypeEnum.USER.getValue().equals(accusation.getUserType())) {
			return get(getSQL("Accusation.getUserAccusation"), AccusationBO.class, id);
		}

		if (AccusationUserTypeEnum.ADMIN.getValue().equals(accusation.getUserType())) {
			return get(getSQL("Accusation.getAdminAccusation"), AccusationBO.class, id);
		}

		return null;
	}

	@Override
	public PageSupport<AccusationBO> queryAccusation(AccusationReportQuery accusationQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(AccusationBO.class, accusationQuery.getPageSize(), accusationQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("typeId", accusationQuery.getTypeId());
		map.put("shopId", accusationQuery.getShopId());
		map.put("status", accusationQuery.getStatus());
		map.put("result", accusationQuery.getResult());
		if (StrUtil.isNotBlank(accusationQuery.getStartDate())) {
			map.put("begDate", DateUtil.beginOfDay(DateUtil.parse(accusationQuery.getStartDate())));
		}
		if (StrUtil.isNotBlank(accusationQuery.getEndDate())) {
			map.put("endDate", DateUtil.endOfDay(DateUtil.parse(accusationQuery.getEndDate())));
		}
		map.like("productName", accusationQuery.getProductName(), MatchMode.ANYWHERE);
		query.setSqlAndParameter("Accusation.queryAccusation", map);
		return querySimplePage(query);
	}


	@Override
	public PageSupport<AccusationBO> queryMyAccusation(AccusationQuery accusationQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(AccusationBO.class, accusationQuery.getPageSize(), accusationQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", accusationQuery.getUserId());
		map.put("typeId", accusationQuery.getTypeId());
		map.put("begDate", accusationQuery.getBegDate());
		map.put("endDate", accusationQuery.getEndDate());
		map.like("content", accusationQuery.getContent(), MatchMode.ANYWHERE);
		query.setSqlAndParameter("Accusation.queryMyAccusation", map);
		return querySimplePage(query);
	}

	@Override
	public Integer getCountByTypeId(List<Long> longList) {
		StringBuffer sb = new StringBuffer("select count(1) from ls_accusation where type_id in(");
		for (Long id : longList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return get(sb.toString(), Integer.class, longList.toArray());
	}

	@Override
	public PageSupport<AccusationBO> shopPage(AccusationQuery query) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(AccusationBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", query.getShopId());
		map.put("accStatus", query.getStatus());
		map.put("accusationTypeId", query.getTypeId());
		map.put("accResult", query.getResult());
		map.like("productName", query.getProductName(), MatchMode.ANYWHERE);

		Date start_Date = null;
		Date end_Date = null;
		try {
			start_Date = DateUtil.parseDate(query.getBegDate());
			end_Date = DateUtil.parseDate(query.getEndDate());
			Calendar c = Calendar.getInstance();
			c.setTime(end_Date);
			c.add(Calendar.DAY_OF_MONTH, 1);
			end_Date = c.getTime();
		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("startDate", start_Date);
		map.put("endDate", end_Date);
		sqlQuery.setSqlAndParameter("Accusation.accusationPage", map);
		return querySimplePage(sqlQuery);
	}


	@Override
	public PageSupport<IllegalProductBO> illegalPage(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(IllegalProductBO.class, productQuery.getPageSize(), productQuery.getCurPage());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.put("shopId", productQuery.getShopId());
		hql.setSqlAndParameter("Accusation.illegalPage", map);
		return querySimplePage(hql);
	}

	@Override
	public List<IllegalExportDTO> illegalList(ProductQuery query) {
		if (ObjectUtil.isNotNull(query)) {
			StringBuilder sb = new StringBuilder();
			List<Object> obj = new ArrayList<Object>();
			sb.append("select p.id,p.pic,p.name,p.stocks,p.price,brand.brand_name, acc.handle_time,acc.handle_info, p.op_status ");
			sb.append("from ls_product p,ls_accusation acc,ls_accusation_type type,ls_brand brand ");
			sb.append("where acc.type_id=type.id and acc.product_id=p.id and brand.id=p.brand_id and p.op_status=2 AND p.del_status = 1 AND p.del_status <> -2 ");
			if (CollUtil.isNotEmpty(query.getProductIdList())) {
				sb.append(" AND p.id in(");
				for (Long productId : query.getProductIdList()) {
					sb.append("?,");
					obj.add(productId);
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			} else {
				if (ObjectUtil.isNotNull(query.getShopId())) {
					sb.append(" AND p.shop_id = ?");
					obj.add(query.getShopId());
				}
				if (ObjectUtil.isNotNull(query.getBrandId())) {
					sb.append(" AND p.brand_id=?");
					obj.add(query.getBrandId());
				}
				if (StrUtil.isNotBlank(query.getName())) {
					sb.append(" AND p.name LIKE ?");
					obj.add("%" + query.getName().trim() + "%");
				}
			}

			//4. 审核异常的商品
			if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(query.getOpStatus())) {
				sb.append(" and p.op_status in (2, 3)");
			} else {
				sb.append(" and p.op_status = ? ");
				obj.add(query.getOpStatus());
			}
			sb.append(" order by acc.update_time desc");
			return this.query(sb.toString(), IllegalExportDTO.class, obj.toArray());
		}
		return Collections.singletonList(new IllegalExportDTO());
	}

	@Override
	public List<AccusationBO> queryLatestAccusation(List<Long> prodIds) {
		if (CollUtil.isEmpty(prodIds)) {
			return Collections.emptyList();
		}
		StringBuilder sql = new StringBuilder("SELECT * FROM ls_accusation la WHERE la.handle_time = (SELECT MAX(lb.handle_time) FROM ls_accusation lb WHERE lb.STATUS = 1 AND lb.result = 1 AND la.product_id = lb.product_id) AND la.product_id in (");
		for (Long prodId : prodIds) {
			sql.append(prodId).append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return query(sql.toString(), AccusationBO.class);
	}
}
