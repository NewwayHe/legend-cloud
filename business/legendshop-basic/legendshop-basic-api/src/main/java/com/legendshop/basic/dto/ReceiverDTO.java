/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.ReceiverBusinessTypeEnum;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "消息已读DTO")
public class ReceiverDTO extends BaseDTO implements Serializable {
	private static final long serialVersionUID = 1944974763562703646L;
	/**
	 * ID
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 业务ID
	 */
	@Schema(description = "业务ID")
	private Long businessId;

	/**
	 * 业务类型
	 * {@link ReceiverBusinessTypeEnum}
	 */
	@Schema(description = "业务类型")
	private Integer businessType;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户类型
	 * {@link MsgReceiverTypeEnum}
	 */
	@Schema(description = "用户类型")
	private Integer userType;
	/**
	 * 状态 0未读 1已读 只插入已读数据
	 */
	@Schema(description = "状态 0未读 1已读")
	private Integer status;

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
}
