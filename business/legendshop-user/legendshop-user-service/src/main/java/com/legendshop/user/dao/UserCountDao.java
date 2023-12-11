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
import com.legendshop.common.datasource.NonTable;
import com.legendshop.data.dto.DataUserAmountDTO;
import com.legendshop.data.dto.UserDataViewsDTO;
import com.legendshop.user.dto.*;
import com.legendshop.user.query.*;

import java.util.Date;
import java.util.List;

/**
 * 用户数据统计dao
 *
 * @author legendshop
 */
public interface UserCountDao extends GenericDao<NonTable, Long> {

	/**
	 * 查询新增用户量数据
	 *
	 * @param source    注册来源
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增用户量数据
	 */
	Integer getNewUserData(String source, Date startDate, Date endDate);

	/**
	 * 查询累计用户数量数据。
	 *
	 * @param source    数据来源
	 * @param startDate 起始日期
	 * @param endDate   结束日期
	 * @return 累计用户数量数据
	 */
	Integer getTotalUserData(String source, Date startDate, Date endDate);

	/**
	 * 查询用户浏览总数数据。
	 *
	 * @return 用户浏览总数数据
	 */
	UserDataViewsDTO getUserViewData();

	/**
	 * 查询用户消费总数数据。
	 *
	 * @param source    数据来源
	 * @param startDate 起始日期
	 * @param endDate   结束日期
	 * @return 用户消费总数数据
	 */
	Integer getConsumerData(String source, Date startDate, Date endDate);

	/**
	 * 查询地区分布用户统计数据。
	 *
	 * @param endDate 统计结束日期
	 * @return 地区分布用户统计数据
	 */
	List<UserDataAreaDTO> queryDistributedArea(Date endDate);

	/**
	 * 分页查询地区分布用户统计数据。
	 *
	 * @param userDataAreaQuery 查询条件
	 * @return 分页的地区分布用户统计数据
	 */
	PageSupport<UserDataAreaDTO> queryDistributedAreaPage(UserDataAreaQuery userDataAreaQuery);

	/**
	 * 查询地区分布用户统计数据导出。
	 *
	 * @param userDataAreaQuery 查询条件
	 * @return 地区分布用户统计数据导出
	 */
	List<UserDataAreaDTO> getDistributedAreaExcel(UserDataAreaQuery userDataAreaQuery);

	/**
	 * 查询用户等级分布统计数据。
	 *
	 * @param endDate 统计结束日期
	 * @return 用户等级分布统计数据
	 */
	List<UserDataGradeDTO> queryDistributedGrade(Date endDate);

	/**
	 * 查询用户总数。
	 *
	 * @param endDate 统计结束日期
	 * @return 用户总数
	 */
	Integer queryUserCount(Date endDate);

	/**
	 * 查询购买力排行
	 *
	 * @param endDate
	 * @return
	 */
	List<UserDataPurchasingPowerDTO> queryPurchasingPower(Date endDate);

	/**
	 * 查询店铺销售排行
	 *
	 * @param endDate
	 * @return
	 */
	List<UserDataShopSaleDTO> queryShopSaleData(Date endDate);

	/**
	 * 获取用户数量统计折线图
	 *
	 * @param amountQuery
	 * @return
	 */
	DataUserAmountDTO getUserAmountLine(UserCountAmountQuery amountQuery);

	/**
	 * 查询用户数量统计分页
	 *
	 * @param amountQuery
	 * @return
	 */
	PageSupport<DataUserAmountDTO> queryUserAmountPage(UserCountAmountQuery amountQuery);

	/**
	 * 查询用户数量统计分页Excel
	 *
	 * @param amountQuery
	 * @return
	 */
	List<DataUserAmountDTO> queryUserAmountPageExcel(UserCountAmountQuery amountQuery);

	/**
	 * 查询用户购买力排行统计柱状图
	 *
	 * @param purchasingQuery
	 * @return
	 */
	List<UserDataPurchasingPageDTO> queryPurchasingPic(UserPurchasingQuery purchasingQuery);

	/**
	 * 查询用户购买力排行统计分页
	 *
	 * @param purchasingQuery
	 * @return
	 */
	PageSupport<UserDataPurchasingPageDTO> queryPurchasingPage(UserPurchasingQuery purchasingQuery);

	/**
	 * 查询用户购买力排行统计分页Excel
	 *
	 * @param purchasingQuery
	 * @return
	 */
	List<UserDataPurchasingPageDTO> queryPurchasingPageExcel(UserPurchasingQuery purchasingQuery);

	/**
	 * 获取用户登录历史分页数据
	 *
	 * @param loginQuery
	 * @return
	 */
	PageSupport<LoginHistoryDTO> queryLoginHistoryPage(UserCountLoginQuery loginQuery);

	/**
	 * 获取用户登陆历史统计分页数据
	 *
	 * @param loginQuery
	 * @return
	 */
	PageSupport<LoginHistoryCountDTO> queryLoginHistoryCountPage(UserCountLoginQuery loginQuery);

	/**
	 * 查询短信发送历史
	 *
	 * @param smsQuery
	 * @return
	 */
	PageSupport<UserDataSmsDTO> querySmsHistory(UserCountSmsQuery smsQuery);

	/**
	 * 获取店铺销售排行分页
	 *
	 * @param saleQuery
	 * @return
	 */
	PageSupport<UserDataShopSalePageDTO> getShopSalePage(UserCountShopSaleQuery saleQuery);

	/**
	 * 获取店铺销售排行柱状图
	 *
	 * @param saleQuery
	 * @return
	 */
	List<UserDataShopSalePageDTO> getShopSalePic(UserCountShopSaleQuery saleQuery);

	/**
	 * 获取店铺销售排行分页Excel
	 *
	 * @param saleQuery
	 * @return
	 */
	List<UserDataShopSalePageDTO> getShopSalePageExcel(UserCountShopSaleQuery saleQuery);


	/**
	 * 通过用户名查询用户昵称
	 *
	 * @param userId
	 * @return
	 */
	String queryNickNameById(Long userId);


}
