/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.OperatorLogDao;
import com.legendshop.basic.entity.OperatorLog;
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.basic.service.OperatorLogService;
import com.legendshop.basic.service.convert.OperatorLogConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.OperatorLogDTO;
import com.legendshop.common.core.enums.EventTypeEnum;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.user.dto.AdminUserDTO;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作日志表(OperatorLog)表服务实现类
 *
 * @author legendshop
 * @since 2023-08-29 14:13:57
 */
@Service
@Slf4j
public class OperatorLogServiceImpl implements OperatorLogService {

	@Autowired
	private OperatorLogDao operatorLogDao;

	@Autowired
	private OperatorLogConverter converter;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.OPERATOR_LOG_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.OPERATOR_LOG_EXCHANGE), key = {AmqpConst.OPERATOR_LOG_ROUTING_KEY}
	))
	@SneakyThrows
	public void systemListener(OperatorLogDTO operatorLogDTO, Message message, Channel channel) {
		Long id = operatorLogDao.save(converter.from(operatorLogDTO));
		if (ObjectUtil.isNull(id)) {
			log.warn("操作日志保存失败");
			return;
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	@Override
	public R save(OperatorLogDTO operatorLogDTO) {
		return R.ok(this.operatorLogDao.save(converter.from(operatorLogDTO)));
	}

	@Override
	public R<PageSupport<OperatorLogDTO>> page(OperatorLogQuery operatorLogQuery) {
		PageSupport<OperatorLogDTO> page = converter.page(operatorLogDao.getOperatorLogPage(operatorLogQuery));
		if (ObjectUtil.isNotEmpty(page.getResultList())) {
			page.getResultList().forEach(operatorLog -> {
				operatorLog.setEventType(EventTypeEnum.getTypeName(operatorLog.getEventType()));
			});
		}
		return R.ok(page);
	}

	@Override
	public R<OperatorLogDTO> getById(Long id) {
		OperatorLog operatorLog = operatorLogDao.getById(id);

		if (ObjectUtil.isEmpty(operatorLog)) {
			return R.fail("当前操作日志不存在！");
		}

		OperatorLogDTO operatorLogDTO = converter.to(operatorLog);
		operatorLogDTO.setAfterModificationObject(JSONUtil.toBean(operatorLog.getAfterModification(), AdminUserDTO.class));
		operatorLogDTO.setBeforeModificationObject(JSONUtil.toBean(operatorLog.getBeforeModification(), AdminUserDTO.class));
		return R.ok(operatorLogDTO);
	}
}
