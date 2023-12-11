/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.*;
import com.legendshop.data.entity.DataUserAmount;
import com.legendshop.data.entity.DataUserPurchasing;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
public interface DataService {

	/**
	 * 保存搜索历史数据
	 *
	 * @param searchHistoryDTO
	 */
	void saveSearchLog(SearchHistoryDTO searchHistoryDTO);

	/**
	 * 保存用户数量统计数据
	 *
	 * @param userAmount
	 */
	void saveUserAmount(DataUserAmount userAmount);

	/**
	 * 获取最新一条数据的时间
	 *
	 * @return
	 */
	DataUserAmountDTO getLastedDataDate();

	/**
	 * 获取用户数量统计数据
	 *
	 * @param num
	 * @return
	 */
	List<DataUserAmountDTO> getUserAmountNewData(Integer num);

	/**
	 * 确定用户详细表是否有用户
	 *
	 * @return
	 */
	boolean confirmUserHaveData();

	/**
	 * 确定用户购买力表用户存在
	 *
	 * @return
	 */
	boolean confirmPurchasingUserExist(Long userId);

	/**
	 * 保存用户购买力数据
	 *
	 * @param dataUserPurchasing
	 */
	void savePurchasingUserData(DataUserPurchasing dataUserPurchasing);

	/**
	 * 根据用户id获取购买力数据
	 *
	 * @param userId
	 * @return
	 */
	DataUserPurchasing getPurchasingDataById(Long userId);

	/**
	 * 保存商品浏览数据
	 *
	 * @param dto
	 */
	void saveViewData(ProdViewMqDTO dto);

	/**
	 * 获取百度统计数据
	 *
	 * @return
	 */
	Integer[] getBaiDuTongData(String startDate, String endDate);

	/**
	 * 获取百度统计数据
	 *
	 * @return
	 */
	Integer[] getBaiDuTongDataString(Date startDate, Date endDate);

	/**
	 * 获取百度统计数据
	 *
	 * @return
	 */
	Integer[] getBaiDuTongDataString(Date startDate, Date endDate, String source);

	/**
	 * 获取百度移动统计数据
	 *
	 * @return
	 */
	Integer[] getBaiDuMobileTongData(String startDate, String endDate, String source);

	/**
	 * 获取百度移动统计数据
	 *
	 * @return
	 */
	Integer[] getBaiDuMobileTongDataString(Date startDate, Date endDate, String source);

	/**
	 * 获取运营概况
	 *
	 * @return
	 */
	BusinessDataDTO getBusinessData(SimpleQuery simpleQuery);

	/**
	 * 获取商品访问次数和用户数
	 *
	 * @return
	 */
	ProductViewDTO getProdViewNum(SimpleQuery simpleQuery);

	/**
	 * 获取购物车新增数量
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer getCartNum(Date startDate, Date endDate, Long shopId);

	/**
	 * 获取喜欢新增数量
	 *
	 * @return
	 */
	Integer getFavoriteNum(SimpleQuery simpleQuery);

	/**
	 * 获取运营概况访问统计折线图
	 *
	 * @return
	 */
	List<ViewPicDTO> getViewPic(SimpleQuery simpleQuery);

	/**
	 * 获取商家端运营概况访问统计折线图
	 *
	 * @return
	 */
	List<ViewPicDTO> getShopViewPic(SimpleQuery simpleQuery);

	/**
	 * 获取运营概况成交统计折线图
	 *
	 * @return
	 */
	List<DealOrderPicDTO> getDealOrderPic(SimpleQuery simpleQuery);

	/**
	 * 获取运营概况售后统计折线图
	 *
	 * @return
	 */
	List<ReturnOrderPicDTO> getReturnOrderPic(SimpleQuery simpleQuery);

	/**
	 * 获取累计用户浏览量数据
	 *
	 * @return
	 */
	UserDataViewsDTO getUserViewData(String source);

	/**
	 * 保存店铺访问数据
	 *
	 * @param shopViewDTO
	 */
	void saveShopView(ShopViewDTO shopViewDTO);

	/**
	 * 保存加入购物车数据
	 *
	 * @param dto
	 */
	void saveViewDataToCart(ProdViewMqDTO dto);

	/**
	 * 获取营销统计最新数据
	 *
	 * @return
	 */
	DataActivityCollectDTO getLastedCollectData();

	/**
	 * 活动汇总
	 *
	 * @param flag
	 * @return
	 */
	Boolean activityCollect(Boolean flag);


	/**
	 * 优惠券汇总
	 *
	 * @return
	 */
	List<DataActivityCollectDTO> couponCollect(Date startDate, Date endDate);

	/**
	 * 营销统计新增使用次数折线图
	 *
	 * @return
	 */
	List<ActivityPublishPicDTO> getActivityPublishPic(ActivityDataQuery activityDataQuery);

	/**
	 * 营销统计新增成交金额折线图
	 *
	 * @return
	 */
	List<ActivityDealPicDTO> getActivityDealPic(ActivityDataQuery activityDataQuery);

	/**
	 * 营销统计列表
	 *
	 * @param activityDataQuery
	 * @return
	 */
	List<ActivityCollectPage> getActivityPage(ActivityDataQuery activityDataQuery);

	/**
	 * 营销分析
	 *
	 * @param activityDataQuery
	 * @return
	 */
	ActivityCollectDetail getActivityDetail(ActivityDataQuery activityDataQuery);

	/**
	 * 保存活动浏览记录
	 *
	 * @param activityViewDTO
	 */
	void saveActivityViewData(ActivityViewDTO activityViewDTO);


	/**
	 * 用户统计定时任务处理
	 *
	 * @return
	 */
	R<Void> dataUserAmountJobHandle();

}
