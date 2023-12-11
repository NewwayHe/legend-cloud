/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponShopBO;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponShopQuery;
import com.legendshop.activity.vo.CouponVO;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.query.ProductQuery;

import java.util.List;
import java.util.Map;

/**
 * 优惠券(Coupon)表服务接口
 *
 * @author legendshop
 * @since 2020-09-10 11:00:01
 */
public interface CouponService {

	/**
	 * 根据优惠券id集合获取优惠券DTO
	 *
	 * @param couponIds
	 * @return
	 */
	List<CouponDTO> getByIds(List<Long> couponIds);

	/**
	 * 优惠券分页查询
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponVO> page(CouponQuery couponQuery);

	/**
	 * 查询优惠券商品
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<SkuBO> queryCouponProductPage(CouponQuery couponQuery);

	/**
	 * 优惠券商品名称分页查询，有子集sku信息
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<ProductBO> productPage(ProductQuery productQuery);

	/**
	 * 查询用户可领取优惠券列表
	 *
	 * @param couponQuery
	 * @return
	 */
	List<CouponDTO> listReceivable(CouponQuery couponQuery);

	/**
	 * 分页查询用户可领取优惠券
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponDTO> receivablePage(CouponQuery couponQuery);


	/**
	 * 查找要上线的优惠券
	 *
	 * @return
	 */
	List<CouponDTO> queryPreOnline();

	/**
	 * 根据id查询优惠券详情
	 *
	 * @param id
	 * @return
	 */
	CouponDTO getById(Long id);

	/**
	 * 根据id查询优惠券详情
	 *
	 * @param id
	 * @param couponUserId
	 * @param userId
	 * @return
	 */
	R<CouponDTO> getById(Long id, Long couponUserId, Long userId);

	/**
	 * 获取相同的可用的 未使用/未生效 的已领取优惠券
	 *
	 * @param couponDTO
	 * @param userId
	 */
	void getIdenticalAvailableCouponUser(CouponDTO couponDTO, Long userId);

	/**
	 * 获取到点下线的id
	 *
	 * @return
	 */
	List<CouponDTO> getOffLineId();

	/**
	 * 获取可用的优惠券
	 *
	 * @param userId
	 * @param shopCouponMap
	 * @return
	 */
	List<CouponItemExtDTO> getAvailableCoupon(Long userId, Map<Long, ShopCouponDTO> shopCouponMap);


	/**
	 * 根据用户 ID 查询未使用的优惠券 ID 列表。
	 *
	 * @param userId 用户 ID
	 * @return 匹配用户 ID 的未使用优惠券 ID 列表
	 */
	List<CouponUserDTO> queryUnusedCouponIdByUserId(Long userId);


	/**
	 * 获取优惠券，根据用户优惠券ID和店铺ID
	 *
	 * @param userCouponId
	 * @param shopId
	 * @return
	 */
	CouponDTO getCouponByUserCouponIdAndShopId(Long userCouponId, Long shopId);

	/**
	 * 查询优惠券关联的店铺列表
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponShopBO> queryCouponShopPage(CouponQuery couponQuery);

	/**
	 * 查询优惠券关联的店铺列表
	 *
	 * @param couponShopQuery
	 * @return
	 */
	R<PageSupport<CouponShopBO>> queryCouponShopPage(CouponShopQuery couponShopQuery);


	/**
	 * 通用简单增删改查操作----------------------------------------------------------------------------------------------------------
	 * 修改
	 */

	/**
	 * 保存优惠券
	 *
	 * @param couponDTO
	 * @return
	 */
	R save(CouponDTO couponDTO);

	/**
	 * 更新状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	R updateStatus(Long id, Integer status);

	/**
	 * 更新发放数量
	 *
	 * @param couponId
	 */
	void updateReceiveNum(Long couponId);

	/**
	 * 批量更新优惠券状态，并清除缓存的方法。
	 *
	 * @param ids    优惠券 ID 列表
	 * @param status 优惠券状态
	 * @param shopId 店铺 ID
	 * @return 批量更新操作的结果
	 */
	R batchUpdateStatus(List<Long> ids, Integer status, Long shopId);

	/**
	 * 批量更新用户优惠券为已使用，并清除缓存的方法。
	 *
	 * @param couponOrderList 优惠券订单信息列表
	 * @return 批量更新操作的结果
	 */
	R<Void> batchUpdateUsedStatus(List<CouponOrderDTO> couponOrderList);

	/**
	 * 更新优惠券
	 *
	 * @param couponDTO
	 */
	void update(CouponDTO couponDTO);

	/**
	 * 更新状态的方法。
	 *
	 * @param id                  订单 ID
	 * @param orderNumber         订单编号
	 * @param couponOrderUpdateDTO 包含更新信息的优惠券订单更新数据传输对象
	 * @return 更新操作的结果
	 */
	R updateStatus(Long id, String orderNumber, CouponOrderUpdateDTO couponOrderUpdateDTO);

	/**
	 * 批量更新的方法。
	 *
	 * @param userId 用户 ID
	 * @param order  订单信息
	 * @param ids    优惠券订单 ID 列表
	 */
	void batchUpdateStatus(Long userId, String order, List<Long> ids);


	/**
	 * 减少优惠券使用数量
	 *
	 * @param couponIds
	 */
	void subUseCount(List<Long> couponIds);

	/**
	 * 减少优惠券使用数量
	 *
	 * @param couponUsers
	 */
	void updateUseCount(List<OrderRefundCouponDTO> couponUsers);

	/**
	 * 复杂业务操作----------------------------------------------------------------------------------------------------------
	 */


	/**
	 * 下单获取优惠券列表,获取用户最优优惠券组合
	 *
	 * @param userId
	 * @param shopCouponMap
	 * @return
	 */
	Map<Long, ShopCouponDTO> getShopBestCoupons(Long userId, Map<Long, ShopCouponDTO> shopCouponMap);

	/**
	 * 获取用户最优平台优惠券的方法。
	 *
	 * @param userId          用户 ID
	 * @param shopCouponDTOMap 店铺优惠券信息映射表
	 * @return 用户最优平台优惠券列表
	 */
	List<CouponItemDTO> getBestPlatFormCoupons(Long userId, Map<Long, ShopCouponDTO> shopCouponDTOMap);

	/**
	 * 获取最优平台优惠券的方法。
	 *
	 * @param platformCoupons 平台优惠券列表
	 * @param shopCouponDTOMap 店铺优惠券信息映射表
	 * @return 最优平台优惠券列表
	 */
	List<CouponItemDTO> getBestPlatFormCoupons(List<CouponItemDTO> platformCoupons, Map<Long, ShopCouponDTO> shopCouponDTOMap);

	/**
	 * 处理选择的优惠券信息的方法。
	 *
	 * @param shopCouponDTO 店铺优惠券信息
	 * @return 处理后的店铺优惠券信息
	 */
	ShopCouponDTO handleSelectCoupons(ShopCouponDTO shopCouponDTO);

	/**
	 * 处理选择平台优惠券
	 *
	 * @param selectPlatformCouponDTO
	 * @return
	 */
	List<CouponItemDTO> handleSelectPlatformCoupons(SelectPlatformCouponDTO selectPlatformCouponDTO);

	/**
	 * 处理优惠券分摊金额的方法。
	 *
	 * @param shopCouponDTO 店铺优惠券信息
	 * @return 处理后的店铺优惠券信息
	 */
	ShopCouponDTO handlerShopCouponsShard(ShopCouponDTO shopCouponDTO);



	/**
	 * 过滤不符合条件的商家
	 *
	 * @param couponId
	 * @return
	 */
	List<Long> filterShop(Long couponId);

	/**
	 * 发送活动访问记录消息队列的方法。
	 *
	 * @param couponDTO 优惠券信息数据传输对象
	 * @param userId    用户 ID
	 */
	void sendActivityViewMQ(CouponDTO couponDTO, Long userId);

	/**
	 * 查找所有正在进行中的优惠券的方法。
	 *
	 * @param couponQuery 优惠券查询对象
	 * @return 包含正在进行中的优惠券信息的分页支持对象
	 */
	PageSupport<CouponDTO> queryCouponsByStatus(CouponQuery couponQuery);


	/**
	 * 根据优惠卷ID查找优惠卷
	 *
	 * @param couponId
	 * @return
	 */
	CouponDTO getCouponsById(Long couponId);


	/**
	 * 从ES中查询用户可领取优惠券列表
	 *
	 * @param couponQuery
	 * @return
	 */
	List<CouponDTO> listReceivableES(CouponQuery couponQuery);

	/**
	 * 处理平台优惠券
	 *
	 * @param platformCoupons
	 */
	void batchUpdateAdminStatus(List<CouponItemDTO> platformCoupons);

	/**
	 * 优惠券到点上线定时任务处理
	 * @return
	 */
	R<Void> couponOnLineJobHandle();

	/**
	 * 优惠券到点下线 定时任务处理
	 *
	 * @return
	 */
	R<Void> couponOffLineJobHandle();
}
