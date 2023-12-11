/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "装修商品分组BO")
public class ProductGroupBO implements Serializable {


	@Schema(description = "主键ID")
	private Long id;


	@Schema(description = "商品分组名称")
	private String name;


	@Schema(description = "分组类型 0:系统定义 1:自定义")
	private Integer type;


	@Schema(description = "分组条件")
	private String conditional;


	@Schema(description = "组内排序条件")
	private String sort;


	@Schema(description = "商品分组描述")
	private String description;


	@Schema(description = "创建时间")
	private Date createTime;

}
