/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.entity.Brand;
import com.legendshop.product.query.BrandQuery;

import java.util.List;

/**
 * 品牌Dao.
 *
 * @author legendshop
 */
public interface BrandDao extends GenericDao<Brand, Long> {

	/**
	 * 获取所有品牌
	 *
	 * @return
	 */
	List<Brand> getAllBrand(Long shopId, String brandName);

	/**
	 * 根据条件连表查询分页列表
	 *
	 * @param brandQuery
	 * @return
	 */
	PageSupport<Brand> queryPage(BrandQuery brandQuery);


	/**
	 * 检查名称是否已存在（与审核通过的品牌比对)
	 *
	 * @param brandName
	 * @param brandId
	 * @return
	 */
	boolean checkBrandByNameByShopId(String brandName, Long brandId);

	/**
	 * 获取导出品牌列表
	 *
	 * @param brandQuery
	 * @return
	 */
	List<BrandBO> getExportBrands(BrandQuery brandQuery);

	/**
	 * 根据id更新品牌状态
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	int updateStatus(Integer status, Long id);

	int updateOpStatus(Long commonId, Integer opStatus);

	/**
	 * 执行逻辑删除操作。
	 *
	 * @param brandId 要执行逻辑删除的品牌 ID
	 * @return 影响的行数，表示执行删除操作后受影响的记录数
	 */
	int updateDeleteFlag(Long brandId);


	Brand getBrand(Long shopId, String brand);
}
