/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.bo.MyCouponBO;
import com.legendshop.activity.bo.MyCouponCountBO;
import com.legendshop.activity.dao.CouponOrderDao;
import com.legendshop.activity.dao.CouponUserDao;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.dto.OrderRefundCouponDTO;
import com.legendshop.activity.entity.CouponOrder;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.mq.producer.CouponProducerService;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponUserQuery;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.activity.service.convert.CouponUserConverter;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.CouponViewDTO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户优惠券服务
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class CouponUserServiceImpl implements CouponUserService {


	private final CouponUserDao couponUserDao;
	private final CouponService couponService;
	private final CouponUserConverter converter;
	private final ShopDetailApi shopDetailApi;
	private final CouponProducerService producerService;
	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final CouponOrderDao couponOrderDao;


	@Override
	public PageSupport<CouponUserBO> queryPage(CouponQuery couponQuery) {
		PageSupport<CouponUserBO> queryPage = couponUserDao.queryPage(couponQuery);
		List<CouponUserBO> resultList = queryPage.getResultList();
		if (CollUtil.isNotEmpty(resultList)) {
			List<Long> userCouponIds = resultList.stream().map(CouponUserBO::getId).collect(Collectors.toList());
			List<CouponOrder> couponOrders = couponOrderDao.queryByUserCouponId(userCouponIds);
			Map<Long, List<CouponOrder>> couponOrderMap = couponOrders.stream().collect(Collectors.groupingBy(CouponOrder::getUserCouponId));

			for (CouponUserBO couponUserBO : resultList) {
				if (couponOrderMap.containsKey(couponUserBO.getId())) {
					List<CouponOrder> couponOrderList = couponOrderMap.get(couponUserBO.getId());
					couponUserBO.setOrderNumber(StrUtil.join("|", couponOrderList.stream().filter(e -> e.getStatus() == 1).map(CouponOrder::getOrderNumber).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList())));
				}
			}
		}
		return queryPage;
	}

	@Override
	public PageSupport<MyCouponBO> queryMyCouponPage(CouponQuery couponQuery) {

		PageSupport<MyCouponBO> myCouponBOPageSupport = couponUserDao.queryMyCouponPage(couponQuery);
		List<MyCouponBO> couponBOList = myCouponBOPageSupport.getResultList();
		if (CollUtil.isEmpty(couponBOList)) {
			return myCouponBOPageSupport;
		}

		for (MyCouponBO myCouponBO : couponBOList) {
			if (ObjectUtil.isNotNull(myCouponBO.getAmount())) {
				String[] amounts = myCouponBO.getAmount().split("\\.");
				if ("00".equals(amounts[1])) {
					myCouponBO.setAmount(amounts[0]);
				}
			}
			if (ObjectUtil.isNotNull(myCouponBO.getMinPoint())) {
				String[] minPoints = myCouponBO.getMinPoint().split("\\.");
				if ("00".equals(minPoints[1])) {
					myCouponBO.setMinPoint(minPoints[0]);
				}
			}
		}
		return myCouponBOPageSupport;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.COUPON_DETAILS, key = "#couponId")
	public synchronized R save(Long couponId, Long userId) {
		CouponDTO couponDTO = couponService.getById(couponId);
		/*判断优惠券是否已经领取完了*/
		if (couponDTO.getReceiveCount() >= couponDTO.getCount()) {
			return R.fail("该优惠券已领完，下次早点喔！");
		}
		if (CouponStatusEnum.NOT_STARTED.getValue().equals(couponDTO.getStatus())) {
			return R.fail("优惠券活动还未开始，领取失败");
		}
		if (DateUtil.date().before(couponDTO.getReceiveStartTime()) || DateUtil.date().after(couponDTO.getReceiveEndTime())) {
			return R.fail("请在活动时间内领取~");
		}
		if (!CouponStatusEnum.CONTAINS.getValue().equals(couponDTO.getStatus())) {
			return R.fail("活动状态异常，领取失败~");
		}
		/*判断是否领取超出优惠券规则的数量、是否有相同的优惠券没有使用*/
		List<CouponUser> couponUserList = couponUserDao.queryByUserIdAndCouponId(userId, couponId);
		if (CollUtil.isNotEmpty(couponUserList)) {
			/*是否有相同的未使用/未生效优惠券*/
			List<CouponUser> unusedList = couponUserList.stream()
					.filter(couponUser -> CouponUserStatusEnum.UNUSED.getValue().equals(couponUser.getStatus()) || CouponUserStatusEnum.NOT_STARTED.getValue().equals(couponUser.getStatus()))
					.collect(Collectors.toList());
			if (CollUtil.isNotEmpty(unusedList)) {
				return R.fail("你已经领取同类优惠券，还没有使用，先使用了优惠券再过来看看吧！");
			}
			Integer perTotalLimit = couponDTO.getPerTotalLimit();
			Integer perDayLimit = couponDTO.getPerDayLimit();
			if (ObjectUtil.isNotEmpty(perTotalLimit) && perTotalLimit > 0 && perTotalLimit <= couponUserList.size()) {
				return R.fail("同类优惠券你已领取" + couponUserList.size() + "次，达到活动领取的上限了，不可再领取！");
			}
			/*获取当天领取的优惠券*/
			List<CouponUser> todayReceive = couponUserList.stream()
					.filter(couponUser -> DateUtil.isSameDay(DateUtil.date(), couponUser.getGetTime()))
					.collect(Collectors.toList());
			if (ObjectUtil.isNotEmpty(perDayLimit) && perDayLimit > 0 && perDayLimit <= todayReceive.size()) {
				return R.fail("同类优惠券你今天已领取" + perDayLimit + "次，达到当天领取的上限了，不可再领取！");
			}
		}
		Long userCouponId = couponUserDao.createId();

		/*优惠券活动生效时间*/
		Date onTime = getOnTime(couponDTO.getUseStartTime(), couponDTO.getUseDayLater());
		/*优惠券活动失效时间*/
		Date offTime = getOffTime(couponDTO.getUseEndTime(), onTime, couponDTO.getWithinDay());

		VisitSourceEnum userRequestSource = SecurityUtils.getUserRequestSource();
		CouponUser couponUser = new CouponUser();
		couponUser.setUserId(userId);
		couponUser.setCouponId(couponId);
		couponUser.setCouponTitle(couponDTO.getTitle());
		couponUser.setCouponCode(RandomUtil.getRandomSn());
		Date date = new Date();
		couponUser.setGetTime(date);
		couponUser.setGetType(couponDTO.getReceiveType());
		couponUser.setUseStartTime(onTime);
		couponUser.setUseEndTime(offTime);
		couponUser.setSource(userRequestSource.name());
		/*保存优惠券保存规则*/
		couponUser.setPaymentRefundableFlag(couponDTO.getPaymentRefundableFlag());
		couponUser.setNonPaymentRefundableFlag(couponDTO.getNonPaymentRefundableFlag());
		couponUser.setPaymentAllAfterSalesRefundableFlag(couponDTO.getPaymentAllAfterSalesRefundableFlag());
		//判断并设置用户优惠券状态
		if (onTime.before(date) && offTime.after(date)) {
			couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
		} else if (onTime.after(date) || onTime.equals(date)) {
			couponUser.setStatus(CouponUserStatusEnum.NOT_STARTED.getValue());
		} else if (offTime.before(date) || offTime.equals(date)) {
			couponUser.setStatus(CouponUserStatusEnum.INVALID.getValue());
		}
		/*更新领取数量*/
		couponService.updateReceiveNum(couponUser.getCouponId());
		couponUserDao.save(couponUser, userCouponId);
		List<Long> userCouponIds = Collections.singletonList(userCouponId);
		producerService.userCouponOnLine(userCouponIds, onTime);
		return R.ok(userCouponId);
	}

	@Override
	public R updateUserIdByPwd(String pwd, Long userId) {
		CouponUser couponUser = couponUserDao.getByPwd(pwd);
		if (ObjectUtil.isEmpty(couponUser)) {
			return R.fail("卡密不正确或已被兑换");
		}
		CouponDTO couponDTO = couponService.getById(couponUser.getCouponId());
		/*判断优惠券是否已经领取完了*/
		if (couponDTO.getReceiveCount() >= couponDTO.getCount()) {
			return R.fail("该优惠券已领完，下次早点喔！");
		}
		/*判断是否领取超出优惠券规则的数量、是否有相同的优惠券没有使用*/
		List<CouponUser> couponUserList = couponUserDao.queryByUserIdAndCouponId(userId, couponUser.getCouponId());
		if (CollUtil.isNotEmpty(couponUserList)) {
			/*是否有相同的未使用/未生效优惠券*/
			List<CouponUser> unusedList = couponUserList.stream()
					.filter(couponUserItem -> CouponUserStatusEnum.UNUSED.getValue().equals(couponUserItem.getStatus()) || CouponUserStatusEnum.NOT_STARTED.getValue().equals(couponUserItem.getStatus()))
					.collect(Collectors.toList());
			if (CollUtil.isNotEmpty(unusedList)) {
				return R.fail("你已经领取同类优惠券，还没有使用，先使用了优惠券再过来看看吧！");
			}
			Integer perTotalLimit = couponDTO.getPerTotalLimit();
			Integer perDayLimit = couponDTO.getPerDayLimit();
			if (ObjectUtil.isNotEmpty(perTotalLimit) && perTotalLimit > 0 && perTotalLimit <= couponUserList.size()) {
				return R.fail("同类优惠券你已领取" + couponUserList.size() + "次，达到活动领取的上限了，不可再领取！");
			}
			/*获取当天领取的优惠券*/
			List<CouponUser> todayReceive = couponUserList.stream()
					.filter(couponUserItem -> DateUtil.isSameDay(DateUtil.date(), couponUserItem.getGetTime()))
					.collect(Collectors.toList());
			if (ObjectUtil.isNotEmpty(perDayLimit) && perDayLimit > 0 && perDayLimit <= todayReceive.size()) {
				return R.fail("同类优惠券你今天已领取" + perDayLimit + "次，达到当天领取的上限了，不可再领取！");
			}
		}
		Date onTime = getOnTime(couponDTO.getUseStartTime(), couponDTO.getUseDayLater());
		Date offTime = getOffTime(couponDTO.getUseEndTime(), onTime, couponDTO.getWithinDay());
		List userCouponIds = new ArrayList();
		userCouponIds.add(couponUser.getCouponId());
		/*如果优惠券没有使用时间段，则设置优惠券生效、失效队列*/
		if (ObjectUtil.isNotEmpty(couponDTO.getUseStartTime())) {
			producerService.userCouponOnLine(userCouponIds, couponDTO.getUseStartTime());
		}
		if (ObjectUtil.isNotEmpty(couponDTO.getUseEndTime())) {
			producerService.userCouponOffLine(userCouponIds, couponDTO.getUseEndTime());
		}
		Integer integer = couponUserDao.updateUserIdByPwd(couponUser.getId(), userId, onTime, offTime);
		couponService.updateReceiveNum(couponUser.getCouponId());
		return R.ok(integer);
	}

	@Override
	public PageSupport<SkuBO> querySkuPageById(CouponUserQuery couponUserQuery) {
		return couponUserDao.querySkuPageById(couponUserQuery);
	}

	@Override
	public Integer userCouponValid() {
		return couponUserDao.userCouponValid();
	}

	@Override
	public Integer userCouponInvalid() {
		return couponUserDao.userCouponInvalid();
	}

	@Override
	public R updateStatus(Long id, Integer status, Long shopId) {
		CouponDTO couponDTO = couponService.getCouponByUserCouponIdAndShopId(id, shopId);
		if (null == couponDTO) {
			return R.fail("找不到该用户优惠券");
		}
		return R.ok(couponUserDao.updateStatus(id, status));
	}

	@Override
	public R<Integer> userCouponCount(Long userId) {
		return R.ok(Optional.ofNullable(this.couponUserDao.userCouponCount(userId)).orElse(0));
	}

	@Override
	public List<CouponUserDTO> getByOrderNumber(String orderNumber) {
		return converter.to(couponUserDao.getByOrderNumber(orderNumber));
	}

	@Override
	public void update(List<CouponUserDTO> couponUsers) {
		couponUserDao.update(converter.from(couponUsers));
	}

	@Override
	public void updateCoupon(List<OrderRefundCouponDTO> couponUsers) {
		List<CouponUserDTO> couponUser = new ArrayList<>();
		CouponUserDTO couponUserDTO = new CouponUserDTO();
		for (OrderRefundCouponDTO user : couponUsers) {
			couponUserDTO.setCouponId(user.getCouponId());
			couponUserDTO.setOrderNumber(user.getOrderNumber());
			couponUserDTO.setId(user.getCouponUserId());
			couponUserDTO.setStatus(user.getStatus());
			couponUser.add(couponUserDTO);
		}
		couponUserDao.update(converter.from(couponUser));

	}

	@Override
	public void updateCouponOrder(List<OrderRefundCouponDTO> couponUsers) {

		if (CollUtil.isNotEmpty(couponUsers)) {
			List<Long> userCouponIds = couponUsers.stream().map(OrderRefundCouponDTO::getCouponUserId).collect(Collectors.toList());
			// 更新优惠券使用数量
			List<CouponOrder> couponOrderList = couponOrderDao.queryByUserCouponId(userCouponIds);
			for (CouponOrder couponOrder : couponOrderList) {
				couponOrder.setStatus(20);
			}
			couponOrderDao.update(couponOrderList);
		}
//		List<CouponOrderDTO> couponOrder =new ArrayList<>();
//		CouponOrderDTO couponUserDTO = new CouponOrderDTO();
//		for (OrderRefundCouponDTO user : couponUsers) {
//			couponUserDTO.setCouponId(user.getCouponId());
//			couponUserDTO.setId(user.getCouponUserId());
//			couponUserDTO.setStatus(20);
//			couponOrder.add(couponUserDTO);
//		}
//		couponOrderService.update(couponOrderConverter.from(couponOrder));
	}

	@Override
	public R<Void> userCouponValidJobHandle() {
		this.userCouponValid();
		return R.ok();
	}

	@Override
	public R<Void> userCouponInvalidJobHandle() {
		this.userCouponInvalid();
		return R.ok();
	}

	@Override
	public R<CouponDTO> getById(Long id, Long userId) {
		CouponUserDTO couponUserDTO = converter.to(couponUserDao.getById(id));
		R<CouponDTO> couponResult = couponService.getById(couponUserDTO.getCouponId(), couponUserDTO.getId(), userId);
		CouponDTO couponDTO = couponResult.getData();
		if (!couponResult.getSuccess() || null == couponDTO) {
			return R.fail("优惠券不存在");
		}

		Long shopId = couponDTO.getShopId();
		if (ObjectUtil.isNotEmpty(shopId) && shopId > 0 && couponDTO.getShopProviderFlag()) {
			ShopDetailDTO shopdetail = shopDetailApi.getById(shopId).getData();
			couponDTO.setShopName(shopdetail.getShopName());
		}
		couponDTO.setReceivedFlag(true);
		couponDTO.setCouponUserDTO(couponUserDTO);
		return R.ok(couponDTO);
	}

	@Override
	public List<CouponUserDTO> queryById(List<Long> ids) {
		return converter.to(couponUserDao.queryAllByIds(ids));
	}

	@Override
	public List<Long> save(List<CouponUser> couponUserList) {
		return couponUserDao.save(couponUserList);
	}

	@Override
	public R<MyCouponCountBO> getUserCouponCount(Long userId) {
		Integer availableCount = couponUserDao.getAvailableCount(userId);
		Integer usedCount = couponUserDao.getUsedCount(userId);
		Integer expireCount = couponUserDao.getExpireCount(userId);
		MyCouponCountBO myCouponCountBO = new MyCouponCountBO();
		myCouponCountBO.setAvailableCount(availableCount);
		myCouponCountBO.setUsedCount(usedCount);
		myCouponCountBO.setExpireCount(expireCount);
		return R.ok(myCouponCountBO);
	}

	@Override
	public void updateVisit(CouponViewDTO couponViewDTO) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.COUPON_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_COUPON_VIEW_LOG_ROUTING_KEY, couponViewDTO);
	}


	/**
	 * 优惠券活动生效时间
	 *
	 * @param useStartTime 生效时间
	 * @param useDayLater  多少天后可以使用
	 * @return
	 */
	private Date getOnTime(Date useStartTime, Integer useDayLater) {
		return ObjectUtil.isNotEmpty(useStartTime) ? useStartTime : DateUtil.offsetDay(new Date(), useDayLater);
	}

	/**
	 * 优惠券活动失效时间
	 *
	 * @param useEndTime 失效时间
	 * @param onTime     生效时间
	 * @param withinDay  几天之内可用
	 * @return
	 */
	private Date getOffTime(Date useEndTime, Date onTime, Integer withinDay) {
		return ObjectUtil.isNotEmpty(useEndTime) ? useEndTime : DateUtil.offsetDay(onTime, withinDay);
	}


}
