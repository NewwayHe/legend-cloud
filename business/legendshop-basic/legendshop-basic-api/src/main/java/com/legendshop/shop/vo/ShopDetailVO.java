/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.vo;

import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.annotation.MobileValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家用户详细信息表(ShopDetail)
 *
 * @author legendshop
 */
@Schema(description = "商家用户详细信息VO")
@Data
public class ShopDetailVO implements Serializable {

	private static final long serialVersionUID = -6588817947203918449L;

	/**
	 * 商家用户详细信息id
	 */
	@Schema(description = "商家用户详细信息id")
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long shopUserId;

	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	private String shopName;

	/**
	 * 营业执照
	 */
	@Schema(description = "印业执照")
	private String businessLicense;

	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;


	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Schema(description = "店铺类型0.专营店1.旗舰店2.自营店")
	private Integer shopType;


	/**
	 * 行业目录
	 */
	@Schema(description = "店铺行业目录ID")
	private Long industryDirectoryId;

	/**
	 * 联系人姓名
	 */
	@Schema(description = "联系人姓名")
	private String contactName;


	/**
	 * 联系手机
	 */
	@Schema(description = "联系手机")
	private String contactPhone;

	/**
	 * 法人姓名
	 */
	@Schema(description = "法人姓名")
	private String corporateName;

	/**
	 * 身份证号码
	 */
	@Schema(description = "身份证号码")
	private String idCardNumber;


	/**
	 * 身份证验证图片
	 */
	@Schema(description = "身份证验证图片")
	private String idCardPic;

	/**
	 * 身份证反面图片
	 */
	@Schema(description = "身份证反面图片")
	private String idCardBackPic;


	/**
	 * 主体申请类型，0，个人。  1，企业
	 * {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "主体申请类型，0，个人。  1，企业")
	private Integer applyForType;

	/**
	 * 省份ID
	 */
	@Schema(description = "省份id")
	private Long provinceId;


	/**
	 * 城市ID
	 */
	@Schema(description = "城市id")
	private Long cityId;


	/**
	 * 地级市ID
	 */
	@Schema(description = "地级市id")
	private Long areaId;


	/**
	 * 街道ID
	 */
	@Schema(description = "街道id")
	private Long streetId;

	/**
	 * 省份
	 */
	@Schema(description = "省份")
	private String province;


	/**
	 * 城市
	 */
	@Schema(description = "城市")
	private String city;


	/**
	 * 地级市
	 */
	@Schema(description = "地级市")
	private String area;


	/**
	 * 街道
	 */
	@Schema(description = "街道")
	private String street;


	/**
	 * 店铺地址
	 */
	@Schema(description = "店铺地址")
	private String shopAddress;


	/**
	 * 店铺简述
	 */
	@Schema(description = "店铺简述")
	private String briefDesc;


	/**
	 * 商城状态：{@link com.legendshop.shop.enums.ShopDetailStatusEnum}
	 */
	@Schema(description = "商城状态：[1.上线，0.下线 -1.关闭]")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核操作状态 [1.通过 ， 0.待审核  -1.拒绝]")
	private Integer opStatus;

	/**
	 * 访问人数
	 */
	@Schema(description = "访问人数")
	private Integer visitTimes;


	/**
	 * 产品数量
	 */
	@Schema(description = "产品数量")
	private Integer productNum;


	/**
	 * 评论个数
	 **/
	@Schema(description = "评论个数")
	private Integer commNum;


	/**
	 * 信誉度，根据用户评论算出
	 */
	@Schema(description = "信誉度，根据用户评论算出")
	private Integer credit;


	/**
	 * 商品是否需要审核 1:是 0:否 为空则采用平台总设置
	 */
	@Schema(description = "商品是否需要审核 1:是 0:否 为空则采用平台总设置")
	private Integer prodRequireAudit;


	/**
	 * 是否开启发票
	 */
	@Schema(description = "是否开启发票")
	private Boolean invoiceFlag;


	/**
	 * 佣金结算比例
	 */
	@Schema(description = "佣金结算比例")
	private Double commissionRate;

	/**
	 * 公司名字
	 */
	@Schema(description = "公司名字")
	private String companyName;

	/**
	 * 统一社会信用代码
	 */
	@Schema(description = "统一社会信用代码")
	private String unifiedSocialCreditCode;

	/**
	 * 用户头像
	 */
	@Schema(description = "用户头像")
	private String userAvatar;

	/**
	 * 二维码
	 */
	@Schema(description = "二维码")
	private String qrCode;

	/**
	 * 链接
	 */
	@Schema(description = "链接")
	private String url;

	@Schema(description = "商品id")
	private Long productId;

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
	 * {@link OpStatusEnum}
	 */
	@Schema(description = "审核状态 0待审核 1通过 -1拒绝")
	private Integer auditStatus;

	@Schema(description = "审核意见")
	private String auditOpinion;

	@Schema(description = "审核时间")
	private Date auditTime;

	//////////////以下是shopuser的信息///////////////////////
	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String username;

	/**
	 * 手机号码
	 */
	@Schema(description = "手机号码")
	@MobileValid
	private String mobile;

	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Boolean delFlag;

	/**
	 * 注释
	 */
	@Schema(description = "注释")
	private Boolean lockFlag;

	/**
	 * 注册资金
	 */
	@Schema(description = "注册资金")
	private BigDecimal registeredCapital;

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
	////////////////以上是shopuser的信息/////////////////////////
}
