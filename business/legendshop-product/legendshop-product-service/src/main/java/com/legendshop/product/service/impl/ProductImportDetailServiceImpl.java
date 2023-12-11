/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.dao.ProductImportDetailDao;
import com.legendshop.product.dto.ProductImportDetailDTO;
import com.legendshop.product.entity.ProductImportDetail;
import com.legendshop.product.service.ProductImportDetailService;
import com.legendshop.product.service.convert.ProductImportDetailConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImportDetailServiceImpl extends BaseServiceImpl<ProductImportDetailDTO, ProductImportDetailDao, ProductImportDetailConverter> implements ProductImportDetailService {
	final ProductImportDetailDao productImportLogisticsDetailDao;
	final ProductImportDetailConverter productImportDetailConverter;


	@Override
	public String getUrl(String path, Long id) {
		return productImportLogisticsDetailDao.getUrl(path, id);
	}

	@Override
	public List<ProductImportDetail> getImportId(Long importId) {
		return productImportLogisticsDetailDao.getImportId(importId);
	}
}
