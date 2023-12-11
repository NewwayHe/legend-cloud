/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.constant;

/**
 * 服务名常量
 *
 * @author legendshop
 */
public interface ServiceNameConstants {

	/**
	 * 认证中心
	 */
	String AUTH_SERVICE = "legendshop-auth";


	/**
	 * 基础模块
	 */
	String BASIC_SERVICE = "legendshop-basic-service";

	/**
	 * 用户模块
	 */
	String USER_SERVICE = "legendshop-user-service";

	/**
	 * 商城模块
	 */
	String SHOP_SERVICE = "legendshop-shop-service";

	/**
	 * 商品模块
	 */
	String PRODUCT_SERVICE = "legendshop-product-service";

	/**
	 * 搜索模块
	 */
	String SEARCH_SERVICE = "legendshop-search-service";

	/**
	 * 营销活动模块
	 */
	String ACTIVITY_SERVICE = "legendshop-activity-service";

	/**
	 * 订单模块
	 */
	String ORDER_SERVICE = "legendshop-order-service";

	/**
	 * 支付模块
	 */
	String PAY_SERVICE = "legendshop-pay-service";

	/**
	 * 数据模块
	 */
	String DATA_SERVICE = "legendshop-data-service";

	/**
	 * ID模块
	 */
	String ID_SERVICE = "legendshop-id-service";


	/**
	 * 用户服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String USER_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/user";

	/**
	 * 活动服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String ACTIVITY_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/activity";

	/**
	 * 订单服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String ORDER_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/order";

	/**
	 * 基础服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String BASIC_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/basic";

	/**
	 * 商品服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String PRODUCT_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/product";

	/**
	 * ID服务
	 * RPC调用前缀，前缀主要用于忽略内部调用鉴权，外部调用使用gateway进行拦截访问
	 */
	String ID_SERVICE_RPC_PREFIX = RpcConstants.RPC_API_PREFIX + "/id";
}
