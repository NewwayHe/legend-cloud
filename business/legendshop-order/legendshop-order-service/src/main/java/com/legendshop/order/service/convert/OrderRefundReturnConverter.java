/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.ApplyRefundReturnItemDTO;
import com.legendshop.order.dto.OrderRefundReturnDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.excel.OrderRefundExportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface OrderRefundReturnConverter extends BaseConverter<OrderRefundReturn, OrderRefundReturnDTO> {

	OrderRefundReturnBO converter2BO(OrderRefundReturn orderRefundReturn);


	List<ApplyRefundReturnItemDTO> converter2ApplyRefundReturnItemDTO(List<OrderItem> orderItemList);

	@Mappings({
			@Mapping(source = "id", target = "id"),
			@Mapping(source = "pic", target = "pic"),
			@Mapping(target = "status", expression = "java(com.legendshop.order.service.convert.mapper.OrderRefundReturnMapper.convert2Status(orderItemList))"),
			@Mapping(source = "actualAmount", target = "refundAmount")
	})
	ApplyRefundReturnItemDTO converter2ApplyRefundReturnItemDTO(OrderItem orderItemList);


	OrderRefundExportDTO converter2ExportDTO(OrderRefundReturnBO bo);
}
