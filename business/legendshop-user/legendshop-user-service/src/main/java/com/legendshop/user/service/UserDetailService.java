/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.UserBasicInformationBO;
import com.legendshop.user.dto.MobileUserCenterDTO;
import com.legendshop.user.dto.UserDetailDTO;
import com.legendshop.user.dto.UserInformationDTO;
import com.legendshop.user.dto.VerifyUserDetailDTO;
import com.legendshop.user.query.UserDetailQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户服务
 *
 * @author legendshop
 */
public interface UserDetailService {

	PageSupport<UserDetailDTO> queryUserDetailPage(UserDetailQuery ordinaryUserQuery);

	/**
	 * 根据Userid获得用户详情
	 */
	UserDetailDTO getUserDetailById(Long userId);

	/**
	 * 修改支付密码
	 */
	R<Void> updatePayPassword(VerifyUserDetailDTO userDetailDTO);

	/**
	 * 根据用户id查询用户信息
	 *
	 * @param userIds the 用户Id
	 * @return detail
	 */
	List<UserDetailDTO> queryById(List<Long> userIds);


	R<MobileUserCenterDTO> mobileInfo(Long userId);

	UserInformationDTO getUserInfoById(Long userId);

	/**
	 * 后台-获取普通用户基本资料
	 *
	 * @param userId
	 * @return
	 */
	R<UserBasicInformationBO> getUserBasicInformationBOByUserId(Long userId);

	/**
	 * 更新用户消费统计
	 *
	 * @param userId
	 * @param amount
	 * @param count
	 * @return
	 */
	int updateConsumptionStatistics(Long userId, BigDecimal amount, Integer count);


	R<Void> updateDistribution(Long userId, Long distributionUserId);

	R<Void> verificationPayPassword(Long userId, String password);

	/**
	 * 通过用户id获取用户信息
	 *
	 * @param userIds
	 * @return
	 */
	List<UserInformationDTO> getUserInfoByIds(List<Long> userIds);

	R<Void> feasibilityTest(String param);

	/**
	 * 修改微信号
	 *
	 * @param userId
	 * @param weChatNumber
	 * @return
	 */
	Boolean updateWeChatNumber(Long userId, String weChatNumber);

	R<List<UserInformationDTO>> getPhoneAndNickNameByIds(List<Long> ids);

	/**
	 * 修改用户电子邮件
	 *
	 * @param userId
	 * @param email
	 * @return
	 */
	R updateEmail(Long userId, String email);

	/**
	 * 获取无地址用户列表
	 *
	 * @return
	 */
	List<UserDetailDTO> queryByNotAddress();

	/**
	 * 更新
	 *
	 * @param userDetailList
	 */
	void updateByList(List<UserDetailDTO> userDetailList);
}
