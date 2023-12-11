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
 * (AttachmentFolder)实体类
 *
 * @author legendshop
 * @since 2021-07-06 17:35:16
 */
@Data
@Entity
@Table(name = "ls_attachment_folder")
public class AttachmentFolder implements GenericEntity<Long> {

	private static final long serialVersionUID = -66561101998183164L;


	/**
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "attachmentFileFolder_SEQ")
	@Column(name = "id")
	private Long id;
	/**
	 * 附件管理图片id
	 */
	@Column(name = "attachment_id")
	private Long attachmentId;

	/**
	 * 附件管理文件夹id
	 */
	@Column(name = "file_folder_id")
	private Long fileFolderId;

	/**
	 * 附件路径
	 */
	@Column(name = "attachment_folder")
	private String attachmentFolder;


}
