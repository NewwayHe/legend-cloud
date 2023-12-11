/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionQuery
 * @date 2022/3/18 14:02
 * @description： 分销数据查询
 */
@Data
public class DistributionDataQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = 1007831105151460210L;

	@Schema(description = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@Schema(description = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 排序的字段
	 */
	@Schema(description = "排序的字段，把要排序的字段名返回过来")
	private String prop;

	/**
	 * 排序的方向：asc 或者 desc
	 */
	@Schema(description = "排序的方向：asc 或者 desc")
	private String order;
}
