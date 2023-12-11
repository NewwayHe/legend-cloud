/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (AttachmentFileFolder)DTO
 *
 * @author legendshop
 * @since 2021-07-06 14:35:27
 */
@Data
@Schema(description = "DTO")
public class AttachmentFileFolderDTO implements Serializable {

	private static final long serialVersionUID = 161596525174949768L;

	@Schema(description = "id")
	private Long id;

	/**
	 * 文件夹名字
	 */
	@Schema(description = "文件夹名字")
	private String fileName;

	/**
	 * 父级id
	 */
	@Schema(description = "父级id")
	private Long parentId;

	/**
	 * 目录等级
	 */
	@Schema(description = "目录等级")
	private Integer typeId;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 创建时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

	/**
	 * 当前文件夹中的图片列表
	 */
	@Schema(description = "当前文件夹下图片List")
	List<AttachmentDTO> attachmentList;

	/**
	 * 当前文件夹中的文件夹列表
	 */
	@Schema(description = "子文件夹list")
	List<AttachmentFileFolderDTO> fileFolderList;

	/**
	 * 商品店家id
	 */
	@Schema(description = "商品店家id")
	private Long shopId;

	/**
	 * 用户类型
	 */
	@Schema(description = "用户类型(0: 平台, 1: 商家)")
	private Integer userType;

	/**
	 * 路径
	 */
	@Schema(description = "文件路径")
	private String url;

	/**
	 * 视频路径
	 */
	@Schema(description = "视频路径")
	private String videoUrl;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型(1: 文件夹, 0: 附件)")
	private Integer type;

	/**
	 * 文件名字（附件表与文件夹表通用字段）
	 */
	@Schema(description = "文件名字（附件表与文件夹表通用字段）")
	private String name;

	/**
	 * 附件表附件类别
	 */
	@Schema(description = "附件类别")
	private String ext;

	/**
	 * 关键字
	 */
	@Schema(description = "关键字")
	private String keyWord;

	/**
	 * 该目录下的子目录以及文件
	 */
	@Schema(description = "是否含有下级文件夹")
	private Boolean isNext;
}
