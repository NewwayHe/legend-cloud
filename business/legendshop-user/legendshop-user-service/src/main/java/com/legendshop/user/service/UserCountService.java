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
import com.legendshop.data.dto.DataUserAmountDTO;
import com.legendshop.user.bo.RegisterCountBO;
import com.legendshop.user.dto.*;
import com.legendshop.user.query.*;

import java.util.List;

/**
 * 用户统计服务
 *
 * @author legendshop
 */
public interface UserCountService {

	/**
	 * 获取新增用户量数据
	 *
	 * @return
	 */
	UserDataNewDTO getNewUserData(String source);

	/**
	 * 获取累计用户数量数据
	 *
	 * @return
	 */
	UserDataAllDTO getTotalUserData(String source);

	/**
	 * 获取用户消费总数数据
	 *
	 * @return
	 */
	UserDataConsumerDTO getConsumerData(String source);

	/**
	 * 获取地区分布统计数据
	 *
	 * @return
	 */
	List<UserDataAreaDTO> getDistributedArea();

	/**
	 * 获取地区分布统计数据分页
	 *
	 * @param userDataAreaQuery
	 * @return
	 */
	PageSupport<UserDataAreaDTO> getDistributedAreaPage(UserDataAreaQuery userDataAreaQuery);

	/**
	 * 获取地区分布统计数据导出
	 *
	 * @param userDataAreaQuery
	 * @return
	 */
	List<UserDataAreaDTO> getDistributedAreaExcel(UserDataAreaQuery userDataAreaQuery);

	/**
	 * 获取等级分布统计数据
	 *
	 * @return
	 */
	List<UserDataGradeDTO> getDistributedGrade();

	/**
	 * 获取购买力排行统计数据
	 *
	 * @return
	 */
	List<UserDataPurchasingPowerDTO> getPurchasingPower();

	/**
	 * 获取店铺销售排行统计数据
	 *
	 * @return
	 */
	List<UserDataShopSaleDTO> getShopSaleData();

	/**
	 * 获取用户数量统计折线图
	 *
	 * @param amountQuery
	 * @return
	 */
	List<DataUserAmountDTO> getUserAmountLine(UserCountAmountQuery amountQuery);

	/**
	 * 获取用户数量统计分页
	 *
	 * @param amountQuery
	 * @return
	 */
	PageSupport<DataUserAmountDTO> getUserAmountPage(UserCountAmountQuery amountQuery);

	/**
	 * 获取用户数量统计分页Excel
	 *
	 * @param amountQuery
	 * @return
	 */
	List<DataUserAmountDTO> getUserAmountPageExcel(UserCountAmountQuery amountQuery);

	/**
	 * 获取用户购买力排行柱状图
	 *
	 * @param purchasingQuery
	 * @return
	 */
	List<UserDataPurchasingPageDTO> getPurchasingPic(UserPurchasingQuery purchasingQuery);

	/**
	 * 获取用户购买力排行分页
	 *
	 * @param purchasingQuery
	 * @return
	 */
	PageSupport<UserDataPurchasingPageDTO> getPurchasingPage(UserPurchasingQuery purchasingQuery);

	/**
	 * 获取用户购买力排行分页Excel
	 *
	 * @param purchasingQuery
	 * @return
	 */
	List<UserDataPurchasingPageDTO> getPurchasingPageExcel(UserPurchasingQuery purchasingQuery);

	/**
	 * 获取用户登录历史分页数据
	 *
	 * @param loginQuery
	 * @return
	 */
	PageSupport<LoginHistoryDTO> getLoginHistoryPage(UserCountLoginQuery loginQuery);

	/**
	 * 获取用户登陆历史统计分页数据
	 *
	 * @param loginQuery
	 * @return
	 */
	PageSupport<LoginHistoryCountDTO> getLoginHistoryCountPage(UserCountLoginQuery loginQuery);

	/**
	 * 获取短信发送历史
	 *
	 * @param smsQuery
	 * @return
	 */
	PageSupport<UserDataSmsDTO> getSmsHistory(UserCountSmsQuery smsQuery);

	/**
	 * 获取用户注册数量
	 *
	 * @return RegisterCountBO-{@link com.legendshop.user.bo.RegisterCountBO}
	 */
	RegisterCountBO getRegisterCount();

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

}
