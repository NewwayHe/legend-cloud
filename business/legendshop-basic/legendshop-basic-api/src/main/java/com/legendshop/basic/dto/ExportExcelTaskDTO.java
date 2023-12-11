/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.ExportExcelBusinessEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 导出Excel数据任务DTO
 *
 * @author legendshop
 * @since 2022-04-19
 */
@Data
@NoArgsConstructor
@Schema(description = "导出Excel数据任务DTO")
public class ExportExcelTaskDTO {

	@Schema(description = "ID")
	private Long id;

	@Schema(description = "文件名称")
	private String fileName;

	@Schema(description = "用户类型(U:普通用户 S:商家 A:平台)")
	private String userType;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "用户名")
	private String userName;

	/**
	 * ExportExcelStatusEnum
	 */
	@Schema(description = "状态(-1:导出失败, 10:正在导出 20:导出成功)")
	private Integer status;

	@Schema(description = "下载链接")
	private String url;

	@Schema(description = "备注")
	private String remark;

	/**
	 * ExportExcelBusinessEnum
	 */
	@Schema(description = "业务名称")
	private String businessName;

	/**
	 * ExportExcelBusinessEnum
	 */
	@Schema(description = "业务类型")
	private String businessType;

	private Date createTime;

	private Date updateTime;

	public ExportExcelTaskDTO(Long userId, String userName, String userType, ExportExcelBusinessEnum businessEnum) {
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
		this.businessName = businessEnum.getDesc();
		this.businessType = businessEnum.getValue();
	}

}
