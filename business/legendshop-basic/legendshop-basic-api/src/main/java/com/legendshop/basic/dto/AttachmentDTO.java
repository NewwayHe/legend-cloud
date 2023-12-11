/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件的dto,最后转变为Attachment再保存到数据库中
 *
 * @author legendshop
 */
@Data
public class AttachmentDTO implements Serializable {

	private static final long serialVersionUID = -2128199535973041830L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 用户Id
	 **/
	private Long userId;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 文件内容
	 */
	private ServiceMultipartFile file;


	/**
	 * 挂载的树节点,默认为0
	 */
	private Long treeId = 0L;

	/**
	 * 记录时间
	 */
	private Date craeteTime;

	/**
	 * 扩展名
	 */
	private String ext;

	/**
	 * 文件大小
	 */
	private Long fileSize;

	/**
	 * 状态 1 正常  0 删除
	 */
	private int status;


	/**
	 * 文件名称 (只作展示用)
	 */
	private String fileName;

	/**
	 * 是否 图片空间管理 1: yes  0: no
	 */
	private Integer managedFlag;

	/**
	 * 视频路径
	 */
	private String videoPath;

	public Map<String, String> checkAttachment() {
		Map<String, String> result = new HashMap<String, String>(16);

		if (ObjectUtil.isEmpty(file)) {
			result.put("file", "文件内容不能为空");
		}

		return result;
	}

	/**
	 * 对应的文件夹id
	 */
	@Schema(description = "对应的文件夹id")
	private Long fileFolderId;

	/**
	 * 文件夹名称
	 */
	@Schema(description = "文件夹名称")
	private String fileFolderName;

	/**
	 * 文件夹父级id
	 */
	@Schema(description = "文件夹父级id")
	private Long fileFolderParentId;

	/**
	 * 文件夹层级
	 */
	@Schema(description = "文件夹层级")
	private Integer fileFolderTypeId;

	/**
	 * 商品商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 路径
	 */
	@Schema(description = "路径")
	private String url;

	/**
	 * 文件类型(1:文件夹  0：附件)
	 */
	@Schema(description = "文件类型(1: 文件夹, 0: 附件)")
	private Integer type;

	/**
	 * 文件名字（附件表与文件夹表通用字段）
	 */
	@Schema(description = "文件名字")
	private String name;

	@Schema(description = "短链路径")
	private String shortPath;

}
