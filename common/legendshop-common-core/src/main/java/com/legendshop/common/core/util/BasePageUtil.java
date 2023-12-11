/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.StringConstant;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

/**
 * 分页对象工具类，json操作
 *
 * @author legendshop
 */
@UtilityClass
public class BasePageUtil {

	public final Integer PAGE_SIZE = 30;

	/**
	 * 获取page的json数据
	 *
	 * @param array
	 * @param pageSize
	 * @param <T>
	 * @return
	 */
	public <T> String getPageJson(T[] array, int pageSize) {
		List<T> lists = Arrays.asList(array);
		return getPageJson(lists, pageSize);
	}

	/**
	 * 获取page的json数据
	 *
	 * @param lists
	 * @param pageSize
	 * @param <T>
	 * @return
	 */
	public <T> String getPageJson(List<T> lists, int pageSize) {
		int size = lists.size();
		int totalPage = PageUtil.totalPage(size, pageSize);
		PageUtil.setFirstPageNo(1);
		JSONObject jsonObject = new JSONObject();
		for (int i = 1; i <= totalPage; i++) {
			int fromIndex = PageUtil.getStart(i, pageSize);
			int endIndex = Math.min(size, i * pageSize);
			//如果总数少于PAGE_SIZE,为了防止数组越界,toIndex直接使用totalCount即可
			List<T> subList = lists.subList(fromIndex, endIndex);
			jsonObject.set(i + "", CollUtil.join(subList, StringConstant.COMMA));
		}
		return JSONUtil.toJsonStr(jsonObject);
	}

	/**
	 * 获取当前页的数据
	 *
	 * @param pageNo
	 * @param json
	 * @return
	 */
	public String getPageData(int pageNo, String json) {
		if (!JSONUtil.isJson(json)) {
			return null;
		}
		JSONObject jsonObject = JSONUtil.parseObj(json);
		String pageData = jsonObject.getStr(pageNo + "");
		return pageData;
	}
}
