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
import com.legendshop.basic.dto.AttachmentFileHistoryDTO;
import com.legendshop.basic.entity.AttachmentFileHistory;

import java.util.List;

/**
 * (AttachmentFileHistory)表数据库访问层
 *
 * @author legendshop
 * @since 2022-02-15 16:11:06
 */
public interface AttachmentFileHistoryDao extends GenericDao<AttachmentFileHistory, Long> {


	/**
	 * 判断用户是否存在这个路径
	 *
	 * @param userId
	 * @param fileId
	 * @return
	 */
	AttachmentFileHistoryDTO getFileId(Long userId, String userType, String folderId);

	/**
	 * 获取用户记录数量
	 *
	 * @param userId
	 * @return
	 */
	AttachmentFileHistoryDTO getUserCount(Long userId, String userType);

	/**
	 * 更新文件夹记录
	 *
	 * @param id
	 * @param folderId
	 * @return
	 */
	Integer updateFileHistory(Long id, String folderId);

	/**
	 * 获取该用户的历史记录
	 *
	 * @param userId
	 * @return
	 */
	List<AttachmentFileHistoryDTO> queryByUserId(Long userId, String userType);
}
