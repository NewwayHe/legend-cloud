/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.dto.ProductImportDetailDTO;
import com.legendshop.product.entity.ProductImportDetail;

import java.util.List;

/**
 * @author legendshop
 */
public interface ProductImportDetailService extends BaseService<ProductImportDetailDTO> {


	String getUrl(String url, Long id);

	List<ProductImportDetail> getImportId(Long importId);
}
