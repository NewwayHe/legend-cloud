/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysParamItem)Query分页查询对象
 *
 * @author legendshop
 * @since 2020-08-28 14:17:48
 */
@Schema(description = "配置项查询对象")
@Data
public class SysParamItemQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -59468890816700074L;


	@Schema(description = "主配置id")
	private String parentId;

	@Schema(description = "描述")
	private String desc;

	@Schema(description = "是否启用 1：是 0：否")
	private String enabled;

	@Schema(description = "分组")
	private String group;

}
