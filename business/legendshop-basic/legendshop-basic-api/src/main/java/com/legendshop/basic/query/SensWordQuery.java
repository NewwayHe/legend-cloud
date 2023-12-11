/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 敏感字过滤表(SensWord)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-12-22 15:04:31
 */
@Data
public class SensWordQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -87860356429131762L;

	/**
	 * 关键字
	 */
	@Schema(description = "关键字")
	private String words;

}
