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
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.enums.UserSexEnum;
import com.legendshop.user.util.PasswordGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;

/**
 * 个人信息
 *
 * @author legendshop
 */
@Data
@Schema(description = "个人信息Dto")
public class MyPersonInfoDTO implements Serializable {


	private static final long serialVersionUID = -8108858270281069057L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "用户名")
	private String userName;

	@Schema(description = "真实姓名")
	private String realName;

	@MobileValid
	@DataSensitive(type = MOBILE_PHONE)
	@Schema(description = "手机")
	private String mobile;

	@Schema(description = "身份证")
	private String idCard;

	@Schema(description = "性别")
	@EnumValid(target = UserSexEnum.class, message = "非法性别入参！", groups = {PasswordGroup.class})
	private String sex;

	@Schema(description = "头像路径")
	private String portraitPic;

	@Schema(description = "可用积分")
	private BigDecimal availableIntegral = BigDecimal.ZERO;

	@Schema(description = "累计积分")
	private BigDecimal cumulativeIntegral;

	@Schema(description = "用户分销员信息，为空时不显示分销信息")
	private UserDistributionInfoDTO distribution;

	@Schema(description = "微信号")
	private String weChatNumber;

	@Schema(description = "是否设置了支付密码")
	private Boolean setUpPaymentPassword;

	@Schema(description = "是否设置了登录密码")
	private Boolean setUpLoginPassword;

	@Schema(description = "电子邮件")
	private String email;

	@Schema(description = "默认地址")
	private UserAddressBO defaultAddress;

	@Schema(description = "个性化开关")
	private Boolean closeRecommend;
}
