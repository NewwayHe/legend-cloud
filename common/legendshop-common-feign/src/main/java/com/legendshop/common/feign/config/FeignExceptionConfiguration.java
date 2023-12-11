/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.feign.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.feign.expetion.FeignAuthException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author legendshop
 */
@Slf4j
public class FeignExceptionConfiguration {

	@Bean
	public ErrorDecoder errorDecoder() {
		return new LegendshopErrorDecoder();
	}

	/**
	 * 重新实现feign的异常处理，捕捉restful接口返回的json格式的异常信息
	 */
	public static class LegendshopErrorDecoder implements ErrorDecoder {

		@Override
		public Exception decode(String methodKey, Response response) {
			byte[] body = new byte[0];
			try {
				if (response.body() != null) {
					body = Util.toByteArray(response.body().asInputStream());
				}
			} catch (IOException ignored) {
			}
			String responseStr = new String(body);
			System.out.println("responseStr = " + responseStr);
			String msg = null;
			Integer code = null;
			if (!StrUtil.isBlank(responseStr)) {
				JSONObject jsonObject = JSONUtil.parseObj(responseStr);
				msg = (String) jsonObject.get("msg");
				code = jsonObject.getInt("code");
			}
			if (null == msg || "".equals(msg)) {
				msg = "错误信息为空，请检查接口URL与接口参数";
			}

			if (null != code && HttpStatus.HTTP_UNAUTHORIZED == code) {
				return new FeignAuthException(msg);
			}

			log.info(" [ Feign Client ] ---> 调用异常，异常抛出服务：{}，异常方法：{}，接口状态：{}，异常信息：{}", response.request().requestTemplate().feignTarget().name(), methodKey, response.status(), msg);
			return new RuntimeException(msg);
		}

	}
}
