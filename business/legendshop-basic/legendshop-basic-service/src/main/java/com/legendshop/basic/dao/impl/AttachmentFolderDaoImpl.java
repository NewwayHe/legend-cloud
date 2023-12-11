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
import com.legendshop.basic.dao.AttachmentFolderDao;
import com.legendshop.basic.entity.AttachmentFolder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (AttachmentFolder)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-07-06 17:32:45
 */
@Repository
public class AttachmentFolderDaoImpl extends GenericDaoImpl<AttachmentFolder, Long> implements AttachmentFolderDao {


	@Override
	public Integer updateAttPathByFileFolderId(Long fileFolderId, Long attachmentId) {
		return this.update("update ls_attachment_folder set file_folder_id = ? where attachment_id = ?", fileFolderId, attachmentId);
	}

	@Override
	public List<Long> queryFileById(Long id) {
		return query("select attachment_id from ls_attachment_folder where file_folder_id = ?", Long.class, id);
	}

	@Override
	public Integer deleteByAttachId(Long id) {
		return update("delete from ls_attachment_folder where attachment_id = ?", id);
	}

	@Override
	public Integer deleteByAttachId(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return -1;
		}

		StringBuffer sql = new StringBuffer("delete from ls_attachment_folder where attachment_id in ( ");
		for (Long id : ids) {
			sql.append(id).append(",");
		}

		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString());
	}

	@Override
	public Long getFolderIdByAttId(Long attId) {
		return get("select file_folder_id from ls_attachment_folder where attachment_id = ?", Long.class, attId);
	}

	@Override
	public Integer updateFileIdByFileId(Long newId, Long fileFolderId) {
		return update("update ls_attachment_folder set file_folder_id = ? where file_folder_id = ?", Integer.class, newId, fileFolderId);
	}

	@Override
	public Integer getMaxTypeId() {
		return get("select max(type_id) from ls_attachment_file_folder", Integer.class);
	}

	@Override
	public Long getFileIdByAttId(Long id) {
		return get("select file_folder_id from ls_attachment_folder where attachment_id = ?", Long.class, id);
	}


}
