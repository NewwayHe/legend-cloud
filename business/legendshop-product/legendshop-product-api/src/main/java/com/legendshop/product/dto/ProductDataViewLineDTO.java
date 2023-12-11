/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "商品访问折线图")
public class ProductDataViewLineDTO {

	/**
	 * 时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("时间")
	@Schema(description = "时间")
	@JsonFormat(
			pattern = "yyyy-MM-dd",
			timezone = "GMT+8"
	)
	private Date time;

	/**
	 * 商品访问用户总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("全渠道商品访问量")
	@Schema(description = "商品访问用户总数")
	private Integer people;

	/**
	 * 商品访问次数总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("全渠道商品访问数")
	@Schema(description = "商品访问次数总数")
	private Integer frequency;

	/**
	 * 小程序商品访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序商品访问量")
	@Schema(description = "小程序商品访问用户数")
	private Integer miniPeople;

	/**
	 * 小程序商品访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序商品访问数")
	@Schema(description = "小程序商品访问次数")
	private Integer miniFrequency;

	/**
	 * h5商品访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("h5商品访问量")
	@Schema(description = "h5商品访问用户数")
	private Integer h5People;

	/**
	 * h5商品访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("h5商品访问数")
	@Schema(description = "h5商品访问次数")
	private Integer h5Frequency;


	private Integer mpPeople;

	private Integer mpFrequency;


	public ProductDataViewLineDTO(Integer num) {
		this.people = num;
		this.frequency = num;
		this.miniPeople = num;
		this.miniFrequency = num;
		this.h5People = num;
		this.h5Frequency = num;
		this.mpPeople = num;
		this.mpFrequency = num;
	}

	public ProductDataViewLineDTO() {
	}
}
