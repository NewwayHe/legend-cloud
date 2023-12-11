/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund.wallet;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.dao.PayRefundSettlementDao;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.RefundRequestDTO;
import com.legendshop.pay.dto.RefundResultItemDTO;
import com.legendshop.pay.dto.UserWalletOperationDTO;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import com.legendshop.pay.handler.refund.RefundHandler;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.pay.service.UserWalletBusinessService;
import com.legendshop.pay.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("WALLET_PAY_REFUND")
public class WalletRefundHandler implements RefundHandler {

	private final UserWalletService userWalletService;
	private final PaySettlementService paySettlementService;
	private final PayRefundSettlementDao payRefundSettlementDao;
	private final UserWalletBusinessService userWalletBusinessService;

	@Override
	public R<RefundResultItemDTO> refund(RefundRequestDTO refundItem) {
		String refundSn = refundItem.getRefundSn();

		PaySettlementDTO settlement = this.paySettlementService.getBySn(refundItem.getNumber());
		if (null == settlement) {
			log.error("对应支付记录查询失败，settlementSn：{}", refundItem.getNumber());
			return R.fail("对应支付记录查询失败！");
		}
		UserWalletOperationDTO dto = new UserWalletOperationDTO();
		dto.setUserId(settlement.getUserId());
		dto.setBusinessId(refundItem.getId());
		dto.setBusinessType(WalletBusinessTypeEnum.REFUND_COMPENSATION);
		dto.setOperationType(WalletOperationTypeEnum.ADDITION);
		dto.setRemarks("订单退款成功, 退款金额: " + refundItem.getRefundAmount() + ", 退款单号: " + refundItem.getRefundSn());
		dto.setAmount(refundItem.getRefundAmount());

		if (null != refundItem.getRefundAmount() && refundItem.getRefundAmount().compareTo(BigDecimal.ZERO) > 0) {
			R<Long> recordUpdate = userWalletBusinessService.synchronizeAvailableRecordUpdate(dto);
			if (TransactionSynchronizationManager.isSynchronizationActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						userWalletBusinessService.synchronizeNotify(recordUpdate.getData());
					}
				});
			} else {
				userWalletBusinessService.synchronizeNotify(recordUpdate.getData());
			}

		} else {
			log.info("退款金额有误{}", refundItem.getRefundAmount());
		}
		// 余额退款，直接成功
		RefundResultItemDTO refundResultItem = new RefundResultItemDTO();

		refundResultItem.setExternalRefundSn(refundItem.getRefundSn());
		refundResultItem.setPayTypeId(PayTypeEnum.WALLET_PAY.name());
		refundResultItem.setAmount(refundItem.getRefundAmount());

		return R.ok(refundResultItem);
	}

	@Override
	public boolean isSupport(String payType, String paySource) {
		return PayTypeEnum.WALLET_PAY.name().equals(payType);
	}

	@Override
	public R<Void> queryRefundResult(RefundRequestDTO refundItem) {
		return R.ok();
	}
}
