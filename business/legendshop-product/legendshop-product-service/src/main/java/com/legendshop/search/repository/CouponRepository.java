/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.repository;

import com.legendshop.search.document.CouponDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 优惠卷查询Repository
 *
 * @author legendshop
 */
@Repository
public interface CouponRepository extends ElasticsearchRepository<CouponDocument, Long> {

}
