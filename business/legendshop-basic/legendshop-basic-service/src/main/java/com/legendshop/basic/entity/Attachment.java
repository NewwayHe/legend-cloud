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
 * 附件表(Attachment)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_attachment")
public class Attachment implements GenericEntity<Long> {

	private static final long serialVersionUID = -42898849023679209L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ATTACHMENT_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 文件名称 (只作展示用)
	 */
	@Column(name = "file_name")
	private String fileName;


	/**
	 * 扩展名
	 */
	@Column(name = "ext")
	private String ext;


	/**
	 * 文件源
	 */
	@Column(name = "file_source")
	private String fileSource;


	/**
	 * 视频路径
	 */
	@Column(name = "video_path")
	private String videPath;

	/**
	 * 文件路径
	 */
	@Column(name = "file_path")
	private String filePath;


	/**
	 * 文件大小
	 */
	@Column(name = "file_size")
	private Long fileSize;


	/**
	 * 状态 1 正常  0 删除
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 分类id
	 */
	@Column(name = "tree_id")
	private Long treeId;

	/**
	 * 是否 图片空间管理 1: yes  0: no
	 */
	@Column(name = "managed_flag")
	private Integer managedFlag;


	/**
	 * 记录时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 图片用途(客服系统)
	 */
	@Column(name = "purpose")
	private String purpose;

	@Column(name = "scope")
	private String scope;

	/**
	 * 短链，用于商品批量导入时快速找到图片时使用
	 */
	@Column(name = "short_path")
	private String shortPath;
}
