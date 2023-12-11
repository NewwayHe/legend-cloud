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
import com.legendshop.shop.enums.DealTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 平台资金流水(PlateCapitalFlow)Query分页查询对象
 *
 * @author legendshop
 * @since 2020-09-18 17:26:14
 */
@Data
@Schema(description = "平台资金流水查询参数")
public class PlateCapitalFlowQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -53754212807129446L;


	/**
	 * 收支类型
	 * {@link com.legendshop.shop.enums.FlowTypeEnum}
	 */
	@Schema(description = "收支类型 1:收入 2:支出")
	private String flowType;

	/**
	 * 交易类型
	 * {@link DealTypeEnum}
	 */
	@Schema(description = "交易类型  1:商家结算 2:预存款提现 3:订单退款 4:定金退款 5:保证金支付 6:预存款充值 7:订单支付 8:定金支付")
	private String dealType;

	/**
	 * 查询起始时间
	 */
	@Schema(description = "查询起始时间")
	private String startDate;


	/**
	 * 查询结束时间
	 */
	@Schema(description = "查询起始时间")
	private String endDate;
}
