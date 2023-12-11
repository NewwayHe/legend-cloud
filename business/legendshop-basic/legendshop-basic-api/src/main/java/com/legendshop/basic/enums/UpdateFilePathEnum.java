/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.Getter;

/**
 * 上传图片或视频
 *
 * @author legendshop
 */
@Getter
public enum UpdateFilePathEnum {
	/**
	 * 上传图片
	 */
	PICTURE(1),

	/**
	 * 上传视频
	 */
	VIDEO(2),

	;

	private Integer value;


	UpdateFilePathEnum(Integer value) {
		this.value = value;
	}

}
