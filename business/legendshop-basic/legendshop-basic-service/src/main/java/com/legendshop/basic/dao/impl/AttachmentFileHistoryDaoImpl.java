/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.basic.dao.AttachmentFileHistoryDao;
import com.legendshop.basic.dto.AttachmentFileHistoryDTO;
import com.legendshop.basic.entity.AttachmentFileHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (AttachmentFileHistory)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-02-15 16:11:07
 */
@Repository
public class AttachmentFileHistoryDaoImpl extends GenericDaoImpl<AttachmentFileHistory, Long> implements AttachmentFileHistoryDao {

	@Override
	public AttachmentFileHistoryDTO getFileId(Long userId, String userType, String folderId) {
		return get("select * from ls_attachment_file_history where user_id = ? and folder_id = ? and user_type = ?", AttachmentFileHistoryDTO.class, userId, folderId, userType);
	}

	@Override
	public AttachmentFileHistoryDTO getUserCount(Long userId, String userType) {
		return get("select count(*) as counts,(select id from ls_attachment_file_history where user_id = ? and user_type = ? order by create_time limit 1) as id from ls_attachment_file_history where user_id = ? and user_type = ?", AttachmentFileHistoryDTO.class, userId, userType, userId, userType);
	}

	@Override
	public Integer updateFileHistory(Long id, String folderId) {
		return update("update  ls_attachment_file_history set folder_id = ?, update_time = ? where id = ?", folderId, DateUtil.date(), id);
	}

	@Override
	public List<AttachmentFileHistoryDTO> queryByUserId(Long userId, String userType) {
		if ("ADMIN_PIC".equals(userType)) {
			return query("select * from ls_attachment_file_history where user_id = ? order by update_time desc", AttachmentFileHistoryDTO.class, userId);
		}
		return query("select * from ls_attachment_file_history where user_id = ? and user_type = ? order by update_time desc", AttachmentFileHistoryDTO.class, userId, userType);
	}
}
