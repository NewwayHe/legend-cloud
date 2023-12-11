/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * (Advertise)Query分页查询对象
 *
 * @author legendshop
 * @since 2022-04-27 15:23:38
 */
@Data
public class AdvertiseQuery extends PageParams {


	@Schema(description = "广告id")
	private Long id;

	@Schema(description = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@Schema(description = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 当前时间
	 */
	@Schema(description = "当前时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date time;

	/**
	 * 渠道来源
	 */
	@Schema(description = "渠道来源")
	private String source;

	/**
	 * 渠道来源
	 */
	@Schema(description = "新增广告渠道来源")
	private List sourceList;

	/**
	 * 广告标题
	 */
	@Schema(description = "广告标题")
	private String title;

	/**
	 * 状态
	 */
	@Schema(description = "广告状态:0未开始,10开始,20暂停,30结束")
	private Integer status;

	/**
	 * 广告投放页面
	 */
	@Schema(description = "广告投放页面")
	private String advertisePage;
}
