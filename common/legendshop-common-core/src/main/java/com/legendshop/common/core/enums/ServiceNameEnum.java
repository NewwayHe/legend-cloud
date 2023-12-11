/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

import com.legendshop.common.core.constant.ServiceNameConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务名常量
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ServiceNameEnum {

	AUTH_SERVICE(ServiceNameConstants.AUTH_SERVICE, "认证服务", "auth"),

	BASIC_SERVICE(ServiceNameConstants.BASIC_SERVICE, "基础服务", "basic"),

	USER_SERVICE(ServiceNameConstants.USER_SERVICE, "用户服务", "user"),

	SHOP_SERVICE(ServiceNameConstants.SHOP_SERVICE, "商城服务", "shop"),

	PRODUCT_SERVICE(ServiceNameConstants.PRODUCT_SERVICE, "商品服务", "product"),

	SEARCH_SERVICE(ServiceNameConstants.SEARCH_SERVICE, "搜索服务", "search"),

	ORDER_SERVICE(ServiceNameConstants.ORDER_SERVICE, "订单服务", "order"),

	ACTIVITY_SERVICE(ServiceNameConstants.ACTIVITY_SERVICE, "活动服务", "activity"),

	PAY_SERVICE(ServiceNameConstants.PAY_SERVICE, "支付服务", "pay"),

	DATA_SERVICE(ServiceNameConstants.DATA_SERVICE, "数据服务", "data"),

	ID_SERVICE(ServiceNameConstants.ID_SERVICE, "Id服务", "id"),

	UNKNOWN_SERVICE("", "未知服务", ""),

	;

	/**
	 * 英文名称
	 */
	private final String en;

	/**
	 * 中文名称
	 */
	private final String cn;

	/**
	 * 路径
	 */
	private final String url;

	public static String getCn(String en) {
		ServiceNameEnum[] licenseEnums = values();
		for (ServiceNameEnum licenseEnum : licenseEnums) {
			if (licenseEnum.getEn().equals(en)) {
				return licenseEnum.getCn();
			}
		}
		return null;
	}

	public static String getCnByRequestUrl(String url) {
		ServiceNameEnum[] licenseEnums = values();
		for (ServiceNameEnum licenseEnum : licenseEnums) {
			if (licenseEnum.getUrl().equals(url)) {
				return licenseEnum.getCn();
			}
		}
		return UNKNOWN_SERVICE.cn;
	}
}
