/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.listener;

import com.legendshop.basic.dao.ExportExcelTaskDao;
import com.legendshop.common.excel.dto.ExportExcelUpdateDTO;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 导出Excel数据任务
 *
 * @author legendshop
 * @create: 2022-04-19 17:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExportExcelTaskListener {

	@Autowired
	private ExportExcelTaskDao exportExcelTaskDao;

	/**
	 * 更新
	 *
	 * @param updateDTO
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.SYSTEM_EXPORT_EXCEL_UPDATE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.SYSTEM_EXPORT_EXCEL_EXCHANGE),
			key = AmqpConst.SYSTEM_EXPORT_EXCEL_UPDATE_ROUTING_KEY))
	public void updateExportExcelTask(ExportExcelUpdateDTO updateDTO, Message message, Channel channel) throws IOException {
		log.info("收到完成通知，通过fileName更新导出任务");
		exportExcelTaskDao.updateStatus(updateDTO.getFileName(), updateDTO.getStatus().getValue(), updateDTO.getUrl(), updateDTO.getRemark());

		//更新ls_export_excel_task
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}
}
