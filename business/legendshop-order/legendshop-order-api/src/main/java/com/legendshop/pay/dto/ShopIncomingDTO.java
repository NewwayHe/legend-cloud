/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家进件表(ShopIncoming)DTO
 *
 * @author legendshop
 * @since 2021-03-12 09:28:00
 */
@Data
@Schema(description = "商家进件表DTO")
public class ShopIncomingDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -91817242066622280L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 入网请求号
	 */
	@Schema(description = "入网请求号")
	private String requestNo;

	/**
	 * 易宝商户编号
	 */
	@Schema(description = "易宝商户编号")
	private String merchantNo;


	/**
	 * 商户签约类型 INDIVIDUAL(个体工商户)：一般为个体户、个体工商户、个体经营 ENTERPRISE(企业)：一般为有限公司、有限责任公司。
	 */
	@NotNull(message = "商户签约类型不能为空")
	@Schema(description = "商户签约类型 INDIVIDUAL(个体工商户)：一般为个体户、个体工商户、个体经营 ENTERPRISE(企业)：一般为有限公司、有限责任公司。")
	private String signType;

	/**
	 * 商户证件编号 统一社会信用代码证编号、事业单位法人证书编号、社会团体证书编号等，与商户签约类型匹配。
	 */
	@NotNull(message = "商户证件编号不能为空")
	@Schema(description = "商户证件编号 统一社会信用代码证编号、事业单位法人证书编号、社会团体证书编号等，与商户签约类型匹配。")
	private String licenceNo;

	/**
	 * 商户证件照片 上传图片前需调用文件上传接口将文件上传至易宝服务器。（请提供证件副本）
	 */
	@NotNull(message = "商户证件照片不能为空")
	@Schema(description = "商户证件照片 上传图片前需调用文件上传接口将文件上传至易宝服务器。（请提供证件副本）")
	private String licenceUrl;

	/**
	 * 商户签约名称 与商户证件主体名称一致。
	 */
	@NotNull(message = "商户签约名称不能为空")
	@Schema(description = "商户签约名称 与商户证件主体名称一致。")
	private String signName;

	/**
	 * 商户简称
	 */
	@NotNull(message = "商户简称不能为空")
	@Schema(description = "商户简称")
	private String shortName;

	/**
	 * 开户许可证编号
	 */
	@NotNull(message = "开户许可证编号不能为空")
	@Schema(description = "开户许可证编号")
	private String openAccountLicenceNo;

	/**
	 * 开户许可证照片
	 */
	@NotNull(message = "开户许可证照片不能为空")
	@Schema(description = "开户许可证照片")
	private String openAccountLicenceUrl;

	/**
	 * 手持营业执照在经营场所的照片
	 */
	@NotNull(message = "手持营业执照在经营场所的照片不能为空")
	@Schema(description = "手持营业执照在经营场所的照片")
	private String handLicenceUrl;

	/**
	 * 法人姓名
	 */
	@NotNull(message = "法人姓名不能为空")
	@Schema(description = "法人姓名")
	private String legalName;

	/**
	 * 法人证件类型
	 */
	@NotNull(message = "法人证件类型不能为空")
	@Schema(description = "法人证件类型")
	private String legalLicenceType;

	/**
	 * 法人证件号码
	 */
	@NotNull(message = "法人证件号码不能为空")
	@Schema(description = "法人证件号码")
	private String legalLicenceNo;

	/**
	 * 法人证件正面照片
	 */
	@NotNull(message = "法人证件正面照片不能为空")
	@Schema(description = "法人证件正面照片")
	private String legalLicenceFrontUrl;

	/**
	 * 法人证件反面照片
	 */
	@NotNull(message = "法人证件反面照片不能为空")
	@Schema(description = "法人证件反面照片")
	private String legalLicenceBackUrl;

	/**
	 * 商户联系人姓名
	 */
	@NotNull(message = "商户联系人姓名不能为空")
	@Schema(description = "商户联系人姓名")
	private String contactName;

	/**
	 * 商户联系人证件号码
	 */
	@NotNull(message = "商户联系人证件号码不能为空")
	@Schema(description = "商户联系人证件号码")
	private String contactLicenceNo;

	/**
	 * 商户联系人手机号
	 */
	@NotNull(message = "商户联系人手机号不能为空")
	@Schema(description = "商户联系人手机号")
	private String contactMobile;

	/**
	 * 商户联系人邮箱
	 */
	@NotNull(message = "商户联系人邮箱")
	@Schema(description = "商户联系人邮箱")
	private String contactEmail;

	/**
	 * 商户实际经营地所在省
	 */
	@NotNull(message = "请选择省份")
	@Schema(description = "商户实际经营地所在省")
	private String province;

	/**
	 * 商户实际经营地所在市
	 */
	@NotNull(message = "请选择城市")
	@Schema(description = "商户实际经营地所在市")
	private String city;

	/**
	 * 商户实际经营地所在区
	 */
	@NotNull(message = "请选择地区")
	@Schema(description = "商户实际经营地所在区")
	private String district;

	/**
	 * 商户实际经营详细地址
	 */
	@NotNull(message = "商户实际经营详细地址不能为空")
	@Schema(description = "商户实际经营详细地址")
	private String address;

	/**
	 * 合同地址
	 */
	@Schema(description = "合同地址")
	private String contractUrl;

	/**
	 * 申请状态 NOT_APPLIED:未申请 REVIEWING:申请审核中 REVIEW_BACK:申请已驳回 AGREEMENT_SIGNING:协议待签署 BUSINESS_OPENING:业务开通中 COMPLETED:申请已完成
	 */
	@Schema(description = "申请状态 NOT_APPLIED:未申请 REVIEWING:申请审核中 REVIEW_BACK:申请已驳回 AGREEMENT_SIGNING:协议待签署 BUSINESS_OPENING:业务开通中 COMPLETED:申请已完成")
	private String status;

	/**
	 * 审核意见: “申请已驳回”或者“申请已完成”时，回传的审核意见
	 */
	@Schema(description = "审核意见: “申请已驳回”或者“申请已完成”时，回传的审核意见")
	private String auditOpinion;

	/**
	 * 结算方向
	 * 如果需要开通结算产品，该字段必传；如不需开通结算产品，该结算账户信息都可不传。
	 * ACCOUNT(结算到支付账户)
	 * BANKCARD(结算到银行账户，如结算到结算账户时对公账户/单位结算卡账户名称系统默认处理为商户签约名称；借记卡/存折账户名称系统默认处理为商户经营者/法人姓名。）
	 */
	@Schema(description = "结算方向 ACCOUNT(结算到支付账户)  BANKCARD(结算到银行账户)")
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
	@NotNull(message = "银行账户类型不能为空")
	@Schema(description = "银行账户类型 UNIT_SETTLEMENT_CARD(单位结算卡)  ENTERPRISE_ACCOUNT(对公账户)")
	private String bankAccountType;

	/**
	 * 银行账户号码
	 */
	@NotNull(message = "银行账户号码不能为空")
	@Schema(description = "银行账户号码")
	private String bankCardNo;


	/**
	 * 银行账户开户总行编码
	 */
	@NotNull(message = "银行账户开户总行编码不能为空")
	@Schema(description = "银行账户开户总行编码")
	private String bankCode;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

}
