/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.dto.DeptDTO;
import com.legendshop.user.dto.DeptTree;

import java.util.List;

/**
 * 部门服务.
 *
 * @author legendshop
 */
public interface DeptService extends BaseService<DeptDTO> {

	Boolean deleteByDeptId(Long id) throws Exception;

	List<DeptDTO> querySubDept(Long deptId);

	List<DeptTree> queryAllDeptTree();

}
