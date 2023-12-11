/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.callback;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.common.logistics.kuaidi100.response.SubscribeResp;
import com.legendshop.order.service.OrderRefundReturnService;
import com.legendshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器
 *
 * @author legendshop
 */
@RestController
@Tag(name = "订单回调接口")
@RequestMapping(value = "/orderPoll", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class OrderCallBack {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRefundReturnService orderRefundReturnService;

	@Operation(summary = "通用物流订阅回调接口", description = "通用物流订阅回调接口")
	@RequestMapping(value = "/callBack")
	SubscribeResp pollCallBack(HttpServletRequest request) {
		String param = request.getParameter("param");
		log.info("接收的参数是：param{}", param);
		SubscribeResp subscribeResp = new SubscribeResp();
		try {
			SubscribePushParamResp subscribePushParamResp = new Gson().fromJson(param, SubscribePushParamResp.class);
			log.info("通用物流订阅回调，参数 ： " + JSONUtil.toJsonStr(subscribePushParamResp));
			if (orderService.pollCallBack(subscribePushParamResp)) {
				subscribeResp.setResult(Boolean.TRUE);
				subscribeResp.setReturnCode("200");
				subscribeResp.setMessage("成功");
			} else {
				subscribeResp.setResult(Boolean.FALSE);
				subscribeResp.setReturnCode("500");
				subscribeResp.setMessage("失败");
			}
			return subscribeResp;
		} catch (Exception ex) {

			subscribeResp.setResult(Boolean.TRUE);
			subscribeResp.setReturnCode("200");
			subscribeResp.setMessage("成功");
		}
		return subscribeResp;
	}

	@Operation(summary = "通用物流订阅回调接口", description = "通用物流订阅回调接口")
	@RequestMapping(value = "/refund/callBack")
	SubscribeResp refundCallBack(HttpServletRequest request) {
		String param = request.getParameter("param");
		log.info("接收的参数是：param{}", param);
		SubscribeResp subscribeResp = new SubscribeResp();
		try {
			SubscribePushParamResp subscribePushParamResp = new Gson().fromJson(param, SubscribePushParamResp.class);
			log.info("通用物流订阅回调，参数 ： " + JSONUtil.toJsonStr(subscribePushParamResp));
			if (orderRefundReturnService.refundCallBack(subscribePushParamResp)) {
				subscribeResp.setResult(Boolean.TRUE);
				subscribeResp.setReturnCode("200");
				subscribeResp.setMessage("成功");
			} else {
				subscribeResp.setResult(Boolean.FALSE);
				subscribeResp.setReturnCode("500");
				subscribeResp.setMessage("失败");
			}
			return subscribeResp;
		} catch (Exception ex) {
			subscribeResp.setResult(Boolean.FALSE);
			subscribeResp.setReturnCode("500");
			subscribeResp.setMessage("失败");
		}
		return subscribeResp;
	}

}
