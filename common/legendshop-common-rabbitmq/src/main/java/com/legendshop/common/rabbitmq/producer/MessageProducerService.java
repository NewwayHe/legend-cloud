/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.producer;

/**
 * 推送消息生产者
 *
 * @author legendshop
 */
public interface MessageProducerService {


	/**
	 * 批量发送消息 List<MsgSendDTO>
	 *
	 * @param msgSendDtoListJsonStr
	 */
	void sendMessage(String msgSendDtoListJsonStr);
}
