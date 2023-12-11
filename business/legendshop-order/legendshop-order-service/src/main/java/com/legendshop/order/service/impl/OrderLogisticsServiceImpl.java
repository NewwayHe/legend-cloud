/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.logistics.kuaidi100.contant.QueryTrackStatusEnum;
import com.legendshop.common.logistics.kuaidi100.request.QueryTrackParam;
import com.legendshop.common.logistics.kuaidi100.request.QueryTrackReq;
import com.legendshop.common.logistics.kuaidi100.response.QueryTrackData;
import com.legendshop.common.logistics.kuaidi100.response.QueryTrackResp;
import com.legendshop.common.logistics.properties.LogisticsProperties;
import com.legendshop.common.logistics.service.QueryTrackService;
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dao.OrderLogisticsDao;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.dto.RefundReturnLogisticsDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.service.LogisticsCompanyService;
import com.legendshop.order.service.OrderLogisticsService;
import com.legendshop.order.service.convert.OrderConverter;
import com.legendshop.order.service.convert.OrderLogisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单物流服务实现
 *
 * @author legendshop
 */
@Service
public class OrderLogisticsServiceImpl implements OrderLogisticsService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderConverter orderConverter;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Autowired
	private OrderLogisticsDao orderLogisticsDao;

	@Autowired
	private QueryTrackService queryTrackService;

	@Autowired
	private OrderLogisticsConverter orderLogisticsConverter;

	@Autowired
	private LogisticsCompanyService logisticsCompanyService;

	@Override
	public OrderLogisticsDTO getByOrderId(Long orderId) {
		return orderLogisticsConverter.to(orderLogisticsDao.getByOrderId(orderId));
	}

	@Override
	public void update(OrderLogisticsDTO orderLogisticsDTO) {
		orderLogisticsDao.update(orderLogisticsConverter.from(orderLogisticsDTO));
	}

	@Override
	public Long save(OrderLogisticsDTO data) {
		return this.orderLogisticsDao.save(this.orderLogisticsConverter.from(data));
	}

	@Override
	public R<RefundReturnLogisticsDTO> queryRefundReturnLogisticsInformation(OrderRefundReturnBO orderRefundReturnBO) {

		if (ObjectUtil.isEmpty(orderRefundReturnBO.getLogisticsId()) || ObjectUtil.isEmpty(orderRefundReturnBO.getLogisticsNumber())) {
			return R.fail();
		}
		OrderLogisticsDTO orderLogisticsDTO = this.getByOrderId(orderRefundReturnBO.getOrderId());
		if (ObjectUtil.isNull(orderLogisticsDTO)) {
			return R.fail("订单物流信息不存在");
		}
		// 获取快递100参数配置
		ObjectMapper mapper = new ObjectMapper();
		LogisticsProperties logisticsProperties = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.LOGISTICS.name(), LogisticsProperties.class).getData(), LogisticsProperties.class);

		if (ObjectUtil.isNull(logisticsProperties) || ObjectUtil.isNull(logisticsProperties.getCustomer())
				|| ObjectUtil.isNull(logisticsProperties.getAppKey())) {
			return R.fail("快递100参数配置错误");
		}
		QueryTrackReq queryTrackReq = new QueryTrackReq();
		queryTrackReq.setCustomer(logisticsProperties.getCustomer());
		QueryTrackParam queryTrackParam = new QueryTrackParam();
		queryTrackParam.setCom(orderRefundReturnBO.getLogisticsCompanyCode());
		queryTrackParam.setNum(orderRefundReturnBO.getLogisticsNumber());
		queryTrackReq.setParam(queryTrackParam);
		queryTrackReq.setSign(logisticsProperties.getAppKey());
		QueryTrackResp queryTrackResp = queryTrackService.queryTrack(queryTrackReq);

		RefundReturnLogisticsDTO refundReturnLogisticsDTO = new RefundReturnLogisticsDTO();
		//获得 订单物流信息 id
		refundReturnLogisticsDTO.setId(orderLogisticsDTO.getId());
		refundReturnLogisticsDTO.setLogisticsCompany(orderRefundReturnBO.getLogisticsCompanyName());
		refundReturnLogisticsDTO.setLogisticsCompanyId(orderRefundReturnBO.getLogisticsId());
		refundReturnLogisticsDTO.setShipmentNumber(orderRefundReturnBO.getLogisticsNumber());
		refundReturnLogisticsDTO.setTrackingInformation(JSONUtil.toJsonStr(queryTrackResp.getData()));
		return R.ok(refundReturnLogisticsDTO);
	}

	@Override
	public R<OrderLogisticsDTO> queryLogisticsInformation(String orderNumber) {
		Order order = orderDao.getByOrderNumber(orderNumber);
		return queryLogisticsInformation(orderConverter.convert2OrderBO(order));
	}

	@Override
	public R<OrderLogisticsDTO> queryLogisticsInformation(OrderBO orderBO) {
		if (null == orderBO) {
			return R.ok();
		}

		OrderLogisticsDTO orderLogisticsDTO = this.getByOrderId(orderBO.getId());
		if (ObjectUtil.isNull(orderLogisticsDTO)) {
			return R.fail("订单物流信息不存在");
		}

		// 判断是否需要再从快递100上获取新数据
		if (QueryTrackStatusEnum.SIGN_FOR.getValue().equals(orderLogisticsDTO.getLogisticsStatus())) {
			return R.ok(orderLogisticsDTO);
		}

		// 获取快递100参数配置
		ObjectMapper mapper = new ObjectMapper();
		LogisticsProperties logisticsProperties = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.LOGISTICS.name(), LogisticsProperties.class).getData(), LogisticsProperties.class);

		if (ObjectUtil.isNull(logisticsProperties) || ObjectUtil.isNull(logisticsProperties.getCustomer())
				|| ObjectUtil.isNull(logisticsProperties.getAppKey())) {
			return R.ok(orderLogisticsDTO, "快递100参数配置错误");
		}
		if (!OrderStatusEnum.CONSIGNMENT.getValue().equals(orderBO.getStatus())
				&& !OrderStatusEnum.TAKE_DELIVER.getValue().equals(orderBO.getStatus())
				&& !OrderStatusEnum.SUCCESS.getValue().equals(orderBO.getStatus())) {
			return R.ok(orderLogisticsDTO, "订单状态异常，未发货不支持查询物流信息");
		}
		QueryTrackReq queryTrackReq = new QueryTrackReq();
		queryTrackReq.setCustomer(logisticsProperties.getCustomer());
		QueryTrackParam queryTrackParam = new QueryTrackParam();
		queryTrackParam.setCom(orderLogisticsDTO.getCompanyCode());
		queryTrackParam.setNum(orderLogisticsDTO.getShipmentNumber());
		queryTrackReq.setParam(queryTrackParam);
		queryTrackReq.setSign(logisticsProperties.getAppKey());
		QueryTrackResp queryTrackResp = queryTrackService.queryTrack(queryTrackReq);
		List<QueryTrackData> trackDataList = queryTrackResp.getData();
		orderLogisticsDTO.setTrackingInformation(JSONUtil.toJsonStr(trackDataList));

		// 判断是否需要更新订单状态
		if (QueryTrackStatusEnum.SIGN_FOR.getValue().equals(queryTrackResp.getState())) {
			QueryTrackData queryTrackData = trackDataList.get(0);
			// 如果存在已签收，则更新订单状态，并保存物流信息
			if (OrderStatusEnum.CONSIGNMENT.getValue().equals(orderBO.getStatus())) {
				orderDao.updateTakeDeliverStatus(orderBO.getId(), DateUtil.parseDateTime(queryTrackData.getFtime()));
				orderBO.setStatus(OrderStatusEnum.TAKE_DELIVER.getValue());
				orderBO.setLogisticsReceivingTime(DateUtil.parseDateTime(queryTrackData.getFtime()));
			}
		}
		// 将物流记录保存起来
		orderLogisticsDTO.setLogisticsStatus(queryTrackResp.getState());
		this.update(orderLogisticsDTO);
		return R.ok(orderLogisticsDTO);
	}


	/**
	 * 快递100查询测试
	 * @param args
	 */
//	public static void main(String[] args) {
//
//		// 授权码
//		String customer = "F1610C08E8D4A82D7B53C9F217C72E0x";
//		// 授权码key
//		String key = "FZlGeZwE69x";
//		// 物流公司编码 示例： yunda  yuantong  shunfeng  zhongtong
//		String companyCode = "yunda";
//		// 物流单号
//		String shipmentNumber = "4307988382419";
//	}

}
