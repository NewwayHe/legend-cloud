/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.dto;

import com.legendshop.common.excel.enums.ExportExcelStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelUpdateDTO
 * @date 2022/4/25 14:58
 * @description： excel导出更新对象
 */
@Data
public class ExportExcelUpdateDTO implements Serializable {

	private static final long serialVersionUID = -3324610166250776476L;

	/**
	 * sso的文件名
	 */
	private String fileName;

	/**
	 * 导出状态
	 */
	private ExportExcelStatusEnum status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 下载地址
	 */
	private String url;
}
