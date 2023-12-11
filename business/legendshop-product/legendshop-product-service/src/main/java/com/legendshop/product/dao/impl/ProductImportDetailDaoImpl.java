/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;


import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.dao.ProductImportDetailDao;
import com.legendshop.product.entity.ProductImportDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ProductImportDetailDaoImpl extends GenericDaoImpl<ProductImportDetail, Long> implements ProductImportDetailDao {

	@Override
	public String getUrl(String path, Long id) {
		return get("select file_path from ls_attachment where short_path = ?", String.class, path);
	}

	@Override
	public List<ProductImportDetail> getImportId(Long importId) {
		return super.queryByProperties(new EntityCriterion().eq("importId", importId));
	}
}
