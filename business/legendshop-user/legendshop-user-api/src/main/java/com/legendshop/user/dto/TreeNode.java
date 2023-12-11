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

import java.util.ArrayList;
import java.util.List;

/**
 * 基础树形节点结构
 *
 * @author legendshop
 */
@Data
@Schema(description = "树形节点")
public class TreeNode<T> {

	@Schema(description = "当前节点id")
	protected Long id;

	@Schema(description = "父节点id")
	protected Long parentId;

	@Schema(description = "父节点名称")
	protected String parentName;

	@Schema(description = "子节点列表")
	protected List<T> children = new ArrayList<>();

	public void add(T node) {
		children.add(node);
	}

}
