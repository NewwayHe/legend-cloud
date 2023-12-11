/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 常用分类(CategoryComm)实体类
 *
 * @author legendshop
 */
@Data
public class ShopCategoryCommDTO implements Serializable {


	private static final long serialVersionUID = -5050315813792308875L;

	private Long id;


	private Long shopId;


	private Long categoryId;


	private Date recDate;

}
