/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;

import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 系统配置表(SysConf)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_system_config")
public class SystemConfig extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -93907155633829641L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SYS_CONF_SEQ")
	private Long id;

	/**
	 * 商城名称
	 */
	@Column(name = "domain_name")
	private String domainName;

	/**
	 * logo
	 */
	@Column(name = "logo")
	private String logo;

	/**
	 * lgoo
	 */
	@Column(name = "pc_user_logo")
	private String pcUserLogo;

	/**
	 * 商家端大logo
	 */
	@Column(name = "shop_big_logo")
	private String shopBigLogo;

	/**
	 * 商家端小logo
	 */
	@Column(name = "shop_small_logo")
	private String shopSmallLogo;

	/**
	 * 平台端大logo
	 */
	@Column(name = "admin_big_logo")
	private String adminBigLogo;

	/**
	 * 平台端小logo
	 */
	@Column(name = "admin_small_logo")
	private String adminSmallLogo;

	/**
	 * icp信息
	 */
	@Column(name = "icp_info")
	private String icpInfo;


	/**
	 * 微信订阅号图片地址
	 */
	@Column(name = "wechat_subscription_pic")
	private String wechatSubscriptionPic;

	/**
	 * 微信的服务号图片地址
	 */
	@Column(name = "wechat_service_pic")
	private String wechatServicePic;

	/**
	 * SEO标题
	 */
	@Column(name = "seo_title")
	private String seoTitle;

	/**
	 * SEO关键字
	 */
	@Column(name = "seo_keywords")
	private String seoKeywords;

	/**
	 * SEO描述
	 */
	@Column(name = "seo_description")
	private String seoDescription;


	/**
	 * 国家代码
	 */
	@Column(name = "international_code")
	private String internationalCode;

	/**
	 * 地区代码(区号)
	 */
	@Column(name = "area_code")
	private String areaCode;

	/**
	 * 电话号码
	 */
	@Column(name = "telephone")
	private String telephone;

	/**
	 * 公司名称
	 */
	@Column(name = "company_name")
	private String companyName;
}
