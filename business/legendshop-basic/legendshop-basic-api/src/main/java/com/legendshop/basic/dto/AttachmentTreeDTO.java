/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 附件类型(AttmntTree)实体类
 *
 * @author legendshop
 */
@Data
public class AttachmentTreeDTO implements Serializable {


	private static final long serialVersionUID = 4816641331167119057L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 分类名称
	 */
	private String name;


	/**
	 * 父节点
	 */
	private Long parentId;


	/**
	 * 商城Id
	 */
	private Long shopId;

}
