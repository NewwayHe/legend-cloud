/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.legendshop.common.sms.dto.AliyunSmsSendResponse;
import com.legendshop.common.sms.dto.SmsSendResponseDTO;
import com.legendshop.common.sms.dto.SmsTemplateDTO;
import com.legendshop.common.sms.properties.SmsProperties;
import com.legendshop.common.sms.service.SmsSender;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static com.legendshop.common.sms.properties.SmsProperties.SmsType;

/**
 * @author legendshop
 */
@Slf4j
public class AliyunSmsSenderImpl implements SmsSender {

	final IAcsClient client;
	final SmsProperties properties;

	public AliyunSmsSenderImpl(SmsProperties smsProperties) {
		this.properties = smsProperties;
		DefaultProfile defaultProfile = DefaultProfile.getProfile(properties.getAliyun().getRegionId(),
				properties.getAliyun().getAccessKey(), properties.getAliyun().getSecretKey());
		this.client = new DefaultAcsClient(defaultProfile);
	}

	@Override
	public SmsSendResponseDTO sendSms(SmsTemplateDTO smsTemplateDTO) {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(properties.getAliyun().getDomain());
		request.setSysVersion(properties.getAliyun().getVersion());
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", properties.getAliyun().getRegionId());
		request.putQueryParameter("PhoneNumbers", String.join(",", smsTemplateDTO.getPhoneNumbers()));
		request.putQueryParameter("SignName", properties.getAliyun().getSignName());
		request.putQueryParameter("TemplateCode", smsTemplateDTO.getTemplateCode());
		request.putQueryParameter("TemplateParam", JSONUtil.toJsonStr(smsTemplateDTO.getTemplateParam()));
		try {
			CommonResponse response = client.getCommonResponse(request);
			String responseData = response.getData();
			log.info("Ali Sms Send result ：{}", responseData);
			AliyunSmsSendResponse aliyunSmsSendResponse = JSONUtil.toBean(responseData, AliyunSmsSendResponse.class);
			return new SmsSendResponseDTO(aliyunSmsSendResponse.getCode(), new Date(), SmsType.aliyun);
		} catch (ClientException e) {
			e.printStackTrace();
			return new SmsSendResponseDTO(e.getErrCode(), new Date(), SmsType.aliyun);
		}
	}
}
