/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dao.VitLogDao;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.entity.VitLog;
import com.legendshop.product.query.VitLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 访问历史Dao实现类
 *
 * @author legendshop
 */
@Repository
public class VitLogDaoImpl extends GenericDaoImpl<VitLog, Long> implements VitLogDao {


	/**
	 * 用户端 商品浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	@Override
	public List<VitLog> vitLogListPage(VitLogQuery vitLogQuery) {
		String sql = "SELECT product_id,product_name,pic,price FROM ls_vit_log WHERE user_id=? AND source='PC' AND user_del_flag=0";
		return queryLimit(sql, VitLog.class, 0, 5, vitLogQuery.getUserId());
	}


	@Override
	public PageSupport<VitLog> queryVitListPage(VitLogQuery vitLogQuery) {

		CriteriaQuery cq = new CriteriaQuery(VitLog.class, vitLogQuery.getPageSize(), vitLogQuery.getCurPage());
		if (null != vitLogQuery.getUserId()) {
			cq.eq("userId", vitLogQuery.getUserId());
		}
		if (ObjectUtil.isNotEmpty(vitLogQuery.getStartTime())) {
			cq.ge("createTime", cn.hutool.core.date.DateUtil.beginOfDay(vitLogQuery.getStartTime()));
		}
		if (ObjectUtil.isNotEmpty(vitLogQuery.getEndTime())) {
			cq.le("createTime", cn.hutool.core.date.DateUtil.endOfDay(vitLogQuery.getEndTime()));
		}
		if (StringUtils.isNotBlank(vitLogQuery.getProductName())) {
			cq.like("productName", vitLogQuery.getProductName(), MatchMode.ANYWHERE);
		}
		if (ObjectUtil.isNotNull(vitLogQuery.getPage())) {
			cq.eq("page", vitLogQuery.getPage());
		}
		cq.eq("userDelFlag", vitLogQuery.getUserDelFlag());
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	/**
	 * 默认查询当前用户足迹
	 *
	 * @return
	 */
	@Override
	public PageSupport<VitLog> queryUserVitList(Integer pageSize, Integer curPage) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(VitLog.class, pageSize, curPage);
		map.put("userId", SecurityUtils.getUser().getUserId());
		hql.setSqlAndParameter("VitLog.getUserVisitLogListPage", map);
		return querySimplePage(hql);
	}

	@Override
	public VitLogDTO getByUserIdAndProductId(Long productId, Long userId) {
		return get("SELECT * from ls_vit_log where user_id=? and product_id=? ", VitLogDTO.class, userId, productId);
	}

	@Override
	public Integer userVisitLogCount(Long userId) {
		return get("SELECT COUNT(*)  FROM (SELECT COUNT(vl.id) FROM ls_vit_log vl WHERE vl.user_id = ? AND vl.product_id is not null and vl.user_del_flag=0 GROUP BY vl.product_id, DATE( vl.create_time )) vis", Integer.class, userId);
	}

	@Override
	public VitLog getVisitedShopIndexLog(VitLogRecordDTO recordDTO) {

		// 30分钟前
		DateTime offsetMinute = DateUtil.offsetMinute(new Date(), -30);
		String sql = "SELECT * FROM ls_vit_log v WHERE v.ip = ? AND v.shop_id = ? AND v.user_id = ?  AND v.create_time > ?";
		List<VitLog> vitLogs = queryLimit(sql, VitLog.class, 0, 1, recordDTO.getIp(), recordDTO.getShopId(), recordDTO.getUserId(), offsetMinute);
		if (CollectionUtil.isNotEmpty(vitLogs)) {
			return vitLogs.get(0);
		}
		return null;
	}

	@Override
	public VitLog getVisitedProductLog(VitLogRecordDTO recordDTO) {
		// 30分钟前
		DateTime offsetMinute = DateUtil.offsetMinute(new Date(), -30);
		String sql = "SELECT * FROM ls_vit_log v WHERE v.ip = ? AND v.product_id = ? AND v.user_id = ?  AND v.create_time > ?";
		List<VitLog> vitLogs = queryLimit(sql, VitLog.class, 0, 1, recordDTO.getIp(), recordDTO.getProductId(), recordDTO.getUserId(), offsetMinute);
		if (CollectionUtil.isNotEmpty(vitLogs)) {
			return vitLogs.get(0);
		}
		return null;
	}

	@Override
	public PageSupport<VitLog> queryVitListPageByUser(VitLogQuery vitLogQuery) {

		SimpleSqlQuery query = new SimpleSqlQuery(VitLog.class, vitLogQuery.getPageSize(), vitLogQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", vitLogQuery.getUserId());
		map.put("page", vitLogQuery.getPage());
		map.put("userDelFlag", vitLogQuery.getUserDelFlag());
		map.put("productName", vitLogQuery.getProductName());
		if (ObjectUtil.isNotEmpty(vitLogQuery.getStartTime())) {
			map.put("startDate", DateUtil.beginOfDay(vitLogQuery.getStartTime()));
		}
		if (ObjectUtil.isNotEmpty(vitLogQuery.getEndTime())) {
			map.put("endDate", DateUtil.endOfDay(vitLogQuery.getEndTime()));
		}
		query.setSqlAndParameter("VitLog.getVisitLogListPage", map);
		return querySimplePage(query);
	}

	@Override
	public Boolean deleteUserVitLog(Long userId, Long productId) {
		if (productId == null) {
			String sql = "update ls_vit_log set user_del_flag = 1  WHERE user_id=? and user_del_flag =0";
			return update(sql, userId) > -1;
		}
		//删除当个商品的所有浏览记录
		else {
			String sql = "update ls_vit_log set user_del_flag = 1  WHERE user_id=? AND product_id=? and user_del_flag =0";
			return update(sql, userId, productId) > -1;
		}
	}

	@Override
	public List<VitLogDTO> queryVitList(Long userId, Long shopId) {
		String sql = "select lv.product_id, lv.product_name, lv.pic, lp.price, lp.buys, lp.stocks from ls_vit_log lv left join ls_product lp on lv.product_id = lp.id where lv.user_del_flag = 0 and lv.user_id = ? and (lv.shop_id = ? or lp.shop_id = ?) GROUP BY lv.id ORDER BY lv.create_time DESC LIMIT 0, 20";
		return query(sql, VitLogDTO.class, userId, shopId, shopId);
	}
}
