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
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.BaseReq;
import com.legendshop.common.logistics.kuaidi100.request.CloudPrintOldParam;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * 复打
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-20 9:26
 */
@Component
public class CloudPrintOld {

	public PrintBaseResp print(BaseReq<CloudPrintOldParam> cloudPrintOldParamReq) throws Exception {
		String url = String.format(ApiInfoConstant.CLOUD_PRINT_URL,
				cloudPrintOldParamReq.getMethod(),
				cloudPrintOldParamReq.getT(),
				cloudPrintOldParamReq.getKey(),
				cloudPrintOldParamReq.getSign(),
				URLEncoder.encode(new Gson().toJson(cloudPrintOldParamReq.getParam()), "UTF-8"));
		HttpResult httpResult = HttpUtils.doPost(url, cloudPrintOldParamReq);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {

			return new Gson().fromJson(httpResult.getBody(), PrintBaseResp.class);
		}
		return null;
	}
}
