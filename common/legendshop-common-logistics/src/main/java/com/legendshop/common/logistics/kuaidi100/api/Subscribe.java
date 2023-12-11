/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.api;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.SubscribeReq;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.common.logistics.kuaidi100.response.SubscribeResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import com.legendshop.common.logistics.kuaidi100.utils.SignUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订阅
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-16 14:38
 */
@Component
public class Subscribe {

	private static Logger log = LoggerFactory.getLogger(Subscribe.class);

	/**
	 * 订阅接口
	 *
	 * @param subscribeReq
	 * @return
	 */
	public SubscribeResp subscribe(SubscribeReq subscribeReq) {
		//向快递100请求快递单订阅
		SubscribeResp subscribeResp = new SubscribeResp();
		log.info("快递单订阅,请求参数：{}", JSONUtil.toJsonStr(subscribeReq));
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.SUBSCRIBE_URL, subscribeReq);
		if (httpResult.getStatus() == 200) {
			log.info("快递单订阅成功，参数：{}", httpResult.getBody());
			subscribeResp = new Gson().fromJson(httpResult.getBody(), SubscribeResp.class);
		} else {
			log.warn("快递单订阅失败，参数：{}", httpResult.getBody());
		}
		return subscribeResp;
	}

	/**
	 * 订阅推送处理(参照)
	 * 订阅成功后，如果该快递单号有轨迹（包括已经签收的单），快递100将会在15分钟-4个小时推送；后面将会4个小时跟踪一次，跟踪到有轨迹变化则推送；
	 * 如果订阅成功后，3天查无结果（录错单/快递公司错了/揽收比较晚），快递100将会推送3天查无结果，可以继续重新订阅。
	 * <p>
	 * 回调接口支持自定义参数,比如订阅时回调地址填写的是 http://www.xxx.com?orderId=1233333
	 * 可以通过下面这种方式获取到orderId： String orderId = request.getParameter("orderId");
	 * <p>
	 * 返回值必须是下面这样的格式，否则快递100将认为该推送失败，快递100将会重试3次该推送，时间间隔35分钟；
	 * 成功结果返回例子： {"result":true,"returnCode":"200","message":"提交成功"}
	 *
	 * @param request
	 * @return: com.kuaidi100.sdk.response.SubscribeResp
	 * @author: api.kuaidi100.com
	 * @time: 2020/7/16 19:48
	 */
	public SubscribeResp callBackUrl(HttpServletRequest request) {
		String param = request.getParameter("param");
		String sign = request.getParameter("sign");
		//建议记录一下这个回调的内容，方便出问题后双方排查问题
		log.debug("快递100订阅推送回调结果|{}|{}", param, sign);
		//订阅时传的salt,没有可以忽略
		String salt = "";
		String ourSign = SignUtils.sign(param + salt);
		SubscribeResp subscribeResp = new SubscribeResp();
		subscribeResp.setResult(Boolean.TRUE);
		subscribeResp.setReturnCode("200");
		subscribeResp.setMessage("成功");
		//加密如果相等，属于快递100推送；否则可以忽略掉当前请求
		if (ourSign.equals(sign)) {
			//TODO 业务处理
			SubscribePushParamResp subscribePushParamResp = new Gson().fromJson(param, SubscribePushParamResp.class);
			return subscribeResp;
		}
		return null;
	}
}
