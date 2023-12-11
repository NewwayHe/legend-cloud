/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SensWordDao;
import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.query.SensWordQuery;
import com.legendshop.basic.service.SensWordService;
import com.legendshop.basic.service.convert.SensWordConverter;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 敏感字过滤表(SensWord)表服务实现类
 *
 * @author legendshop
 * @since 2021-06-30 14:19:30
 */
@Service
public class SensWordServiceImpl extends BaseServiceImpl<SensWordDTO, SensWordDao, SensWordConverter> implements SensWordService {

	@Autowired
	private SensWordDao sensWordDao;

	@Autowired
	private SensWordConverter sensWordConverter;

	/**
	 * @param src 要过滤的语句
	 */
	@Override
	public Set<String> checkSensitiveWords(String src) {
		if (StrUtil.isBlank(src)) {
			return null;
		}
		List<String> sensitiveWordList = sensWordDao.getWords();
		Map<Character, List<String>> wordMap = new HashMap<Character, List<String>>(16);
		for (String s : sensitiveWordList) {
			char c = s.charAt(0);
			List<String> strs = wordMap.get(c);
			if (strs == null) {
				strs = new ArrayList<String>();
				wordMap.put(c, strs);
			}
			strs.add(s);
		}
		String temp = null;
		String find;
		char c;
		Set<String> findwords = new HashSet<String>();
		for (int i = 0; i < src.length(); i++) {
			c = src.charAt(i);
			find = null;
			if (wordMap.containsKey(c)) {
				List<String> words = wordMap.get(c);
				for (String s : words) {
					temp = src.substring(i, (s.length() <= (src.length() - i)) ? i + s.length() : i);
					if (s.equals(temp)) {
						find = s;
						break;
					}
				}
			}
			if (find != null && findwords.size() <= 10) {
				findwords.add(find);
				i += (find.length() - 1);
			}
		}

		return findwords;
	}

	@Override
	public PageSupport<SensWordDTO> querySensWordPage(SensWordQuery sensWordQuery) {
		return sensWordDao.querySensWordPage(sensWordQuery);
	}

	@Override
	public Long saveSensWord(SensWordDTO sensWordDTO) {
		sensWordDTO.setCreateTime(new Date());
		return sensWordDao.save(sensWordConverter.from(sensWordDTO));
	}

	@Override
	public int updateSensWord(SensWordDTO sensWordDTO) {
		sensWordDTO.setUpdateTime(new Date());
		return sensWordDao.update(sensWordConverter.from(sensWordDTO));
	}

	@Override
	public String getBySensWord(SensWordDTO sensWordDTO) {
		return sensWordDao.getBySensWord(sensWordDTO);
	}
}
