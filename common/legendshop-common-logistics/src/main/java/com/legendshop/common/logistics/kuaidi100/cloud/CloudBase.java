/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.cloud.CloudBaseReq;
import com.legendshop.common.logistics.kuaidi100.response.cloud.CloudBaseResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-11-20 10:36
 */
public class CloudBase<T> {
	public CloudBaseResp<T> execute(CloudBaseReq param) throws Exception {
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.CLOUD_NORMAL_URL, param);
		if (httpResult.getStatus() == 200 && StringUtils.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<CloudBaseResp<T>>() {
			}.getType());
		}
		return null;
	}
}
