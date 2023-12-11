/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 *
 * @author legendshop
 */
@ApiModel(value = "分页对象")
@Data
public class BasePage<E> implements Serializable {

	private static final long serialVersionUID = -7058040526668991342L;

	/**
	 * 当前页
	 */
	@ApiModelProperty("当前页")
	private int curPageNO;

	/**
	 * 每页大小.
	 */
	@ApiModelProperty("每页大小")
	private int pageSize;

	/**
	 * 页数.
	 */
	@ApiModelProperty("页数")
	private int pageCount;

	/**
	 * 记录开始位置
	 */
	@ApiModelProperty("记录开始位置")
	private long offset;

	/**
	 * 总记录数.
	 */
	@ApiModelProperty("总记录数")
	private long total;

	/**
	 * The result list.
	 */
	@ApiModelProperty("结果对象列表")
	private List<E> resultList = null;

	public BasePage() {
		this.pageCount = 0;
		this.total = 0;
		this.resultList = Collections.emptyList();
	}

	public BasePage(int curPageNO, int pageSize) {
		this.curPageNO = curPageNO;
		this.pageSize = pageSize;
		this.pageCount = 0;
		this.total = 0;
		this.resultList = Collections.emptyList();
	}

	public BasePage(BasePageRequest request, List<E> resultList, long total) {
		this.curPageNO = request.getCurPage();
		this.pageSize = request.getPageSize();
		this.resultList = ObjectUtil.isNotEmpty(resultList) ? resultList : Collections.emptyList();
		this.setTotal(total);
	}

	public void setTotal(long total) {
		this.total = total;
		if (total > 0) {
			// 京东接口只允许查询前10000条数据，超过就会报错，这里不可以向上取整
			if (pageSize > 0) {
				this.pageCount = (int) NumberUtil.div(total, this.pageSize, 0, RoundingMode.DOWN);
			}
		} else {
			this.pageCount = 0;
			this.total = 0;
		}
	}

	public void setTotal(long total, RoundingMode roundingMode) {
		this.total = total;
		if (total > 0) {
			if (pageSize > 0) {
				this.pageCount = (int) NumberUtil.div(total, this.pageSize, 0, roundingMode);
			}
		} else {
			this.pageCount = 0;
			this.total = 0;
		}
	}
}
