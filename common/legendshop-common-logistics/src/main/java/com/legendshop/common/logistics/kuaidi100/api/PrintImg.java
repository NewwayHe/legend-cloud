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
import com.legendshop.common.logistics.kuaidi100.request.PrintImgReq;
import com.legendshop.common.logistics.kuaidi100.response.PrintBaseResp;
import com.legendshop.common.logistics.kuaidi100.response.PrintImgData;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-17 15:46
 */
@Component
public class PrintImg {

	/**
	 * 获取电子面单图片（BASE64）
	 *
	 * @param printImgReq
	 * @return: java.lang.String
	 * @author: api.kuaidi100.com
	 * @time: 2020/7/17 17:15
	 */
	public PrintBaseResp<PrintImgData> printImG(PrintImgReq printImgReq) {
		if (printImgReq == null || StrUtil.isBlank(printImgReq.getKey()) || StrUtil.isBlank(printImgReq.getT()) || printImgReq.getParam() == null) {
			return null;
		}
		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.ELECTRONIC_ORDER_PIC_URL, printImgReq);
		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			return new Gson().fromJson(httpResult.getBody(), new TypeToken<PrintBaseResp<PrintImgData>>() {
			}.getType());
		}
		return null;
	}

	/**
	 * 处理返回的base64字符串（返回的是一个字符串json数组，多个子单时会有多个）
	 *
	 * @param imgBase64 base64 json字符串数组
	 * @return
	 */
	public List<String> getBase64Img(String imgBase64) {
		List<String> stringList = new Gson().fromJson(imgBase64, new TypeToken<List<String>>() {
		}.getType());
		List<String> base64Img = new ArrayList<String>();
		if (stringList != null && stringList.size() > 0) {
			for (String s : stringList) {
				s = "data:image/png;base64," + s.replace("\\\\n", "");
				base64Img.add(s);
			}
		}
		return base64Img;
	}
}
