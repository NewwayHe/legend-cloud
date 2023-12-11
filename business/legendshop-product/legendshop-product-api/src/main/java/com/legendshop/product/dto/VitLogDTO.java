/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.enums.VitLogSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户浏览历史(VitLog)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "浏览历史")
public class VitLogDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 194550812846456795L;

	/**
	 * 主键
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	private String shopName;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;


	/**
	 * 产品名称
	 */
	@Schema(description = "产品名称")
	private String productName;


	/**
	 * IP
	 */
	@Schema(description = "IP")
	private String ip;


	/**
	 * 获得IP所在国家，如果在中国，直接显示省市
	 */
	@Schema(description = "获得IP所在国家")
	private String country;


	/**
	 * 获得IP所在区域
	 */
	@Schema(description = "获得IP所在区域")
	private String area;


	/**
	 * 产品图片
	 */
	@Schema(description = "产品图片")
	private String pic;


	/**
	 * 产品售价
	 */
	@Schema(description = "产品售价")
	private String price;


	/**
	 * 访问页面
	 * {@link VitLogPageEnum}
	 */
	@Schema(description = "访问的页面类型, 0: 商品页, 1: 店铺页")
	private Integer page;

	/**
	 * 来源
	 * {@link VitLogSourceEnum}
	 */
	@Schema(description = "参数为pc,mobile")
	private String source;

	/**
	 * 访问次数
	 */
	@Schema(description = "访问次数")
	private Integer visitNum;


	/**
	 * 用户是否已删除
	 */
	@Schema(description = "用户是否已删除")
	private Boolean userDelFlag;

	@Schema(description = "销量")
	private Long buys;

	@Schema(description = "库存")
	private Long stocks;

}
