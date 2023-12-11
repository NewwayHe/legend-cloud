/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "活动访问记录MQ")
public class ActivityViewDTO implements Serializable {

	private static final long serialVersionUID = 900189821209974446L;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "活动id和类型")
	List<String[]> activityList;

}
