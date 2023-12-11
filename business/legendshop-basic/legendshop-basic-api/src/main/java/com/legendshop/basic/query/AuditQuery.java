/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.legendshop.basic.enums.AuditTypeEnum;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class AuditQuery extends PageParams {

	/**
	 * 根据AuditTypeEnum，商家id,商品id等
	 */
	Long commonId;

	/**
	 * 审核类型
	 * {@link AuditTypeEnum}
	 */
	Integer auditType;
}
