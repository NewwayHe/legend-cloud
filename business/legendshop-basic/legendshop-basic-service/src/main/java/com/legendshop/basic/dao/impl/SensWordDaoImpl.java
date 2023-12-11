/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import com.legendshop.basic.dao.SensWordDao;
import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.entity.SensWord;
import com.legendshop.basic.query.SensWordQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 敏感字过滤表(SensWord)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-06-30 14:19:30
 */
@Repository
public class SensWordDaoImpl extends GenericDaoImpl<SensWord, Long> implements SensWordDao {

	@Override
	public List<String> getWords() {
		QueryMap map = new QueryMap();
		String sql = "select words from ls_sens_word where is_global = 1";
		return getJdbcTemplate().queryForList(sql, map.toArray(), String.class);
	}

	@Override
	public PageSupport<SensWordDTO> querySensWordPage(SensWordQuery sensWordQuery) {
		LambdaCriteriaQuery<SensWordDTO> query = new LambdaCriteriaQuery<>(SensWordDTO.class, sensWordQuery.getCurPage());
		query.setPageSize(sensWordQuery.getPageSize());
		query.like(SensWordDTO::getWords, sensWordQuery.getWords(), MatchMode.ANYWHERE);
		query.addDescOrder(SensWordDTO::getUpdateTime);
		return queryDTOPage(query);
	}

	@Override
	public String getBySensWord(SensWordDTO sensWordDTO) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("words", sensWordDTO.getWords());
		queryMap.put("id", sensWordDTO.getId());

		SQLOperation operation = this.getSQLAndParams("SensWord.getBySensWord", queryMap);
		return get(operation.getSql(), String.class, operation.getParams());
	}

}
