/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.api;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.BaseReq;
import com.legendshop.common.logistics.kuaidi100.response.BOrderQueryData;
import com.legendshop.common.logistics.kuaidi100.response.BOrderResp;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-09-17 11:14
 */
@Component
public class BOrder {


	public PrintBaseResp transportCapacity(BaseReq param) throws Exception {
		param.setMethod(ApiInfoConstant.B_ORDER_QUERY_TRANSPORT_CAPACITY_METHOD);
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.B_ORDER_URL, param);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<BOrderQueryData>>() {
			}.getType());
		}
		return null;
	}

	public PrintBaseResp order(BaseReq param) throws Exception {
		param.setMethod(ApiInfoConstant.B_ORDER_SEND_METHOD);
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.B_ORDER_URL, param);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<BOrderResp>>() {
			}.getType());
		}
		return null;
	}

	public PrintBaseResp getCode(BaseReq param) throws Exception {
		param.setMethod(ApiInfoConstant.B_ORDER_CODE_METHOD);
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.B_ORDER_URL, param);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<Map<String, String>>>() {
			}.getType());
		}
		return null;
	}

	public PrintBaseResp cancel(BaseReq param) throws Exception {
		param.setMethod(ApiInfoConstant.B_ORDER_CANCEL_METHOD);
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.B_ORDER_URL, param);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp>() {
			}.getType());
		}
		return null;
	}

}
