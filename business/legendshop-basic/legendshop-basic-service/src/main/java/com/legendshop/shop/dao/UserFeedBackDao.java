/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.entity.UserFeedBack;
import com.legendshop.shop.query.UserFeedBackQuery;

/**
 * 用户反馈Dao.
 *
 * @author legendshop
 */
public interface UserFeedBackDao extends GenericDao<UserFeedBack, Long> {


	PageSupport<UserFeedBack> getUserFeedBackPage(UserFeedBackQuery userFeedBackQuery);

	/**
	 * 反馈意见分页查询
	 *
	 * @param userFeedBackQuery
	 * @return
	 */
	PageSupport<UserFeedBack> page(UserFeedBackQuery userFeedBackQuery);

	/**
	 * 回复反馈意见
	 *
	 * @param userFeedbackDTO
	 * @return
	 */
	int updateReply(UserFeedbackDTO userFeedbackDTO);


}
