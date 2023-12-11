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
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.validator.group.Update;
import com.legendshop.shop.enums.FeedbackSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户反馈表(UserFeedback)实体类
 *
 * @author legendshop
 */
@Schema(description = "用户反馈DTO")
@Data
public class UserFeedbackDTO implements Serializable {


	private static final long serialVersionUID = 2355335664751076747L;
	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 反馈人名字
	 */
	@Schema(description = "反馈人名字")
	private String name;


	/**
	 * 联系方式
	 */
	@Schema(description = "联系方式")
	@DataSensitive(type = DataSensitive.SensitiveTypeEnum.MOBILE_PHONE)
	private String mobile;


	/**
	 * 发起反馈的IP地址
	 */
	@Schema(description = "发起反馈的IP地址")
	private String ip;
	/**
	 * 反馈内容
	 */
	@NotBlank(message = "内容不能为空")
	@Schema(description = "反馈内容")
	@Length(max = 500, message = "内容不能超过500字")
	private String content;

	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date createTime;

	/**
	 * 是否已经阅读
	 */
	@Schema(description = "是否已经阅读")
	private Integer status;


	/**
	 * 回复的管理员ID
	 */
	@Schema(description = "回复的管理员ID")
	private Long respMgntId;


	/**
	 * 处理意见
	 */
	@Schema(description = "处理意见")
	@NotBlank(message = "处理意见不能为空", groups = Update.class)
	private String respContent;


	/**
	 * 处理时间
	 */
	@Schema(description = "处理时间")
	private Date respDate;


	/**
	 * 反馈来源[pc：pc端 mobile：移动端] {@link com.legendshop.shop.enums.FeedbackSourceEnum}
	 */
	@Schema(description = "反馈来源[pc：pc端 mobile：移动端]")
	@EnumValid(target = FeedbackSourceEnum.class, message = "反馈来源不能为空")
	private String feedbackSource;

	/**
	 * id 集合
	 */
	@Schema(description = "id 集合")
	private List<Long> ids;
}
