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
import lombok.Data;

import java.util.Date;

/**
 * 属性值(ProdPropValue)实体类
 *
 * @author legendshop
 */
@Data
public class ProductPropertyValueQuery extends PageParams {


	/**
	 * 属性值ID
	 */
	private Long id;


	/**
	 * 属性ID
	 */
	private Long propId;


	/**
	 * 属性值名称
	 */
	private String name;


	/**
	 * 状态。可选值:normal(正常),deleted(删除)
	 */
	private String status;


	/**
	 * 图片路径
	 */
	private String pic;


	/**
	 * 排序
	 */
	private Integer sequence;


	/**
	 * 修改时间
	 */
	private Date modifyDate;


	/**
	 * 记录时间
	 */
	private Date recDate;

}
