/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.basic.dao.AttachmentTreeDao;
import com.legendshop.basic.entity.AttachmentTree;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件类型Dao.
 *
 * @author legendshop
 */
@Repository
public class AttachmentTreeDaoImpl extends GenericDaoImpl<AttachmentTree, Long> implements AttachmentTreeDao {

	@Override
	public List<AttachmentTree> getAttachmentTreeByPid(Long pId, Long shopId) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId).eq("parentId", pId));
	}

	@Override
	public int updateAttachmentTreeNameById(Integer id, String name) {
		return this.update("update ls_attmnt_tree set name=? where id=?", name, id);
	}

	@Override
	public AttachmentTree getAttachmentTree(Long treeId, Long shopId) {
		List<AttachmentTree> attmntList = this.queryByProperties(new EntityCriterion().eq("id", treeId).eq("shopId", shopId));
		if (CollUtil.isNotEmpty(attmntList)) {
			return attmntList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查找所有子节点树
	 */
	@Override
	public List<AttachmentTree> getAllChildByTreeId(Long treeId) {
		//由于采用了递归算法，因此不同的数据库会有不同的sql实现, 见dialectSQL.dal.xml
		String sql = "select * from ls_attmnt_tree where FIND_IN_SET(id, queryChildrenAttachmentTree(?))";
		return this.query(sql, AttachmentTree.class, treeId);
	}


}
