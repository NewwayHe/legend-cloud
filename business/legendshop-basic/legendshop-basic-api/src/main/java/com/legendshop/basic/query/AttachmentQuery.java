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

import java.io.Serializable;
import java.util.Date;

/**
 * 附件表(Attachment)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-07-12 08:59:23
 */
@Data
public class AttachmentQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = 264332803638605361L;

	@Schema(description = "文件名字")
	private String fileName;

	@Schema(description = "主键Id")
	private Long id;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型(0: 文件夹, 1: 附件)")
	private Integer fileType;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date creatTime;

}
