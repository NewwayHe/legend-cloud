/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.enums.OrderRefundSouceEnum;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategy;
import com.legendshop.product.api.StockApi;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 预售订单退款策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class PreSaleConfirmRefundStrategy extends BaseConfirmRefundStrategy implements ConfirmRefundStrategy {

	@Autowired
	private StockApi stockApi;

	@Getter
	@Setter
	private OrderTypeEnum orderTypeEnum = OrderTypeEnum.PRE_SALE;


	@Override
	public R confirmRefund(ConfirmRefundDTO confirmRefundDTO) {
		log.info("进入预售订单退款策略, params: {}", JSONUtil.toJsonStr(confirmRefundDTO));
		return super.confirmRefund(confirmRefundDTO);
	}

	@Override
	public void specialBusiness(OrderRefundReturn orderRefundReturn) {

	}

	@Override
	protected void releaseInventory(OrderItem orderItem, Long activityId) {
		stockApi.makeUpStocks(orderItem.getSkuId(), orderItem.getBasketCount());
	}

	@Override
	protected void sendMessage(OrderRefundReturn orderRefundReturn) {
		if (OrderRefundSouceEnum.SHOP.value().equals(orderRefundReturn.getRefundSource())) {
			//发送平台确认退款站内信给商家
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//微信 first.DATA，可自定义
			MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "   ", "black");
			MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, orderRefundReturn.getProductName(), "black");
			MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, orderRefundReturn.getOrderMoney() + "元", "black");
			MsgSendParamDTO KEYWORD3 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD3, orderRefundReturn.getCreateTime().toString(), "black");
			MsgSendParamDTO KEYWORD4 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD4, orderRefundReturn.getRefundSn(), "black");
			//remark商家自定义
			MsgSendParamDTO REMARK = new MsgSendParamDTO(MsgSendParamEnum.REMARK, " ", "black");
			msgSendParamDTOS.add(first);
			msgSendParamDTOS.add(KEYWORD1);
			msgSendParamDTOS.add(KEYWORD2);
			msgSendParamDTOS.add(KEYWORD3);
			msgSendParamDTOS.add(KEYWORD4);
			msgSendParamDTOS.add(REMARK);

			//站内信
			MsgSendParamDTO orderNumberDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, orderRefundReturn.getRefundSn(), "black");
			MsgSendParamDTO reasonDTO = new MsgSendParamDTO(MsgSendParamEnum.REASON, orderRefundReturn.getRefundSn(), "black");
			msgSendParamDTOS.add(orderNumberDTO);
			msgSendParamDTOS.add(reasonDTO);

			//未设置跳转

			//发送平台确认退款站内信给用户
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{orderRefundReturn.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_REFUND)
					.setSysParamNameEnum(SysParamNameEnum.ORDER_REFUND_TO_USER)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderRefundReturn.getId())
			);
		} else {
			super.sendMessage(orderRefundReturn);
		}
	}
}
