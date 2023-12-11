/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 后台消息实体.
 *
 * @author legendshop
 */
public enum AdminMessageEnum implements StringEnum {

	/**
	 * DEV待处理事项：
	 * 1. 等待审核的店铺
	 * 2. 等待审核的商品
	 * 3. 等待审核的品牌
	 * 4. 退货申请
	 * 5. 退款申请
	 * 6. 待处理的举报
	 * 7. 待确认的结算单
	 * 8. 待审核的结算单
	 * 9. 待审核的拍卖
	 * 10. 待审核的秒杀活动
	 * 11. 待审核的团购活动
	 * 12. 待审核的预售活动
	 * 13. 待审核的推广员
	 * 14. 待处理的反馈意见
	 * 15. 待审核的评论
	 * 16. 待回复的咨询
	 * 17. 待回复的客服消息
	 */

	AUDITUING_SHOP("待审核店铺", "/admin/shopDetail/query?status=-1", "131", "M_SHOPDETAIL"),

	AUDITUING_PROD("待审核商品", "/admin/product/query?status=3", "211", "M_ADMIN_PRODUCT_QUERY"),

	AUDITUING_BRAND("待审核品牌", "/admin/brand/query?status=-1", "213", "M_ADMIN_BRAND_QUERY"),

	RETURNGOODS("退货申请", "/admin/returnGoods/query/pending", "793", "M_RETUM"),

	RETURNMONEY("退款申请", "/admin/returnMoney/query/pending", "795", "M_REFUND"),

	ACCUSATION("待处理的举报", "/admin/accusation/query", "761", "M_ACCUSATION"),

	SHOP_BILL("待确认的结算单", "/admin/shopBill/query?status=1", "801", "M_SHOP_BILL"),

	SHOP_BILL_CONFIRM("待审核的结算单", "/admin/shopBill/query?status=2", "801", "SHOP_BILL_CONFIRM"),

	PRESELL_PROD("待审核的预售活动", "/admin/pre-sell/query?status=-1", "1028", "M_PRESELL_MANAGER"),

	USERCOMMIS("待审核的推广员", "/admin/userCommis/query", "1477", "M_PROMOTERS_LIST"),

	USER_FEEDBACK("待处理的反馈意见", "/admin/userFeedBack/query?status=0", "798", "M_FEELBACK"),

	PRODUCT_COMMENT("待审核的评论", "/admin/productcomment/query?status=0", "217", "M_PRODUCTCOMMENT"),

	PROD_CONSULT("待回复的咨询", "/admin/productConsult/query", "216", "M_PRODUCTCONSULT"),

	IM("待回复的客服消息", "/admin/customer/im", "13677", "M_CUSTOMER_IM"),
	;

	/**
	 * 显示值
	 */
	private final String value;

	/**
	 * 跳转地址
	 */
	private final String url;

	/**
	 * 菜单Id
	 **/
	private final String menuId;

	/***权限标签**/
	private final String label;

	/**
	 * Instantiates a new function enum
	 *
	 * @param value
	 * @param url
	 * @param menuId
	 * @param label
	 */
	private AdminMessageEnum(String value, String url, String menuId, String label) {
		this.value = value;
		this.url = url;
		this.menuId = menuId;
		this.label = label;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.StringEnum#value()
	 **/
	@Override
	public String value() {
		return this.value;
	}

	public String url() {
		return this.url;
	}

	public String menuId() {
		return this.menuId;
	}

	public String getLabel() {
		return label;
	}
}
