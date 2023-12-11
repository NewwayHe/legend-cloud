/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassportBO implements Serializable {

	private static final long serialVersionUID = -8253068827286978532L;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "用户授权类型")
	private String type;

	@Schema(description = "授权来源")
	private String source;

	@Schema(description = "标识符")
	private String thirdPartyIdentifier;
}
