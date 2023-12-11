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

import java.util.Date;

/**
 * (AttachmentFileFolder)实体类
 *
 * @author legendshop
 * @since 2021-07-06 14:19:22
 */
@Data
@Entity
@Table(name = "ls_attachment_file_folder")
public class AttachmentFileFolder implements GenericEntity<Long> {

	private static final long serialVersionUID = 261868919940359506L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "attachmentFileFolder_SEQ")
	private Long id;

	/**
	 * 文件夹名字
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * 父级id
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 目录等级
	 */
	@Column(name = "type_id")
	private Integer typeId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新
	 */
	@Column(name = "update_time")
	private Date updateTime;


	/**
	 * 商品店家id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 用户类型
	 */
	@Column(name = "user_type")
	private Integer userType;
}
