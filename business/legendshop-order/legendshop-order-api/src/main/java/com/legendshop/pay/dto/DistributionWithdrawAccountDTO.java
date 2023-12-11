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
import com.legendshop.common.core.validator.group.FeignSave;
import com.legendshop.common.core.validator.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提现账户
 * (DistributionWithdrawAccount)DTO
 *
 * @author legendshop
 * @since 2022-03-10 15:57:24
 */
@Data
@Schema(description = "提现账户DTO")
public class DistributionWithdrawAccountDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 657177064387638558L;

	@NotNull(message = "请选择提现账户！", groups = Update.class)
	private Long id;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不能为空", groups = {FeignSave.class})
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不能为空", groups = {FeignSave.class})
	@Schema(description = "用户名")
	private String realName;

	/**
	 * 账号
	 */
	@NotNull(message = "账号不能为空", groups = {FeignSave.class})
	@Schema(description = "账号")
	private String account;

	/**
	 * 提现方式  WithdrawTypeEnum
	 */
	@NotNull(message = "提现账号类型不能为空", groups = {FeignSave.class})
	@Schema(description = "提现账号类型 ALI:支付宝, WECHAT: 微信")
	private String withdrawType;

	@Schema(description = "是否删除 0:未删除, 1:已删除")
	private Integer delFlag;

	@Schema(description = "默认账号 0：否，1：是")
	private Integer defaultFlag;

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
