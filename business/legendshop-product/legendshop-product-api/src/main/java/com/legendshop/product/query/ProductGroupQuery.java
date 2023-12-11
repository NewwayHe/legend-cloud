/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * (ProdGroup)实体类
 *
 * @author legendshop
 */
@Schema(description = "商品分组查询参数")
@Data
public class ProductGroupQuery extends PageParams {


	/**
	 * 商品分组名称
	 */
	@Schema(description = "商品分组名称")
	private String name;


	/**
	 * 分组类型 0:系统定义 1:自定义
	 */
	@Schema(description = "分组类型 0:系统定义 1:自定义名称")
	private Integer type;


	/**
	 * 创建/修改时间
	 */
	@Schema(description = "创建/修改时间")
	private Date createTime;

}
