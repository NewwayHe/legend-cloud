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
import cn.legendshop.jpaplus.PageSupportHandler;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.dao.AttachmentDao;
import com.legendshop.basic.dto.AttachmentDTO;
import com.legendshop.basic.entity.Attachment;
import com.legendshop.basic.query.AttachmentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件dao
 *
 * @author legendshop
 */
@Repository
public class AttachmentDaoImpl extends GenericDaoImpl<Attachment, Long> implements AttachmentDao {

	@Override
	public Long getIdByPath(String url) {
		return this.get("select id from ls_attachment where file_path = ?", Long.class, url);
	}

	@Override
	public PageSupportHandler<AttachmentDTO> queryPage(AttachmentQuery attachmentQuery) {
		return null;
	}

	@Override
	public Integer updateManagedFlage(String filePath) {

		return update("update ls_attachment set managed_flag = 1 where file_path = ?", filePath);
	}

	@Override
	public Integer updateName(String fileName, Long id) {
		return update("update ls_attachment set file_name = ? where id = ?", fileName, id);
	}

	@Override
	public List<Long> getIdByName(String fileName) {
		return query("select id from ls_attachment where file_name = ?", Long.class, fileName);
	}

	@Override
	public String getNameByUrl(String path) {
		return get("select file_name from ls_attachment where file_path = ?", String.class, path);
	}

	@Override
	public Attachment getAttachmentByPath(String filePath) {

		return this.getByProperties(new EntityCriterion().eq("filePath", filePath));
	}

	@Override
	public Integer updateNameByPath(String fileName, List<String> filePath) {
		return update("update ls_attachment set file_name = ? where file_path = ? ", fileName, filePath);
	}

	@Override
	public List<Long> getCountByName(String fileName) {

		return query("select id from ls_attachment where file_name like ? and id = ?", Long.class, "%" + fileName + "%");
	}

	@Override
	public List<Long> getIdsByName(String fileName) {
		return query("select id from ls_attachment where file_name like ?", Long.class, "%" + fileName + "%");
	}

	@Override
	public Long getAttIdByName(String fileName, Long fileFolderId) {

		return get("select la.id from ls_attachment as la left join ls_attachment_folder as laf on  la.id = laf.attachment_id where la.file_name = ? and laf.file_folder_id = ?", Long.class, fileName, fileFolderId);
	}

	@Override
	public Integer deleteByAttId(List<Long> attIds) {
		if (CollUtil.isEmpty(attIds)) {
			return -1;
		}
		StringBuffer sql = new StringBuffer("update ls_attachment set status = 0 where status = 1 and id in ( ");
		for (Long attId : attIds) {
			sql.append(attId).append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString());
	}

	@Override
	public Long getIdByFilePath(String url, Boolean uploadType) {
		if (uploadType) {
			return this.get("select id from ls_attachment where file_path = ?", Long.class, url);
		}
		return this.get("select id from ls_attachment where video_path = ?", Long.class, url);
	}

	@Override
	public Integer updateVideoManagedFlage(String path) {
		return update("update ls_attachment set managed_flag = 1 where video_path = ?", path);
	}

	@Override
	public int deleteAttachmentByFilePath(String filePath) {
		return update("delete from ls_attachment where file_path=?", filePath);
	}

	@Override
	public List<Attachment> getAttachmentByShortPath(Long userId, List<String> shortPath) {
		LambdaEntityCriterion<Attachment> criterion = new LambdaEntityCriterion<>(Attachment.class);
		criterion.eq(Attachment::getUserId, userId)
				.in(Attachment::getShortPath, shortPath);
		return queryByProperties(criterion);
	}

}
