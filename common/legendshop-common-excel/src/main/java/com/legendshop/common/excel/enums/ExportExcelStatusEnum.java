/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelStatusEnum
 * @date 2022/4/25 15:13
 * @description：
 */
@Getter
@AllArgsConstructor
public enum ExportExcelStatusEnum {

	FAIL(-1, "导出失败"),

	IN_PROGRESS(10, "正在导出"),

	SUCCESS(20, "导出成功");

	private final Integer value;

	private final String desc;
}
