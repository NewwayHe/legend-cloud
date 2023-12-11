/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 营销活动运营数据统计
 *
 * @author legendshop
 */
@Data
@Schema(description = "营销活动运营数据统计")
public class OperateStatisticsBO implements Serializable {

	private static final long serialVersionUID = -6379631165363809931L;

	/**
	 * 交易总金额
	 */
	@Schema(description = "交易总金额")
	private Double totalAmount;

	/**
	 * 成功交易金额
	 */
	private Double succeedAmount;

	/**
	 * 成功数量
	 */
	private Integer succeedCount;

	/**
	 * 拼团----待成团
	 */
	private Integer waitFightCount;

	/**
	 * 活动参与人数
	 */
	@Schema(description = "参加人数")
	private Integer participants;

	/**
	 * 团购 ---成团人数
	 **/
	private Integer peopleCount;

	@Schema(description = "交易总量")
	private Integer totalOrderNum;

	/**
	 * 活动ID
	 */
	private Long activityId;

}
