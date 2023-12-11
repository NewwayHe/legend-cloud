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
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 部门树对象
 *
 * @author legendshop
 */
@Data
@Schema(description = "部门树结构")
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode implements Serializable {

	@Schema(description = "部门名称")
	private String name;

	/**
	 * 联系人
	 */
	@Schema(description = "联系人")
	private String contact;

	/**
	 * 联系电话
	 */
	@Schema(description = "联系电话")
	private String mobile;

}
