/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.refund;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.StringRedisUtil;
import com.legendshop.pay.dto.PayConfigDTO;
import com.legendshop.pay.dto.PayRefundSettlementDTO;
import com.legendshop.pay.dto.RefundNotifyDTO;
import com.legendshop.pay.enums.PayRefundStateEnum;
import com.legendshop.pay.service.PayRefundSettlementService;
import com.legendshop.pay.service.PaySystemConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractCallbackRefundHandler implements CallbackRefundHandler {

	@Autowired
	protected StringRedisUtil stringRedisUtil;

	@Autowired
	protected PaySystemConfigService systemConfigService;

	@Autowired
	protected PayRefundSettlementService refundSettlementService;

	protected final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String handler(HttpServletRequest request, HttpServletResponse response) {
		try {
			log.info(" [ CallbackRefundHandler ]  退款异步回调处理开始 --------------> ");
			// 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
			Map<String, String> notifyParamMap = getNotifyParamMap(request, response);
			log.info(" [ CallbackRefundHandler ]  异步回调 response : {}  ", this.objectMapper.writeValueAsString(notifyParamMap));
			// 2. 验签
			if (!verifyNotifyParamMap(notifyParamMap)) {
				log.error(" [ CallbackRefundHandler ]  异步回调验签失败！ ");
				return fail();
			}
			// 3. 转Bean
			RefundNotifyDTO refundNotifyDTO = notifyParamsToBean(notifyParamMap);
			Boolean paymentLock = stringRedisUtil.paymentLock(StringRedisUtil.PAY_CALLBACK_LOCK_KEY_PREFIX, refundNotifyDTO.getExternalRefundSn());
			if (!paymentLock) {
				return fail();
			}
			// 4. 处理退款结算单
			PayRefundSettlementDTO refundSettlement = refundSettlementHandler(refundNotifyDTO);
			// 5. 全部退款回调验证
			this.refundSettlementService.refreshRefundStatus(refundSettlement.getRefundSn());
		} catch (Exception e) {
			log.error("", e);
			return e.toString();
		}
		return success();
	}

	/**
	 * 完结退款结算单
	 */
	protected PayRefundSettlementDTO refundSettlementHandler(RefundNotifyDTO notifyDTO) {
		PayRefundSettlementDTO refundSettlement = this.refundSettlementService.getByExternalRefundSn(notifyDTO.getExternalRefundSn());
		if (null == refundSettlement) {
			log.error(" [ CallbackRefundHandler ]  异步回调  退款记录查询错误！ 第三方回调业务单号：{}， 内部退款单号：{}", notifyDTO.getExternalRefundSn(), notifyDTO.getRefundSn());
			throw new BusinessException("notify callback refund find error");
		}

		refundSettlement.setState(PayRefundStateEnum.SUCCESS.getCode());
		if (!notifyDTO.getRefundStatus()) {
			refundSettlement.setState(PayRefundStateEnum.FAIL.getCode());
			refundSettlement.setResultDesc("退款回调失败，请联系系统管理员进行处理！");
		}
		refundSettlement.setUpdateTime(new Date());

		this.refundSettlementService.update(refundSettlement);
		return refundSettlement;
	}

	public PayConfigDTO config(String payCode) {
		return this.systemConfigService.getConfig(payCode);
	}


	/**
	 * 获取所有请求的参数，封装成Map集合 并且验证是否被篡改
	 *
	 * @param request  the request
	 * @param response the response
	 * @return Map
	 */
	public abstract Map<String, String> getNotifyParamMap(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 验签
	 */
	public abstract boolean verifyNotifyParamMap(Map<String, String> notifyParamMap);

	/**
	 * 通知参数Map转为Bean
	 */
	public abstract RefundNotifyDTO notifyParamsToBean(Map<String, String> notifyMap);

	/**
	 * 成功响应
	 */
	public abstract String success();

	/**
	 * 失败响应
	 */
	public abstract String fail();
}
