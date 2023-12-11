/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.legendshop.basic.dto.SmsLogDTO;
import com.legendshop.basic.dto.SmsSendParamDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.service.SmsLogService;
import com.legendshop.basic.service.SmsSendService;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.core.properties.EnvironmentProperties;
import com.legendshop.common.sms.dto.SmsSendResponseDTO;
import com.legendshop.common.sms.dto.SmsTemplateDTO;
import com.legendshop.common.sms.event.SmsSendEvent;
import com.legendshop.common.sms.properties.SmsProperties;
import com.legendshop.common.sms.service.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsSendServiceImpl implements SmsSendService {

	final SmsSender smsSender;

	final StringRedisTemplate redisTemplate;

	final SysParamsService sysParamsService;

	final SysParamItemService sysParamItemService;

	final ApplicationEventPublisher applicationEventPublisher;

	final SmsLogService smsLogService;

	final EnvironmentProperties environmentProperties;

	final SmsProperties smsProperties;

	@Override
	public R<Void> sendSmsAsync(Object smsTemplateDTO) {
		// 发送短信
		log.info("Start Send SMS");
		try {
			SmsTemplateDTO smsTemplate = (SmsTemplateDTO) smsTemplateDTO;
			SmsSendResponseDTO sendResponse = new SmsSendResponseDTO();
			if (!environmentProperties.isDebug()) {
				log.info("全局环境变量debug为关闭状态，真实调用发送短信发送");
				sendResponse = smsSender.sendSms(smsTemplate);
			}
			// 记录短信发送历史
			Map<String, Object> smsLogParam = smsTemplate.getSmsLogParam();
			List<Long> userId = (List<Long>) smsLogParam.get("userId");
			//现有短信发送历史记录
			for (int x = 0; x < smsTemplate.getPhoneNumbers().size(); x++) {
				SmsLogDTO smsLogDTO = new SmsLogDTO();
				smsLogDTO.setMobilePhone(smsTemplate.getPhoneNumbers().get(x));
				if (userId != null && userId.size() > x) {
					smsLogDTO.setUserId(userId.get(x));
				}
				smsLogDTO.setContent(smsLogParam.get("content") == null ? null : (String) smsLogParam.get("content"));
				smsLogDTO.setType(smsLogParam.get("type") == null ? null : smsLogParam.get("type") + "");
				smsLogDTO.setRequestIp(smsLogParam.get("requestIp") == null ? null : (String) smsLogParam.get("requestIp"));
				smsLogDTO.setResponseStatus(sendResponse.getResultCode());
				smsLogDTO.setCreateTime(sendResponse.getSendTime());
				smsLogDTO.setChannelType(sendResponse.getChannelType().name());
				smsLogService.save(smsLogDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("SMS Send Success");
		return R.ok();
	}

	@Override
	public R<String> sendSms(SmsSendParamDTO sendParamDTO) {
		if (CollectionUtils.isEmpty(sendParamDTO.getPhoneNumbers())) {
			return R.fail("待发送手机号不能为空");
		}

		// 获取短信发送模板
		R<MsgSendConfig> smsTemplateConfigResult = smsValCodeSendTemplate();
		if (smsTemplateConfigResult.getSuccess() == null || !smsTemplateConfigResult.getSuccess()) {
			return R.fail(smsTemplateConfigResult.getMsg());
		}

		MsgSendConfig smsTemplateConfig = smsTemplateConfigResult.getData();
		sendParamDTO.setSmsTemplateConfig(smsTemplateConfig);

		// 感觉发送类型，拼接不同的SmsTemplateDTO ( CODE , PARAM )
		R<SmsTemplateDTO> templateResult;
		switch (sendParamDTO.getTypeEnum()) {
			case VAL:
				templateResult = smsVerifyCode(sendParamDTO);
				break;
//			case USER:
//			case PROD:
			// TODO 其他短信发送暂未实现
			default:
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未实现的短信发送方法");
		}


		if (templateResult.getSuccess() == null || !templateResult.getSuccess()) {
			return R.fail(templateResult.getMsg());
		}
		log.info(" send sms template : {} ", sendParamDTO.getSmsTemplateConfig().getSmsContent());
		SmsTemplateDTO smsTemplateDTO = templateResult.getData();
		//保存短信日志需要参数
		HashMap<String, Object> smsLogParam = new HashMap<>(16);
		smsLogParam.put("type", sendParamDTO.getTypeEnum().getValue());
		smsLogParam.put("requestIp", sendParamDTO.getIp());
		smsLogParam.put("content", smsTemplateConfig.getSmsContent());
		smsTemplateDTO.setSmsLogParam(smsLogParam);
		// 创建短信发送对象
		applicationEventPublisher.publishEvent(new SmsSendEvent(smsTemplateDTO));
		//获取短信验证码，返回
		String code = smsTemplateDTO.getTemplateParam().get("code");
		log.info("短信验证码为：{}", code);
		if (!environmentProperties.isDebug()) {
			log.info("全局环境变量debug为关闭状态，接口不返回验证码~");
		}
		return R.ok(environmentProperties.isDebug() ? code : null);
	}

	/**
	 * 拼接验证码参数  {templateText : 验证码${code}，您正在登录，若非本人操作，请勿泄露。}
	 *
	 * @param sendParamDTO the sendParamDTO
	 */
	private R<SmsTemplateDTO> smsVerifyCode(SmsSendParamDTO sendParamDTO) {
		String mobile = sendParamDTO.getPhoneNumbers().get(0);
		String ip = sendParamDTO.getIp();
		//验证码放Redis，建议15分钟内有效
		String key = sendParamDTO.getUserType() + StringConstant.COLON + sendParamDTO.getTypeEnum() + sendParamDTO.getSmsTemplateTypeEnum() + StringConstant.COLON + mobile;
		//重复获取验证码的Key限制，用于判断1分钟内不能重复获取验证码，但验证码有效期应是15分钟，
		String duplicateKey = key + "_duplicate";
		//重复获取验证码的IP限制，按当前分钟算
		String requestIpKey = sendParamDTO.getUserType() + StringConstant.COLON + sendParamDTO.getTypeEnum() + sendParamDTO.getSmsTemplateTypeEnum() + StringConstant.COLON + sendParamDTO.getIp() + StringConstant.COLON + DateTime.now().toString("HHmm");

		MsgSendConfig smsTemplateConfig = sendParamDTO.getSmsTemplateConfig();
		String codeObj = this.redisTemplate.opsForValue().get(duplicateKey);
		String requestIpCount = this.redisTemplate.opsForValue().get(requestIpKey);
		if (codeObj != null) {
			log.info("验证码发送过频繁 mobile : {}，code : {}", mobile, codeObj);
			return R.fail("验证码发送过频繁，请稍后");
		}
		if (requestIpCount == null) {
			requestIpCount = "0";
		} else if (Integer.parseInt(requestIpCount) >= 5) {
			log.info("验证码发送过频繁 ip : {}，count : {}", ip, requestIpCount);
			return R.fail("验证码发送过频繁，请稍后");
		}
		// 发送验证码
		String code = RandomUtil.randomNumbers(6);
		log.info("【验证码】 code : {}", code);
		smsTemplateConfig.setSmsContent(smsTemplateConfig.getSmsContent().replace("${code}", code));
		SmsTemplateDTO smsTemplateDTO = new SmsTemplateDTO();
		smsTemplateDTO.setPhoneNumbers(Collections.singletonList(mobile));
		HashMap<String, String> param = new HashMap<>(16);
		param.put("code", code);
		smsTemplateDTO.setTemplateParam(param);
		smsTemplateDTO.setTemplateCode(smsTemplateConfig.getSmsTemplateCode());

		//验证码有效时间为15分钟，
		this.redisTemplate.opsForValue().set(key, code, 15, TimeUnit.MINUTES);
		//重复提交限制1分钟
		this.redisTemplate.opsForValue().set(duplicateKey, code, 1, TimeUnit.MINUTES);
		//重复提交限制1分钟
		this.redisTemplate.opsForValue().set(requestIpKey, (Integer.parseInt(requestIpCount) + 1) + "", 1, TimeUnit.MINUTES);
		return R.ok(smsTemplateDTO);
	}

	/**
	 * 通过短信类型，获取短信模板
	 */
	private R<MsgSendConfig> smsValCodeSendTemplate() {
		// 获取短信发送模板
		MsgSendConfig sendConfig = sysParamsService.getConfigDtoByParamName(SysParamNameEnum.VAL_CODE.name(), MsgSendConfig.class);
		if (null == sendConfig) {
			log.info("{} 站内信参数为空！", SysParamNameEnum.VAL_CODE.name());
			return R.fail("没有对应的短信模板");
		}
		if (StringUtils.isNotBlank(sendConfig.getSmsTemplateCode()) &&
				StringUtils.isNotBlank(sendConfig.getSmsContent()) &&
				sendConfig.getSmsEnabled() != null &&
				sendConfig.getSmsEnabled()) {
			return R.ok(sendConfig);
		}
		return R.fail("模板错误！");
	}

}
