/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "我的关注、粉丝")
public class ConcernUserBO implements Serializable {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "用户昵称")
	private String nickName;

	@Schema(description = "用户头像")
	private String avatar;

	@Schema(description = "是否关注")
	private Boolean concernFlag;
}
