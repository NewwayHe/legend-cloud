/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.user.dao.AdminUserDao;
import com.legendshop.user.dao.DeptDao;
import com.legendshop.user.dto.DeptDTO;
import com.legendshop.user.dto.DeptTree;
import com.legendshop.user.entity.AdminUser;
import com.legendshop.user.entity.Dept;
import com.legendshop.user.service.DeptService;
import com.legendshop.user.service.convert.DeptConverter;
import com.legendshop.user.utils.TreeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class DeptServiceImpl extends BaseServiceImpl<DeptDTO, DeptDao, DeptConverter> implements DeptService {

	private final DeptDao deptDao;
	private final AdminUserDao adminUserDao;
	private final DeptConverter deptConverter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R save(DeptDTO dto) {
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		Dept dept = deptConverter.from(dto);
		dept.setParentId(Optional.ofNullable(dept.getParentId()).orElse(-1L));
		Long id = deptDao.save(dept);
		return R.ok(Boolean.TRUE);
	}

	@Override
	public Boolean update(DeptDTO dto) {
		// 更新部门信息
		dto.setUpdateTime(new Date());
		super.update(dto);
		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteByDeptId(Long id) throws Exception {
		//删除指定部门
		//查询是否有子部门，如果有子部门，则不能删除
		List<Dept> deptList = deptDao.queryByProperties(new EntityCriterion().eq("parentId", id));
		if (deptList.size() > 0) {
			throw new Exception("当前部门存在子部门，不能删除!");
		}

		//查询当前部门是否存在
		Dept dept = deptDao.getById(id);
		if (dept == null) {
			throw new Exception("当前部门不存在!");
		}

		//查看部门是否已有管理员在使用
		List<AdminUser> adminUsers = adminUserDao.queryByProperties(new EntityCriterion().eq("deptId", id));
		if (adminUsers.size() > 0) {
			throw new Exception("当前部门已使用，不能删除，请检查管理员是否设置了该部门!");
		}

		deptDao.delete(dept);
		return Boolean.TRUE;
	}

	@Override
	public List<DeptDTO> querySubDept(Long deptId) {
		return this.deptDao.querySubDept(deptId);
	}


	@Override
	public List<DeptTree> queryAllDeptTree() {
		// 查询全部部门
		List<Dept> deptList = deptDao.queryAll();
		//转换为树结构对像
		return getDeptTrees(deptList);
	}

	private List<DeptTree> getDeptTrees(List<Dept> deptList) {
		List<DeptTree> collect = deptList.stream().map(dept -> {
			DeptTree node = new DeptTree();
			node.setId(dept.getId());
			node.setParentId(dept.getParentId());
			node.setName(dept.getName());
			node.setContact(dept.getContact());
			node.setMobile(dept.getMobile());
			return node;
		}).collect(Collectors.toList());
		return TreeUtil.build(collect, CommonConstants.CATEGORY_TREE_ROOT_ID);
	}
}
