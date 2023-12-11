/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ImportDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductImportErrorDetailDTO;
import com.legendshop.product.entity.ProductImport;
import com.legendshop.product.query.ProductQuery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
public interface ProductImportService {
	R<Void> batchInsert(String username, MultipartFile file, ProductDTO productDTO) throws IOException;


	List<ImportDTO> template();

	R<PageSupport<ProductImport>> page(ProductQuery query);

	List<ProductImportErrorDetailDTO> getInsertProductFailPage(Long importId);


//	R<Void> page(Long shopId);
}
