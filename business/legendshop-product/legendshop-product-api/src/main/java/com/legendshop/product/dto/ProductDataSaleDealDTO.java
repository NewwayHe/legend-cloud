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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "销售排行趋势图成交分页")
public class ProductDataSaleDealDTO {

	@Schema(description = "时间")
	@ColumnWidth(30)
	@ExcelProperty("时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date time;

	@ColumnWidth(30)
	@ExcelProperty("成交金额")
	@Schema(description = "成交金额")
	private BigDecimal dealAmount;

	@ColumnWidth(30)
	@ExcelProperty("累计成交金额")
	@Schema(description = "累计成交金额")
	private BigDecimal totalDealAmount;


	@ColumnWidth(30)
	@ExcelProperty("成交数量")
	@Schema(description = "成交数量")
	private Integer dealGoodNum;

	@ColumnWidth(30)
	@ExcelProperty("累计成交数量")
	@Schema(description = "累计成交数量")
	private Integer totalDealGoodNum;

	public ProductDataSaleDealDTO() {
		this.dealAmount = BigDecimal.ZERO;
		this.totalDealAmount = BigDecimal.ZERO;
		this.dealGoodNum = 0;
		this.totalDealGoodNum = 0;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
		if (dealAmount == null) {
			this.dealAmount = new BigDecimal(0);
		}
	}

	public void setDealGoodNum(Integer dealGoodNum) {
		this.dealGoodNum = dealGoodNum;
		if (dealGoodNum == null) {
			this.dealGoodNum = 0;
		}
	}

	public void setTotalDealAmount(BigDecimal totalDealAmount) {
		this.totalDealAmount = totalDealAmount;
		if (totalDealAmount == null) {
			this.totalDealAmount = new BigDecimal(0);
		}
	}

	public void setTotalDealGoodNum(Integer totalDealGoodNum) {
		this.totalDealGoodNum = totalDealGoodNum;
		if (totalDealGoodNum == null) {
			this.totalDealGoodNum = 0;
		}
	}
}
