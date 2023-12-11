/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.expetion;

import com.legendshop.common.core.expetion.BusinessException;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
public class PayBusinessException extends BusinessException {

	private static final long serialVersionUID = -6428532217105632683L;

	/**
	 * 错误等级
	 */
	private Integer level;

	/**
	 * 错误关键字
	 */
	private String keyword;

	/**
	 * 错误内容
	 */
	private String content;

	/**
	 * 错误信息描述
	 */
	private String description;


	public PayBusinessException(String msg) {
		super(msg);
	}

	public PayBusinessException(String msg, Integer level, String keyword, String content) {
		super(msg);
		this.level = level;
		this.keyword = keyword;
		this.content = content;
		this.description = msg;
	}

	public PayBusinessException(String msg, Integer level, String keyword, String content, String description) {
		super(msg);
		this.level = level;
		this.keyword = keyword;
		this.content = content;
		this.description = description;
	}
}
