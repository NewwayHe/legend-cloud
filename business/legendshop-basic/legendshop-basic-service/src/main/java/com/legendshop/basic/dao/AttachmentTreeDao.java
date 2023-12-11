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
import com.legendshop.basic.entity.AttachmentTree;

import java.util.List;

/**
 * 附件类型Dao.
 *
 * @author legendshop
 */
public interface AttachmentTreeDao extends GenericDao<AttachmentTree, Long> {

	List<AttachmentTree> getAttachmentTreeByPid(Long pId, Long shopId);

	int updateAttachmentTreeNameById(Integer id, String name);

	AttachmentTree getAttachmentTree(Long treeId, Long shopId);

	List<AttachmentTree> getAllChildByTreeId(Long treeId);

}
