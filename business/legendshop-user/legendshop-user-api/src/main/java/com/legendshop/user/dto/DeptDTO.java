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
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class DeptDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -9123553943308225011L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 部门名称
	 */
	@Length(max = 50, message = "部门名称长度不能超过50")
	private String name;

	/**
	 * 排序
	 */
	private Integer sort;


	/**
	 * 联系人
	 */
	@Length(max = 50, message = "联系人长度不能超过50")
	private String contact;


	/**
	 * 联系电话
	 */
	@Length(min = 11, max = 11, message = "手机号码为11位")
	private String mobile;


	/**
	 * 父节点
	 */
	private Long parentId;


	/**
	 * 父节点名称
	 */
	private String parentName;

	/**
	 * 是否有子节点
	 */
	private Boolean hasChildren;

}
