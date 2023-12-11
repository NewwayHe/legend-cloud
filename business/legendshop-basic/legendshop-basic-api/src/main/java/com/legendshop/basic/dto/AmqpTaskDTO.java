/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队列任务表(AmqpTask)DTO
 *
 * @author legendshop
 * @since 2022-04-29 14:16:54
 */
@Data
@Schema(description = "队列任务表DTO")
public class AmqpTaskDTO implements Serializable {

	private static final long serialVersionUID = -67123356991876640L;

	@Schema(description = "id", hidden = true)
	private Long id;

	/**
	 * 交换机
	 */
	@NotNull(message = "交换机不能为空")
	@Schema(description = "交换机")
	private String exchange;

	/**
	 * 路由键
	 */
	@NotNull(message = "路由键不能为空")
	@Schema(description = "路由键")
	private String routingKey;

	/**
	 * 消息体
	 */
	@NotNull(message = "消息体不能为空")
	@Schema(description = "消息体")
	private String message;

	/**
	 * 延时时间
	 */
	@NotNull(message = "延时时间不能为空")
	@Schema(description = "延时时间")
	private Date delayTime;

	/**
	 * 状态 0、未发 1、已发
	 */
	@Schema(description = "状态", hidden = true)
	private Integer status;

	@Schema(description = "更新时间", hidden = true)
	private Date updateTime;

}
