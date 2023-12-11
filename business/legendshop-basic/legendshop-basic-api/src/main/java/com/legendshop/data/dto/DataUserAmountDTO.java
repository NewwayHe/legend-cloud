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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数量统计表(DataUserAmount)DTO
 *
 * @author legendshop
 * @since 2021-03-22 14:13:10
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "用户数量统计表DTO")
public class DataUserAmountDTO implements Serializable {

	private static final long serialVersionUID = 980652591693553050L;

	/**
	 * ID
	 */
	@ExcelIgnore
	@Schema(description = "ID")
	private Long id;

	/**
	 * 创建时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 累计用户数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计用户数量")
	@Schema(description = "累计用户数量")
	private Integer peopleAmount;

	/**
	 * 全渠道新增用户数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("全渠道新增用户数量")
	@Schema(description = "全渠道新增用户数量")
	private Integer peopleNew;

	/**
	 * app端新增用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("app端新增用户")
	@Schema(description = "app端新增用户")
	private Integer appNew;

	/**
	 * h5端新增用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("h5端新增用户")
	@Schema(description = "h5端新增用户")
	private Integer h5New;

	/**
	 * 公众号端新增用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("公众号端新增用户")
	@Schema(description = "公众号端新增用户")
	private Integer mpNew;

	/**
	 * 小程序端新增用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序端新增用户")
	@Schema(description = "小程序端新增用户")
	private Integer miniNew;

	/**
	 * 未知端新增用户
	 */
	@ExcelIgnore
	@Schema(description = "未知新增用户")
	private Integer unknown;

	public DataUserAmountDTO(Integer num) {
		this.peopleAmount = num;
		this.peopleNew = num;
		this.appNew = num;
		this.h5New = num;
		this.mpNew = num;
		this.miniNew = num;
		this.unknown = num;
	}

	public DataUserAmountDTO() {
	}
}
