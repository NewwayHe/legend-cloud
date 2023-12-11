/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户详细信息表(ShopDetail)实体类
 *
 * @author legendshop
 * @since 2020-09-02 10:27:27
 */
@Data
@Entity
@Table(name = "ls_shop_detail")
public class ShopDetail extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 302815575468464416L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "shopDetail_SEQ")
	private Long id;

	/**
	 * 商家用户ID
	 */
	@Column(name = "shop_user_id")
	private Long shopUserId;

	/**
	 * 店铺名称
	 */
	@Column(name = "shop_name")
	private String shopName;
	/**
	 * 营业执照
	 */
	@Column(name = "business_license")
	private String businessLicense;
	/**
	 * 店铺头像
	 */
	@Column(name = "shop_avatar")
	private String shopAvatar;

	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Column(name = "shop_type")
	private Integer shopType;


	/**
	 * 行业目录
	 */
	@Column(name = "industry_directory_id")
	private Long industryDirectoryId;

	/**
	 * 联系人姓名
	 */
	@Column(name = "contact_name")
	private String contactName;

	/**
	 * 联系手机
	 */
	@Column(name = "contact_phone")
	private String contactPhone;

	/**
	 * 法人姓名
	 */
	@Column(name = "corporate_name")
	private String corporateName;

	/**
	 * 身份证号码
	 */
	@Column(name = "id_card_number")
	private String idCardNumber;

	/**
	 * 身份证验证图片
	 */
	@Column(name = "id_card_pic")
	private String idCardPic;

	/**
	 * 身份证反面图片
	 */
	@Column(name = "id_card_back_pic")
	private String idCardBackPic;

	/**
	 * 主体申请类型，0，个人。  1，商家
	 */
	@Column(name = "apply_for_type")
	private Integer applyForType;

	/**
	 * 省份id
	 */
	@Column(name = "province_id")
	private Long provinceId;

	/**
	 * 城市id
	 */
	@Column(name = "city_id")
	private Long cityId;

	/**
	 * 地级市id
	 */
	@Column(name = "area_id")
	private Long areaId;

	/**
	 * 地级市id
	 */
	@Column(name = "street_id")
	private Long streetId;

	/**
	 * 店铺地址
	 */
	@Column(name = "shop_address")
	private String shopAddress;

	/**
	 * 店铺简述
	 */
	@Column(name = "shop_brief")
	private String shopBrief;

	/**
	 * 状态,是否上线1：在线，0下线，-1审核中,-2拒绝,-3关闭
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 店铺入驻流程操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Column(name = "op_status")
	private Integer opStatus;

	/**
	 * 访问人数
	 */
	@Column(name = "visit_times")
	private Integer visitTimes;

	/**
	 * 产品数量
	 */
	@Column(name = "product_num")
	private Integer productNum;

	/**
	 * 评论个数
	 */
	@Column(name = "comm_num")
	private Integer commNum;


	/**
	 * 信誉度，根据用户评论算出
	 */
	@Column(name = "credit")
	private Integer credit;

	/**
	 * 商品是否需要审核 1:是 0:否 为空则采用平台总设置
	 */
	@Column(name = "prod_require_audit")
	private Integer prodRequireAudit;


	/**
	 * 是否开启发票
	 */
	@Column(name = "invoice_flag")
	private Boolean invoiceFlag;

	/**
	 * 允许开具发票类型
	 */
	@Column(name = "invoice_type")
	private String invoiceType;

	/**
	 * 佣金结算比例
	 */
	@Column(name = "commission_rate")
	private Double commissionRate;

	/**
	 * 公司名字
	 */
	@Column(name = "company_name")
	private String companyName;

	/**
	 * 统一社会信用代码
	 */
	@Column(name = "unified_social_credit_code")
	private String unifiedSocialCreditCode;

	/**
	 * 二维码
	 */
	@Column(name = "qr_code")
	private String qrCode;

	/**
	 * 链接
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 退货收货人
	 */
	@Column(name = "return_consignee")
	private String returnConsignee;

	/**
	 * 退货收货人电话号码
	 */
	@Column(name = "return_consignee_phone")
	private String returnConsigneePhone;


	/**
	 * 退货省份Id.
	 */
	@Column(name = "return_province_id")
	protected Long returnProvinceId;

	/**
	 * 退货城市Id.
	 */
	@Column(name = "return_city_id")
	protected Long returnCityId;

	/**
	 * 退货地区Id.
	 */
	@Column(name = "return_area_id")
	protected Long returnAreaId;


	/**
	 * 退货街道Id.
	 */
	@Column(name = "return_street_id")
	protected Long returnStreetId;

	/**
	 * 退货详细地址.
	 */
	@Column(name = "return_shop_addr")
	protected String returnShopAddr;

	/**
	 * 是否申请过积分计划 0否 1是
	 */
	@Column(name = "integral_flag")
	private Integer integralFlag;

	/**
	 * 积分计划状态
	 */
	@Column(name = "integral_status")
	private Integer integralStatus;

	/**
	 * 积分计划审核状态
	 */
	@Column(name = "integral_op_status")
	private Integer integralOpStatus;

	/**
	 * 申请计划状态
	 */
	@Column(name = "apply_integral_status")
	private Integer applyIntegralStatus;

	/**
	 * 申请计划内容
	 */
	@Column(name = "apply_integral_content")
	private String applyIntegralContent;

	/**
	 * 退出计划内容
	 */
	@Column(name = "exit_integral_content")
	private String exitIntegralContent;

	/**
	 * 店铺完整地址
	 */
	@Column(name = "shop_complete_address")
	private String shopCompleteAddress;

	/**
	 * 已经销售数量
	 */
	@Column(name = "buys")
	private Integer buys;

	/**
	 * 默认分销比例
	 */
	@Column(name = "default_dis_scale")
	private BigDecimal defaultDisScale;

	@Column(name = "shop_new_bie_status")
	private Integer shopNewBieStatus;

	@Column(name = "business_scope")
	private String businessScope;

	@Column(name = "business_start_time")
	private Date businessStartTime;

	@Column(name = "business_end_time")
	private Date businessEndTime;

	/**
	 * 注册资金
	 */
	@Column(name = "registered_capital")
	private BigDecimal registeredCapital;
}
