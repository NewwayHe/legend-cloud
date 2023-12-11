/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城公告DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "商城公告DTO")
public class PubDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = 3147369232766852493L;


	@Schema(description = "主键ID")
	private Long id;


	@Schema(description = "发布者ID")
	private Long adminUserId;


	@Schema(description = "发布者名称")
	private String adminUserName;


	@Schema(description = "标题")
	@NotBlank(message = "公告名称不能为空")
	private String title;


	@Schema(description = "内容")
	@NotBlank(message = "公告内容不能为空")
	private String content;

	/**
	 * 开始有效时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "开始有效时间")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "失效时间")
	private Date endTime;


	@Schema(description = "状态，1:上线，0：下线")
	private Integer status;

	@Schema(description = "状态，0未读，1已读 ")
	private Integer receiverStatus;

	@Schema(description = "公告类型:0:买家,1:卖家")
	private Integer type;

}
