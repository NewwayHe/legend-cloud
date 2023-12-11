/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.user.dto.DeptDTO;
import com.legendshop.user.entity.Dept;

import java.util.List;

/**
 * 部门Dao.
 *
 * @author legendshop
 */
public interface DeptDao extends GenericDao<Dept, Long> {

	List<DeptDTO> querySubDept(Long parentId);

}
