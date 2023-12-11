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
import java.math.BigDecimal;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ZipDecompressUploadResultDTO
 * @date 2022/9/22 17:31
 * @description：压缩包解压上传结果DTO
 */
@Data
public class ZipDecompressUploadResultDTO implements Serializable {

	private static final long serialVersionUID = 8988741899190036804L;

	@Schema(description = "压缩包内文件总数")
	private Integer totalNum;

	@Schema(description = "成功数")
	private Integer successNum;

	@Schema(description = "失败数")
	private Integer failNum;

	@Schema(description = "进度")
	private BigDecimal schedule;

	@Schema(description = "状态，0、未开始，1、进行中 2、结束")
	private Integer status;

	@Schema(description = "错误信息")
	private String errMsg;

	public ZipDecompressUploadResultDTO() {
		this.totalNum = 0;
		this.successNum = 0;
		this.failNum = 0;
		this.schedule = BigDecimal.ZERO;
		this.status = 1;
	}
}
