/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.dto.InvalidOrderSkuDTO;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.vo.SubmitOrderVO;
import com.legendshop.product.dto.ProductItemDTO;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface OrderConverter extends BaseConverter<Order, OrderDTO> {

	/**
	 * SubmitOrderBO to SubmitOrderVO
	 *
	 * @param result
	 * @return
	 */
	R<SubmitOrderVO> convert2SubmitOrderVO(R<ConfirmOrderBO> result);

	/**
	 * 预订单商品列表信息转换成计算运费商品DTO列表
	 *
	 * @param skuList
	 * @return
	 */
	List<TransFeeCalProductDTO> convert2TransFeeCalProductDtoList(List<SubmitOrderSkuDTO> skuList);


	/**
	 * 有效商品 to 失效商品
	 *
	 * @param submitOrderSkuDTO
	 * @return
	 */
	InvalidOrderSkuDTO convert2InvalidOrderSkuDTO(SubmitOrderSkuDTO submitOrderSkuDTO);


	/**
	 * submitOrderSkuDTOS to ProductItemDTO
	 *
	 * @param submitOrderSkuDTOS
	 * @return
	 */
	List<ProductItemDTO> convertProductItemDTO(List<SubmitOrderSkuDTO> submitOrderSkuDTOS);

	/**
	 * order to OrderBO
	 *
	 * @param order
	 * @return
	 */
	OrderBO convert2OrderBO(Order order);
}
