/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductSnapshotDTO;
import com.legendshop.product.dto.SaveProductSnapshotDTO;

/**
 * 商品快照Service.
 *
 * @author legendshop
 */
public interface ProductSnapshotService {

	/**
	 * 获取商品快照
	 */
	R<ProductSnapshotDTO> getProductSnapshot(Long snapshotId);

	/**
	 * 保存商品快照
	 */
	R<Long> saveProdSnapshot(SaveProductSnapshotDTO saveProductSnapshotDTO);
}
