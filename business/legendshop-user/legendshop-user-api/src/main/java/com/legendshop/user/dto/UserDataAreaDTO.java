/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户地区分布统计DTO
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "用户地区分布统计DTO")
public class UserDataAreaDTO {

	/**
	 * 省名
	 */
	@ColumnWidth(30)
	@ExcelProperty("省")
	@Schema(description = "省名")
	private String province;

	/**
	 * 城市名
	 */
	@ColumnWidth(30)
	@ExcelProperty("市")
	@Schema(description = "城市名")
	private String city;

	/**
	 * 人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("用户人数")
	@Schema(description = "人数")
	private Integer people;
}
