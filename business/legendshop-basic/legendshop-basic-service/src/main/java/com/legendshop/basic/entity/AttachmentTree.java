/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 附件类型(AttmntTree)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_attmnt_tree")
public class AttachmentTree implements GenericEntity<Long> {

	private static final long serialVersionUID = -90681251021545149L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ATTMNT_TREE_SEQ")
	private Long id;


	/**
	 * 分类名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 父节点
	 */
	@Column(name = "parent_id")
	private Long parentId;


	/**
	 * 商城Id
	 */
	@Column(name = "shop_id")
	private Long shopId;

}
