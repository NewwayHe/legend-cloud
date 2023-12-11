/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.convert;

import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.activity.dto.CouponItemExtDTO;
import com.legendshop.activity.entity.Coupon;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券(Coupon)转换器
 *
 * @author legendshop
 * @since 2020-09-10 10:52:38
 */
@Mapper
public interface CouponConverter extends BaseConverter<Coupon, CouponDTO> {

	default CouponItemDTO converterCouponItemDTO(CouponDTO couponDTO) {
		CouponItemDTO couponItemDTO = new CouponItemDTO();
		couponItemDTO.setCouponId(couponDTO.getId());
		couponItemDTO.setCouponName(couponDTO.getTitle());
		couponItemDTO.setRemark(couponDTO.getRemark());
		couponItemDTO.setShopProviderFlag(couponDTO.getShopProviderFlag());
		couponItemDTO.setAmount(couponDTO.getAmount());
		couponItemDTO.setUseType(couponDTO.getUseType());
		couponItemDTO.setMinPoint(couponDTO.getMinPoint());
		couponItemDTO.setUseStartTime(couponDTO.getUseStartTime());
		couponItemDTO.setUseEndTime(couponDTO.getUseEndTime());
		couponItemDTO.setProductItems(couponDTO.getProductItems());
		couponItemDTO.setShopItems(couponDTO.getShopItems());
		return couponItemDTO;
	}

	default CouponItemDTO converterCouponItemDTO(CouponItemExtDTO couponItemExtDTO) {
		CouponItemDTO couponItemDTO = new CouponItemDTO();
		couponItemDTO.setCouponId(couponItemExtDTO.getId());
		couponItemDTO.setUserCouponId(couponItemExtDTO.getUserCouponId());
		couponItemDTO.setCouponName(couponItemExtDTO.getTitle());
		couponItemDTO.setRemark(couponItemExtDTO.getRemark());
		couponItemDTO.setShopProviderFlag(couponItemExtDTO.getShopProviderFlag());
		couponItemDTO.setAmount(couponItemExtDTO.getAmount());
		couponItemDTO.setUseType(couponItemExtDTO.getUseType());
		couponItemDTO.setMinPoint(couponItemExtDTO.getMinPoint());
		couponItemDTO.setUseStartTime(couponItemExtDTO.getUseStartTime());
		couponItemDTO.setUseEndTime(couponItemExtDTO.getUseEndTime());
		couponItemDTO.setProductItems(couponItemExtDTO.getProductItems());
		couponItemDTO.setShopItems(couponItemExtDTO.getShopItems());
		couponItemDTO.setUnAvailableReason(couponItemExtDTO.getUnAvailableReason());
		couponItemDTO.setSelectStatus(couponItemExtDTO.getSelectStatus());
		return couponItemDTO;
	}

	default CouponDTO converterCouponDTO(CouponItemDTO couponItemDTO) {
		CouponDTO couponDTO = new CouponDTO();
		couponDTO.setId(couponItemDTO.getCouponId());
		couponDTO.setTitle(couponItemDTO.getCouponName());
		couponDTO.setAmount(couponItemDTO.getAmount());
		couponDTO.setUseType(couponItemDTO.getUseType());
		couponDTO.setMinPoint(couponItemDTO.getMinPoint());
		couponDTO.setUseStartTime(couponItemDTO.getUseStartTime());
		couponDTO.setUseEndTime(couponItemDTO.getUseEndTime());
		couponDTO.setProductItems(couponItemDTO.getProductItems());
		couponDTO.setShopItems(couponItemDTO.getShopItems());
		return couponDTO;
	}

	default CouponItemExtDTO converterCouponItemExtDTO(CouponItemDTO couponItemDTO) {
		CouponItemExtDTO couponItemExtDTO = new CouponItemExtDTO();
		couponItemExtDTO.setId(couponItemDTO.getCouponId());
		couponItemExtDTO.setUserCouponId(couponItemDTO.getUserCouponId());
		couponItemExtDTO.setTitle(couponItemDTO.getCouponName());
		couponItemExtDTO.setAmount(couponItemDTO.getAmount());
		couponItemExtDTO.setUseType(couponItemDTO.getUseType());
		couponItemExtDTO.setMinPoint(couponItemDTO.getMinPoint());
		couponItemExtDTO.setUseStartTime(couponItemDTO.getUseStartTime());
		couponItemExtDTO.setUseEndTime(couponItemDTO.getUseEndTime());
		couponItemExtDTO.setProductItems(couponItemDTO.getProductItems());
		couponItemExtDTO.setShopItems(couponItemDTO.getShopItems());
		return couponItemExtDTO;
	}

	default List<CouponItemDTO> converterCouponItemDTO(List<CouponDTO> couponList) {
		return couponList.stream().map(this::converterCouponItemDTO).collect(Collectors.toList());
	}

	default List<CouponItemDTO> converterCouponItemFromExtDTO(List<CouponItemExtDTO> couponList) {
		return couponList.stream().map(this::converterCouponItemDTO).collect(Collectors.toList());
	}


	default List<CouponDTO> converterCouponDTO(List<CouponItemDTO> couponList) {
		return couponList.stream().map(this::converterCouponDTO).collect(Collectors.toList());
	}

	default List<CouponItemExtDTO> converterCouponFromExtDTO(List<CouponItemDTO> couponList) {
		return couponList.stream().map(this::converterCouponItemExtDTO).collect(Collectors.toList());
	}
}
