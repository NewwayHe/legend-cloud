/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.sensitive;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.legendshop.common.core.annotation.DataSensitive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum;

/**
 * 序列化脱敏
 *
 * @author legendshop
 */
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveDataSerialize extends JsonSerializer<String> implements
		ContextualSerializer {

	/**
	 * 处理枚举类型
	 */
	private SensitiveTypeEnum sensitiveType;


	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		switch (sensitiveType) {
			case MOBILE_PHONE:
				//处理手机号码
				jsonGenerator.writeString(SensitiveInfoUtil.mobilePhone(value));
				break;
			case ID_CARD:
				//处理身份证号码
				jsonGenerator.writeString(SensitiveInfoUtil.idCardNum(value));
				break;
			case BANK_CARD:
				//处理银行卡号码
				jsonGenerator.writeString(SensitiveInfoUtil.idCardNum(value));
			case PASSWORD:
				//处理密码脱敏
				jsonGenerator.writeString(SensitiveInfoUtil.password(value));
			default:
				throw new IllegalArgumentException("UnKnow sensitive type " + sensitiveType);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
		// 为空直接跳过
		if (ObjectUtil.isNull(beanProperty)) {
			return serializerProvider.findNullValueSerializer(null);
		}
		// 非 String 类直接跳过
		if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
			DataSensitive sensitiveInfo = beanProperty.getAnnotation(DataSensitive.class);
			if (sensitiveInfo == null) {
				sensitiveInfo = beanProperty.getContextAnnotation(DataSensitive.class);
			}
			if (sensitiveInfo != null) {
				return new SensitiveDataSerialize(sensitiveInfo.type());
			}
		}
		return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
	}
}
