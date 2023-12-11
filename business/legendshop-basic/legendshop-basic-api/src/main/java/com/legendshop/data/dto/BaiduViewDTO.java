/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 百度（移动）统计记录(BaiduView)DTO
 *
 * @author legendshop
 * @since 2021-06-19 17:32:13
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "百度（移动）统计记录DTO")
public class BaiduViewDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -59955912724479470L;

	/**
	 * id
	 */
	@ExcelIgnore
	@Schema(description = "id")
	private Long id;

	/**
	 * h5访问总人数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "H5端访问人数", index = 3)
	@Schema(description = "H5端访问人数")
	private Integer h5Uv;

	/**
	 * h5访问总次数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "H5端访问次数", index = 4)
	@Schema(description = "H5端访问次数")
	private Integer h5Pv;

	/**
	 * 小程序访问总人数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "小程序端访问人数", index = 5)
	@Schema(description = "小程序端访问人数")
	private Integer miniUv;

	/**
	 * 小程序访问总次数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "小程序端访问次数", index = 6)
	@Schema(description = "小程序端访问次数")
	private Integer miniPv;

	/**
	 * 访问总人数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "访问总人数", index = 1)
	@Schema(description = "全部渠道访问人数")
	private Integer totalUv;

	/**
	 * 访问总次数
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "访问总次数", index = 2)
	@Schema(description = "全部渠道访问次数")
	private Integer totalPv;

	/**
	 * 归档时间
	 */
	@ColumnWidth(30)
	@ExcelProperty(value = "日期", index = 0)
	@Schema(description = "归档时间")
	private String archiveTime;

}
