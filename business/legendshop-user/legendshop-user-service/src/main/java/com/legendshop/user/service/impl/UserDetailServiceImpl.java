/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.api.CouponUserApi;
import com.legendshop.basic.api.SystemLogApi;
import com.legendshop.basic.api.UserMsgApi;
import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.StringRedisUtil;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.bo.OrderStatusNumBO;
import com.legendshop.pay.api.UserWalletApi;
import com.legendshop.pay.dto.UserWalletDTO;
import com.legendshop.product.api.FavoriteProductApi;
import com.legendshop.product.api.VitLogApi;
import com.legendshop.shop.api.FavoriteShopApi;
import com.legendshop.shop.api.PubApi;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.bo.UserBasicInformationBO;
import com.legendshop.user.dao.OrdinaryUserDao;
import com.legendshop.user.dao.UserDetailDao;
import com.legendshop.user.dao.UserInvoiceDao;
import com.legendshop.user.dto.*;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.query.UserDetailQuery;
import com.legendshop.user.service.OrdinaryMenuService;
import com.legendshop.user.service.OrdinaryRoleService;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.UserDetailService;
import com.legendshop.user.service.convert.UserDetailConverter;
import com.legendshop.user.utils.VerifyCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务.
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();


	final UserDetailDao userDetailDao;

	final UserMsgApi userMsgApi;

	final VitLogApi vitLogApi;

	final SystemLogApi systemLogApi;

	final PubApi pubApi;

	final OrdinaryUserDao ordinaryUserDao;

	final CouponUserApi couponUserApi;

	final FavoriteShopApi favoriteShopApi;

	final OrdinaryRoleService ordinaryRoleService;

	final OrdinaryMenuService ordinaryMenuService;

	final UserDetailConverter userDetailConverter;

	final OrdinaryUserService ordinaryUserService;

	final FavoriteProductApi favoriteProductApi;

	final StringRedisUtil stringRedisUtil;

	final StringRedisTemplate redisTemplate;

	final OrderApi orderApi;

	final UserWalletApi userWalletApi;

	final UserInvoiceDao userInvoiceDao;

	@Override
	public UserDetailDTO getUserDetailById(Long userId) {
		return userDetailConverter.to(userDetailDao.getUserDetailById(userId));
	}


	@Override
	public PageSupport<UserDetailDTO> queryUserDetailPage(UserDetailQuery userDetailQuery) {
		return this.userDetailConverter.page(this.userDetailDao.queryUserDetailPage(userDetailQuery));
	}

	@Override
	public R<Void> updatePayPassword(VerifyUserDetailDTO userDetailDTO) {
		if (!VerifyCodeUtil.validateCode(new VerifyCodeDTO(userDetailDTO.getCode(), userDetailDTO.getMobile(), UserTypeEnum.USER, userDetailDTO.getCodeType()))) {
			return R.fail("凭证不正确或已失效！");
		}
		this.userDetailDao.updatePayPassword(userDetailDTO.getUserId(), ENCODER.encode(userDetailDTO.getPayPassword()));
		return R.ok();
	}

	@Override
	public List<UserDetailDTO> queryById(List<Long> userIds) {
		return userDetailConverter.to(userDetailDao.queryByUserId(userIds));
	}

	@Override
	public R<MobileUserCenterDTO> mobileInfo(Long userId) {
		MobileUserCenterDTO mobileUserCenter = new MobileUserCenterDTO();

		// 多线程查询
		ExecutorService exec = Executors.newCachedThreadPool();

		// 用户优惠卷数量
		exec.submit(() -> {
			R<Integer> integerR = this.couponUserApi.userCouponCount(userId);
			if (integerR.success()) {
				mobileUserCenter.setCoupon(integerR.getData());
			} else {
				mobileUserCenter.setCoupon(0);
			}
		});

		// 用户收藏
		exec.submit(() -> {
			R<Integer> integerR = this.favoriteProductApi.userFavoriteCount(userId);
			if (integerR.success()) {
				mobileUserCenter.setProdFavorites(integerR.getData());
			} else {
				mobileUserCenter.setProdFavorites(0);
			}
		});
		exec.submit(() -> {
			R<Integer> integerR = this.favoriteShopApi.userFavoriteCount(userId);
			if (integerR.success()) {
				mobileUserCenter.setShopFavorites(integerR.getData());
			} else {
				mobileUserCenter.setShopFavorites(0);
			}
		});

		// 消息数量
		exec.submit(() -> {
			R<UserMsgBo> userMsgBoR = this.userMsgApi.userUnreadMsg(userId);
			mobileUserCenter.setMessage(Optional.ofNullable(userMsgBoR.getData()).map(UserMsgBo::getTotalUnreadMsgCount).orElse(0));
		});

		// 我的公告
		exec.submit(() -> {
			R<Integer> integerR = this.pubApi.userUnreadMsg(userId);
			if (integerR.success()) {
				mobileUserCenter.setMessage(mobileUserCenter.getMessage() + integerR.getData());
			} else {
				mobileUserCenter.setMessage(mobileUserCenter.getMessage());
			}
		});

		// 浏览足迹
		exec.submit(() -> {
			R<Integer> integerR = this.vitLogApi.userVisitLogCount(userId);
			if (integerR.success()) {
				mobileUserCenter.setFootprint(integerR.getData());
			} else {
				mobileUserCenter.setFootprint(0);
			}
		});

		// 用户所有订单状态，发票数量
		exec.submit(() -> {
			OrderStatusNumBO orderStatusNum = this.orderApi.getOrderStatusNum(userId).getData();
			if (ObjectUtil.isNotNull(orderStatusNum)) {
				mobileUserCenter.setPayment(orderStatusNum.getUnpaidNum());
				mobileUserCenter.setPaid(orderStatusNum.getWaitDeliveryNum());
				mobileUserCenter.setConsignment(orderStatusNum.getConsignmentNum());
				mobileUserCenter.setShipped(orderStatusNum.getTakeDeliverNum());
				mobileUserCenter.setUnCommCount(orderStatusNum.getUnCommCount());
				mobileUserCenter.setRefund(orderStatusNum.getProcessingNum());
				mobileUserCenter.setToBeInvoicedOrderCount(orderStatusNum.getToBeInvoicedOrderCount());
				mobileUserCenter.setInvoicedOrderCount(orderStatusNum.getInvoicedOrderCount());
			}
		});

		//可用余额
		exec.submit(() -> {
			R<UserWalletDTO> userWalletPayDTOR = userWalletApi.getByUserId(userId);
			if (ObjectUtil.isNotEmpty(userWalletPayDTOR.getData())) {
				mobileUserCenter.setAvailableAmount(userWalletPayDTOR.getData().getAvailableAmount());
			}
		});

		// 等待所有线程执行完成再返回结果
		exec.shutdown();
		try {
			if (!exec.awaitTermination(10, TimeUnit.SECONDS)) {
				exec.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return R.ok(mobileUserCenter);
	}

	@Override
	public UserInformationDTO getUserInfoById(Long userId) {
		UserDetail userDetail = this.userDetailDao.getUserDetailById(userId);
		OrdinaryUserDTO userDTO = this.ordinaryUserService.getById(userId);
		UserInformationDTO informationDTO = new UserInformationDTO();
		informationDTO.setUserId(userId);
		if (userDetail != null) {
			informationDTO.setNickName(userDetail.getNickName());
			informationDTO.setCreateTime(userDetail.getCreateTime());
		}
		if (userDTO != null) {
			informationDTO.setAvatar(userDTO.getAvatar());
			informationDTO.setMobile(userDTO.getMobile());
		}
		return informationDTO;
	}

	@Override
	public R<UserBasicInformationBO> getUserBasicInformationBOByUserId(Long userId) {

		UserBasicInformationBO userBasicInformationBO = new UserBasicInformationBO();
		OrdinaryUserDTO ordinaryUserDTO = ordinaryUserService.getByUserId(userId);
		if (ObjectUtil.isNull(ordinaryUserDTO)) {
			return R.fail("用户不存在或已被删除");
		}
		UserDetail userDetail = userDetailDao.getUserDetailById(userId);
		if (ObjectUtil.isNotNull(userDetail)) {
			userBasicInformationBO.setNickName(userDetail.getNickName());
			userBasicInformationBO.setLastConsumptionTime(userDetail.getRecentConsumptionTime());
			userBasicInformationBO.setCumulativeConsumptionAmount(userDetail.getConsumptionAmount());
			userBasicInformationBO.setCumulativeOrderQuantity(userDetail.getConsumptionOrderCount());
			userBasicInformationBO.setEmail(userDetail.getEmail());
		}
		// 组装用户信息
		userBasicInformationBO.setUsername(ordinaryUserDTO.getUsername());
		userBasicInformationBO.setAvatar(ordinaryUserDTO.getAvatar());
		userBasicInformationBO.setMobile(ordinaryUserDTO.getMobile());
		userBasicInformationBO.setCreateTime(ordinaryUserDTO.getCreateTime());

		// 获取用户角色
		List<OrdinaryRoleDTO> ordinaryRoleDTOS = ordinaryRoleService.queryByUserId(userId);
		userBasicInformationBO.setOrdinaryRoleDTOList(ordinaryRoleDTOS);

		// 获取用户权限
		List<MenuBO> menuList = ordinaryMenuService.queryByRoleId(Optional.ofNullable(ordinaryRoleDTOS).orElse(Collections.emptyList()).stream().map(OrdinaryRoleDTO::getId).collect(Collectors.toList()));
		userBasicInformationBO.setMenuList(menuList);

		// 获取相关统计数据
		return R.ok(userBasicInformationBO);
	}

	@Override
	public int updateConsumptionStatistics(Long userId, BigDecimal amount, Integer count) {
		return userDetailDao.updateConsumptionStatistics(userId, amount, count);
	}


	@Override
	public R<Void> updateDistribution(Long userId, Long distributionUserId) {
		UserDetail userDetail = userDetailDao.getUserDetailById(userId);
		if (null != userDetail) {
			userDetail.setParentBindingTime(DateUtil.date());
			userDetail.setParentUserId(distributionUserId);
			userDetailDao.update(userDetail);
		}
		return R.ok();
	}

	@Override
	public R<Void> verificationPayPassword(Long userId, String password) {
		UserDetail userDetail = this.userDetailDao.getUserDetailById(userId);
		if (null == userDetail) {
			return R.fail();
		}
		if (!ENCODER.matches(ENCODER.encode(password), userDetail.getPayPassword())) {
			return R.fail();
		}
		return R.ok();
	}

	@Override
	public List<UserInformationDTO> getUserInfoByIds(List<Long> userIds) {
		return this.userDetailDao.getUserInfoByIds(userIds);
	}

	@Override
	public R<Void> feasibilityTest(String param) {
		SystemLogDTO dto = new SystemLogDTO();
		dto.setTitle("全局事务处理！");
		this.systemLogApi.save(dto);
		return null;
	}

	@Override
	@CacheEvict(value = "UserDetail", key = "#userId")
	public Boolean updateWeChatNumber(Long userId, String weChatNumber) {
		UserDetail userDetail = this.userDetailDao.getUserDetailById(userId);
		if (ObjectUtil.isNull(userDetail)) {
			throw new BusinessException("添加失败，该用户不存在");
		}
		userDetail.setWeChatNumber(weChatNumber);
		return this.userDetailDao.update(userDetail) > 0;
	}

	@Override
	public R<List<UserInformationDTO>> getPhoneAndNickNameByIds(List<Long> ids) {
		return R.ok(userDetailDao.getUserInfoByIds(ids));
	}


	@Override
	public R updateEmail(Long userId, String email) {
		UserDetail userDetail = this.userDetailDao.getUserDetailById(userId);
		if (ObjectUtil.isNull(userDetail)) {
			return R.fail("该用户不存在！");
		}
		userDetail.setEmail(email);
		userDetail.setUpdateTime(DateUtil.date());
		this.userDetailDao.update(userDetail);
		return R.ok();
	}

	@Override
	public List<UserDetailDTO> queryByNotAddress() {
		List<UserDetail> userDetailList = userDetailDao.queryByNotAddress();
		return userDetailConverter.to(userDetailList);
	}

	@Override
	public void updateByList(List<UserDetailDTO> userDetailList) {
		List<UserDetail> detailList = userDetailConverter.from(userDetailList);
		userDetailDao.update(detailList);
	}

}
