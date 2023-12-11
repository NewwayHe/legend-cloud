/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户浏览历史(VitLog)实体类
 *
 * @author legendshop
 */
@Data
public class VitLogBO implements Serializable {


	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 用户ID
	 */
	private Long userId;


	/**
	 * 商城名称
	 */
	private String shopName;


	/**
	 * 商品ID
	 */
	private Long productId;


	/**
	 * 产品名称
	 */
	private String productName;


	/**
	 * IP
	 */
	private String ip;


	/**
	 * 获得IP所在国家，如果在中国，直接显示省市
	 */
	private String country;


	/**
	 * 获得IP所在区域
	 */
	private String area;


	/**
	 * 产品图片
	 */
	private String pic;


	/**
	 * 产品售价
	 */
	private String price;


	/**
	 * 访问页面
	 */
	private String page;


	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建时间
	 */
	private Date updateTime;

	/**
	 * 访问次数
	 */
	private Integer visitNum;


	/**
	 * 参数为PC,Mobile,App
	 */
	private String source;


	/**
	 * 用户是否已删除
	 */
	private Boolean userDelFlag;

}
