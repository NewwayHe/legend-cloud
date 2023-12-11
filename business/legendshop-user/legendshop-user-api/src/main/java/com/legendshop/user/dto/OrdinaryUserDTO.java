/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.common.core.enums.VisitSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表(User)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户端用户中心Dto")
public class OrdinaryUserDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 5945041576831221666L;

	/**
	 * 名称
	 */
	@Schema(description = "用户名")
	private String username;

	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;


	/**
	 * 删除标记,1:正常,0:已删除
	 */
	@Schema(description = "删除标记,1:正常,0:已删除")
	private Boolean delFlag;


	/**
	 * 锁定状态，1:正常,0:已锁定
	 */
	@Schema(description = "锁定状态，1:正常,0:已锁定")
	private Boolean lockFlag;

	/**
	 * 手机号
	 */
	@NotBlank(groups = {Default.class}, message = "手机号不能为空")
	@Schema(description = "手机号")
	private String mobile;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 微信openid
	 */
	@Schema(description = "微信openid")
	private String openId;


	/**
	 * 注册ip
	 */
	@Schema(description = "注册ip")
	private String regIp;


	/**
	 * 注册来源
	 */
	@Schema(description = "来源")
	private VisitSourceEnum source;

	/**
	 * 游客ID
	 */
	@Schema(description = "游客id")
	private String visitorId;

	/**
	 * 邀请码
	 */
	@Schema(description = "邀请码")
	private String invitationCode;

	public OrdinaryUserDTO() {
	}

	public OrdinaryUserDTO(String mobile) {
		this.mobile = mobile;
	}

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

	@Schema(description = "用户文章")
	private Integer articleCount;

	@Schema(description = "用户粉丝")
	private Integer fansCount;

	@Schema(description = "关注状态")
	private Boolean focusFlag;
}
