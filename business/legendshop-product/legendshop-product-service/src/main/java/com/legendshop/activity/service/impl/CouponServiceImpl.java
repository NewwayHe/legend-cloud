/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponShopBO;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dao.CouponProductDao;
import com.legendshop.activity.dao.CouponShopDao;
import com.legendshop.activity.dao.CouponUserDao;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.entity.Coupon;
import com.legendshop.activity.entity.CouponProduct;
import com.legendshop.activity.entity.CouponShop;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.enums.*;
import com.legendshop.activity.handle.ActivitySkuValidators;
import com.legendshop.activity.handle.CouponHandle;
import com.legendshop.activity.mq.producer.CouponProducerService;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponShopQuery;
import com.legendshop.activity.service.CouponOrderService;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.convert.CouponConverter;
import com.legendshop.activity.service.convert.CouponUserConverter;
import com.legendshop.activity.vo.CouponVO;
import com.legendshop.basic.config.CouponRefundSettingConfig;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.ActivityViewDTO;
import com.legendshop.data.enums.ActivityTypeEnum;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.ShopOrderCountDTO;
import com.legendshop.product.api.FavoriteProductApi;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.dto.CouponDocumentDTO;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.api.ShopParamsApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.UserInformationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券(Coupon)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-10 10:52:37
 */
@Service
@AllArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

	private final CouponDao couponDao;
	private final CouponConverter couponConverter;
	private final CouponProductDao couponProductDao;
	private final CouponShopDao couponShopDao;
	private final CouponUserDao couponUserDao;
	private final CouponUserConverter couponUserConverter;
	private final CouponHandle couponHandle;
	private final CacheManagerUtil cacheManagerUtil;
	private final ShopParamsApi shopParamsApi;
	private final ProductApi productApi;
	private final SkuApi skuApi;
	private final ActivitySkuValidators activitySkuValidators;
	private final UserDetailApi userDetailApi;
	private final ShopDetailApi shopDetailApi;
	private final CouponProducerService producerService;
	private final OrderApi orderApi;
	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final FavoriteProductApi favoriteProductApi;
	private final EsIndexApi esIndexApi;
	private final CouponOrderService couponOrderService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R save(CouponDTO couponDTO) {
		if (ObjectUtil.isNotEmpty(couponDTO.getUseStartTime()) || ObjectUtil.isNotEmpty(couponDTO.getUseEndTime())) {
			if (couponDTO.getUseStartTime().after(couponDTO.getUseEndTime())) {
				return R.fail("使用开始时间不能晚于使用结束时间");
			}
			if (couponDTO.getReceiveStartTime().after(couponDTO.getUseStartTime())) {
				return R.fail("领取开始时间不能晚于使用开始时间");
			}
			if (couponDTO.getReceiveEndTime().after(couponDTO.getUseEndTime())) {
				return R.fail("领取结束时间不能晚于使用结束时间");
			}
		}
		if (DateUtil.date().after(couponDTO.getReceiveEndTime())) {
			return R.fail("领取结束时间不能晚于当前时间");
		}
		if (ObjectUtil.isEmpty(couponDTO.getUseEndTime()) && (ObjectUtil.isEmpty(couponDTO.getWithinDay()) || couponDTO.getWithinDay() == 0)) {
			return R.fail("【使用结束时间】、【几天之内可用】不能同时为空");
		}
		if (ObjectUtil.isNotEmpty(couponDTO.getUseEndTime()) && couponDTO.getUseEndTime().before(couponDTO.getReceiveEndTime())) {
			return R.fail("【使用结束时间】不能早于【领取结束时间】");
		}
		if (ObjectUtil.isEmpty(couponDTO.getUseStartTime()) && ObjectUtil.isEmpty(couponDTO.getUseDayLater())) {
			return R.fail("【使用开始时间】、【多少天后可以使用】不能同时为空");
		}
		if (couponDTO.getPerDayLimit() > 0 && couponDTO.getPerTotalLimit() > 0) {
			return R.fail("非法操作");
		}
		/*保存优惠券保存规则*/
		if (0 != couponDTO.getShopId()) {
			if (!CouponUseTypeEnum.GENERAL.getValue().equals(couponDTO.getUseType())) {
				List<CouponProduct> couponProducts = couponProductDao.queryByCouponId(couponDTO.getId());
				if (CollUtil.isEmpty(couponProducts)) {
					return R.fail("请选择商品");
				}
			}

			// 获取商家规则
			R result = shopParamsApi.getConfigDtoByParamName(couponDTO.getShopId(), SysParamNameEnum.COUPON_REFUND_SETTING.name(), CouponRefundSettingConfig.class);
			LinkedHashMap data = (LinkedHashMap) result.getData();
			CouponRefundSettingConfig settingConfig = BeanUtil.mapToBean(data, CouponRefundSettingConfig.class, true);
			couponDTO.setPaymentRefundableFlag(settingConfig.getPayment_REFUNDABLE());
			couponDTO.setNonPaymentRefundableFlag(settingConfig.getNon_PAYMENT_REFUNDABLE());
			couponDTO.setPaymentAllAfterSalesRefundableFlag(settingConfig.getPayment_ALL_AFTER_SALES_REFUNDABLE());
		} else {
			if (!CouponUseTypeEnum.GENERAL.getValue().equals(couponDTO.getUseType())) {
				List<CouponShop> couponShopList = couponShopDao.getCouponShopByCouponId(couponDTO.getId());
				if (CollUtil.isEmpty(couponShopList)) {
					return R.fail("请选择店铺");
				}
			}

			// 平台
			couponDTO.setPaymentRefundableFlag(false);
			couponDTO.setNonPaymentRefundableFlag(true);
			couponDTO.setPaymentAllAfterSalesRefundableFlag(false);
		}
		if (ObjectUtil.isEmpty(couponDTO.getId())) {
			couponDTO.setId(couponDao.createId());
		}
		// 如果是卡密领取，则不可以指定用户
		if (CouponReceiveTypeEnum.PWD.getValue().equals(couponDTO.getReceiveType())) {
			couponDTO.setDesignatedUser(CouponDesignateEnum.NONE.getValue());
		}
		if (CouponDesignateEnum.MOBILE.getValue().equals(couponDTO.getDesignatedUser())) {
			if (ObjectUtil.isEmpty(couponDTO.getMobileList())) {
				throw new BusinessException("指定用户是手机号码，手机号码不能为空");
			}
			couponDTO.setDesignatedUserMobile(couponDTO.getMobileList());
		}
		couponDTO.setCreateTime(new Date());
		couponDTO.setReceiveCount(0);
		couponDTO.setUseCount(0);
		couponDTO.setPlatformDeleteStatus(false);
		couponDTO.setStatus(CouponStatusEnum.NOT_STARTED.getValue());
		couponDao.save(couponConverter.from(couponDTO), couponDTO.getId());
		/*优惠券活动生效*/
		producerService.couponOnLine(couponDTO);
		/*优惠券活动失效*/
		producerService.couponOffLine(couponDTO.getId(), couponDTO.getReceiveEndTime());
		return R.ok(couponDTO.getId());
	}

	@Override
	public PageSupport<SkuBO> queryCouponProductPage(CouponQuery couponQuery) {
		Coupon coupon = couponDao.getById(couponQuery.getCouponId());
		PageSupport<SkuBO> productPage = couponProductDao.queryCouponProductPage(couponQuery);
		List<SkuBO> resultList = productPage.getResultList();

		if (CollUtil.isNotEmpty(resultList)) {
			for (SkuBO skuBO : resultList) {
				if (coupon != null) {
					skuBO.setEstimatedIncome(skuBO.getPrice().subtract(skuBO.getCostPrice()).subtract(coupon.getAmount()));
				}
			}
			if (ObjectUtil.isNotNull(couponQuery.getUserId())) {
				//组装所有productid
				List<Long> productIdList = resultList.stream().map(SkuBO::getProductId).collect(Collectors.toList());
				//查询当前用户是否已收藏商品
				List<FavoriteProductDTO> favoriteProductList = favoriteProductApi.getFavouriteProductId(couponQuery.getUserId(), productIdList).getData();
				for (SkuBO skuBO : resultList) {
					skuBO.setFavouriteFlag(0L);
					Optional<FavoriteProductDTO> favorite = favoriteProductList.stream().filter(e -> skuBO.getProductId().equals(e.getProductId())).findFirst();
					if (favorite.isPresent()) {
						skuBO.setFavouriteFlag(favorite.get().getId());
					}
				}
			}
		}


		return productPage;
	}

	@Override
	@CacheEvict(value = CacheConstants.COUPON_DETAILS, key = "#id")
	public R updateStatus(Long id, Integer status) {
		return R.ok(couponDao.updateStatus(id, status));
	}

	@Override
	public R updateStatus(Long userId, String orderNumber, CouponOrderUpdateDTO couponOrderUpdateDTO) {
		if (ObjectUtil.isNotEmpty(couponOrderUpdateDTO)) {
			List<CouponOrderDTO> couponOrderList = couponOrderUpdateDTO.getCouponOrderList();

			couponUserDao.batchUpdateStatus(userId, couponOrderList.stream().map(CouponOrderDTO::getUserCouponId)
					.collect(Collectors.toList()), orderNumber, CouponUserStatusEnum.USED.getValue());
			for (CouponOrderDTO platformCoupon : couponOrderList) {
				cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, platformCoupon.getCouponId());
			}
			List<CouponItemDTO> couponItemDTOList = Convert.toList(CouponItemDTO.class, couponOrderList);
			this.batchUpdateAdminStatus(couponItemDTOList);
		}
		return R.ok();

	}

	/**
	 * 批量更新，并清除缓存
	 * todo 应该再加一个参数用于判断是否是平台调用，但目前没有其它需求，所以暂时不加
	 *
	 * @param ids
	 * @param status
	 * @param shopId
	 * @return
	 */
	@Override
	public R batchUpdateStatus(List<Long> ids, Integer status, Long shopId) {
		List<Coupon> couponList = couponDao.queryAllByIds(ids);
		if (CollUtil.isEmpty(couponList)) {
			return R.ok();
		}

		// 如果是将优惠券恢复成进行中
		if (CouponStatusEnum.CONTAINS.getValue().equals(status)) {
			List<Coupon> startList = new ArrayList<>();
			for (Coupon coupon : couponList) {
				// 如果当前时间在领券开始前，则优惠券状态为未开始
				if (DateUtil.date().before(coupon.getReceiveStartTime())) {
					coupon.setStatus(CouponStatusEnum.NOT_STARTED.getValue());
				} else {
					// 如果当前时间在领券结束前，则优惠券状态为进行中
					if (DateUtil.date().before(coupon.getReceiveEndTime())) {
						if (CouponStatusEnum.NOT_STARTED.getValue().equals(coupon.getStatus())) {
							// 需要走一遍优惠券生效流程
							coupon.setStatus(CouponStatusEnum.NOT_STARTED.getValue());
							startList.add(coupon);
						} else {
							coupon.setStatus(CouponStatusEnum.CONTAINS.getValue());
						}
					} else {
						// 如果超过领券结束时间，则优惠券状态为已结束
						coupon.setStatus(CouponStatusEnum.FINISHED.getValue());
					}
				}
				cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, coupon.getId());
			}
			couponDao.update(couponList);

			// 创建优惠券索引，平台券不处理
			for (Coupon coupon : couponList) {
				if (coupon.getShopProviderFlag()) {
					esIndexApi.createCouponIndexByCouponId(coupon.getId());
				}
			}

			// 优惠券活动生效
			for (Coupon coupon : startList) {
				producerService.couponOnLine(couponConverter.to(coupon));
				/*优惠券活动失效*/
				producerService.couponOffLine(coupon.getId(), coupon.getReceiveEndTime());
			}

			return R.ok();
		} else {
			if (!CouponStatusEnum.DELETE.getValue().equals(status)) {
				for (Coupon coupon : couponList) {
					// 删除优惠券索引
					esIndexApi.deleteCouponIndexByCouponId(coupon.getId());
				}
			} else {
				if (null != shopId) {
					for (Coupon coupon : couponList) {
						// 删除优惠券索引
						esIndexApi.deleteCouponIndexByCouponId(coupon.getId());
					}
				}
			}
		}

		if (null != shopId) {
			if (couponList.stream().noneMatch(coupon -> shopId.equals(coupon.getShopId()))) {
				return R.fail("更新失败");
			}
		}

		// 如果需要删除优惠券
		if (CouponStatusEnum.DELETE.getValue().equals(status)) {

			// 则只有已结束和已失效才可以删除
			if (couponList.stream().anyMatch(coupon -> !CouponStatusEnum.FINISHED.getValue().equals(coupon.getStatus()) && !CouponStatusEnum.OFF_LINE.getValue().equals(coupon.getStatus()))) {
				return R.fail("更新失败，只有已结束和已失效的优惠券才可以删除~");
			}

			// candy确认: 如果是平台删除店铺发布的优惠券，则只是将平台删除状态标记为删除，不会影响店铺的删除状态
			if (null == shopId) {
				for (Coupon coupon : couponList) {
					coupon.setPlatformDeleteStatus(true);
				}
				couponDao.update(couponList);
				return R.ok();
			}
		}

		couponDao.batchUpdateStatus(ids, status);
		//修改用户已经领取的优惠券状态
		ids.forEach(couponUserDao::updateUserCouponStatus);

		for (Long id : ids) {
			cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, id);
		}
		return R.ok();
	}

	@Override
	public R<Void> batchUpdateUsedStatus(List<CouponOrderDTO> couponOrderList) {

		// 一个优惠券可以被多个订单项使用（注：目前只有平台优惠券会被多个订单项使用），所以需要聚合一下去重
		Map<Long, List<CouponOrderDTO>> couponMap = couponOrderList.stream().collect(Collectors.groupingBy(CouponOrderDTO::getUserCouponId));

		// 获取用户优惠券ID
		List<Long> userCouponIds = couponOrderList.stream().map(CouponOrderDTO::getUserCouponId).distinct().collect(Collectors.toList());

		// 获取用户优惠券列表
		List<CouponUser> couponUserList = couponUserDao.queryAllByIds(userCouponIds);
		for (CouponUser couponUser : couponUserList) {
			if (CouponUserStatusEnum.USED.getValue().equals(couponUser.getStatus())) {
				throw new BusinessException("优惠券【" + couponUser.getCouponTitle() + "】已被使用或已过期！");
			}
			couponUser.setStatus(CouponUserStatusEnum.USED.getValue());
			couponUser.setUseTime(DateUtil.date());
			String orderNumbers = StrUtil.join("|", couponMap.get(couponUser.getId()).stream().map(CouponOrderDTO::getOrderNumber).distinct().collect(Collectors.toList()));
			couponUser.setOrderNumber(orderNumbers);
		}

		// 批量更新优惠券的信息
		couponUserDao.update(couponUserList);

		couponMap = couponOrderList.stream().collect(Collectors.groupingBy(CouponOrderDTO::getCouponId));
		couponMap.forEach((couponId, couponOrders) -> {
			// 更新优惠券使用数量
			long useCount = couponOrders.stream().map(CouponOrderDTO::getUserCouponId).distinct().count();
			couponDao.updateUseCount(couponId, useCount);

			// 删除缓存
			cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, couponId);
		});

		// 保存优惠券与订单项的关系数据
		couponOrderService.saveCoupon(couponOrderList);

		return R.ok();
	}

	@Override
	public List<CouponDTO> getByIds(List<Long> couponIds) {
		return couponConverter.to(couponDao.getByIds(couponIds));
	}

	@Override
	@CacheEvict(value = CacheConstants.COUPON_DETAILS, key = "#couponDTO.getId()")
	public void update(CouponDTO couponDTO) {
		Coupon origin = couponDao.getById(couponDTO.getId());
		BeanUtil.copyProperties(couponDTO, origin, new CopyOptions().ignoreNullValue());
		/*保存优惠券保存规则*/
		CouponRefundSettingConfig settingConfig = shopParamsApi.getConfigDtoByParamName(couponDTO.getShopId(), SysParamNameEnum.COUPON_REFUND_SETTING.name(), CouponRefundSettingConfig.class).getData();
		couponDTO.setPaymentRefundableFlag(settingConfig.getPayment_REFUNDABLE());
		couponDTO.setNonPaymentRefundableFlag(settingConfig.getNon_PAYMENT_REFUNDABLE());
		couponDTO.setPaymentAllAfterSalesRefundableFlag(settingConfig.getPayment_ALL_AFTER_SALES_REFUNDABLE());

		couponDao.update(origin);
	}

	@Override
	public PageSupport<CouponVO> page(CouponQuery couponQuery) {
		return couponDao.queryCouponPage(couponQuery);
	}

	@Override
	public List<CouponDTO> listReceivable(CouponQuery couponQuery) {
		Map<Integer, List<CouponDTO>> designateMap = couponDao.listReceivable(couponQuery)
				.stream().collect(Collectors.groupingBy(CouponDTO::getDesignatedUser));
		List<CouponDTO> userAbleList = new ArrayList<>();
		/*添加不指定用户的优惠券*/
		if (designateMap.containsKey(CouponDesignateEnum.NONE.getValue())) {
			userAbleList.addAll(designateMap.get(CouponDesignateEnum.NONE.getValue()));
		}

		if (ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			UserInformationDTO userInformationDTO = userDetailApi.getUserInfoById(couponQuery.getUserId()).getData();
			if (ObjectUtil.isNotEmpty(userInformationDTO)) {
				if (ObjectUtil.isNotEmpty(userInformationDTO.getCreateTime())) {
					/*todo 添加  平台新注册用户的优惠券=发布活动后才注册的用户 */
					List<CouponDTO> newUserCouponList = designateMap.get(CouponDesignateEnum.NEW_REGISTRATION_OF_PLATFORM.getValue());
					if (CollUtil.isNotEmpty(newUserCouponList)) {
						for (CouponDTO couponDTO : newUserCouponList) {
							if (couponDTO.getCreateTime().compareTo(userInformationDTO.getCreateTime()) <= 0) {
								userAbleList.add(couponDTO);
							}
						}
					}

					/*todo 添加  平台旧注册用户=发布活动前已注册的用户 */
					List<CouponDTO> oldUserCouponList = designateMap.get(CouponDesignateEnum.OLD_REGISTRATION_OF_PLATFORM.getValue());
					if (CollUtil.isNotEmpty(oldUserCouponList)) {
						for (CouponDTO couponDTO : oldUserCouponList) {
							if (couponDTO.getCreateTime().compareTo(userInformationDTO.getCreateTime()) >= 0) {
								userAbleList.add(couponDTO);
							}
						}
					}
				}

				/*todo 添加  平台新用户=在平台内没有购买过商品的用户（包括申请售后成功的用户）*/
				Long platformCount = orderApi.getPlatformOrderCountExceptRefundSuccess().getData();
				if (ObjectUtil.isEmpty(platformCount) || platformCount == 0) {
					List<CouponDTO> newPlatformCouponList = designateMap.get(CouponDesignateEnum.PLATFORM_NEW_USERS.getValue());
					if (CollUtil.isNotEmpty(newPlatformCouponList)) {
						userAbleList.addAll(newPlatformCouponList);
					}
				}
				/*todo 添加  平台老用户=在平台内已购买过商品的用户（剔除申请售后的用户）*/
				else {
					List<CouponDTO> oldPlatformCouponList = designateMap.get(CouponDesignateEnum.PLATFORM_OLD_USERS.getValue());
					if (CollUtil.isNotEmpty(oldPlatformCouponList)) {
						userAbleList.addAll(oldPlatformCouponList);
					}
				}

				List<CouponDTO> newShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_NEW_USER.getValue());
				if (CollUtil.isNotEmpty(newShopUserCouponList)) {
					/*todo 添加  店铺新用户=在店铺内没有购买过商品的用户（包括申请售后成功的用户）*/

					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(newShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(newShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return true;
									}
								} else {
									// 如果不存在，就是新用户
									return true;
								}
								// 老用户
								return false;
							}).collect(Collectors.toList()));
				}

				/*todo 添加  店铺老用户=在店铺内已购买过商品的用户（剔除申请售后的用户）*/
				List<CouponDTO> oldShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_OLD_USER.getValue());
				if (CollUtil.isNotEmpty(oldShopUserCouponList)) {
					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(oldShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(oldShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return false;
									}
								} else {
									// 如果不存在，就是新用户
									return false;
								}
								// 老用户
								return true;
							}).collect(Collectors.toList()));
				}
			}
		}

		if (CollUtil.isNotEmpty(userAbleList) && ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			List<CouponUserDTO> couponUsers = couponUserDao.queryUnusedCouponIdByUserId(couponQuery.getUserId());
			if (CollUtil.isNotEmpty(couponUsers)) {
				/*标记已经领取的优惠券*/
				Map<Long, List<CouponUserDTO>> listMap = couponUsers.stream().collect(Collectors.groupingBy(CouponUserDTO::getCouponId));
				userAbleList.forEach(e -> {
					if (listMap.containsKey(e.getId())) {
						e.setReceivedFlag(true);
						CouponUserDTO couponUser = listMap.get(e.getId()).get(0);
						e.setUseStartTime(couponUser.getUseStartTime());
						e.setUseEndTime(couponUser.getUseEndTime());
					}
				});
			}
			/*排序：先按照未领取的优惠券排在前，已领取的使用过期的时间先后顺序排序*/
			userAbleList = userAbleList.stream().sorted((o1, o2) -> {
				int i = o2.isReceivedFlag() ^ o1.isReceivedFlag() ? (o1.isReceivedFlag() ? 1 : -1) : 0;
				if (i == 0 && ObjectUtil.isNotEmpty(o1.getUseEndTime()) && ObjectUtil.isNotEmpty(o1.getUseEndTime())) {
					i = DateUtil.compare(o1.getUseEndTime(), (o2.getUseEndTime()));
				}
				return i;
			}).collect(Collectors.toList());
		}
		return userAbleList;
	}

	@Override
	public PageSupport<CouponDTO> receivablePage(CouponQuery couponQuery) {
		PageSupport<CouponDTO> pageSupport = couponDao.queryReceivablePage(couponQuery);
		if (ObjectUtil.isNull(pageSupport)) {
			pageSupport.setTotal(0);
			pageSupport.setCurPageNO(1);
			pageSupport.setPageCount(0);
			return pageSupport;
		}
		Map<Integer, List<CouponDTO>> designateMap = pageSupport.getResultList()
				.stream().collect(Collectors.groupingBy(CouponDTO::getDesignatedUser));
		List<CouponDTO> userAbleList = new ArrayList<>();
		/*添加不指定用户的优惠券*/
		if (designateMap.containsKey(CouponDesignateEnum.NONE.getValue())) {
			userAbleList.addAll(designateMap.get(CouponDesignateEnum.NONE.getValue()));
		}

		if (ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			UserInformationDTO userInformationDTO = userDetailApi.getUserInfoById(couponQuery.getUserId()).getData();
			if (ObjectUtil.isNotEmpty(userInformationDTO)) {
				if (ObjectUtil.isNotEmpty(userInformationDTO.getCreateTime())) {
					/*todo 添加  平台新注册用户的优惠券=发布活动后才注册的用户 */
					List<CouponDTO> newUserCouponList = designateMap.get(CouponDesignateEnum.NEW_REGISTRATION_OF_PLATFORM.getValue());
					if (CollUtil.isNotEmpty(newUserCouponList)) {
						for (CouponDTO couponDTO : newUserCouponList) {
							if (couponDTO.getCreateTime().compareTo(userInformationDTO.getCreateTime()) <= 0) {
								userAbleList.add(couponDTO);
							}
						}
					}

					/*todo 添加  平台旧注册用户=发布活动前已注册的用户 */
					List<CouponDTO> oldUserCouponList = designateMap.get(CouponDesignateEnum.OLD_REGISTRATION_OF_PLATFORM.getValue());
					if (CollUtil.isNotEmpty(oldUserCouponList)) {
						for (CouponDTO couponDTO : oldUserCouponList) {
							if (couponDTO.getCreateTime().compareTo(userInformationDTO.getCreateTime()) >= 0) {
								userAbleList.add(couponDTO);
							}
						}
					}
				}

				/*todo 添加  平台新用户=在平台内没有购买过商品的用户（包括申请售后成功的用户）*/
				Long platformCount = orderApi.getPlatformOrderCountExceptRefundSuccess().getData();
				if (ObjectUtil.isEmpty(platformCount) || platformCount == 0) {
					List<CouponDTO> newPlatformCouponList = designateMap.get(CouponDesignateEnum.PLATFORM_NEW_USERS.getValue());
					if (CollUtil.isNotEmpty(newPlatformCouponList)) {
						userAbleList.addAll(newPlatformCouponList);
					}
				}
				/*todo 添加  平台老用户=在平台内已购买过商品的用户（剔除申请售后的用户）*/
				else {
					List<CouponDTO> oldPlatformCouponList = designateMap.get(CouponDesignateEnum.PLATFORM_OLD_USERS.getValue());
					if (CollUtil.isNotEmpty(oldPlatformCouponList)) {
						userAbleList.addAll(oldPlatformCouponList);
					}
				}

				List<CouponDTO> newShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_NEW_USER.getValue());
				if (CollUtil.isNotEmpty(newShopUserCouponList)) {
					/*todo 添加  店铺新用户=在店铺内没有购买过商品的用户（包括申请售后成功的用户）*/

					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(newShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(newShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return true;
									}
								} else {
									// 如果不存在，就是新用户
									return true;
								}
								// 老用户
								return false;
							}).collect(Collectors.toList()));
				}

				/*todo 添加  店铺老用户=在店铺内已购买过商品的用户（剔除申请售后的用户）*/
				List<CouponDTO> oldShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_OLD_USER.getValue());
				if (CollUtil.isNotEmpty(oldShopUserCouponList)) {
					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(oldShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(oldShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return false;
									}
								} else {
									// 如果不存在，就是新用户
									return false;
								}
								// 老用户
								return true;
							}).collect(Collectors.toList()));
				}
			}
		}

		if (CollUtil.isNotEmpty(userAbleList) && ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			List<CouponUserDTO> couponUsers = couponUserDao.queryUnusedCouponIdByUserId(couponQuery.getUserId());
			if (CollUtil.isNotEmpty(couponUsers)) {
				/*标记已经领取的优惠券*/
				Map<Long, List<CouponUserDTO>> listMap = couponUsers.stream().collect(Collectors.groupingBy(CouponUserDTO::getCouponId));
				userAbleList.forEach(e -> {
					if (listMap.containsKey(e.getId())) {
						e.setReceivedFlag(true);
						CouponUserDTO couponUser = listMap.get(e.getId()).get(0);
						e.setUseStartTime(couponUser.getUseStartTime());
						e.setUseEndTime(couponUser.getUseEndTime());
					}
				});
			}
			/*排序：先按照未领取的优惠券排在前，已领取的使用过期的时间先后顺序排序*/
			userAbleList = userAbleList.stream().sorted((o1, o2) -> {
				int i = o2.isReceivedFlag() ^ o1.isReceivedFlag() ? (o1.isReceivedFlag() ? 1 : -1) : 0;
				if (i == 0 && ObjectUtil.isNotEmpty(o1.getUseEndTime()) && ObjectUtil.isNotEmpty(o1.getUseEndTime())) {
					i = DateUtil.compare(o1.getUseEndTime(), (o2.getUseEndTime()));
				}
				return i;
			}).collect(Collectors.toList());
		}
		pageSupport.setResultList(userAbleList);
		return pageSupport;
	}

	@Override
	public CouponDTO getById(Long id) {
		CouponDTO couponDTO = couponConverter.to(couponDao.getById(id));
		if (ObjectUtil.isNotEmpty(couponDTO)) {
			couponDTO.setMobileList(couponDTO.getDesignatedUserMobile());
			//已领取数量
			Integer receiveCount = couponDTO.getReceiveCount();
			if (receiveCount == null) {
				receiveCount = 0;
			}
			//发放数量
			Integer count = couponDTO.getCount();
			//已领取百分比
			couponDTO.setReceiveCountPercent(NumberUtil.div(receiveCount, count)
					.setScale(2, RoundingMode.DOWN).toString() + "%");

			if (ObjectUtil.isNotEmpty(couponDTO.getShopId())) {
				// 店铺
				if (0 != couponDTO.getShopId() && couponDTO.getShopProviderFlag()) {
					ShopDetailDTO shopdetail = shopDetailApi.getById(couponDTO.getShopId()).getData();
					couponDTO.setShopName(shopdetail.getShopName());
				} else {
					List<CouponShop> shopList = couponShopDao.getCouponShopByCouponId(id);
					couponDTO.setSelectShopId(Optional.ofNullable(shopList).map(couponShops -> couponShops.stream().map(CouponShop::getShopId).collect(Collectors.toList())).orElse(Collections.emptyList()));
				}
			}
		}
		cacheManagerUtil.putCache(CacheConstants.COUPON_DETAILS, id, couponDTO);
		return couponDTO;
	}

	@Override
	public R<CouponDTO> getById(Long id, Long couponUserId, Long userId) {
		CouponDTO couponDTO = couponConverter.to(couponDao.getById(id));
		if (ObjectUtil.isEmpty(couponDTO)) {
			return R.fail("优惠券不存在！");
		}

		//已领取数量
		Integer receiveCount = couponDTO.getReceiveCount();
		if (receiveCount == null) {
			receiveCount = 0;
		}
		//发放数量
		Integer count = couponDTO.getCount();
		//已领取百分比
		couponDTO.setReceiveCountPercent(NumberUtil.div(receiveCount, count).multiply(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.DOWN) + "%");

		if (ObjectUtil.isNotEmpty(couponDTO.getShopId())) {
			// 店铺
			if (0 != couponDTO.getShopId() && couponDTO.getShopProviderFlag()) {
				ShopDetailDTO shopdetail = shopDetailApi.getById(couponDTO.getShopId()).getData();
				couponDTO.setShopName(shopdetail.getShopName());
			} else {
				List<CouponShop> shopList = couponShopDao.getCouponShopByCouponId(id);
				couponDTO.setSelectShopId(Optional.ofNullable(shopList).map(couponShops -> couponShops.stream().map(CouponShop::getShopId).collect(Collectors.toList())).orElse(Collections.emptyList()));
			}
		}

		couponDTO.setUseFlag(false);

		// 默认前端传的优惠券用户ID是不存在的
		boolean existCouponUserId = false;
		if (ObjectUtil.isNotEmpty(couponUserId)) {
			CouponUserDTO couponUser = couponUserConverter.to(couponUserDao.getById(couponUserId));
			couponDTO.setCouponUserDTO(couponUser);
			if (null != couponUser) {
				existCouponUserId = true;
				couponDTO.setReceivedFlag(true);

				// 获取历史信息
				CouponHistoryDTO couponHistoryDTO = null;
				List<CouponHistoryDTO> historyList = new ArrayList<>();

				if (CouponUserStatusEnum.USED.getValue().equals(couponUser.getStatus())) {
					couponDTO.setUseFlag(true);
					List<String> orderNumbers = new ArrayList<>(Arrays.asList(couponUser.getOrderNumber().split("\\|")));
					R<List<OrderDTO>> orderResult = orderApi.queryByNumber(orderNumbers);
					for (OrderDTO orderDTO : orderResult.getData()) {
						couponHistoryDTO = new CouponHistoryDTO();
						couponHistoryDTO.setDatetime(couponUser.getUseTime());
						couponHistoryDTO.setType(2);
						couponHistoryDTO.setOrderId(orderDTO.getId());
						// 如果是店铺优惠券
						if (couponDTO.getShopProviderFlag()) {
							couponHistoryDTO.setAmount(couponDTO.getAmount());
						} else {
							// 如果是平台优惠券
							couponHistoryDTO.setAmount(orderDTO.getPlatformCouponAmount());
						}
						historyList.add(couponHistoryDTO);
					}
				}

				// 领取信息
				couponHistoryDTO = new CouponHistoryDTO();
				couponHistoryDTO.setType(1);
				couponHistoryDTO.setDatetime(couponUser.getGetTime());
				couponHistoryDTO.setAmount(couponDTO.getAmount());
				couponDTO.setHistoryList(historyList);
				historyList.add(couponHistoryDTO);
			}
		}

		// 如果没有优惠券用户ID，则需要查询用户是否存在历史领取优惠券
		if (!existCouponUserId) {
			getIdenticalAvailableCouponUser(couponDTO, userId);
		}
		//发送活动访问记录MQ
		sendActivityViewMQ(couponDTO, userId);
		return R.ok(couponDTO);
	}

	@Override
	public void getIdenticalAvailableCouponUser(CouponDTO couponDTO, Long userId) {
		/*判断是否领取超出优惠券规则的数量、是否有相同的优惠券没有使用*/
		if (null == userId) {
			return;
		}
		List<CouponUser> couponUserList = couponUserDao.queryByUserIdAndCouponId(userId, couponDTO.getId());
		if (CollUtil.isNotEmpty(couponUserList)) {
			/*是否有相同的未使用/未生效优惠券*/
			Optional<CouponUser> optional = couponUserList.stream()
					.filter(couponUser -> CouponUserStatusEnum.UNUSED.getValue().equals(couponUser.getStatus()) || CouponUserStatusEnum.NOT_STARTED.getValue().equals(couponUser.getStatus()))
					.findFirst();
			if (optional.isPresent()) {
				CouponUser couponUser = optional.get();
				couponDTO.setReceivedFlag(true);

				// 获取历史信息
				CouponHistoryDTO couponHistoryDTO = null;
				List<CouponHistoryDTO> historyList = new ArrayList<>();

				if (CouponUserStatusEnum.USED.getValue().equals(couponUser.getStatus())) {
					List<String> orderNumbers = new ArrayList<>(Arrays.asList(couponUser.getOrderNumber().split("\\|")));
					R<List<OrderDTO>> orderResult = orderApi.queryByNumber(orderNumbers);

					for (OrderDTO orderDTO : orderResult.getData()) {
						couponHistoryDTO = new CouponHistoryDTO();
						couponHistoryDTO.setDatetime(couponUser.getUseTime());
						couponHistoryDTO.setType(2);
						couponHistoryDTO.setOrderId(orderDTO.getId());
						// 如果是店铺优惠券
						if (couponDTO.getShopProviderFlag()) {
							couponHistoryDTO.setAmount(couponDTO.getAmount());
						} else {
							// 如果是平台优惠券
							couponHistoryDTO.setAmount(orderDTO.getPlatformCouponAmount());
						}
						historyList.add(couponHistoryDTO);
					}
				}

				// 领取信息
				couponHistoryDTO = new CouponHistoryDTO();
				couponHistoryDTO.setType(1);
				couponHistoryDTO.setDatetime(couponUser.getGetTime());
				couponHistoryDTO.setAmount(couponDTO.getAmount());
				couponDTO.setHistoryList(historyList);
				historyList.add(couponHistoryDTO);

				couponDTO.setCouponUserDTO(couponUserConverter.to(couponUser));
			}
		}
	}

	@Override
	public List<CouponDTO> queryPreOnline() {
		return couponConverter.to(couponDao.queryPreOnline());
	}

	@Override
	public void updateReceiveNum(Long couponId) {
		couponDao.updateReceiveNum(couponId);
		cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, couponId);
	}

	@Override
	public List<CouponDTO> getOffLineId() {
		return couponConverter.to(couponDao.getOffLineId());
	}

	@Override
	public List<CouponItemExtDTO> getAvailableCoupon(Long userId, Map<Long, ShopCouponDTO> shopCouponMap) {
		//获取用户所有能用的优惠券集合
		List<CouponItemExtDTO> couponList = couponDao.queryByUserId(userId, true, CouponUserStatusEnum.UNUSED.getValue());
		//没有优惠券
		if (CollUtil.isEmpty(couponList)) {
			return null;
		}
		//做一遍过滤和筛选出真正可以用的优惠券
		return couponHandle.handleFilter(couponList, shopCouponMap);
	}

	/**
	 * 处理用户互斥的优惠券
	 *
	 * @param shopCouponDTO
	 */
	@Override
	public ShopCouponDTO handleSelectCoupons(ShopCouponDTO shopCouponDTO) {
		return couponHandle.handleSelectCoupons(shopCouponDTO);
	}

	@Override
	public ShopCouponDTO handlerShopCouponsShard(ShopCouponDTO shopCouponDTO) {
		return couponHandle.handlerShopCouponsShard(shopCouponDTO);
	}

	@Override
	public List<CouponItemDTO> getBestPlatFormCoupons(Long userId, Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		//查询平台可用的优惠券
		List<CouponItemExtDTO> platFormCoupons = couponDao.queryByUserId(userId, false, CouponUserStatusEnum.UNUSED.getValue());
		if (CollUtil.isEmpty(platFormCoupons)) {
			return null;
		}

		//过滤平台优惠券，过滤可用，不可用状态
		platFormCoupons = couponHandle.handleFilterPlatForm(platFormCoupons, shopCouponDTOMap);

		//计算最优的平台优惠券
		return couponHandle.handlerPlatFormBest(platFormCoupons, shopCouponDTOMap);
	}

	@Override
	public List<CouponItemDTO> getBestPlatFormCoupons(List<CouponItemDTO> platformCoupons, Map<Long, ShopCouponDTO> shopCouponDTOMap) {
		//获取过滤过后的平台优惠券
		List<CouponItemExtDTO> platFormCouponList = couponHandle.handleFilterPlatForm(couponConverter.converterCouponFromExtDTO(platformCoupons), shopCouponDTOMap);
		//计算最优的平台优惠券
		return couponHandle.handlerPlatFormBest(platFormCouponList, shopCouponDTOMap);
	}

	@Override
	public void batchUpdateStatus(Long userId, String orderNumber, List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return;
		}
		//批量更新优惠券的状态
		couponUserDao.batchUpdateStatus(userId, ids, orderNumber, CouponUserStatusEnum.USED.getValue());
		for (Long id : ids) {
			cacheManagerUtil.evictCache(CacheConstants.COUPON_DETAILS, id);
		}

		// 更新优惠券使用数量
		List<CouponUser> couponUsers = couponUserDao.queryAllByIds(ids);
		List<Long> couponIds = couponUsers.stream().map(CouponUser::getCouponId).collect(Collectors.toList());
		List<Coupon> coupons = couponDao.queryAllByIds(couponIds);
		for (Coupon coupon : coupons) {
			coupon.setUseCount(Optional.ofNullable(coupon.getUseCount()).orElse(0) + 1);
		}
		couponDao.update(coupons);
	}

	@Override
	public void batchUpdateAdminStatus(List<CouponItemDTO> platformCoupons) {
		if (CollUtil.isEmpty(platformCoupons)) {
			return;
		}
		List<Long> couponUserId = platformCoupons.stream().map(CouponItemDTO::getUserCouponId).collect(Collectors.toList());
		log.info("开始处理平台优惠券, couponUserId:{}", couponUserId);
		List<CouponUser> couponUsers = couponUserDao.queryAllByIds(couponUserId);
		if (CollUtil.isEmpty(couponUsers)) {
			return;
		}
		List<Long> couponIds = couponUsers.stream().map(CouponUser::getCouponId).collect(Collectors.toList());
		log.info("获取到的平台优惠券, couponIds:{}", couponUserId);
		List<Coupon> coupons = couponDao.queryAllByIds(couponIds);
		for (Coupon coupon : coupons) {
			if (ObjectUtil.isEmpty(coupon)) {
				continue;
			}
			coupon.setUseCount(Optional.ofNullable(coupon.getUseCount()).orElse(0) + 1);
		}
		couponDao.update(coupons);
	}

	@Override
	public R<Void> couponOnLineJobHandle() {

		List<CouponDTO> couponDTOList = queryPreOnline();
		//正常情况下，MQ队列上线优惠劵。couponDTOList为空
		if (CollUtil.isNotEmpty(couponDTOList)) {
			//当MQ没有执行成功，进行补偿操作。把优惠劵上线。
			batchUpdateStatus(
					couponDTOList.stream().map(CouponDTO::getId).collect(Collectors.toList()),
					CouponStatusEnum.CONTAINS.getValue(), null);

			//卡密优惠劵上线，需要同步生成对应的卡密优惠劵，因为卡密优惠劵，是到了可领取时间，才会生成卡密。
			for (CouponDTO couponDTO : couponDTOList) {
				/*如果是卡密兑换，优惠券活动开始的同时，生成卡密优惠券【用户信息为空，用户兑换时补全】*/
				if (CouponReceiveTypeEnum.PWD.getValue().equals(couponDTO.getReceiveType())) {
					List<CouponUser> couponUserList = new ArrayList<>();
					for (int i = 0; i < couponDTO.getCount(); i++) {
						CouponUser couponUser = new CouponUser();
						/*保存优惠券保存规则*/
						couponUser.setPaymentRefundableFlag(couponDTO.getPaymentRefundableFlag());
						couponUser.setNonPaymentRefundableFlag(couponDTO.getNonPaymentRefundableFlag());
						couponUser.setPaymentAllAfterSalesRefundableFlag(couponDTO.getPaymentAllAfterSalesRefundableFlag());
						couponUser.setCouponId(couponDTO.getId());
						couponUser.setCouponTitle(couponDTO.getTitle());
						couponUser.setCouponCode(RandomUtil.getRandomSn());
						couponUser.setId(couponUserDao.createId());
						couponUser.setPassword(couponUser.getId() + RandomUtil.getRandomString(4));
						couponUser.setGetTime(couponDTO.getReceiveStartTime());
						couponUser.setGetType(CouponReceiveTypeEnum.PWD.getValue());
						couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
						/*如果优惠券有指定使用时间段，则设置进去*/
						if (ObjectUtil.isNotEmpty(couponDTO.getUseStartTime())) {
							couponUser.setUseStartTime(couponDTO.getUseStartTime());
						}
						if (ObjectUtil.isNotEmpty(couponDTO.getUseEndTime())) {
							couponUser.setUseEndTime(couponDTO.getUseEndTime());
						}
						if (couponDTO.getUseDayLater() > 0) {
							couponUser.setStatus(CouponUserStatusEnum.NOT_STARTED.getValue());
						}
						couponUserList.add(couponUser);
					}
					List<Long> idList = couponUserDao.saveWithId(couponUserList);

					/*如果优惠券有指定使用时间段，则设置优惠券生效、失效队列*/
					if (ObjectUtil.isNotEmpty(couponDTO.getUseStartTime())) {
						producerService.userCouponOnLine(idList, couponDTO.getUseStartTime());
					}
					if (ObjectUtil.isNotEmpty(couponDTO.getUseEndTime())) {
						producerService.userCouponOffLine(idList, couponDTO.getUseEndTime());
					}
				}
			}
		}

		return R.ok();
	}

	@Override
	public R<Void> couponOffLineJobHandle() {

		List<CouponDTO> offLineId = getOffLineId();
		log.info("offLineId" + offLineId.toString());
		List<Long> updateId;
		if (CollUtil.isNotEmpty(offLineId)) {
			updateId = offLineId.stream().filter(couponDTO -> {
				if (CouponStatusEnum.CONTAINS.getValue().equals(couponDTO.getStatus()) || CouponStatusEnum.PAUSE.getValue().equals(couponDTO.getStatus())) {
					return true;
				}
				return false;
			}).map(CouponDTO::getId).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(updateId)) {
				batchUpdateStatus(updateId, CouponStatusEnum.FINISHED.getValue(), null);
			}
		}
		return R.ok();
	}


	@Override
	public Map<Long, ShopCouponDTO> getShopBestCoupons(Long userId, Map<Long, ShopCouponDTO> shopCouponMap) {
		List<CouponItemExtDTO> availableCoupon = getAvailableCoupon(userId, shopCouponMap);
		if (CollUtil.isEmpty(availableCoupon)) {
			return null;
		}
		//计算最优的优惠券
		return couponHandle.handlerShopBest(availableCoupon, shopCouponMap);

	}


	@Override
	public PageSupport<ProductBO> productPage(ProductQuery productQuery) {
		/*查询商品*/
		PageSupport<ProductBO> productServicePage = productApi.getPage(productQuery).getData();
		if (productServicePage.getTotal() > 0) {
			/*根据productId查找所有的skuList*/
			List<ProductBO> productBOList = productServicePage.getResultList();
			List<Long> productIdList = productBOList.stream().map(ProductBO::getId).collect(Collectors.toList());
			List<SkuBO> skuBOList = skuApi.querySkuByProductIdList(productIdList).getData();
			List<Long> skuIdList = skuBOList.stream().map(SkuBO::getId).collect(Collectors.toList());
			if (ObjectUtil.isNotEmpty(productQuery.getCouponId())) {
				/*校验sku有没有勾选*/
				List<Long> selectSkuIds = couponProductDao.queryByCouponIdAndSkuList(productQuery.getCouponId(), skuIdList);
				if (CollUtil.isNotEmpty(selectSkuIds)) {
					for (SkuBO skuBO : skuBOList) {
						if (selectSkuIds.contains(skuBO.getId())) {
							skuBO.setCheck(true);
						}
					}
				}
			}

			// 商品主图Map
			Map<Long, String> picMap = productBOList.stream().collect(Collectors.toMap(ProductBO::getId, ProductBO::getPic));

			Map<Long, List<SkuBO>> originSkuMap = skuBOList.stream().collect(Collectors.groupingBy(SkuBO::getProductId));

			/*将活动模块的sku信息组装到sku中*/
			for (List<SkuBO> value : originSkuMap.values()) {
				for (SkuBO skuBO : value) {
					if (StrUtil.isBlank(skuBO.getPic())) {
						skuBO.setPic(picMap.get(skuBO.getProductId()));
					}
				}
			}
			/*组装sku到对应的product中*/
			for (ProductBO productBO : productBOList) {
				if (ObjectUtil.isNotEmpty(originSkuMap.get(productBO.getId()))) {
					productBO.setSkuBOList(originSkuMap.get(productBO.getId()));
				}
			}
		}
		return productServicePage;
	}

	@Override
	public List<Long> filterShop(Long couponId) {
		List<CouponShop> couponShopList = couponShopDao.getCouponShopByCouponId(couponId);
		if (CollUtil.isEmpty(couponShopList)) {
			return null;
		}
		List<Long> shopIds = couponShopList.stream().map(CouponShop::getShopId).collect(Collectors.toList());
		return shopIds;
	}

	@Override
	public List<CouponUserDTO> queryUnusedCouponIdByUserId(Long userId) {
		return couponUserDao.queryUnusedCouponIdByUserId(userId);
	}

	@Override
	public List<CouponItemDTO> handleSelectPlatformCoupons(SelectPlatformCouponDTO selectPlatformCouponDTO) {
		return couponHandle.handleSelectPlatformCoupons(selectPlatformCouponDTO);
	}

	@Override
	public CouponDTO getCouponByUserCouponIdAndShopId(Long userId, Long shopId) {
		return couponConverter.to(couponDao.getCouponByUserCouponIdAndShopId(userId, shopId));
	}

	@Override
	public PageSupport<CouponShopBO> queryCouponShopPage(CouponQuery couponQuery) {
		return couponShopDao.queryCouponShopPage(couponQuery);
	}

	@Override
	public R<PageSupport<CouponShopBO>> queryCouponShopPage(CouponShopQuery couponShopQuery) {
		Coupon coupon = couponDao.getById(couponShopQuery.getCouponId());
		if (null == coupon) {
			return R.ok();
		}
		couponShopQuery.setUseType(coupon.getUseType());
		PageSupport<CouponShopBO> result = couponShopDao.queryCouponShopPage(couponShopQuery);
		if (CollUtil.isNotEmpty(result.getResultList())) {
			List<Long> shopIds = result.getResultList().stream().map(CouponShopBO::getShopId).collect(Collectors.toList());
			R<List<SkuBO>> skuResult = skuApi.queryCouponSkuByShopId(shopIds);
			List<SkuBO> skuList = skuResult.getData();
			Map<Long, List<SkuBO>> skuMap = skuList.stream().collect(Collectors.groupingBy(SkuBO::getShopId));
			//处理sku无图片规格数据,显示商品主图
			if (CollUtil.isNotEmpty(skuList)) {
				//无图片规格sku
				List<SkuBO> noPicSku = skuList.stream().filter(skuBO -> StrUtil.isBlank(skuBO.getPic())).collect(Collectors.toList());
				//没有数据直接返回
				if (CollUtil.isEmpty(noPicSku)) {
					return R.ok(result);
				}

				//存在无图片规格商品则查询商品主图
				List<Long> productIdList = noPicSku.stream().map(SkuBO::getProductId).collect(Collectors.toList());
				R<List<ProductDTO>> productList = productApi.queryAllByIds(productIdList);
				if (CollUtil.isEmpty(productList.getData())) {
					return R.ok(result);
				}

				for (ProductDTO productDTO : productList.getData()) {
					if (skuMap.containsKey(productDTO.getId()) && CollUtil.isEmpty(skuMap.get(productDTO.getId()))) {
						continue;
					}
					//给每个sku设置商品主图
					Map<Long, List<SkuBO>> noPicSkuMap = noPicSku.stream().collect(Collectors.groupingBy(SkuBO::getProductId));
					List<SkuBO> skuBOS = noPicSkuMap.get(productDTO.getId());
					if (CollUtil.isEmpty(skuBOS)) {
						continue;
					}
					skuBOS.forEach(skuBO -> skuBO.setPic(productDTO.getPic()));
				}
			}
			for (CouponShopBO couponShopBO : result.getResultList()) {
				if (skuMap.containsKey(couponShopBO.getShopId())) {
					couponShopBO.setSkuList(skuMap.get(couponShopBO.getShopId()));
				}
			}
		}
		Long couponId = couponShopQuery.getCouponId();
		//	getCouponId(couponId);
		return R.ok(result);
	}

	@Override
	public void subUseCount(List<Long> couponIds) {
		// 更新优惠券使用数量
		List<Coupon> coupons = couponDao.queryAllByIds(couponIds);
		for (Coupon coupon : coupons) {
			coupon.setUseCount(Optional.ofNullable(coupon.getUseCount()).orElse(0) - 1);
		}
		couponDao.update(coupons);
	}

	@Override
	public void updateUseCount(List<OrderRefundCouponDTO> couponUsers) {
		if (CollUtil.isNotEmpty(couponUsers)) {
			List<Long> couponIds = couponUsers.stream().map(OrderRefundCouponDTO::getCouponId).collect(Collectors.toList());
			// 更新优惠券使用数量
			List<Coupon> coupons = couponDao.queryAllByIds(couponIds);
			for (Coupon coupon : coupons) {
				coupon.setUseCount(Optional.ofNullable(coupon.getUseCount()).orElse(0) - 1);
			}
			couponDao.update(coupons);
		}

	}

	@Override
	public void sendActivityViewMQ(CouponDTO couponDTO, Long userId) {

		if (ObjectUtil.isNull(couponDTO)) {
			return;
		}

		ActivityViewDTO activityViewDTO = new ActivityViewDTO();
		activityViewDTO.setUserId(userId);
		List<String[]> list = new ArrayList<>();
		String[] strings = new String[2];
		if (couponDTO.getId() == null) {
			return;
		}
		strings[0] = couponDTO.getId().toString();

		if (couponDTO.getShopProviderFlag()) {
			strings[1] = ActivityTypeEnum.SHOP_COUPON.name();
		} else {
			strings[1] = ActivityTypeEnum.PLARTFORM_COUPON.name();
		}

		list.add(strings);
		activityViewDTO.setActivityList(list);
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_ACTIVITY_VIEW_LOG_ROUTING_KEY, activityViewDTO);
	}

	@Override
	public PageSupport<CouponDTO> queryCouponsByStatus(CouponQuery couponQuery) {
		couponQuery.setStatus(CouponStatusEnum.CONTAINS.getValue());
		PageSupport<CouponDTO> couponDTOPageSupport = couponDao.queryCoupon(couponQuery);
		List<CouponDTO> resultList = couponDTOPageSupport.getResultList();
		for (CouponDTO couponDTO : resultList) {
			if (CouponUseTypeEnum.GENERAL.getValue().equals(couponDTO.getUseType())) {
				continue;
			}
			if (couponDTO.getShopProviderFlag()) {
				couponDTO.setSkuIdList(couponDao.queryCouponSkuIds(couponDTO.getId()));
			} else {
				couponDTO.setSelectShopId(couponDao.queryCouponShopIds(couponDTO.getId()));
			}
		}
		couponDTOPageSupport.setResultList(resultList);
		return couponDTOPageSupport;
	}

	@Override
	public CouponDTO getCouponsById(Long couponId) {
		CouponDTO couponDTO = couponConverter.to(couponDao.getById(couponId));
		if (null == couponDTO) {
			return null;
		}

		// 如果不是通用券，获取指定店铺和指定商品
		if (!CouponUseTypeEnum.GENERAL.getValue().equals(couponDTO.getUseType())) {
			if (couponDTO.getShopProviderFlag()) {
				couponDTO.setSkuIdList(couponDao.queryCouponSkuIds(couponDTO.getId()));
			} else {
				couponDTO.setSelectShopId(couponDao.queryCouponShopIds(couponDTO.getId()));
			}
		}
		return couponDTO;
	}

	@Override
	public List<CouponDTO> listReceivableES(CouponQuery couponQuery) {
		List<CouponDocumentDTO> couponDocumentDTOList = esIndexApi.queryCouponBySkuIdsAndShopId(couponQuery.getSkuIds(), couponQuery.getShopId());
		List<CouponDTO> couponDTOList = createCouponDTOList(couponDocumentDTOList);
		Map<Integer, List<CouponDTO>> designateMap = couponDTOList.stream().collect(Collectors.groupingBy(CouponDTO::getDesignatedUser));
		List<CouponDTO> userAbleList = new ArrayList<>();
		/*添加不指定用户的优惠券*/
		if (designateMap.containsKey(CouponDesignateEnum.NONE.getValue())) {
			userAbleList.addAll(designateMap.get(CouponDesignateEnum.NONE.getValue()));
		}

		if (ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			UserInformationDTO userInformationDTO = userDetailApi.getUserInfoById(couponQuery.getUserId()).getData();
			if (ObjectUtil.isNotEmpty(userInformationDTO)) {
				// TODO 平台优惠券不显示在商品详情页面

				List<CouponDTO> newShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_NEW_USER.getValue());
				if (CollUtil.isNotEmpty(newShopUserCouponList)) {
					/*todo 添加  店铺新用户=在店铺内没有购买过商品的用户（包括申请售后成功的用户）*/

					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(newShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(newShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return true;
									}
								} else {
									// 如果不存在，就是新用户
									return true;
								}
								// 老用户
								return false;
							}).collect(Collectors.toList()));
				}

				/*todo 添加  店铺老用户=在店铺内已购买过商品的用户（剔除申请售后的用户）*/
				List<CouponDTO> oldShopUserCouponList = designateMap.get(CouponDesignateEnum.SHOP_OLD_USER.getValue());
				if (CollUtil.isNotEmpty(oldShopUserCouponList)) {
					// 获取用户在该店铺下的订单数(除了售后成功的订单)，如果有，则代表是店铺老用户；如果没有，则代表是店铺新用户
					List<ShopOrderCountDTO> dtoList = orderApi.getShopOrderCountExceptRefundSuccess(oldShopUserCouponList.stream().map(CouponDTO::getShopId).collect(Collectors.toList())).getData();
					Map<Long, Long> countMap = null;
					if (CollUtil.isNotEmpty(dtoList)) {
						countMap = dtoList.stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, dto -> Optional.ofNullable(dto.getCount()).orElse(0L)));
					}

					if (CollUtil.isEmpty(countMap)) {
						countMap = new HashMap<>(16);
					}

					Map<Long, Long> finalCountMap = countMap;
					userAbleList.addAll(oldShopUserCouponList.stream()
							.filter(couponDTO -> {
								if (finalCountMap.containsKey(couponDTO.getShopId())) {
									// 如果存在，并且为0，就是新用户
									if (finalCountMap.get(couponDTO.getShopId()) == 0) {
										return false;
									}
								} else {
									// 如果不存在，就是新用户
									return false;
								}
								// 老用户
								return true;
							}).collect(Collectors.toList()));
				}
			}
		}

		if (CollUtil.isNotEmpty(userAbleList) && ObjectUtil.isNotEmpty(couponQuery.getUserId())) {
			List<CouponUserDTO> couponUsers = couponUserDao.queryUnusedCouponIdByUserId(couponQuery.getUserId());
			if (CollUtil.isNotEmpty(couponUsers)) {
				/*标记已经领取的优惠券*/
				Map<Long, List<CouponUserDTO>> listMap = couponUsers.stream().collect(Collectors.groupingBy(CouponUserDTO::getCouponId));
				userAbleList.forEach(e -> {
					if (listMap.containsKey(e.getId())) {
						e.setReceivedFlag(true);
						CouponUserDTO couponUser = listMap.get(e.getId()).get(0);
						e.setUseStartTime(couponUser.getUseStartTime());
						e.setUseEndTime(couponUser.getUseEndTime());
					}
				});
			}
			/*排序：先按照未领取的优惠券排在前，已领取的使用过期的时间先后顺序排序*/
			userAbleList = userAbleList.stream().sorted((o1, o2) -> {
				int i = o2.isReceivedFlag() ^ o1.isReceivedFlag() ? (o1.isReceivedFlag() ? 1 : -1) : 0;
				if (i == 0 && ObjectUtil.isNotEmpty(o1.getUseEndTime()) && ObjectUtil.isNotEmpty(o1.getUseEndTime())) {
					i = DateUtil.compare(o1.getUseEndTime(), (o2.getUseEndTime()));
				}
				return i;
			}).collect(Collectors.toList());
		}
		return userAbleList;
	}

	/**
	 * 将优惠券文档对象转换成优惠券DTO
	 *
	 * @param couponDocumentDTOList 优惠券文档对象
	 * @return
	 */
	private List<CouponDTO> createCouponDTOList(List<CouponDocumentDTO> couponDocumentDTOList) {
		List<CouponDTO> couponDTOList = new ArrayList<>();
		couponDocumentDTOList.forEach(e -> {
			CouponDTO couponDTO = new CouponDTO();
			couponDTO.setId(e.getCouponId());
			couponDTO.setCouponId(e.getCouponId());
			couponDTO.setShopId(e.getShopId());
			couponDTO.setSkuIdList(e.getSkuIds());
			couponDTO.setShopProviderFlag(e.getShopProviderFlag());
			couponDTO.setUseType(e.getUseType());
			couponDTO.setStatus(e.getStatus());
			couponDTO.setTitle(e.getTitle());
			couponDTO.setRemark(e.getRemark());
			couponDTO.setAmount(e.getAmount());
			couponDTO.setMinPoint(e.getMinPoint());
			couponDTO.setReceiveStartTime(e.getReceiveStartTime());
			couponDTO.setReceiveEndTime(e.getReceiveEndTime());
			couponDTO.setUseStartTime(e.getUseStartTime());
			couponDTO.setUseEndTime(e.getUseEndTime());
			couponDTO.setDesignatedUser(e.getDesignatedUser());
			couponDTO.setSkuIdList(e.getSkuIds());
			couponDTOList.add(couponDTO);
		});
		return couponDTOList;
	}

}
