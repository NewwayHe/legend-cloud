/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.UserFeedbackBatchHandleDTO;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.query.UserFeedBackQuery;

/**
 * 用户反馈服务.
 *
 * @author legendshop
 */
public interface UserFeedBackService {

	UserFeedbackDTO getById(Long id);

	void deleteById(Long id);

	Long save(UserFeedbackDTO userFeedBack);

	void update(UserFeedbackDTO userFeedBack);

	PageSupport<UserFeedbackDTO> getUserFeedBackPage(UserFeedBackQuery userFeedBackQuery);

	/**
	 * 反馈意见分页查询
	 *
	 * @param userFeedBackQuery
	 * @return
	 */
	public PageSupport<UserFeedbackDTO> page(UserFeedBackQuery userFeedBackQuery);

	/**
	 * 回复反馈意见
	 *
	 * @param userFeedbackDTO
	 * @return
	 */
	int updateReply(UserFeedbackDTO userFeedbackDTO);


	Long saveUserFeedback(UserFeedbackDTO userFeedbackDTO);

	/**
	 * 批量回复
	 *
	 * @param userFeedbackBatchHandleDTO
	 */
	boolean updateListById(UserFeedbackBatchHandleDTO userFeedbackBatchHandleDTO);
}
