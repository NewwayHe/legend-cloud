/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.constant.CommonConstants;
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
 * 用户端帮助文章控制器
 *
 * @author legendshop
 */
@Tag(name = "帮助文章")
@RestController
public class UserNewsCategoryController {

	@Autowired
	private NewsCategoryService newsCategoryService;

	@Autowired
	private NewsService newsService;

	/**
	 * 获取所有的用户端帮助栏目
	 *
	 * @return
	 */
	@Operation(summary = "【用户】获取所有的用户端帮助栏目")
	@GetMapping("/all/userNewsCategory")
	public R<List<NewsCategoryDTO>> queryUseAndAll(String word) {
		//获取用户端和全部端显示的栏目
		List<NewsCategoryDTO> categoryDTOS = newsCategoryService.queryUseAndAll(DisplayPageEnum.USER.getValue(), word);
		return R.ok(categoryDTOS);
	}

	/**
	 * 获取所有的用户端帮助栏目
	 *
	 * @return
	 */
	@Operation(summary = "【用户】获取所有的用户端帮助栏目")
	@GetMapping("/page/userNewsCategory")
	public R<PageSupport<NewsCategoryDTO>> queryUsePage(NewsCategoryQuery query) {
		//获取用户端和全部端显示的栏目
		query.setDisplayPage(DisplayPageEnum.USER.getValue());
		return R.ok(newsCategoryService.queryUsePage(query));
	}


	/**
	 * 获取所有的用户端帮助栏目
	 *
	 * @return
	 */
	@Operation(summary = "【用户】获取所有的用户端帮助栏目树结构")
	@GetMapping("/all/userNewsCategoryTree")
	public R<List<NewsCategoryDTO>> queryNewsCategoryTree(NewsCategoryQuery query) {
		//获取用户端和全部端显示的栏目
		query.setDisplayPage(DisplayPageEnum.USER.getValue());
		query.setStatus(CommonConstants.STATUS_NORMAL);
		List<NewsCategoryDTO> categoryDTOS = newsCategoryService.queryNewsCategoryTree(query);
		return R.ok(categoryDTOS);
	}

	/**
	 * 通过栏目Id获取该栏目下所有文章
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【用户】通过栏目Id获取该栏目下所有文章")
	@GetMapping("/all/userNews")
	public R<PageSupport<NewsDTO>> getNewsByCatId(NewsCategoryQuery query) {
		if (ObjectUtil.isEmpty(query.getId())) {
			return R.fail("栏目id不能为空");
		}
		query.setDisplayPage(DisplayPageEnum.USER.value());
		return R.ok(newsService.getCatNews(query));
	}

	/**
	 * 查看文章详情
	 *
	 * @param id
	 * @return
	 */
	@Operation(summary = "【用户】查看文章详情")
	@Parameter(name = "id", description = "文章id", required = true)
	@GetMapping("/get/userNews")
	public R<NewsDTO> getNews(@RequestParam Long id) {
		if (ObjectUtil.isEmpty(id)) {
			return R.fail("文章id不能为空");
		}
		return R.ok(newsService.getNewsByDisplay(id, DisplayPageEnum.USER.value()));
	}

	/**
	 * 通过关键字搜索帮助文章
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【用户】通过关键字搜索帮助文章")
	@GetMapping("/word")
	public R<PageSupport<NewsDTO>> getByWord(NewsCategoryQuery query) {
		if (ObjectUtil.isEmpty(query.getWord())) {
			return R.fail("关键字不能为空");
		}
		query.setDisplayPage(DisplayPageEnum.USER.value());
		return R.ok(newsService.getByWord(query));
	}


	/**
	 * 查询栏目及其文章  如果栏目数量大于5只显示前五条，如果文章大于2，只显示前两条
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	@Operation(summary = "【用户】查询栏目及其文章")
	@GetMapping("/query/with/Items")
	public R<List<NewsCategoryDTO>> queryWithItems(NewsCategoryQuery newsCategoryQuery) {
		return R.ok(newsCategoryService.pageWithChildren(newsCategoryQuery));
	}

}
