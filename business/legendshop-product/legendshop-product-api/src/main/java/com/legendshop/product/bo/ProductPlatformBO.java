/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 平台商品(Prod)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品BO")
public class ProductPlatformBO extends ProductBO {

	@Override
	public String getRemarks() {
		Integer delStatus = this.getDelStatus();
		Integer status = this.getStatus();
		StringBuffer sb = new StringBuffer("商家:");
		//'删除状态：【-2：商家永久删除 -1：商家删除  1：默认未删除】',
		//默认未删除 显示是否上下架
		if (delStatus.equals(ProductDelStatusEnum.PROD_NORMAL.getValue())) {
			//状态：【0：下架 1：上架】'
			if (status.equals(ProductStatusEnum.PROD_ONLINE.getValue())) {
				sb.append("上线");
			} else {
				sb.append("下线");
			}
		}
		if (delStatus.equals(ProductDelStatusEnum.PROD_DELETE.getValue())) {
			sb.append("商家删除");
		}
		if (delStatus.equals(ProductDelStatusEnum.PROD_SHOP_DELETE.getValue())) {
			sb.append("商家永久删除");
		}
		return sb.toString();
	}


}
