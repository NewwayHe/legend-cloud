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
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.DecoratePageBO;
import com.legendshop.shop.dto.DecoratePageDTO;

/**
 * 装修页面(DecoratePage)表服务接口
 *
 * @author legendshop
 */
public interface DecoratePageService {


	/**
	 * 获取分页列表
	 *
	 * @param decoratePageQuery
	 * @return
	 */
	PageSupport<DecoratePageBO> queryPageList(DecoratePageQuery decoratePageQuery);

	/**
	 * 获取分页排序列表
	 *
	 * @param decoratePageQuery
	 * @return
	 */
	PageSupport<DecoratePageBO> queryPageListDesc(DecoratePageQuery decoratePageQuery);

	/**
	 * 保存页面装修数据
	 *
	 * @param decoratePageDTO
	 * @return
	 */
	R<Long> save(DecoratePageDTO decoratePageDTO);

	/**
	 * 发布页面装修数据
	 *
	 * @param decoratePageDTO
	 * @return
	 */
	R<Long> release(DecoratePageDTO decoratePageDTO);

	/**
	 * 发布页面
	 *
	 * @param id 页面ID
	 * @return
	 */
	R releasePage(Long id);

	/**
	 * 设为首页
	 *
	 * @param id
	 * @param category
	 * @return
	 */
	R usePage(Long id, String category);

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
	 * 获取使用中的首页装修数据
	 *
	 * @param source 页面来源
	 * @return
	 */
	R<String> getUsedIndexPage(String source);

	/**
	 * 根据ID、来源获取页面内容
	 *
	 * @param pageId 页面ID
	 * @param source 来源
	 * @return
	 */
	R<JSONObject> getPosterPageById(Long pageId, String source);

	/**
	 * 获取主题颜色
	 *
	 * @param source 页面来源
	 * @return
	 */
	R<String> getThemeColor(String source);

	/**
	 * 编辑页面
	 *
	 * @param id
	 * @return
	 */
	R<DecoratePageDTO> edit(Long id);

	/**
	 * 获取使用中的用户中心装修数据
	 *
	 * @param source 页面来源
	 */
	R<String> getUsedCenterPage(String source);

	/**
	 * 是否开启用户中心装修
	 */
	R<Boolean> enableUserCenter(String source);

	/**
	 * 获取使用中的分销中心装修数据
	 *
	 * @param source
	 * @return
	 */
	R<String> getUsedDistributionCenterPage(String source);

	/**
	 * 获取首页装修数据
	 *
	 * @return
	 */
	R<String> getUsedIndexPag();
}
