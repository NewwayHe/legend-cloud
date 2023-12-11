/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.pay.dto.PaySettlementLogDTO;
import com.legendshop.pay.entity.PaySettlementLog;
import org.mapstruct.Mapper;

/**
 * 支付单日志
 *
 * @author legendshop
 */
@Mapper
public interface PaySettlementLogConverter extends BaseConverter<PaySettlementLog, PaySettlementLogDTO> {
}
