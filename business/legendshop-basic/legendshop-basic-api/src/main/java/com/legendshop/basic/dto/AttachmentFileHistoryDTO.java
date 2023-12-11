/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (AttachmentFileHistory)DTO
 *
 * @author legendshop
 * @since 2022-02-15 16:11:57
 */
@Data
@Schema(description = "DTO")
public class AttachmentFileHistoryDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 398229840428284748L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 文件夹id列表
	 */
	@Schema(description = "文件夹id列表")
	private String folderId;

	@Schema(description = "文件id列表")
	private List<Long> folderIdList;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "历史数量")
	private Integer counts;

	@Schema(description = "用户类型")
	private String userType;

}
