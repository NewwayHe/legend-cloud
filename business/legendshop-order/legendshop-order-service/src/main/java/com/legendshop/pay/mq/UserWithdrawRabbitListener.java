/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.mq;

import com.legendshop.basic.api.MessageApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.WalletDetailsStateEnum;
import com.legendshop.pay.handler.withdraw.WithdrawContext;
import com.legendshop.pay.service.UserWalletDetailsService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserWithdrawRabbitListener {


	private final UserWalletDetailsService userWalletDetailsService;

	private final MessageApi messageApi;

	private final WithdrawContext withdrawContext;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = PayAmqpConst.USER_WITHDRAW_QUEUE, durable = "true"),
			exchange = @Exchange(value = PayAmqpConst.PAY_EXCHANGE), key = {PayAmqpConst.USER_WITHDRAW_ROUTING_KEY}
	))
	public void receiveDelay(Long id, Message message, Channel channel) throws IOException {
		log.info("用户提现处理！, {}", id);
		UserWalletDetailsDTO details = this.userWalletDetailsService.getById(id);
		if (null == details) {
			log.error("错误的提现记录！ID：{}", id);
			return;
		}

		String errorMsg = null;
		try {
			R<Void> result = this.withdrawContext.executeWithdraw(null, details.getSerialNo());
			log.info("提现处理完成，响应数据：{}", result);
			if (result.success()) {
				String str = new StringBuffer().append("提现审核通过，预计提现金额")
						.append(details.getAmount().setScale(2, RoundingMode.HALF_DOWN))
						.append("元，实际到账金额")
						.append(details.getAmount().subtract(Optional.ofNullable(details.getWithdrawalCharge()).orElse(BigDecimal.ZERO)).setScale(2, RoundingMode.HALF_DOWN))
						.append("元，手续费")
						.append(Optional.ofNullable(details.getWithdrawalCharge()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN))
						.append("元。")
						.toString();
				this.send(details.getUserId(), details.getUserId(), str);
				return;
			}
			errorMsg = result.getMsg();
		} catch (Exception e) {
			errorMsg = e.getMessage();
		}


		log.error("用户提现失败，错误信息：{}", errorMsg);
		details.setRemarks(errorMsg);
		details.setState(WalletDetailsStateEnum.ABNORMAL.getCode());
		this.userWalletDetailsService.abnormalMsg(id, errorMsg);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	/**
	 * 站内信
	 */
	private void send(Long adminUserId, Long receiverId, String content) {
		messageApi.send(adminUserId, receiverId, content);
	}


}
