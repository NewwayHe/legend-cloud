/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.dto.TransRuleDTO;
import com.legendshop.product.entity.TransRule;
import org.mapstruct.Mapper;

/**
 * 店铺运费规则(TransRule)转换器
 *
 * @author legendshop
 * @since 2020-09-08 17:00:51
 */
@Mapper
public interface TransRuleConverter extends BaseConverter<TransRule, TransRuleDTO> {
}
