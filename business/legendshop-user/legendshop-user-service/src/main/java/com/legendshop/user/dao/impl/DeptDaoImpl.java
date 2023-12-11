/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;


import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.user.dao.DeptDao;
import com.legendshop.user.dto.DeptDTO;
import com.legendshop.user.entity.Dept;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门DaoImpl.
 *
 * @author legendshop
 */
@Repository
public class DeptDaoImpl extends GenericDaoImpl<Dept, Long> implements DeptDao {


	@Override
	public List<DeptDTO> querySubDept(Long parentId) {
		return query(getSQL("Dept.querySubDept"), DeptDTO.class, parentId);
	}
}
