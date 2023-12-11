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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class PassportDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 4548696592433344311L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "通行证")
	private String pass;

	@Schema(description = "通行证类型")
	private String type;

	@Schema(description = "第三方昵称")
	private String nickName;

	@Schema(description = "第三方头像路径")
	private String headPortraitUrl;

	@Schema(description = "性别")
	private String sex;

	@Schema(description = "城市")
	private String city;

	@Schema(description = "省份")
	private String province;

	@Schema(description = "国家")
	private String country;
}
