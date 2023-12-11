/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "文件夹查询")
public class AttachmentFileFolderQuery extends PageParams {

	@Schema(description = "文件名字")
	private String fileName;

	@Schema(description = "父级Id")
	private Long parentId;

	@Schema(description = "主键Id")
	private Long id;

	@Schema(description = "店铺Id")
	private Long shopId;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型(1: 文件夹, 0: 附件)")
	private Integer type;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date creatTime;

	@Schema(description = "关键字")
	private String keyWord;

	@Schema(description = "用户类型(0是平台，1是商家)")
	private Integer userType;

	@Schema(description = "目录等级")
	private Integer typeId;

	@Schema(description = "上一页每页大小")
	private Integer breakPageSize;

	private Long startLimit;

	private Long endLimit;
}
