/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.data.dto.*;
import com.legendshop.data.entity.DataUserPurchasing;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
public interface DataDao extends GenericDao<NonTable, Long> {

	/**
	 * 获取最新一条数据的时间
	 *
	 * @return 最新数据的日期
	 */
	DataUserAmountDTO getLastedDataDate();

	/**
	 * 获取用户数量统计新数据
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return 在指定日期范围内的用户数量统计数据
	 */
	DataUserAmountDTO getUserAmountNewData(Date startDate, Date endDate);

	/**
	 * 根据时间获取用户总数
	 *
	 * @param endDate 结束日期
	 * @return 在指定日期结束时的用户总数
	 */
	Integer getUserCount(Date endDate);

	/**
	 * 获取用户详情表条数
	 *
	 * @return 用户详情表中的条目数
	 */
	Integer getUserDetailNum();

	/**
	 * 查询该用户是否存在
	 *
	 * @param userId 用户ID
	 * @return
	 */
	Integer queryUserPurchasingExist(Long userId);

	/**
	 * 根据用户id查询购买力数据
	 *
	 * @param userId 用户ID
	 * @return 该用户的购买力数据
	 */
	DataUserPurchasing queryPurchasingDataById(Long userId);

	/**
	 * 根据用户id查询手机号和昵称
	 *
	 * @param userId 用户ID
	 * @return 用户的手机号和昵称信息
	 */
	NameAndMobileDTO queryMobileById(Long userId);

	/**
	 * 根据商品id、时间和来源查询商品浏览信息
	 *
	 * @param prodId 商品ID
	 * @param time   时间
	 * @param source 来源
	 * @return 特定商品在指定时间和来源下的浏览信息
	 */
	ProductViewDTO queryViewDataById(Long prodId, Date time, String source);

	/**
	 * 查询商品访问次数和用户数
	 *
	 * @param source    来源
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param shopId    店铺ID
	 * @return 在指定日期范围内，特定来源和店铺下的商品访问次数和用户数
	 */
	ProductViewDTO queryProdViewNum(String source, Date startDate, Date endDate, Long shopId);


	/**
	 * 查询购物车新增数量
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param shopId    店铺ID
	 * @return 在指定日期范围内的购物车新增数量
	 */
	Integer queryCartNum(Date startDate, Date endDate, Long shopId);

	/**
	 * 查询喜欢新增数量
	 *
	 * @param simpleQuery 简单查询对象
	 * @return 喜欢新增的数量
	 */
	Integer queryFavoriteNum(SimpleQuery simpleQuery);

	/**
	 * 查询运营概况订单相关数据
	 *
	 * @param simpleQuery 简单查询对象
	 * @return 运营概况的订单相关数据
	 */
	BusinessDataDTO queryBusinessData(SimpleQuery simpleQuery);

	/**
	 * 查询运营概况售后订单数据
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param shopId    店铺ID
	 * @return 运营概况的售后订单数据
	 */
	ReturnOrderDataDTO queryReturnOrder(Date startDate, Date endDate, Long shopId);

	/**
	 * 查询运营概况成交统计折线图
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param source    来源
	 * @param shopId    店铺ID
	 * @return 运营概况的成交统计折线图数据
	 */
	DealOrderPicDTO queryDealOrderPic(Date startDate, Date endDate, String source, Long shopId);

	/**
	 * 查询特定类型的运营概况售后统计折线图
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param applyType 申请类型
	 * @param shopId    店铺ID
	 * @return 特定类型的运营概况售后统计折线图数据
	 */
	Integer queryReturnOrderPic(Date startDate, Date endDate, Integer applyType, Long shopId);

	/**
	 * 查询支付订单商品数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param shopId    店铺ID
	 * @return 在指定日期范围内的支付订单商品数
	 */
	Integer queryPayGoodNum(Date startDate, Date endDate, Long shopId);

	/**
	 * 根据店铺ID、时间和来源查询商品浏览信息
	 *
	 * @param shopId 店铺ID
	 * @param time   时间
	 * @param source 来源
	 * @return 特定店铺在指定时间和来源下的商品浏览信息
	 */
	ShopViewDTO queryShopViewDataById(Long shopId, Date time, String source);

	/**
	 * 获取店铺商品浏览统计折线图
	 *
	 * @param shopId    店铺ID
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param source    来源
	 * @return 店铺商品浏览统计折线图数据
	 */
	ShopViewDTO getShopViewPic(Long shopId, Date startDate, Date endDate, String source);

	/**
	 * 获取最新一条营销汇总数据
	 *
	 * @return 最新的营销汇总数据
	 */
	DataActivityCollectDTO getLastedActivityData();

	/**
	 * 查询是否为营销活动新购买用户
	 *
	 * @param userId 用户ID
	 * @param time   时间
	 * @return 是否为营销活动新购买用户
	 */
	Boolean queryIfNewCustomer(Long userId, Date time);

	/**
	 * 优惠券汇总
	 *
	 * @param startDate   开始日期
	 * @param endDate     结束日期
	 * @param activityIds 活动ID列表
	 * @return 优惠券活动的汇总数据列表
	 */
	List<ActivityCollectDTO> couponCollect(Date startDate, Date endDate, List<Long> activityIds);

	/**
	 * 限时折扣汇总
	 *
	 * @param startDate   开始日期
	 * @param endDate     结束日期
	 * @param activityIds 活动ID列表
	 * @return 限时折扣活动的汇总数据列表
	 */
	List<ActivityCollectDTO> marketingLimitCollect(Date startDate, Date endDate, List<Long> activityIds);

	/**
	 * 满减满折汇总
	 *
	 * @param startDate   开始日期
	 * @param endDate     结束日期
	 * @param activityIds 活动ID列表
	 * @return 满减满折活动的汇总数据列表
	 */
	List<ActivityCollectDTO> marketingRewardCollect(Date startDate, Date endDate, List<Long> activityIds);

	/**
	 * 营销统计新增使用次数折线图
	 *
	 * @param activityDataQuery 营销活动数据查询对象
	 * @return 营销活动新增使用次数的统计折线图数据列表
	 */
	List<DataActivityCollectDTO> getActivityPublishPic(ActivityDataQuery activityDataQuery);

	/**
	 * 获取营销统计新增成交金额折线图数据
	 *
	 * @param activityDataQuery 营销活动数据查询对象
	 * @return 营销活动新增成交金额的统计折线图数据列表
	 */
	List<DataActivityOrderDTO> getActivityDealPic(ActivityDataQuery activityDataQuery);

	/**
	 * 获取营销统计支付数据
	 *
	 * @param activityDataQuery 营销活动数据查询对象
	 * @return 营销活动的支付数据列表
	 */
	List<ActivityPayDataDTO> getActivityPayData(ActivityDataQuery activityDataQuery);

	/**
	 * 获取所有支付金额数据
	 *
	 * @param activityDataQuery 营销活动数据查询对象
	 * @return 所有支付金额的数据列表
	 */
	List<BigDecimal> getPayData(ActivityDataQuery activityDataQuery);

	/**
	 * 查询店铺浏览数据
	 *
	 * @param activityDataQuery 营销活动数据查询对象
	 * @return 店铺浏览数据列表
	 */
	List<DataActivityViewDTO> queryActivityView(ActivityDataQuery activityDataQuery);


}
