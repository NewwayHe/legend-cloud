/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;

/**
 * 分销佣金处理工具
 *
 * @author legendshop
 */
public interface DistributionUtil {

	/**
	 * 构建分销信息
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	R<ConfirmOrderBO> buildCommissionInfo(ConfirmOrderBO confirmOrderBO);

	/**
	 * 计算分销信息
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	R<ConfirmOrderBO> calculationCommission(ConfirmOrderBO confirmOrderBO);

	/**
	 * 检查分销佣金是否发生变化
	 *
	 * @param confirmOrderBo
	 * @return
	 */
	R checkCommission(ConfirmOrderBO confirmOrderBo);


}
