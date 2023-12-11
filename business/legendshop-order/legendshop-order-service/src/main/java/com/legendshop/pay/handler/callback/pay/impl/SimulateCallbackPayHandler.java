/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.pay.impl;

import cn.hutool.core.bean.BeanUtil;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.config.sys.params.SimulatePayConfigDTO;
import com.legendshop.common.util.SignerHelper;
import com.legendshop.pay.dto.PayConfigDTO;
import com.legendshop.pay.dto.PayNotifyDTO;
import com.legendshop.pay.dto.SimulatePayNotifyDTO;
import com.legendshop.pay.handler.callback.pay.AbstractCallbackPayHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 */
@Component(value = "SIMULATE_PAY")
public class SimulateCallbackPayHandler extends AbstractCallbackPayHandler {

	private SimulatePayConfigDTO simulatePayConfigDTO;

	public SimulatePayConfigDTO getSimulatePayConfigDTO() {
		if (null != simulatePayConfigDTO) {
			return simulatePayConfigDTO;
		}
		PayConfigDTO config = config(SysParamNameEnum.SIMULATE_PAY.name());
		this.simulatePayConfigDTO = BeanUtil.mapToBean(config.getMap(), SimulatePayConfigDTO.class, true);
		return simulatePayConfigDTO;
	}

	@Override
	public Map<String, String> getNotifyParamMap(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> requestParams = request.getParameterMap();
		Map<String, String> params = new HashMap<>(16);
		for (String name : requestParams.keySet()) {
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}

	@Override
	public boolean verifyNotifyParamMap(Map<String, String> notifyParamMap) {
		SimulatePayConfigDTO payConfig = getSimulatePayConfigDTO();
		if (null == payConfig) {
			return false;
		}
		// 参数属性转换
		SimulatePayNotifyDTO requestDTO = BeanUtil.mapToBean(notifyParamMap, SimulatePayNotifyDTO.class, true);
		return SignerHelper.checkSign(BeanUtil.beanToMap(requestDTO), payConfig.getKey());
	}

	@Override
	public PayNotifyDTO notifyParamsToBean(Map<String, String> notifyMap) {
		PayNotifyDTO payNotify = new PayNotifyDTO();
		SimulatePayNotifyDTO requestDTO = BeanUtil.mapToBean(notifyMap, SimulatePayNotifyDTO.class, true);
		BeanUtils.copyProperties(requestDTO, payNotify);
		payNotify.setPayType(SysParamNameEnum.SIMULATE_PAY.name());
		return payNotify;
	}

	@Override
	public String success() {
		return "success";
	}

	@Override
	public String fail() {
		return "fail";
	}
}
