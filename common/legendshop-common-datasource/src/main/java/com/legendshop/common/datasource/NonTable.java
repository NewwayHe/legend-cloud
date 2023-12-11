/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.datasource;

import cn.legendshop.jpaplus.persistence.GeneratedValue;
import cn.legendshop.jpaplus.persistence.Id;
import cn.legendshop.jpaplus.support.GenericEntity;

/**
 * 用于表示某些Dao不再以单表为中心，例如报表等，会涉及到多张表，并没有对应到一个主表。
 * 在这种前提下， 单表的增删改查将会失效。
 *
 * @author legendshop
 */
public class NonTable implements GenericEntity<Long> {

	private static final long serialVersionUID = 8250796640863620262L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	private Long id;

	@Override
	public Long getId() {
		return 0L;
	}

	@Override
	public void setId(Long id) {

	}

}
