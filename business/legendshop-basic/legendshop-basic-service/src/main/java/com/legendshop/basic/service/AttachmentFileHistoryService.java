/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.AttachmentFileHistoryDTO;
import com.legendshop.common.core.service.BaseService;

import java.util.List;

/**
 * (AttachmentFileHistory)表服务接口
 *
 * @author legendshop
 * @since 2022-02-15 16:11:08
 */
public interface AttachmentFileHistoryService extends BaseService<AttachmentFileHistoryDTO> {


	/**
	 * 获取用户的文件夹记录
	 *
	 * @param userId
	 * @param fileId
	 * @return
	 */
	AttachmentFileHistoryDTO getFileId(Long userId, String userType, String folderId);

	/**
	 * 获取当前用户的记录数量
	 *
	 * @param userId
	 * @return
	 */
	AttachmentFileHistoryDTO getUserCount(Long userId, String userType);

	/**
	 * 更新历史数据
	 *
	 * @param id
	 * @param folderId
	 * @return
	 */
	Integer updateFileHistory(Long id, String folderId);

	/**
	 * 获取当前用户的文件夹历史
	 *
	 * @param userId
	 * @return
	 */
	List<AttachmentFileHistoryDTO> queryByUserId(Long userId, String userType);
}
