/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;


import lombok.Data;

import java.io.Serializable;

/**
 * 模拟支付
 *
 * @author legendshop
 */
@Data
public class SimulatePayConfigDTO implements Serializable {

	private static final long serialVersionUID = 1883956604818369975L;
	private Boolean enabled;

	private String key;

}
