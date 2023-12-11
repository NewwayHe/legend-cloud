/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.comparer;

/**
 * 对象比较接口
 *
 * @param <T>
 * @param <D>
 * @author legendshop
 */
public interface DataComparer<T, D> {

	/**
	 * 是否需要更新.
	 *
	 * @param dto   the dto
	 * @param dbObj the db obj
	 * @param obj   the obj
	 * @return true, if successful
	 */
	boolean needUpdate(T dto, D dbObj, Object obj);

	/**
	 * 在新的对象列表中是否存在.
	 *
	 * @param dto   the dto
	 * @param dbObj the db obj
	 * @return true, if is exist
	 */
	boolean isExist(T dto, D dbObj);


	/**
	 * 复制到新的数据模型.
	 *
	 * @param dtoObj 页面传递过来的DTO
	 * @param obj    the 要组装的对象的部分元素
	 * @return the 将要操作的对象
	 */
	D copyProperties(T dtoObj, Object obj);


}
