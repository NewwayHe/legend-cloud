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
import com.legendshop.common.logistics.kuaidi100.request.PrintHtmlReq;
import com.legendshop.common.logistics.kuaidi100.response.PrintHtmlResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-17 17:20
 */
@Component
public class PrintHtml {

	/**
	 * 电子面单打印
	 *
	 * @param printHtmlReq
	 * @return: java.lang.String
	 * @author: api.kuaidi100.com
	 * @time: 2020/7/17 17:15
	 */
	public PrintHtmlResp print(PrintHtmlReq printHtmlReq) {
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.ELECTRONIC_ORDER_HTML_URL, printHtmlReq);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {

			return new Gson().fromJson(httpResult.getBody(), PrintHtmlResp.class);
		}
		return null;
	}
}
