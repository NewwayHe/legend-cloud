/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * AdminUser查询对象
 *
 * @author legendshop
 */
@Data
public class AdminUserQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -15934681953183653L;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String username;

	/**
	 * 部门名称，模糊搜索
	 */
	@Schema(description = "部门名称")
	private String deptName;

	/**
	 * 角色名称，模糊搜索
	 */
	@Schema(description = "角色名称")
	private String roleName;

	/**
	 * 是否锁定
	 */
	@Schema(description = "是否锁定")
	private Boolean lockFlag;
}
