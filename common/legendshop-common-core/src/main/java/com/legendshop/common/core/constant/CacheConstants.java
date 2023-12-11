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
 * 缓存常量集合
 *
 * @author legendshop
 */
public interface CacheConstants {

	/**
	 * oauth 客户端信息
	 */
	String CLIENT_DETAILS_KEY = "legendshop_oauth:client:details";

	/**
	 * 管理员用户信息缓存
	 */
	String ADMIN_USER_DETAILS = "admin_user_details";


	/**
	 * 商家用户信息缓存
	 */
	String SHOP_USER_DETAILS = "shop_user_details";

	/**
	 * 商家用户证件信息缓存
	 */
	String SHOP_USER_DOCUMENTS_DETAILS = "shop_user_documents_details";

	/**
	 * 商家信息缓存
	 */
	String SHOP_DETAIL = "shop_detail";

	/**
	 * 商家详细信息缓存
	 */
	String SHOP_DETAIL_VIEW = "shop_detail_view";

	/**
	 * 店铺列表信息缓存
	 */
	String SHOP_DETAIL_PAGE = "shop_detail_page";

	/**
	 * 列表缓存 第1页 每页10条
	 */
	String CUR_1_PAGE_10 = "cur_page";

	/**
	 * 普通用户信息缓存
	 */
	String ORDINARY_USER_DETAILS = "ordinary_user_details";


	/**
	 * 商家子账号信息缓存
	 */
	String SHOP_SUB_USER_DETAILS = "shop_sub_user_details";


	/**
	 * 公告缓存
	 */
	String PUB_DETAILS = "pub_details";

	/**
	 * 后台菜单缓存
	 */
	String ADMIN_MENU_DETAILS = "admin_menu_details";

	/**
	 * 商家菜单缓存
	 */
	String SHOP_MENU_DETAILS = "shop_menu_details";

	/**
	 * 用户菜单缓存
	 */
	String USER_MENU_DETAILS = "user_menu_details";

	/**
	 * 类目缓存
	 */
	String CATEGORY_DETAILS = "category_details";

	/**
	 * 店铺类目缓存
	 */
	String SHOP_CATEGORY_DETAILS = "shop_category_details";
	/**
	 * 品牌缓存
	 */
	String BRAND_DETAILS = "brand_details";

	/**
	 * 手机验证码登录前缀
	 */
	String MOBILE_CODE_LOGIN_KEY = "MOBILE_CODE_LOGIN_KEY:";

	/**
	 * 手机验证码过期时间60秒
	 */
	Integer CODE_TIME = 60;

	/**
	 * 系统参数缓存
	 */
	String SYSTEM_PARAMS_DETAILS = "system_params:details";

	/**
	 * 系统参数缓存
	 */
	String SYSTEM_PARAMS_DETAILS_ITEM = "system_params:details_item";

	/**
	 * 举报类型缓存
	 */
	String ACCUSATIONTYPE = "accusation_type";

	/**
	 * 购物车缓存
	 */
	String SHOPPING_CART_DETAILS = "shopping_cart_details";


	/**
	 * 登录用户购物车项缓存
	 */
	String CART_ITEMS = "cart_items";

	/**
	 * 未登录用户购物车项缓存
	 */
	String CART_ITEMS_TMP = "cart_items_tmp";


	/**
	 * 用户购物车选择的促销活动缓存
	 */
	String CART_PROMOTION_ITEMS = "cart_promotion_items";

	/**
	 * 商品缓存
	 */
	String PRODUCT_DETAILS = "product_details:";
	/**
	 * 商品BO缓存
	 */
	String PRODUCT_BO = "productBO";

	/**
	 * 商品DTO缓存
	 */
	String PRODUCT_DTO = "productDTO";

	/**
	 * 评价分数统计
	 */
	String COMMENT_SCORE = "comment_score:";

	/**
	 * 商品评价分数统计
	 */
	String PRODUCT_STATISTICS = "product_statistics";

	/**
	 * 店铺评价分数统计
	 */
	String SHOP_STATISTICS = "shop_statistics";

	/**
	 * 物流评价分数统计
	 */
	String LOGISTICS_STATISTICS = "logistics_statistics";

	/**
	 * 用户收货地址缓存
	 */
	String USER_ADDRESS = "user_address";


	/**
	 * 下单用户收货地址缓存
	 */
	String USER_ADDRESS_FOR_ORDER = "user_address_for_order";


	/**
	 * 优惠券缓存
	 */
	String COUPON_DETAILS = "coupon_details";


	/**
	 * 最佳优惠券缓存
	 */
	String BAST_COUPON_DETAILS = "best_coupon_details";

	/**
	 * 商城主题颜色缓存
	 */
	String THEME_COLOR = "theme_color";

	/**
	 * 路由存放
	 */
	String ROUTE_KEY = "gateway_route_key";

	/**
	 * redis reload 事件
	 */
	String ROUTE_REDIS_RELOAD_TOPIC = "gateway_redis_route_reload_topic";

	/**
	 * 内存reload 时间
	 */
	String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";


	String WECHAT_CONFIG_RELOAD_TOPIC = "wechat_config_reload_topic";


	String WECHAT_CONFIG_KEY = "wechat_config_key";

	/**
	 * 积分设置规则
	 */
	String INTEGRAL_SETTING_DETAILS = "integral_setting_details";

	/**
	 * 用户积分, 后面跟userId
	 */
	String USER_INTEGRAL = "user_integral:";


	// ------------- 分销 -------------
	/**
	 * 用户身份
	 */
	String USER_IDENTITY = "user_identity:";

	// ------------- 分账 -------------

	/**
	 * 进件
	 */
	String SHOP_INCOMING = "shop_incoming:";

	/**
	 * 进件
	 */
	String SHOP_INCOMING_MERCHANT_NO = "shop_incoming:merchant_no:";

	/**
	 * 微信小程序access_token
	 */
	String WXMINI_ACCESS_TOKEN = "wxmini_access_token";

	String INDEX_INIT = "es:index:init";
}
