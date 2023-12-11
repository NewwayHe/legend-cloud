/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.dao.BusinessSettingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统的常量表
 *
 * @author legendshop
 */
@Service
public class MapCodeTablesCache implements InitializingBean {

	/**
	 * The log.
	 */
	private static Logger log = LoggerFactory.getLogger(MapCodeTablesCache.class);

	/**
	 * The code tables.
	 */
	private Map<String, Map<String, String>> codeTables = new HashMap<String, Map<String, String>>();

	@Autowired
	private BusinessSettingDao constTableDao;

	/**
	 * Gets the code tables.
	 *
	 * @return the code tables
	 */
	public Map<String, Map<String, String>> getCodeTables() {
		return codeTables;
	}

	/**
	 * Sets the code tables.
	 *
	 * @param codeTables the code tables
	 */
	public void setCodeTables(Map<String, Map<String, String>> codeTables) {
		this.codeTables = codeTables;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.tag.TableCache#getCodeTable(java.lang.String)
	 */
//	@Override
//	public Map<String, String> getCodeTable(String beanName) {
//		if (beanName == null || beanName.trim().length() == 0) {
//			return null;
//		}
//		Map<String, String> table = codeTables.get(beanName);
//		return table;
//	}
//
//	/**
//	 * Inits the code tables cache.
//	 */
//	@Override
//	public void initCodeTablesCache() {
//		List<ConstTable> list = constTableDao.loadAllConstTable();
//		for (ConstTable constTable : list) {
//			String type = constTable.getType();
//			Map<String, String> items = codeTables.get(type);
//			if (items == null) {
//				items = new LinkedHashMap<String, String>();
//			}
//			items.put(constTable.getKey(), constTable.getValue());
//			codeTables.put(type, items);
//		}
//
//		log.info("codeTables size = {}", codeTables.size());
//	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Start to init constants table");
		//initCodeTablesCache();
	}
}
