/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 商家进件表(ShopIncoming)实体类
 *
 * @author legendshop
 * @since 2021-03-12 09:27:58
 */
@Data
@Entity
@Table(name = "ls_shop_incoming")
public class ShopIncoming extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -65398185327838042L;

	/**
	 * 主键ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "shopIncoming_SEQ")
	private Long id;

	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 入网请求号 ps:这个参数是后面加的，前面进件成功的数据是没有这个的
	 */
	@Column(name = "request_no")
	private String requestNo;

	/**
	 * 易宝商户编号
	 */
	@Column(name = "merchant_no")
	private String merchantNo;

	/**
	 * 商户签约类型 INDIVIDUAL(个体工商户)：一般为个体户、个体工商户、个体经营 ENTERPRISE(企业)：一般为有限公司、有限责任公司。
	 */
	@Column(name = "sign_type")
	private String signType;

	/**
	 * 商户证件编号 统一社会信用代码证编号、事业单位法人证书编号、社会团体证书编号等，与商户签约类型匹配。
	 */
	@Column(name = "licence_no")
	private String licenceNo;

	/**
	 * 商户证件照片 上传图片前需调用文件上传接口将文件上传至易宝服务器。（请提供证件副本）
	 */
	@Column(name = "licence_url")
	private String licenceUrl;

	/**
	 * 商户签约名称 与商户证件主体名称一致。
	 */
	@Column(name = "sign_name")
	private String signName;

	/**
	 * 商户简称
	 */
	@Column(name = "short_name")
	private String shortName;

	/**
	 * 开户许可证编号
	 */
	@Column(name = "open_account_licence_no")
	private String openAccountLicenceNo;

	/**
	 * 开户许可证照片
	 */
	@Column(name = "open_account_licence_url")
	private String openAccountLicenceUrl;

	/**
	 * 手持营业执照在经营场所的照片
	 */
	@Column(name = "hand_licence_url")
	private String handLicenceUrl;

	/**
	 * 法人姓名
	 */
	@Column(name = "legal_name")
	private String legalName;

	/**
	 * 法人证件类型
	 * ID_CARD(法人身份证)
	 * PASSPORT(护照)
	 * HMT_VISITORPASS(港澳台居民往来内地通行证)
	 * SOLDIER(士兵证)
	 * OFFICERS(军官证)
	 * OVERSEAS_CARD(境外证件)
	 */
	@Column(name = "legal_licence_type")
	private String legalLicenceType;

	/**
	 * 法人证件号码
	 */
	@Column(name = "legal_licence_no")
	private String legalLicenceNo;

	/**
	 * 法人证件正面照片
	 */
	@Column(name = "legal_licence_front_url")
	private String legalLicenceFrontUrl;

	/**
	 * 法人证件反面照片
	 */
	@Column(name = "legal_licence_back_url")
	private String legalLicenceBackUrl;

	/**
	 * 商户联系人姓名
	 */
	@Column(name = "contact_name")
	private String contactName;

	/**
	 * 商户联系人证件号码
	 */
	@Column(name = "contact_licence_no")
	private String contactLicenceNo;

	/**
	 * 商户联系人手机号
	 */
	@Column(name = "contact_mobile")
	private String contactMobile;

	/**
	 * 商户联系人邮箱
	 */
	@Column(name = "contact_email")
	private String contactEmail;

	/**
	 * 商户实际经营地所在省
	 */
	@Column(name = "province")
	private String province;

	/**
	 * 商户实际经营地所在市
	 */
	@Column(name = "city")
	private String city;

	/**
	 * 商户实际经营地所在区
	 */
	@Column(name = "district")
	private String district;

	/**
	 * 商户实际经营详细地址
	 */
	@Column(name = "address")
	private String address;


	/**
	 * 商户实际经营详细地址
	 */
	@Column(name = "application_no")
	private String applicationNo;

	/**
	 * 合同地址
	 */
	@Column(name = "contract_url")
	private String contractUrl;

	/**
	 * 申请状态 NOT_APPLIED:未申请 REVIEWING:申请审核中 REVIEW_BACK:申请已驳回 AGREEMENT_SIGNING:协议待签署 BUSINESS_OPENING:业务开通中 COMPLETED:申请已完成
	 */
	@Column(name = "status")
	private String status;

	/**
	 * 审核意见: “申请已驳回”或者“申请已完成”时，回传的审核意见
	 */
	@Column(name = "audit_opinion")
	private String auditOpinion;

	/**
	 * 结算方向
	 * 如果需要开通结算产品，该字段必传；如不需开通结算产品，该结算账户信息都可不传。
	 * ACCOUNT(结算到支付账户)
	 * BANKCARD(结算到银行账户，如结算到结算账户时对公账户/单位结算卡账户名称系统默认处理为商户签约名称；借记卡/存折账户名称系统默认处理为商户经营者/法人姓名。）
	 */
	@Column(name = "settlement_direction")
	private String settlementDirection;

	/**
	 * 银行账户类型
	 * 企业：对公账户/单位结算卡
	 * 个体户：对公账户/借记卡/存折
	 * <p>
	 * UNIT_SETTLEMENT_CARD(单位结算卡)
	 * ENTERPRISE_ACCOUNT(对公账户)
	 * DEBIT_CARD(借记卡)
	 * PASSBOOK(存折)
	 */
	@Column(name = "bank_account_type")
	private String bankAccountType;


	/**
	 * 银行账户号码
	 */
	@Column(name = "bank_card_no")
	private String bankCardNo;


	/**
	 * 银行账户开户总行编码
	 */
	@Column(name = "bank_code")
	private String bankCode;


	/**
	 * 微信配置，判断是否已经调用过微信配置
	 */
	@Column(name = "wechat_config")
	private Boolean wechatConfig;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
