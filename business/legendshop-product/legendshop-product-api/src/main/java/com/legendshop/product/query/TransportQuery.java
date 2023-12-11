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

/**
 * 运费模板DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "运费模板搜索参数")
public class TransportQuery extends PageParams {

	private static final long serialVersionUID = 8529072663546409423L;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 运费名称
	 */
	@Schema(description = "模板名称")
	private String transName;

	/**
	 * 计费方式
	 */
	@Schema(description = "计费方式 1、按件数 2、按重量 3、按体积 4、固定运费")
	private String transType;

	@Schema(description = "查询条件 1、按重/体积/件计算运费 2、固定运费 3、包邮")
	private Integer transQueryType;

}
