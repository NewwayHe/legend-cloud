/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * (AdvertiseCount)DTO
 *
 * @author legendshop
 * @since 2022-04-27 17:21:40
 */
@Data
@Schema(description = "广告数据统计")
public class AdvertiseCountDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 320263857008992649L;

	private Long id;

	/**
	 * 广告id
	 */
	private Long advertiseId;


	/**
	 * 投放人数
	 */
	private BigDecimal putPeople;

	/**
	 * 投放次数
	 */
	private BigDecimal putCount;

	/**
	 * 点击人数
	 */
	private BigDecimal clickPeople;

	/**
	 * 点击次数
	 */
	private BigDecimal clickCount;

	/**
	 * 新增投放人数
	 */
	private BigDecimal newPutPeople;

	/**
	 * 新增投放次数
	 */
	private BigDecimal newPutCount;

	/**
	 * 新增点击人数
	 */
	private BigDecimal newClickPeople;

	/**
	 * 新增点击次数
	 */
	private BigDecimal newClickCount;

	/**
	 * 渠道来源
	 */
	private String source;

	/**
	 * 渠道来源
	 */
	private List sourceList;

	/**
	 * 日期
	 */
	private Date createTime;

	/**
	 * 用户id
	 */
	private Long userId;

	@Schema(description = "时间")
	private String time;

	/**
	 * 转化率
	 */
	@Schema(description = "转化率")
	private BigDecimal inversionRate;

	private String userKey;

	private List<AdvertiseCountDTO> AdvertiseCountList;
}
