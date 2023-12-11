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
import com.legendshop.common.logistics.kuaidi100.request.PrintCloudReq;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.response.PrintCloudData;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-17 15:46
 */
@Component
public class PrintCloud {

	/**
	 * 电子面单打印
	 *
	 * @param printCloudReq
	 * @author: api.kuaidi100.com
	 * @time: 2020/7/17 17:15
	 */
	public PrintBaseResp<PrintCloudData> print(PrintCloudReq printCloudReq) {
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.ELECTRONIC_ORDER_PRINT_URL, printCloudReq);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {

			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<PrintCloudData>>() {
			}.getType());
		}
		return null;
	}
}
