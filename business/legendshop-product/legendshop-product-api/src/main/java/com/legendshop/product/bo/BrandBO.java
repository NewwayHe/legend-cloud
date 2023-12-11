/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "品牌BO")
public class BrandBO implements Serializable {


	private static final long serialVersionUID = -7793018148831317389L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 品牌-类目关联表 关联id
	 */
	@Schema(description = "品牌-类目关联表 关联id")
	private Long aggBrandId;

	/**
	 *	类目关联管理id
	 */
	@Schema(description = "类目关联管理id")
	private Long aggId;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 该品牌的商品数量
	 */
	@Schema(description = "该品牌的商品数量")
	private Long brandCount;


	/**
	 * 品牌logo
	 */
	@Schema(description = "品牌logo")
	private String brandPic;

	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;

	/**
	 * 品牌状态：{@link com.legendshop.product.enums.BrandStatusEnum}
	 */
	@Schema(description = "品牌状态: 1: 上线， 0： 下线")
	private Integer status;


	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过")
	private Integer opStatus;

	@Schema(description = "")
	private String statusStr;


	/**
	 * 是否推荐 1:是 0：否
	 */
	@Schema(description = "是否推荐")
	private Boolean commendFlag;


	/**
	 * 简要描述
	 */
	@Schema(description = "简要描述")
	private String brief;

	/**
	 * 商家名称
	 */
	@Schema(description = "商家名称")
	private String siteName;

	/**
	 * 品牌大图（这张图片的尺寸是:770*350）
	 */
	@Schema(description = "品牌大图（这张图片的尺寸是:770*350）")
	private String bigImage;

	/**
	 *	创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 *	更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Schema(description = "结束时间")
	private Date endTime;


	@Schema(description = "开始时间")
	private Date startTime;

	@Schema(description = "注册人")
	private String registrationPeople;

	@Schema(description = "注册地址")
	private String registrationAddess;

	@Schema(description = "注册证图片")
	private String registrationPic;

	@Schema(description = "注册号")
	private String trademarkingNumber;
}
