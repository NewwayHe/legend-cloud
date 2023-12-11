/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.basic.entity.AmqpTask;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * 队列任务表(AmqpTask)转换器
 *
 * @author legendshop
 * @since 2022-04-29 14:16:54
 */
@Mapper
public interface AmqpTaskConverter extends BaseConverter<AmqpTask, AmqpTaskDTO> {
}

