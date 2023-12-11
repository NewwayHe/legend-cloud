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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 平台商品销售排行
 *
 * @author legendshop
 * @create: 2021-06-17 10:29
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "商品销售排行")
public class AdminProductDataShopSaleDTO extends ProductDataShopSaleDTO {

	@ColumnWidth(30)
	@ExcelProperty(value = "店铺名称", index = 0)
	@Schema(description = "店铺名称")
	private String shopName;

}
