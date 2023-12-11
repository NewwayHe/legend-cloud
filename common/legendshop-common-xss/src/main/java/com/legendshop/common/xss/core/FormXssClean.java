/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss.core;

import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.xss.util.XssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * @author legendshop
 */
@ControllerAdvice
public class FormXssClean {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 处理前端传来的表单字符串
		binder.registerCustomEditor(String.class, new StringPropertiesEditor());
	}

	@Slf4j
	public static class StringPropertiesEditor extends PropertyEditorSupport {

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : StringConstant.EMPTY;
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			} else if (XssHolder.isEnabled()) {
				String value = XssUtil.clean(text);
				setValue(value);
				log.warn("Request parameter value:{} cleaned up by legendshop-xss, current value is:{}.", text, value);
			} else {
				setValue(text);
			}
		}
	}
}
