/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.response;

import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-09-17 14:17
 */
@Data
public class BOrderQueryData {

	private String province;

	private String city;

	private String district;

	private String addr;

	private String latitude;

	private String longitude;

	private List<BOrderQueryDataInfo> mktInfo;
}
