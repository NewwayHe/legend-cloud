/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.bo.UserBO;
import com.legendshop.user.dto.UserInformationDTO;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.query.UserDetailQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户详情Dao.
 *
 * @author legendshop
 */
public interface UserDetailDao extends GenericDao<UserDetail, Long>, UserLoginDao {


	UserDetail getUserDetailById(Long userId);

	/**
	 * 根据店铺ID获取用户信息
	 *
	 * @param shopId
	 * @return
	 */
	UserDetail getUserDetailByShopId(Long shopId);

	/**
	 * 根据邮件获取用户登陆名称
	 *
	 * @param email
	 * @return
	 */
	String getUserNameByMail(String email);


	/**
	 * 根据邮件获取商家用户登陆名称
	 *
	 * @param email
	 * @return
	 */
	String getShopUserNameByMail(String email);

	/**
	 * 根据电话获取用户登陆名称
	 *
	 * @param phone
	 * @return
	 */

	String getUserNameByPhone(String phone);

	/**
	 * 根据电话获取商家登陆名称
	 *
	 * @param phone
	 * @return
	 */

	String getShopUserNameByPhone(String phone);

	/**
	 * Find user score.
	 *
	 * @param userId the user name
	 * @return the long
	 */
	Integer getUserScore(Long userId);

	/**
	 * 根据ID查找手机.
	 *
	 * @param userId the user id
	 * @return the user mobile
	 */
	String getUserMobile(Long userId);

	/**
	 * 更新账户的密码.
	 *
	 * @param userId      the user name
	 * @param newPassword the new password
	 * @return the boolean
	 */
	Boolean updateNewPassword(Long userId, String newPassword);

	/**
	 * 第三方登陆 查找用户名
	 **/
	String getUserIdByOpenId(String openId, String type);

	/**
	 * 获取用户最后的登陆时间
	 **/
	Date getLastLoginTime(Long userId);

	/**
	 * 根据微信公众号OpenId获取用户
	 *
	 * @param wxOpenId
	 * @return
	 */
	UserBO getUserByWxOpenId(String wxOpenId);

	UserBO getUserByName(Long userId);

	/**
	 * 减去用户积分
	 */
	int updateScore(Integer updateSore, Integer originalSore, Long userId);

	/**
	 * 充值积分
	 *
	 * @param updateSore
	 * @param userId
	 * @return
	 */
	int updateScore(Integer updateSore, Long userId);

	String getuserIdByShopId(Long userId);

	/**
	 * 更新最后登录时间
	 *
	 * @param userId
	 */
	void updateLastLoginTime(Long userId);


	Long getShopIdByUserId(Long userId);

	PageSupport<UserDetail> queryUserDetailPage(UserDetailQuery ordiuserDetailQuerynaryUserQuery);

	UserBO getByOpenId(String openId);

	/**
	 * 通过手机号或用户名来获取用户名
	 */
	String getUserNameByPhoneOrUsername(String userMobile, Long userId);

	/**
	 * 记录用户的登录历史
	 */
	void updateUserLoginHistory(String ip, Long userId);

	/**
	 * 修改支付密码
	 *
	 * @param userId
	 * @param payPassword
	 * @return
	 */
	int updatePayPassword(Long userId, String payPassword);

	/**
	 * 根据userId查
	 *
	 * @param userIds
	 * @return
	 */
	List<UserDetail> queryByUserId(List<Long> userIds);

	String getNickNameByUserId(Long id);

	/**
	 * 更新用户消费统计
	 *
	 * @param userId
	 * @param amount
	 * @param count
	 * @return
	 */
	int updateConsumptionStatistics(Long userId, BigDecimal amount, Integer count);

	/**
	 * 通过用户id获取用户信息
	 *
	 * @param userIds
	 * @return
	 */
	List<UserInformationDTO> getUserInfoByIds(List<Long> userIds);

	void evictUserCache(Long userId);

	List<UserDetail> queryByNotAddress();

	/**
	 * 更新用户手机号
	 *
	 * @param userId
	 * @param mobile
	 * @return
	 */
	Integer updateMobileByUserId(Long userId, String mobile);
}
