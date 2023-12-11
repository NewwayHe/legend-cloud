/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "时间查询")
public class ActivityDataQuery {

	@Schema(description = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;

	@Schema(description = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;

	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 升降序
	 */
	@Schema(description = "升降序:true 升序 false 降序")
	private Boolean order;

	/**
	 * 排序方式
	 */
	@Schema(description = "排序方式")
	private String prop;

	@JsonIgnore
	private String type;

	@JsonIgnore
	private List<Long> ids;

	@JsonIgnore
	private List<String> idType;

}
