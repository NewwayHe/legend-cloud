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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * (AttachmentFileHistory)实体类
 *
 * @author legendshop
 * @since 2022-02-15 16:11:05
 */
@Data
@Entity
@Table(name = "ls_attachment_file_history")
public class AttachmentFileHistory extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 784193549310463253L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "attachmentFileHistory_SEQ")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 文件夹id列表
	 */
	@Column(name = "folder_id")
	private String folderId;


	/**
	 * 用户类型
	 */
	@Column(name = "user_type")
	private String userType;

}
