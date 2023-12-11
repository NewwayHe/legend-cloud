/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.executor;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelRunner
 * @date 2022/4/26 17:10
 * @description：导出文件线程
 */
public interface ExportExcelRunnable extends Runnable {

	/**
	 * 文件名
	 *
	 * @return
	 */
	String getFileName();
}
