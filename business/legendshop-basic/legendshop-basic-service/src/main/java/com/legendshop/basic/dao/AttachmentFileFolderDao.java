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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AttachmentDTO;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.query.AttachmentFileFolderQuery;

import java.util.List;

/**
 * (AttachmentFileFolder)表数据库访问层
 *
 * @author legendshop
 * @since 2021-07-06 14:19:22
 */
public interface AttachmentFileFolderDao extends GenericDao<AttachmentFileFolder, Long> {

	/**
	 * 查询当前文件夹
	 *
	 * @param attachmentFileFolderQuery 查询对象
	 * @return 分页结果
	 */
	PageSupport<AttachmentFileFolder> queryPageByFolderId(AttachmentFileFolderQuery attachmentFileFolderQuery);

	/**
	 * 平台根目录，根据父级ID查询文件夹列表
	 *
	 * @param parentId 父级ID
	 * @return 文件夹列表
	 */
	List<AttachmentFileFolderDTO> queryByParentId(Long parentId);

	/**
	 * 平台根目录，查询当前文件夹内所有图片
	 *
	 * @param fileFolderId 文件夹ID
	 * @return 图片列表
	 */
	List<AttachmentDTO> queryAttachment(Long fileFolderId);

	/**
	 * 分页查询附件表
	 *
	 * @param attachmentFileFolderQuery 查询对象
	 * @return 分页结果
	 */
	List<AttachmentFileFolderDTO> queryAttachmentPage(AttachmentFileFolderQuery attachmentFileFolderQuery);

	/**
	 * 商家第一次访问，根据商家ID获取对象
	 *
	 * @param shopId 商家ID
	 * @return 对象ID
	 */
	Long getByShopId(Long shopId);

	/**
	 * 获取根目录ID，商家
	 *
	 * @param parentId 父级ID
	 * @param shopId   商家ID
	 * @return ID
	 */
	Long getIdByParentId(Long parentId, Long shopId);

	/**
	 * 获取根目录，非商家
	 *
	 * @param parentId 父级ID
	 * @return ID
	 */
	Long getIdByParentId(Long parentId);

	/**
	 * 更新文件夹名字
	 *
	 * @param fileName 文件名
	 * @param id       文件夹ID
	 * @return 更新数量
	 */
	Integer updateFileFolderName(String fileName, Long id);

	/**
	 * 查找对应的商家文件夹
	 *
	 * @param fileId 文件ID
	 * @param shopId 商家ID
	 * @return 商家文件夹列表
	 */
	List<AttachmentFileFolderDTO> queryByFileIdAndShopId(Long fileId, Long shopId);

	/**
	 * 查询对应商家图片
	 *
	 * @param fileId 文件ID
	 * @param shopId 商家ID
	 * @return 商家图片列表
	 */
	List<AttachmentDTO> queryAttachmentByFileIdAndShopId(Long fileId, Long shopId);

	/**
	 * 根据名字查询当前文件夹的所有文件名
	 *
	 * @param fileName 文件名
	 * @param id       文件夹ID
	 * @return 文件名列表
	 */
	String getNameByFileName(String fileName, Long id);

	/**
	 * 根据名称查询文件夹
	 *
	 * @param fileName 文件名
	 * @param shopId   商家ID
	 * @param parentId 父级ID
	 * @return 文件夹对象
	 */
	AttachmentFileFolder getByFileName(String fileName, Long shopId, Long parentId);

	/**
	 * 更新所有商家文件夹的ParentID
	 *
	 * @param parentId 父级ID
	 * @return 更新数量
	 */
	Integer updateAllParentId(Long parentId);


	/**
	 * 查询文件夹分页
	 *
	 * @param attachmentFileFolderQuery
	 * @return
	 */
	PageSupport<AttachmentFileFolderDTO> queryPage(AttachmentFileFolderQuery attachmentFileFolderQuery);


	/**
	 * 修改该文件夹上级id
	 *
	 * @param fileFolderId
	 * @param id
	 * @param typeId
	 * @return
	 */
	Integer updateParentId(Long fileFolderId, Long id, Long typeId);

	/**
	 * 计算文件夹总数
	 *
	 * @param attachmentFileFolderQuery
	 * @return
	 */
	Long getCountByQuery(AttachmentFileFolderQuery attachmentFileFolderQuery);

	/**
	 * 计算文件总数
	 *
	 * @param attachmentFileFolderQuery
	 * @return
	 */
	Long getAttCountByQuery(AttachmentFileFolderQuery attachmentFileFolderQuery);

	/**
	 * [平台]根据id,查找上级id
	 *
	 * @param id
	 * @return
	 */
	Long getParentIdById(Long id);

	/**
	 * [商家]根据id,shopId查找上级id
	 *
	 * @param id
	 * @return
	 */
	Long getParentIdById(Long id, Long shopId);

	/**
	 * [平台] 查询所有非商家的文件夹
	 *
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryFolder();

	/**
	 * [商家] 查询所有本商家的文件夹
	 *
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryShopFolder(Long shopId);

	List<Long> queryIds();

	Long getIdByShopId(Long id);

	Long getIdByShopIdFirst(Long id);

	/**
	 * [商家]修改路径展示列表
	 *
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryFileFolderList(Long id, Long shopId);

	/**
	 * [平台] 查询所有文件夹
	 *
	 * @return
	 */
	List<AttachmentFileFolderDTO> queryPageAllFolder(Long id, Integer userType);

	/**
	 * [商家] 查询所有文件夹
	 *
	 * @param shopId
	 * @return
	 */
	List<AttachmentFileFolder> queryShopAll(Long shopId);

	/**
	 * 根据文件夹ID和店铺ID获取文件夹
	 *
	 * @param folderIds
	 * @param shopId
	 * @return
	 */
	List<AttachmentFileFolder> queryByFolderIdAndShopId(List<Long> folderIds, Long shopId);

	Long getByParentId(Long parentId);

	AttachmentFileFolder getByShopIdFirst(Long shopId);
}
