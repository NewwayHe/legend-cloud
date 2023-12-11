/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.comparer;

import cn.hutool.core.collection.CollUtil;

import java.util.*;

/**
 * 两个对象列表的对照转移.
 *
 * @param <T>emp data 从外面传过来的数据
 * @param <D>b   data 数据库里的数据
 * @author legendshop
 */
public class DataListComparer<T, D> {

	/**
	 * The comparer.
	 */
	private DataComparer<T, D> comparer;

	/**
	 * Instantiates a new data list comparer.
	 *
	 * @param comparer the comparer
	 */
	public DataListComparer(DataComparer<T, D> comparer) {
		this.comparer = comparer;
	}

	public DataComparerResult<D> compare(List<T> dtoList, List<D> dbList) {
		return compare(dtoList, dbList, null);
	}

	/**
	 * 对比两个列表的内容是否有差异
	 * 结果：
	 * -1： 需要删除记录
	 * 0： 需要更新记录
	 * 1： 新增记录.
	 *
	 * @param dtoList： 页面过来的数据
	 * @param dbList   数据库里的数据
	 * @param obj      the obj
	 * @return the map
	 */
	public DataComparerResult<D> compare(List<T> dtoList, List<D> dbList, Object obj) {
		if (dtoList == null) {
			return null;
		}

		Map<Integer, List<D>> resultMap = new HashMap<Integer, List<D>>(16);
		List<D> addList = new ArrayList<D>();
		List<D> updateList = new ArrayList<D>();
		List<D> delList = new ArrayList<D>(dbList);

		if (CollUtil.isNotEmpty(dtoList) || CollUtil.isNotEmpty(dbList)) {

			Iterator<T> dtoIt = dtoList.iterator();
			while (dtoIt.hasNext()) {
				T dto = dtoIt.next();
				D dbObject = getBizObject(dto, dbList, comparer);
				if (dbObject == null) {
					addList.add(comparer.copyProperties(dto, obj));
				} else {
					if (comparer.needUpdate(dto, dbObject, obj)) {
						updateList.add(dbObject);
					}
					delList.remove(dbObject);
				}
			}
		}
		resultMap.put(-1, delList);
		resultMap.put(0, updateList);
		resultMap.put(1, addList);
		return new DataComparerResult<D>(resultMap);

	}

	/**
	 * 得到业务对象.
	 *
	 * @param dto      the dto
	 * @param dbList   the db list
	 * @param comparer the comparer
	 * @return the biz object
	 */
	private D getBizObject(T dto, List<D> dbList, DataComparer<T, D> comparer) {
		if (CollUtil.isNotEmpty(dbList)) {
			for (D business : dbList) {
				if (comparer.isExist(dto, business)) {
					return business;
				}
			}
		}
		return null;
	}


}
