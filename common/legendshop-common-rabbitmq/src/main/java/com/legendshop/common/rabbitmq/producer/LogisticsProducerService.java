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
 * 物流订阅生产者
 *
 * @author legendshop
 */
public interface LogisticsProducerService {

	/**
	 * MQ订阅物流消息
	 *
	 * @param company 物流公司
	 * @param number  物流单号
	 * @return
	 */
	String poll(String company, String number);

	/**
	 * MQ订阅物流消息-用户退货
	 *
	 * @param company 物流公司
	 * @param number  物流单号
	 * @return
	 */
	String refundPoll(String company, String number);

}
