/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.constant;

/**
 * 基本的常量
 *
 * @author legendshop
 */
public interface CommonConstants {

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 1;

	/**
	 * 失败标记
	 */
	Integer FAIL = 0;


	/**
	 * 异常状态
	 */
	Integer STATUS_ERROR = 0;

	/**
	 * 正常状态
	 */
	Integer STATUS_NORMAL = 1;


	/**
	 * 滑块验证码
	 */
	String IMAGE_CODE_TYPE = "blockPuzzle";

	/**
	 * 菜单树根节点
	 */
	Long MENU_TREE_ROOT_ID = -1L;
	/**
	 * 类目树根节点
	 */
	Long CATEGORY_TREE_ROOT_ID = -1L;

	String OK = "OK";

	/**
	 * 操作失败
	 */
	String NO = "NO";

	String ORDER_INDICATOR = "orderIndicator";
}
