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
public class UserInformationDTO {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "用户昵称")
	private String nickName;

	@Schema(description = "用户头像路径")
	private String avatar;

	@Schema(description = "用户手机号")
	private String mobile;

	@Schema(description = "微信号")
	private String weChatNumber;

	@Schema(description = "注册时间")
	private Date createTime;

}
