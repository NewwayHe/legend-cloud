/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.query.SensWordQuery;
import com.legendshop.common.core.service.BaseService;

import java.util.Set;

/**
 * 敏感字过滤表(SensWord)表服务接口
 *
 * @author legendshop
 * @since 2021-06-30 14:19:30
 */
public interface SensWordService extends BaseService<SensWordDTO> {

	/**
	 * 关键字过滤
	 *
	 * @param src
	 * @return
	 */
	Set<String> checkSensitiveWords(String src);

	/**
	 * 分页查询所有敏感字
	 *
	 * @param sensWordQuery
	 * @return
	 */
	PageSupport<SensWordDTO> querySensWordPage(SensWordQuery sensWordQuery);

	/**
	 * 保存敏感字
	 *
	 * @param sensWordDTO
	 * @return
	 */
	Long saveSensWord(SensWordDTO sensWordDTO);

	/**
	 * 修改敏感字
	 *
	 * @param sensWordDTO
	 * @return
	 */
	int updateSensWord(SensWordDTO sensWordDTO);

	/**
	 * 判断敏感词是否存在
	 *
	 * @param sensWordDTO
	 * @return
	 */
	String getBySensWord(SensWordDTO sensWordDTO);

}
