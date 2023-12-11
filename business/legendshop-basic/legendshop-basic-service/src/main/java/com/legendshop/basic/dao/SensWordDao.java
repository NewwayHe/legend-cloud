/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.entity.SensWord;
import com.legendshop.basic.query.SensWordQuery;

import java.util.List;

/**
 * 敏感字过滤表(SensWord)表数据库访问层
 *
 * @author legendshop
 * @since 2021-06-30 14:19:30
 */
public interface SensWordDao extends GenericDao<SensWord, Long> {


	/**
	 * 查询需要全局过滤的敏感词
	 *
	 * @return
	 */
	List<String> getWords();

	/**
	 * 分页查询所有敏感字
	 *
	 * @param sensWordQuery
	 * @return
	 */
	PageSupport<SensWordDTO> querySensWordPage(SensWordQuery sensWordQuery);

	/**
	 * 判断敏感词是否存在
	 *
	 * @param sensWordDTO
	 * @return
	 */
	String getBySensWord(SensWordDTO sensWordDTO);

}
