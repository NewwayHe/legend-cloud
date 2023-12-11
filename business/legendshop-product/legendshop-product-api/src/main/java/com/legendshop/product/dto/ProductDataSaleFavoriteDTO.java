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
@Schema(description = "销售排行趋势图收藏列表")
public class ProductDataSaleFavoriteDTO {

	@ColumnWidth(30)
	@ExcelProperty("时间")
	@Schema(description = "时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date time;

	@ColumnWidth(30)
	@ExcelProperty("商品收藏量")
	@Schema(description = "商品收藏量")
	private Integer favoriteNum;

	@ColumnWidth(30)
	@ExcelProperty("累计商品收藏量")
	@Schema(description = "累计商品收藏量")
	private Integer totalFavoriteNum;
}
