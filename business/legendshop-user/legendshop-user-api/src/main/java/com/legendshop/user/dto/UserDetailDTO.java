/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.annotation.DataSensitive;
import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.common.core.annotation.PasswordValid;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;
import static com.legendshop.common.core.annotation.PasswordValid.PasswordType;

/**
 * 用户详细表(UserDetail)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户信息详情Dto")
public class UserDetailDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -7103695397762318695L;
	/**
	 * ID
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 用户所在的商城Id，如果是买家就没有shopId
	 */
	@Schema(description = "userId")
	private Long userId;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 支付密码
	 */
	@PasswordValid(type = PasswordType.PAY, message = "支付密码必须为6位数字")
	@Schema(description = "支付密码")
	private String payPassword;


	/**
	 * 注册IP
	 */
	@Schema(description = "注册IP")
	private String regIp;


	/**
	 * 最后登录时间
	 */
	@Schema(description = "最后登录时间")
	private Date lastLoginTime;


	/**
	 * 最后登录IP
	 */
	@Schema(description = "最后登录IP")
	private String lastLoginIp;


	/**
	 * M(男) or F(女)
	 */
	@Schema(description = "M(男) or F(女)")
	private String sex;


	/**
	 * 注册时生成的注册码
	 */
	@Schema(description = "注册时生成的注册码")
	private String regCode;


	/**
	 * 积分
	 */
	@Schema(description = "积分")
	private Integer score;


	/**
	 * 省份
	 */
	@Schema(description = "省份")
	private Long provinceId;


	/**
	 * 城市
	 */
	@Schema(description = "城市")
	private Long cityId;


	/**
	 * 地级市
	 */
	@Schema(description = "地级市")
	private Long areaId;

	/**
	 * 分销上级用户
	 */
	@Schema(description = "分销上级用户")
	private Long parentUserId;


	/**
	 * 绑定上级时间
	 */
	@Schema(description = "绑定上级时间")
	private Date parentBindingTime;


	/**
	 * 号码
	 */
	@Schema(description = "号码")
	@MobileValid
	@DataSensitive(type = MOBILE_PHONE)
	private String mobile;

	/**
	 * 消费金额
	 */
	@Schema(description = "消费金额")
	private BigDecimal consumptionAmount;

	/**
	 * 消费订单数
	 */
	@Schema(description = "消费订单数")
	private Integer consumptionOrderCount;

	/**
	 * 最近消费时间
	 */
	@Schema(description = "最近消费时间")
	private Date recentConsumptionTime;

	/**
	 * 可用积分
	 */
	@Schema(description = "可用积分")
	private Integer availableIntegral;

	/**
	 * 累计积分
	 */
	@Schema(description = "累计积分")
	private Integer cumulativeIntegral;

	@Schema(description = "微信号")
	private String weChatNumber;

	/**
	 * 电子邮箱
	 */
	@Pattern(regexp = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", message = "邮箱格式不正确~")
	@Schema(description = "电子邮箱")
	private String email;

	/**
	 * 个性化开关
	 */
	@Schema(description = "个性化开关")
	private Boolean closeRecommend;
}
