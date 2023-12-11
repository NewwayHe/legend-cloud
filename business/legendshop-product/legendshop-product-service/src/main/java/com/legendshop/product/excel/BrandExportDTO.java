/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌DTO
 *
 * @author legendshop
 */
@Data
public class BrandExportDTO implements Serializable {

	/**
	 * 主键
	 */
	@ColumnWidth(30)
	@ExcelProperty("品牌id")
	@Schema(description = "品牌id")
	private Long id;


	/**
	 * 品牌名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("品牌名称")
	@Schema(description = "品牌名称")
	private String brandName;


	/**
	 * PC品牌logo
	 */
	@ColumnWidth(30)
	@ExcelProperty("品牌logo")
	@Schema(description = "品牌logo")
	private String brandPic;

	/**
	 * 申请时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("申请时间")
	@Schema(description = "申请时间")
	private Date createTime;

	@ColumnWidth(30)
	@ExcelProperty("状态")
	@Schema(description = "状态")
	private String statusStr;

}
