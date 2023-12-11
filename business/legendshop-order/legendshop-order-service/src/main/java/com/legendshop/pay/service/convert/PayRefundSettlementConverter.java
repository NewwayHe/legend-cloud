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
import com.legendshop.pay.dto.PayRefundSettlementDTO;
import com.legendshop.pay.entity.PayRefundSettlement;
import org.mapstruct.Mapper;

/**
 * (PayRefundSettlement)转换器
 *
 * @author legendshop
 * @since 2021-05-12 18:09:17
 */
@Mapper
public interface PayRefundSettlementConverter extends BaseConverter<PayRefundSettlement, PayRefundSettlementDTO> {
}

