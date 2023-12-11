/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.validator;

import com.legendshop.common.validator.BusinessValidator;
import com.legendshop.common.validator.ValidatorContext;
import com.legendshop.common.validator.exception.ValidateException;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.enums.ProductStatusEnum;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
public class ProductBusinessValidator implements BusinessValidator<ProductBO> {
	@Override
	public void validate(ValidatorContext context, ProductBO productBO) {
		if (!ProductStatusEnum.PROD_ONLINE.getValue().equals(productBO.getStatus())) {
			throw new ValidateException("商品状态校验失败");
		}
	}
}
