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
import cn.hutool.core.util.StrUtil;
import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.dto.SysMsgReceiverDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.service.MessageHandler;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.sms.dto.SmsTemplateDTO;
import com.legendshop.common.sms.event.SmsSendEvent;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.api.ShopUserApi;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.ShopUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 短信推送处理
 *
 * @author legendshop
 */
@Slf4j
@Component("sms")
public class MessageSmsHandler implements MessageHandler {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private OrdinaryUserApi ordinaryUserApi;

	@Autowired
	private ShopUserApi shopUserApi;

	@Override
	public void handleMessage(MsgSendDTO msgSendDTO, MsgSendConfig msgSendConfig) {
		if (ObjectUtil.isNull(msgSendConfig.getSmsEnabled())) {
			return;
		}
		Boolean sms = msgSendConfig.getSmsEnabled();
		if (!sms) {
			return;
		}

		// 根据用户id获取手机号码
		List<Long> userIds = new ArrayList<>();
		// 根据短信接收者区分普通用户 商家用户 平台用户 拿取手机号
		Map<Integer, List<SysMsgReceiverDTO>> msgReceiverMap = msgSendDTO.getSysMsgReceiverDTOS().stream().collect(Collectors.groupingBy(SysMsgReceiverDTO::getReceiverType));


		// 发送用户手机号
		List<String> mobileList = new ArrayList<>();

		// 如果短信接收者包含普通用户 发送手机号集合里添加手机号
		if (msgReceiverMap.containsKey(MsgReceiverTypeEnum.ORDINARY_USER.value())) {
			msgReceiverMap.get(MsgReceiverTypeEnum.ORDINARY_USER.value()).forEach(e -> {
				Long[] ordinaryUserIds = e.getReceiveUserIds();
				userIds.addAll(Arrays.asList(ordinaryUserIds));
			});
			R<List<OrdinaryUserDTO>> userData = ordinaryUserApi.getByIds(userIds.stream().distinct().collect(Collectors.toList()));
			if (CollUtil.isNotEmpty(userData.getData())) {
				// 根据用户ID查询出手机号集合
				mobileList.addAll(userData.getData().stream().map(OrdinaryUserDTO::getMobile).collect(Collectors.toList()));
			}
		}

		// 如果短信接收者包含商家用户 发送手机号集合里添加手机号
		if (msgReceiverMap.containsKey(MsgReceiverTypeEnum.SHOP_USER.value())) {
			msgReceiverMap.get(MsgReceiverTypeEnum.SHOP_USER.value()).forEach(e -> {
				Long[] shopUserIds = e.getReceiveUserIds();
				userIds.clear();
				userIds.addAll(Arrays.asList(shopUserIds));
			});
			List<Long> collect = userIds.stream().distinct().collect(Collectors.toList());
			R<List<ShopUserDTO>> shopUserData = shopUserApi.getByIds(collect);
			if (CollUtil.isNotEmpty(shopUserData.getData())) {
				// 根据用户ID查询出手机号集合
				mobileList.addAll(shopUserData.getData().stream().map(ShopUserDTO::getMobile).collect(Collectors.toList()));
			}
		}
		// 如果短信接收者包含后台管理员 发送手机号集合里添加手机号 目前后台只有一个手机号 只往这个手机号里发送短信
		if (msgReceiverMap.containsKey(MsgReceiverTypeEnum.ADMIN_USER.value())) {
			msgReceiverMap.get(MsgReceiverTypeEnum.ADMIN_USER.value()).forEach(e -> {
				Long[] adminUserIds = e.getReceiveUserIds();
				userIds.clear();
				userIds.addAll(Arrays.asList(adminUserIds));
			});
		}

		if (CollUtil.isNotEmpty(msgSendDTO.getReceiveUserPhoneNumbers())) {
			mobileList.addAll(msgSendDTO.getReceiveUserPhoneNumbers());
		}

		if (StrUtil.isNotBlank(msgSendConfig.getSmsPhone())) {
			mobileList.addAll(new ArrayList<>(Arrays.asList(msgSendConfig.getSmsPhone().split(StringConstant.COMMA))));
		}

		if (CollUtil.isEmpty(mobileList)) {
			log.info("接收手机号不能为空！");
			return;
		}
		//短信发送参数
		SmsTemplateDTO smsTemplateDTO = new SmsTemplateDTO();
		smsTemplateDTO.setPhoneNumbers(mobileList);
		smsTemplateDTO.setTemplateCode(msgSendConfig.getSmsTemplateCode());
		Map<String, String> map = new HashMap<>(16);
		List<MsgSendParamDTO> dataDTOList = msgSendDTO.getMsgSendParamDTOList();
		if (CollUtil.isNotEmpty(dataDTOList)) {
			for (MsgSendParamDTO w : dataDTOList) {
				map.put(w.getMsgSendParam().value(), w.getValue());
			}
		}
		smsTemplateDTO.setTemplateParam(map);

		// 短信日志内容
		String content = msgSendConfig.getSmsContent();
		List<MsgSendParamDTO> msgSendParamDTOList = msgSendDTO.getMsgSendParamDTOList();
		if (CollUtil.isNotEmpty(msgSendParamDTOList)) {
			for (MsgSendParamDTO m : msgSendParamDTOList) {
				content = content.replaceAll("\\$\\{" + m.getMsgSendParam().value() + "\\}", m.getValue());
			}
		}
		// 保存短信日志需要参数
		HashMap<String, Object> smsLogParam = new HashMap<>(16);
		smsLogParam.put("type", msgSendDTO.getMsgSendType().getValue());
		smsLogParam.put("content", content);
		smsLogParam.put("userId", userIds);
		smsTemplateDTO.setSmsLogParam(smsLogParam);

		// 创建短信发送对象
		applicationEventPublisher.publishEvent(new SmsSendEvent(smsTemplateDTO));
	}
}
