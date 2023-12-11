/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "短信发送历史")
public class UserDataSmsDTO {

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "手机号码")
	private String mobile;

	@Schema(description = "短信内容")
	private String content;

	@Schema(description = "发送时间")
	private Date createTime;

	@Schema(description = "短信类型")
	private Integer type;

	@Schema(description = "响应码")
	private String response;

	@Schema(description = "发送状态")
	private String status;


}
