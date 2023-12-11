/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SmsLogDao;
import com.legendshop.basic.entity.SmsLog;
import com.legendshop.basic.query.SmsLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 短信记录服务Dao
 *
 * @author legendshop
 */
@Repository
public class SmsLogDaoImpl extends GenericDaoImpl<SmsLog, Long> implements SmsLogDao {

	@Override
	public PageSupport<SmsLog> getSmsLogPage(SmsLogQuery smsLogQuery) {
		CriteriaQuery cq = new CriteriaQuery(SmsLog.class, smsLogQuery.getPageSize(), smsLogQuery.getCurPage());

		if (StringUtils.isNotBlank(smsLogQuery.getMobilePhone())) {
			cq.like("mobilePhone", smsLogQuery.getMobilePhone(), MatchMode.ANYWHERE);
		}
		if (null != smsLogQuery.getCreateTime()) {
			cq.gt("createTime", DateUtil.beginOfDay(DateUtil.parse(smsLogQuery.getCreateTime())));
			cq.lt("createTime", DateUtil.endOfDay(DateUtil.parse(smsLogQuery.getCreateTime())));
		}
		if (null != smsLogQuery.getStatus()) {
			if (smsLogQuery.getStatus() == 1) {
				cq.eq("responseStatus", "OK");
			} else {
				cq.notEq("responseStatus", "OK");
			}
		}
		if (null != smsLogQuery.getChannelType()) {
			cq.eq("channelType", smsLogQuery.getChannelType());
		}
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}
}
