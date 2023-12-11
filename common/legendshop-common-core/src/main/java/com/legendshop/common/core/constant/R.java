/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author legendshop
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记：成功标记=1，失败标记=0")
	private int code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String msg;


	@Getter
	@Setter
	@ApiModelProperty(value = "请求结果")
	private Boolean success;


	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, null, true);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, null, true);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg, true);
	}

	public static <T> R<T> fail() {
		return restResult(null, CommonConstants.FAIL, null, false);
	}

	public static <T> R<T> serviceFail() {
		return restResult(null, CommonConstants.FAIL, "服务器错误，请稍后重试！", false);
	}

	public static <T> R<T> fail(String msg) {
		return restResult(null, CommonConstants.FAIL, msg, false);
	}

	public static <T> R<T> fail(T data) {
		return restResult(data, CommonConstants.FAIL, null, false);
	}

	public static <T> R<T> fail(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg, false);
	}

	public static <T> R<T> fail(int code, String msg) {
		return restResult(null, code, msg, false);
	}

	public static <T> R<T> fail(int code, String msg, T data) {
		return restResult(data, code, msg, false);
	}

	/**
	 * success is false, return fail(msg)
	 */
	public static <T> R<T> process(Boolean success, String msg) {
		if (null == success || FALSE.equals(success)) {
			return fail(msg);
		} else {
			return ok();
		}
	}

	/**
	 * 判断状态码等于1是否成功
	 *
	 * @return boolean
	 */
	public boolean success() {
//		if (CommonConstants.SUCCESS == code) {
//			return TRUE;
//		}
//		return FALSE;
		return success;
	}

	private static <T> R<T> restResult(T data, int code, String msg, Boolean success) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		apiResult.setSuccess(success);
		return apiResult;
	}
}
