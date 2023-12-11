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
import com.google.gson.reflect.TypeToken;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.CloudAttachmentReq;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Map;

/**
 * 附件云打印
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-20 9:26
 */
@Component
public class CloudPrintAttachment {

	public PrintBaseResp print(CloudAttachmentReq cloudPrintAttachmentReq) throws Exception {
		String url = String.format(ApiInfoConstant.CLOUD_PRINT_URL,
				cloudPrintAttachmentReq.getMethod(),
				cloudPrintAttachmentReq.getT(),
				cloudPrintAttachmentReq.getKey(),
				cloudPrintAttachmentReq.getSign(),
				URLEncoder.encode(new Gson().toJson(cloudPrintAttachmentReq.getParam()), "UTF-8"));
		HttpResult httpResult = HttpUtils.doPostFile(url, cloudPrintAttachmentReq.getFile());
		if (httpResult.getStatus() == 200 && StringUtils.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<Map<String, String>>>() {
			}.getType());
		}
		return null;
	}
}
