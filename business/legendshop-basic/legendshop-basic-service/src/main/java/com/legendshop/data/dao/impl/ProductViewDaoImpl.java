/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.data.dao.ProductViewDao;
import com.legendshop.data.entity.ProductView;
import org.springframework.stereotype.Repository;

/**
 * 商品访问记录(ProductView)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-24 17:24:04
 */
@Repository
public class ProductViewDaoImpl extends GenericDaoImpl<ProductView, Long> implements ProductViewDao {

}
