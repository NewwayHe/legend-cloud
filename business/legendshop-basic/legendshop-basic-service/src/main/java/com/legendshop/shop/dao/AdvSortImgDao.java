/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.shop.entity.AdvSortImg;
import com.legendshop.shop.query.AdvSortImgQuery;

import java.util.List;

/**
 * (AdvSortImg)表数据库访问层
 *
 * @author legendshop
 * @since 2021-07-09 15:28:04
 */
public interface AdvSortImgDao extends GenericDao<AdvSortImg, Long> {


	List<AdvSortImg> queryByAdvSortId(Long sortId);


	PageSupport<AdvSortImg> queryPageImg(AdvSortImgQuery advSortImgQuery);

	/**
	 * 查询所有顶层分类
	 *
	 * @return
	 */
	List<CategoryBO> getTopCategory();

	/**
	 * 批量更新状态
	 *
	 * @param ids
	 * @param status
	 * @return
	 */
	void batchUpdateStatus(List<Long> ids, Integer status);

	/**
	 * 根据分类id查询分类广告
	 *
	 * @param categoryId
	 * @return
	 */
	List<AdvSortImg> queryByCategoryId(Long categoryId);
}
