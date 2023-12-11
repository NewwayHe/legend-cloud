/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.api;

import com.google.gson.Gson;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.SendSmsReq;
import com.legendshop.common.logistics.kuaidi100.response.SendSmsResp;
import com.legendshop.common.logistics.kuaidi100.response.SmsCallbackResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-20 19:00
 */
@Component
public class SendSms {
	private static Logger log = LoggerFactory.getLogger(SendSms.class);

	/**
	 * 发送短信
	 *
	 * @param sendSmsReq
	 * @return
	 */
	public SendSmsResp sendSms(SendSmsReq sendSmsReq) {
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.SEND_SMS_URL, sendSmsReq);
		System.out.println(httpResult.getBody());
		if (httpResult.getBody() != null && httpResult.getStatus() == 200) {
			return new Gson().fromJson(httpResult.getBody(), SendSmsResp.class);
		}
		return null;
	}

	/**
	 * 短信回调接口 例子
	 *
	 * @param
	 * @return: java.util.Map<java.lang.String, java.lang.Boolean>
	 * @author: api.kuaidi100.com
	 * @time: 2020/7/21 10:11
	 */
	public Map<String, Boolean> callback(SmsCallbackResp smsCallbackResp) {
		//建议记录一下这个回调的内容，方便出问题后双方排查问题
		log.debug("快递100短信回调结果|{}", new Gson().toJson(smsCallbackResp));
		Map<String, Boolean> result = new HashMap<>(16);
		if (smsCallbackResp == null) {
			return result;
		}

		result.put("status", Boolean.TRUE);
		return result;
	}
}
