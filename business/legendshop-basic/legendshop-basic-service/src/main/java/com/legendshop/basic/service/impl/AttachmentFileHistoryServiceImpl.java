/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.dao.AttachmentFileHistoryDao;
import com.legendshop.basic.dto.AttachmentFileHistoryDTO;
import com.legendshop.basic.service.AttachmentFileHistoryService;
import com.legendshop.basic.service.convert.AttachmentFileHistoryConverter;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (AttachmentFileHistory)表服务实现类
 *
 * @author legendshop
 * @since 2022-02-15 16:11:09
 */
@Service
@AllArgsConstructor
public class AttachmentFileHistoryServiceImpl extends BaseServiceImpl<AttachmentFileHistoryDTO, AttachmentFileHistoryDao, AttachmentFileHistoryConverter> implements AttachmentFileHistoryService {

	private final AttachmentFileHistoryDao attachmentFileHistoryDao;

	@Override
	public AttachmentFileHistoryDTO getFileId(Long userId, String userType, String folderId) {
		return attachmentFileHistoryDao.getFileId(userId, userType, folderId);
	}

	@Override
	public AttachmentFileHistoryDTO getUserCount(Long userId, String userType) {
		return attachmentFileHistoryDao.getUserCount(userId, userType);
	}

	@Override
	public Integer updateFileHistory(Long id, String folderId) {
		return attachmentFileHistoryDao.updateFileHistory(id, folderId);
	}

	@Override
	public List<AttachmentFileHistoryDTO> queryByUserId(Long userId, String userType) {
		return attachmentFileHistoryDao.queryByUserId(userId, userType);
	}
}
