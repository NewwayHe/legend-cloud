/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class PreSellProductQuery extends PageParams {

	private static final long serialVersionUID = -7473355653479553101L;

	boolean isExpires;

	/**
	 * 状态,-2:未提审,-1:已提审,0:审核通过(上线),1:拒绝,2:已完成,-3:已过期
	 */
	private Integer status;

	/**
	 * 商家ID
	 */
	private Long shopId;

	/**
	 * 商家名称
	 */
	private String shopName;

	/**
	 * 商品名称
	 */
	private String prodName;

	/**
	 * 方案名称
	 */
	private String schemeName;

	/**
	 * 商品编码
	 */
	private String prodCode;

	/**
	 * 预售结束时间
	 */
	private Date preSaleEnd;

	/**
	 * 预售开始时间
	 */
	private Date preSaleStart;

	/**
	 * 搜索类型 （tab搜索用）
	 */
	private String searchType;

	/**
	 * 排序
	 */
	private String orders;

	/**
	 * 标签
	 */
	private String tag;
}
