/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 系统配置表(SysConf)实体类
 *
 * @author legendshop
 */
@Schema(description = "系统配置DTO")
@Data
public class SystemConfigDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -4994293488089245947L;

	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	@Length(max = 50, message = "商城名称不能超过50个字")
	private String domainName;

	/**
	 * lgoo
	 */
	@Length(max = 255, message = "logo图片链接过长")
	@Schema(description = "logo")
	private String logo;

	/**
	 * lgoo
	 */
	@Length(max = 255, message = "用户PC端logo图片链接过长")
	@Schema(description = "用户PC端logo")
	private String pcUserLogo;

	/**
	 * 商家端大logo
	 */
	@Schema(description = "商家端大logo")
	@Length(max = 255, message = "商家端大logo图片链接过长")
	private String shopBigLogo;

	/**
	 * 商家端小logo
	 */
	@Schema(description = "商家端小logo")
	@Length(max = 255, message = "商家端小logo图片链接过长")
	private String shopSmallLogo;

	/**
	 * 平台端大logo
	 */
	@Schema(description = "平台端大logo")
	@Length(max = 255, message = "平台端大logo图片链接过长")
	private String adminBigLogo;

	/**
	 * 平台端小logo
	 */
	@Schema(description = "平台端小logo")
	@Length(max = 255, message = "平台端小logo图片链接过长")
	private String adminSmallLogo;

	/**
	 * icp信息
	 */
	@Schema(description = "icp信息")
	@Length(max = 50, message = "icp信息不能超过50个字")
	private String icpInfo;


	/**
	 * 微信订阅号图片地址
	 */
	@Schema(description = "微信订阅号图片地址")
	private String wechatSubscriptionPic;

	/**
	 * 微信的服务号图片地址
	 */
	@Schema(description = "微信的服务号图片地址")
	private String wechatServicePic;

	/**
	 * SEO标题
	 */
	@Schema(description = "SEO标题")
	@Length(max = 50, message = "SEO标题不能超过50个字")
	private String seoTitle;

	/**
	 * SEO关键字
	 */
	@Schema(description = "SEO关键字")
	@Length(max = 500, message = "SEO关键字不能超过500个字")
	private String seoKeywords;

	/**
	 * SEO描述
	 */
	@Schema(description = "SEO描述")
	@Length(max = 500, message = "SEO描述不能超过500个字")
	private String seoDescription;

	/**
	 * 国家代码
	 */
	@Schema(description = "国家代码")
	private String internationalCode;

	/**
	 * 地区代码(区号)
	 */
	@Schema(description = "地区代码(区号)")
	private String areaCode;

	/**
	 * 电话号码
	 */
	@Schema(description = "电话号码")
	private String telephone;

	@Schema(description = "公司名称")
	@Length(max = 50, message = "公司名称不能超过50个字")
	private String companyName;

	/**
	 * 平台客服信息
	 */
	@Schema(description = "平台客服信息")
	private AdminCustomerInformationDTO shopContactDTO;
}
