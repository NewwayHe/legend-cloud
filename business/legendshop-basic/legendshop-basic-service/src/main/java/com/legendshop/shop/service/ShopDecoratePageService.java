/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.hutool.json.JSONObject;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.ShopDecoratePageBO;
import com.legendshop.shop.dto.ShopDecoratePageDTO;
import com.legendshop.shop.query.ShopDecoratePageQuery;

/**
 * 移动端店铺主页装修服务接口
 *
 * @author legendshop
 */
public interface ShopDecoratePageService {


	/**
	 * 获取店铺装修页面分页列表
	 *
	 * @param shopDecoratePageQuery
	 * @return
	 */
	PageSupport<ShopDecoratePageBO> queryPageList(ShopDecoratePageQuery shopDecoratePageQuery);

	/**
	 * 获取店铺装修页面分页排序列表
	 *
	 * @param shopDecoratePageQuery
	 * @return
	 */
	PageSupport<ShopDecoratePageBO> queryPageListDesc(ShopDecoratePageQuery shopDecoratePageQuery);

	/**
	 * 保存店铺装修
	 *
	 * @param shopDecoratePageDTO
	 * @return
	 */
	R save(ShopDecoratePageDTO shopDecoratePageDTO);

	/**
	 * 发布店铺装修
	 *
	 * @param shopDecoratePageDTO
	 * @return
	 */
	R release(ShopDecoratePageDTO shopDecoratePageDTO);


	/**
	 * 发布店铺装修页面
	 *
	 * @param id 页面ID
	 * @return
	 */
	R releasePage(Long id);

	/**
	 * 设为店铺首页
	 *
	 * @param shopId
	 * @param id
	 * @return
	 */
	R usePage(Long shopId, Long id);

	/**
	 * 复制
	 *
	 * @param id
	 * @param newName
	 * @return
	 */
	R clone(Long id, String newName);

	/**
	 * 根据ID删除
	 *
	 * @param id
	 * @return
	 */
	R deleteById(Long id);

	/**
	 * 修改页面名称
	 *
	 * @param id
	 * @param newName
	 * @return
	 */
	R updateName(Long id, String newName);


	/**
	 * 更改封面图
	 *
	 * @param id
	 * @param coverPicture
	 * @return
	 */
	R updateCoverPicture(Long id, String coverPicture);

	/**
	 * 获取使用中的店铺首页装修数据
	 *
	 * @param shopId 所属商家ID
	 * @param source 页面来源
	 * @return
	 */
	R<JSONObject> getUsedIndexPage(Long shopId, String source);

	/**
	 * 根据ID、来源获取页面内容
	 *
	 * @param shopId
	 * @param pageId 页面ID
	 * @param source 来源
	 * @return
	 */
	R<JSONObject> getPosterPageById(Long shopId, Long pageId, String source);

	/**
	 * 编辑装修
	 *
	 * @param pageId
	 * @param shopId
	 * @return
	 */
	R<ShopDecoratePageBO> edit(Long pageId, Long shopId);

	R<ShopDecoratePageBO> edit(Long pageId);

	/**
	 * 修改店铺装修默认状态
	 *
	 * @param shopId 店铺id
	 * @param source 来源
	 * @return
	 */
	R updateDefaultState(Long shopId, String source);
}
