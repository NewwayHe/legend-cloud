/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.legendshop.common.xss.util.XssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author legendshop
 */
@Slf4j
public class JacksonXssClean extends JsonDeserializer<String> {
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		// XSS filter
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		} else if (XssHolder.isEnabled()) {
			String value = XssUtil.clean(text);
			log.warn("Json property value:{} cleaned up by legendshop-xss, current value is:{}.", text, value);
			return value;
		} else {
			return text;
		}
	}
}
