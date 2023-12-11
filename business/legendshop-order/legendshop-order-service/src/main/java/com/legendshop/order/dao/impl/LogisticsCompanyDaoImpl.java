/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.alibaba.excel.util.CollectionUtils;
import com.legendshop.order.dao.LogisticsCompanyDao;
import com.legendshop.order.entity.LogisticsCompany;
import com.legendshop.order.query.LogisticsCompanyQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.legendshop.jpaplus.criterion.MatchMode.ANYWHERE;

/**
 * 物流公司服务
 *
 * @author legendshop
 */
@Repository
public class LogisticsCompanyDaoImpl extends GenericDaoImpl<LogisticsCompany, Long> implements LogisticsCompanyDao {

	@Override
	public LogisticsCompany getByTemplateId(Long templateId) {
		return get("select ld.* from ls_logistics_company ld left join ls_logistics_comment_statistics ldt " +
				"on ld.id=ldt.count where ldt.dvy_type_id=? ", LogisticsCompany.class, templateId);
	}

	@Override
	public void deleteByShopId(Long shopId) {
		update("delete from ls_logistics_company where shop_id = ?", shopId);
	}

	@Override
	public PageSupport<LogisticsCompany> queryPage(LogisticsCompanyQuery logisticsCompanyQuery) {
		CriteriaQuery cq = new CriteriaQuery(LogisticsCompany.class, logisticsCompanyQuery.getPageSize(), logisticsCompanyQuery.getCurPage());
		cq.eq("shopId", logisticsCompanyQuery.getShopId());
		cq.like("name", logisticsCompanyQuery.getName(), ANYWHERE);
		cq.like("companyCode", logisticsCompanyQuery.getCompanyCode(), ANYWHERE);
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<LogisticsCompany> getList(Long shopId) {
		return queryByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public int addUseCount(Long id) {
		return update("update ls_logistics_company set use_count = use_count + 1 where id = ?", id);
	}

	@Override
	public int subUseCount(Long id) {
		return update("update ls_logistics_company set use_count = use_count - 1 where id = ?", id);
	}

	@Override
	public List<LogisticsCompany> queryAllByShopId(Long shopId) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public LogisticsCompany getByShopId(Long logisticsId, Long shopId) {
		return getByProperties(new EntityCriterion().eq("shopId", shopId).eq("id", logisticsId));
	}

	@Override
	public List<LogisticsCompany> queryByNameList(ArrayList<String> logisticsCompanyList) {
		if (CollectionUtils.isEmpty(logisticsCompanyList)) {
			return new ArrayList<>();
		}
		return this.queryByProperties(new EntityCriterion().in("name", logisticsCompanyList.toArray()));
	}


}
