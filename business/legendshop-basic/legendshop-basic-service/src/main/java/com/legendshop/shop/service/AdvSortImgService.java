/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.shop.dto.AdvSortImgDTO;
import com.legendshop.shop.query.AdvSortImgQuery;

import java.util.List;

/**
 * (AdvSortImg)表服务接口
 *
 * @author legendshop
 * @since 2021-07-09 15:28:04
 */
public interface AdvSortImgService extends BaseService<AdvSortImgDTO> {


	/**
	 * 根据分类id查询广告图片
	 *
	 * @param sortId
	 * @return
	 */
	List<AdvSortImgDTO> queryByAdvSortId(Long sortId);

	/**
	 * 分页查询
	 *
	 * @param advSortImgQuery
	 * @return
	 */
	R<PageSupport<AdvSortImgDTO>> queryPage(AdvSortImgQuery advSortImgQuery);

	/**
	 * 查询所有顶层分类
	 *
	 * @return
	 */
	R<List<CategoryBO>> getTopCategory();

	/**
	 * 批量更新状态
	 *
	 * @param ids
	 * @param status
	 * @return
	 */
	R batchUpdateStatus(List<Long> ids, Integer status);

	/**
	 * 根据分类id查询分类广告
	 *
	 * @param categoryId
	 * @return
	 */
	R<List<AdvSortImgDTO>> queryByCategoryId(Long categoryId);

	/**
	 * 保存分类广告
	 *
	 * @param advSortImgDTO
	 * @return
	 */
	R saveCategoryAdv(AdvSortImgDTO advSortImgDTO);

	/**
	 * 删除分类广告
	 *
	 * @param id
	 * @return
	 */
	R deleteCategoryAdv(Long id);

	/**
	 * 更新分类广告
	 *
	 * @param advSortImgDTO
	 * @return
	 */
	R updateCategoryAdv(AdvSortImgDTO advSortImgDTO);
}
