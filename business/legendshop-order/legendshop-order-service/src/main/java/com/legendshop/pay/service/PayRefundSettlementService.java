/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PayRefundSettlementDTO;

import java.util.List;

/**
 * (PayRefundSettlement)表服务接口
 *
 * @author legendshop
 * @since 2021-05-12 18:09:16
 */
public interface PayRefundSettlementService {

	Long save(PayRefundSettlementDTO dto);

	List<Long> save(List<PayRefundSettlementDTO> dtoList);

	/**
	 * 根据退款单号，查询发起退款的退款记录
	 */
	List<PayRefundSettlementDTO> queryByRefundSn(String refundSn);

	/**
	 * 根据退款单号，查询发起退款的退款记录
	 */
	PayRefundSettlementDTO getByRefundSnAndType(String refundSn, String refundType);

	/**
	 * 根据第三方系统退款单号
	 */
	PayRefundSettlementDTO getByExternalRefundSn(String externalRefundSn);

	int update(List<PayRefundSettlementDTO> refundSettlementList);

	int update(PayRefundSettlementDTO refundSettlement);

	/**
	 * 通过退款单号，查询退款状态，并刷新退款处理状态
	 */
	R<Void> refreshQuery(String refundSn);

	/**
	 * 重新发起退款
	 */
	R<Void> reApply(String refundSn);

	/**
	 * 判断refundSn是否全部完成退款，是否需要更新退款单状态
	 */
	R<Void> refreshRefundStatus(String refundSn);

	/**
	 * 根据支付退款号获取支付退款信息
	 *
	 * @param payRefundSn
	 * @return
	 */
	PayRefundSettlementDTO getByPayRefundSn(String payRefundSn);
}
