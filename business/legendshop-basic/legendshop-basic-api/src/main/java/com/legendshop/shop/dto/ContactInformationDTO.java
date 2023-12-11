/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家微信联系方式存储表(ShopCustomerInformation)DTO
 *
 * @author legendshop
 * @since 2021-12-27 11:34:38
 */
@Data
@Schema(description = "商家微信联系方式存储表DTO")
public class ContactInformationDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 613298224272176331L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 商家name
	 */
	@Schema(description = "商家name")
	private String shopName;

	/**
	 * 微信二维码
	 */
	@NotNull(message = "微信二维码不能为空！")
	@Schema(description = "微信二维码")
	private String wvCode;

	/**
	 * 微信号
	 */
	@NotNull(message = "微信号不能为空！")
	@Schema(description = "微信号")
	private String wxNumber;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private Date updateTime;

	/**
	 * 客服电话
	 */
	@Schema(description = "客服电话")
	private String contactPhone;

	/**
	 * 客服是否设置（0关闭，1开启）
	 */
	@Schema(description = "客服是否设置（0关闭，1开启）")
	private Boolean openFlag;

	/**
	 * 是否是平台客服（0否，1是）
	 */
	@Schema(description = "是否是平台客服（0否，1是）")
	private Boolean adminFlag;

}
