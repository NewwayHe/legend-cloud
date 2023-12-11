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
import com.legendshop.basic.entity.AttachmentFolder;

import java.util.List;

/**
 * (AttachmentFolder)表数据库访问层
 *
 * @author legendshop
 * @since 2021-07-06 17:32:45
 */
public interface AttachmentFolderDao extends GenericDao<AttachmentFolder, Long> {


	/**
	 * 更新文件路径
	 *
	 * @param fileFolderId
	 * @param attachmentId
	 * @return
	 */
	Integer updateAttPathByFileFolderId(Long fileFolderId, Long attachmentId);

	/**
	 * 查找文件夹下是否有图片
	 *
	 * @param id
	 * @return
	 */
	List<Long> queryFileById(Long id);

	/**
	 * 根据图片id删除图片
	 *
	 * @param id
	 * @return
	 */
	Integer deleteByAttachId(Long id);

	/**
	 * 根据图片id删除图片关联
	 *
	 * @param ids
	 * @return
	 */
	Integer deleteByAttachId(List<Long> ids);


	Long getFolderIdByAttId(Long attId);

	Integer updateFileIdByFileId(Long newId, Long fileFolderId);


	/**
	 * 根据图片id获取文件夹id
	 *
	 * @param id
	 * @return
	 */
	Long getFileIdByAttId(Long id);

	/**
	 * 查询文件夹总层级
	 *
	 * @return
	 */
	Integer getMaxTypeId();

}
