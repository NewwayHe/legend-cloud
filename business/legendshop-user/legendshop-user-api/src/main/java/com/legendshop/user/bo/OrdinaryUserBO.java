/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import cn.legendshop.jpaplus.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.common.core.annotation.DataSensitive;
import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.common.core.annotation.PasswordValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;

/**
 * 普通用户列表
 *
 * @author legendshop
 */
@Schema(description = "普通用户列表")
@Data
public class OrdinaryUserBO implements Serializable {

	private static final long serialVersionUID = 4425403636263011694L;

	/**
	 * 用户ID
	 */
	@Column(name = "id")
	private Long id;


	/**
	 * 名称
	 */
	@Column(name = "username")
	private String username;


	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;


	/**
	 * 删除标记,1:正常,0:已删除
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;


	/**
	 * 锁定状态,1:正常,0:已锁定
	 */
	@Column(name = "lock_flag")
	private Boolean lockFlag;

	/**
	 * 手机号码
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 手机号
	 */
	@Column(name = "avatar")
	private String avatar;


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
	@PasswordValid(type = PasswordValid.PasswordType.PAY, message = "支付密码必须为6位数字")
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
	private String mobilePhone;

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


	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private Date createTime;


	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "修改时间")
	private Date updateTime;

}
