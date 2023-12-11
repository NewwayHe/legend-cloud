/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.vo;

import com.legendshop.product.bo.BrandBO;
import com.legendshop.search.dto.SearchCategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static java.util.Collections.EMPTY_LIST;

/**
 * 返回搜索结果集封装
 *
 * @author legendshop
 */
@Data
@Slf4j
public class SearchResult<T> {
	public SearchResult(List<SearchCategoryDTO> categories, List<BrandBO> brands, List<Map<String, Object>> specs) {
		this.categories = categories;
		this.brands = brands;
		this.specs = specs;
	}

	public SearchResult(Long total, List<T> items, List<SearchCategoryDTO> categories, List<BrandBO> brands, List<Map<String, Object>> specs) {
		this.resultList = items;
		this.total = total;
		this.categories = categories;
		this.brands = brands;
		this.specs = specs;
	}

	public SearchResult(Long total, Long totalPage, List<T> items, Integer pageSize, Integer currPage) {
		this.resultList = items;
		this.total = total;
		this.pageSize = pageSize;
		this.curPageNO = currPage;
		this.pageCount = totalPage;
	}

	public SearchResult(Long total, Long totalPage, List<T> items, List<SearchCategoryDTO> categories, List<BrandBO> brands, List<Map<String, Object>> specs) {
		this.resultList = items;
		this.total = total;
		this.pageCount = totalPage;
		this.categories = categories;
		this.brands = brands;
		this.specs = specs;
	}

	public SearchResult(Long total, Long totalPage, List<T> items, List<SearchCategoryDTO> categories, List<BrandBO> brands, List<Map<String, Object>> specs, Integer pageSize, Integer currPage) {
		this.resultList = items;
		this.total = total;
		this.curPageNO = currPage;
		this.pageSize = pageSize;
		this.categories = categories;
		this.brands = brands;
		this.pageCount = totalPage;
		this.specs = specs;
	}

	public SearchResult() {

	}

	/**
	 * 当前页.
	 */
	@Schema(description = "当前页")
	private int curPageNO;

	/**
	 * 每页大小.
	 */
	@Schema(description = "每页大小")
	private int pageSize;

	/**
	 * 总记录数.
	 */
	@Schema(description = "总记录数")
	private long total = 0;

	/**
	 * 总页数
	 */
	@Schema(description = "总页数")
	private long pageCount;

	/**
	 * 结果集.
	 */
	@Schema(description = "返回结果集")
	private List<T> resultList;

	/**
	 * 分类的集合
	 */
	@Schema(description = "分类的集合")
	private List<SearchCategoryDTO> categories = EMPTY_LIST;

	/**
	 * 品牌的集合
	 */
	@Schema(description = "品牌的集合")
	private List<BrandBO> brands = EMPTY_LIST;

	/**
	 * 规格参数的过滤条件
	 */
	@Schema(description = "规格参数的过滤条件")
	private List<Map<String, Object>> specs;
}
