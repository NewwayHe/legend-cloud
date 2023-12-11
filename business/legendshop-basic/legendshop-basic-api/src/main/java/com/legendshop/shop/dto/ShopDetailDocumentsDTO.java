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
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺证件详细信息表实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家用户证件详细信息DTO")
public class ShopDetailDocumentsDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -6542517947203918449L;

	/**
	 * 公司名字
	 */
	@Schema(description = "公司名字")
	private String companyName;

	/**
	 * 法人姓名
	 */
	@Schema(description = "法人姓名")
	private String corporateName;

	/**
	 * 营业执照
	 */
	@Schema(description = "营业执照")
	private String businessLicense;

	/**
	 * 经营范围
	 */
	@Schema(description = "经营范围")
	private String businessScope;

	/**
	 * 营业执照开始时间
	 */
	@Schema(description = "营业执照开始时间")
	private Date businessStartTime;

	/**
	 * 营业执照结束时间
	 */
	@Schema(description = "营业执照结束时间")
	private Date businessEndTime;

	/**
	 * 统一社会信用代码
	 */
	@Schema(description = "统一社会信用代码")
	private String unifiedSocialCreditCode;

	/**
	 * 注册资金
	 */
	@Schema(description = "注册资金")
	private BigDecimal registeredCapital;

	@Schema(description = "店铺完整地址")
	private String shopCompleteAddress;

	/**
	 * 营业执照有效期
	 */
	@Schema(description = "营业执照有效期")
	private String businessLicenseTime;
}
