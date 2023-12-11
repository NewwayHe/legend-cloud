/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.legendshop.common.core.annotation.DataSensitive;
import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.ID_CARD;

/**
 * 用户详细信息表(ShopDetail)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家用户详细信息DTO")
public class ShopDetailDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -6588817947203918449L;

	/**
	 * 店铺ID
	 */
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long shopUserId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	@NotBlank(message = "店铺名不能为空")
	private String shopName;


	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;
	/**
	 * 营业执照
	 */
	@Schema(description = "印业执照")
	private String businessLicense;

	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Schema(description = "店铺类型 0.专营店1.旗舰店2.自营店")
	private Integer shopType;

	//  火狐需求 ⬇ ====================================================================== ⬇⬇⬇
	/**
	 * 行业目录
	 */
	@Schema(description = "店铺行业目录ID")
	private Long industryDirectoryId;

	@Schema(description = "分销员手机号")
	private String distributionMobile;

	@Schema(description = "用户绑定验证码")
	private String checkCode;

	//  火狐需求 ⬆ ====================================================================== ⬆⬆⬆
	/**
	 * 联系人姓名
	 */
	@Schema(description = "联系人姓名")
	@NotBlank(message = "姓名不能为空")
	private String contactName;


	/**
	 * 联系手机
	 */
	@DataSensitive(type = DataSensitive.SensitiveTypeEnum.MOBILE_PHONE)
	@Schema(description = "联系手机")
	@MobileValid
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
	@DataSensitive(type = ID_CARD)
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
	 * 主体申请类型，0，个人。  1，商家
	 * {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "主体申请类型.1:个人、2:商家、3:分销员")
	private Integer applyForType;

	/**
	 * 省份
	 */
	@NotNull(message = "店铺地址：请选择省份")
	@Schema(description = "省份id")
	private Long provinceId;


	/**
	 * 城市
	 */
	@NotNull(message = "店铺地址：请选择城市")
	@Schema(description = "城市id")
	private Long cityId;


	/**
	 * 地级市
	 */
	@NotNull(message = "店铺地址：请选择区域")
	@Schema(description = "地级市id")
	private Long areaId;


	/**
	 * 街道ID
	 */
	@Schema(description = "街道id")
	private Long streetId;


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
	@Schema(description = "商城状态 状态,是否上线1：在线，0下线，-1审核中,-2拒绝,-3关闭")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核操作状态")
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
	 * 包邮状态：1：开启 0 关闭
	 */
	@Schema(description = "包邮状态：1：开启 0 关闭")
	private Integer mailFeeSts;


	/**
	 * 包邮类型：1：满金额 2：满件
	 */
	@Schema(description = "包邮类型：1：满金额 2：满件")
	private Integer mailFeeType;


	/**
	 * 包邮条件的数量
	 */
	@Schema(description = "包邮条件的数量")
	private BigDecimal mailFeeCon;


	/**
	 * 是否开启发票
	 */
	@Schema(description = "是否开启发票")
	private Boolean invoiceFlag;

	/**
	 * 允许开具发票类型
	 */
	@Schema(description = "允许开具发票类型  发票类型，NORMAL:增值税普票 DEDICATED:增值税专票")
	private String invoiceType;

	/**
	 * 佣金结算比例，需要支持小数位
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


	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "累计消费金额")
	private BigDecimal salesAmount;

	@Schema(description = "累计消费订单数量")
	private Integer salesOrderCount;

	@Schema(description = "累计退款金额")
	private BigDecimal refundAmount;


	@Schema(description = "店铺完整地址")
	private String shopCompleteAddress;


	/**
	 * 已经销售数量
	 */
	@Schema(description = "已经销售数量")
	private Integer buys;


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
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;


	/**
	 * 退货收货人
	 */
	@Schema(description = "return_consignee")
	private String returnConsignee;

	/**
	 * 退货收货人电话号码
	 */
	@Schema(description = "return_consignee_phone")
	private String returnConsigneePhone;


	/**
	 * 退货省份Id.
	 */
	@Schema(description = "return_province_id")
	protected Long returnProvinceId;

	/**
	 * 退货城市Id.
	 */
	@Schema(description = "return_city_id")
	protected Long returnCityId;

	/**
	 * 退货地区Id.
	 */
	@Schema(description = "return_area_id")
	protected Long returnAreaId;


	/**
	 * 退货街道Id.
	 */
	@Schema(description = "return_street_id")
	protected Long returnStreetId;

	/**
	 * 退货详细地址.
	 */
	@Schema(description = "return_shop_addr")
	protected String returnShopAddr;
	////////////////以上是shopuser的信息/////////////////////////

	@Schema(description = "默认分销比例")
	private BigDecimal defaultDisScale;

	@Schema(description = "新手引导状态 0:新手,1:非新手")
	private Integer shopNewBieStatus;

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
	 * 注册资金
	 */
	@Schema(description = "注册资金")
	private BigDecimal registeredCapital;

	/**
	 * 经营范围
	 */
	@Schema(description = "经营范围")
	private String businessScope;

}
