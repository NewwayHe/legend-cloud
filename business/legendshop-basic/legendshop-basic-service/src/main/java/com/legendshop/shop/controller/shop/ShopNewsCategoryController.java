/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.service.NewsCategoryService;
import com.legendshop.shop.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商家端端帮助文章控制器
 *
 * @author legendshop
 */
@Tag(name = "帮助文章")
@RestController
public class ShopNewsCategoryController {

	@Autowired
	private NewsCategoryService newsCategoryService;

	@Autowired
	private NewsService newsService;

	/**
	 * 获取所有的商家端帮助栏目
	 *
	 * @return
	 */
	@Operation(summary = "【商家】获取所有的商家端帮助栏目")
	@GetMapping("/s/all/shopNewsCategory")
	public R<List<NewsCategoryDTO>> queryUseAndAll() {
		//获取商家端和全部端显示的栏目
		List<NewsCategoryDTO> categoryDTOList = newsCategoryService.queryUseAndAll(DisplayPageEnum.SHOP.getValue(), null);
		return R.ok(categoryDTOList);
	}

	/**
	 * 通过栏目Id获取该栏目下所有文章
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【商家】通过栏目Id获取该栏目下所有文章")
	@GetMapping("/all/shopNews")
	public R<PageSupport<NewsDTO>> getNewsByCatId(NewsCategoryQuery query) {
		if (ObjectUtil.isEmpty(query.getId())) {
			return R.fail("栏目id不能为空");
		}
		query.setDisplayPage(DisplayPageEnum.SHOP.value());
		return R.ok(newsService.getCatNews(query));
	}

	/**
	 * 查看文章详情
	 *
	 * @param id
	 * @return
	 */
	@Operation(summary = "【商家】查看文章详情")
	@Parameter(name = "id", description = "文章id", required = true)
	@GetMapping("/get/shopNews")
	public R<NewsDTO> getNews(@RequestParam Long id) {
		if (ObjectUtil.isEmpty(id)) {
			return R.fail("文章id不能为空");
		}
		return R.ok(newsService.getNewsByDisplay(id, DisplayPageEnum.SHOP.value()));
	}

	/**
	 * 通过关键字搜索帮助文章
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【商家】通过关键字搜索帮助文章")
	@GetMapping("/shopWord")
	public R<PageSupport<NewsDTO>> getByWord(NewsCategoryQuery query) {
		query.setDisplayPage(DisplayPageEnum.SHOP.value());
		return R.ok(newsService.getByWord(query));
	}
}
