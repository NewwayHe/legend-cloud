/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.result;

import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExporttResult
 * @date 2022/4/25 11:43
 * @description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportResult<T> extends R<T> implements Serializable {

	private static final long serialVersionUID = 6470083340410811346L;

	/**
	 * 导出的内容
	 */
	private List<T> exportList;

	/**
	 * sso的文件名
	 */
	private String fileName;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, null, true);
	}

	private static <T> R<T> restResult(T data, int code, String msg, Boolean success) {
		ExportResult<T> apiResult = new ExportResult<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		apiResult.setSuccess(success);
		return apiResult;
	}
}
