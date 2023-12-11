/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.basic.dto.SysMsgReceiverDTO;
import com.legendshop.basic.dto.WeChatParamDTO;
import com.legendshop.basic.service.MessageHandler;
import com.legendshop.basic.service.WxService;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.user.api.PassportApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@Component("weChat")
public class MessageWeChatHandler implements MessageHandler {


	@Autowired
	private WxService wxService;

	@Autowired
	private PassportApi passportApi;


	@Override
	public void handleMessage(MsgSendDTO msgSendDTO, MsgSendConfig msgSendConfig) {
		//微信公众号通知是否为空
		if (ObjectUtil.isNull(msgSendConfig.getWxEnabled())) {
			return;
		}
		//获取开启微信公众号通知的值
		Boolean weChat = msgSendConfig.getWxEnabled();
		//为空则返回
		if (!weChat) {
			return;
		}
		// 获取微信公众号模板
		String templateId = msgSendConfig.getWxTemplateId();
		WeChatParamDTO weChatParamDTO = new WeChatParamDTO();
		//设置模板id
		weChatParamDTO.setTemplateId(templateId);
		//设置替换模板的内容
		weChatParamDTO.setDataDTOList(msgSendDTO.getMsgSendParamDTOList());
		/**
		 * 短信、微信模板替换的内容
		 * 短信、微信模板替换的内容
		 * 内容
		 * 注："dataDTOList":[
		 * {
		 * "dataName":"PRODUCT_NAME",
		 * "value":"iphone11"
		 * }
		 * ]
		 */
		// 设置微信公众号链接
		weChatParamDTO.setUrl(msgSendConfig.getWxUrl());
		// 设置跳转路径替换参数
		weChatParamDTO.setUrlParamList(msgSendDTO.getUrlParamList());


		//根据用户id获取公众号用户openid
		List<Long> userIds = new ArrayList<>();
		//获取站内信接收者（接收者id和接收类型）
		for (SysMsgReceiverDTO sysMsgReceiverDTO : msgSendDTO.getSysMsgReceiverDTOS()) {
			Long[] receiveUserIds = sysMsgReceiverDTO.getReceiveUserIds();
			if (receiveUserIds.length > 0) {
				userIds.addAll(Arrays.asList(receiveUserIds));
			}
		}
		// 获取接收用户的openId
		R<List<String>> openIdsResult = passportApi.getOpenIdByUserIds(userIds.stream().distinct().collect(Collectors.toList()), VisitSourceEnum.MP.name());

		List<String> touser = new ArrayList<>();
		if (CollUtil.isNotEmpty(openIdsResult.getData())) {
			touser.addAll(openIdsResult.getData());
		}

		// 将自定义的接收方openId也放进去
		if (CollUtil.isNotEmpty(msgSendDTO.getReceiveUserWxOpenIds())) {
			touser.addAll(msgSendDTO.getReceiveUserWxOpenIds());
		}

		// 如果没有可以发送的openId，则不发送微信推送
		if (CollUtil.isEmpty(touser)) {
			return;
		}
		weChatParamDTO.setTouser(touser);
		wxService.sendMsg(weChatParamDTO);
	}


}
