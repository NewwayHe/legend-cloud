/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dao.UserFeedBackDao;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.entity.UserFeedBack;
import com.legendshop.shop.query.UserFeedBackQuery;
import org.springframework.stereotype.Repository;

/**
 * 用户反馈Dao.
 *
 * @author legendshop
 */
@Repository
public class UserFeedBackDaoImpl extends GenericDaoImpl<UserFeedBack, Long> implements UserFeedBackDao {

	@Override
	public PageSupport<UserFeedBack> getUserFeedBackPage(UserFeedBackQuery userFeedBackQuery) {
		CriteriaQuery cq = new CriteriaQuery(UserFeedBack.class, userFeedBackQuery.getPageSize(), userFeedBackQuery.getCurPage());
		cq.like("content", userFeedBackQuery.getContent(), MatchMode.ANYWHERE);
		cq.eq("status", userFeedBackQuery.getStatus());
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	/**
	 * 反馈意见分页查询
	 *
	 * @param userFeedBackQuery
	 * @return
	 */
	@Override
	public PageSupport<UserFeedBack> page(UserFeedBackQuery userFeedBackQuery) {
		CriteriaQuery cq = new CriteriaQuery(UserFeedBack.class, userFeedBackQuery.getPageSize(), userFeedBackQuery.getCurPage());
		cq.like("content", userFeedBackQuery.getContent(), MatchMode.ANYWHERE);
		if (StrUtil.isNotBlank(userFeedBackQuery.getEndTime())) {
			cq.between("createTime", userFeedBackQuery.getBeginTime(),
					DateUtil.endOfDay(DateUtil.parse(userFeedBackQuery.getEndTime())));
		}
		cq.eq("feedbackSource", userFeedBackQuery.getFeedbackSource());
		cq.eq("status", userFeedBackQuery.getStatus());
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	/**
	 * 回复反馈意见
	 *
	 * @param userFeedbackDTO
	 * @return
	 */
	@Override
	public int updateReply(UserFeedbackDTO userFeedbackDTO) {
		return update("update `ls_user_feedback` set resp_mgnt_id=? ,resp_content=? ,resp_date=?,status=1," +
						"resp_mgnt_id=?  where id=?"
				, userFeedbackDTO.getRespMgntId(), userFeedbackDTO.getRespContent(), userFeedbackDTO.getRespDate(),
				userFeedbackDTO.getRespMgntId(), userFeedbackDTO.getId());
	}
}
