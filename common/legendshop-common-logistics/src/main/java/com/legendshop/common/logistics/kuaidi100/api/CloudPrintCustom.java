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
import com.legendshop.common.logistics.kuaidi100.request.CloudPrintCustomParam;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * 自定义云打印
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-20 9:22
 */
@Component
public class CloudPrintCustom {

	public PrintBaseResp print(BaseReq<CloudPrintCustomParam> baseReq) throws Exception {
		String url = String.format(ApiInfoConstant.CLOUD_PRINT_URL,
				baseReq.getMethod(), baseReq.getT(),
				baseReq.getKey(), baseReq.getSign(),
				URLEncoder.encode(new Gson().toJson(baseReq.getParam()), "UTF-8"));
		HttpResult httpResult = HttpUtils.doPost(url, baseReq);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {

			return new Gson().fromJson(httpResult.getBody(), PrintBaseResp.class);
		}

		return null;
	}

}
