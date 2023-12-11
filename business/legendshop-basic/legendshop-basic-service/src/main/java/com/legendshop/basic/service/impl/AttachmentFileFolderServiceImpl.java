/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.DefaultPagerProvider;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.ActiveBannerDao;
import com.legendshop.basic.dao.AttachmentDao;
import com.legendshop.basic.dao.AttachmentFileFolderDao;
import com.legendshop.basic.dao.AttachmentFolderDao;
import com.legendshop.basic.dto.*;
import com.legendshop.basic.entity.Attachment;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.enums.FileSourceEnum;
import com.legendshop.basic.query.AttachmentFileFolderQuery;
import com.legendshop.basic.service.AttachmentFileFolderService;
import com.legendshop.basic.service.AttachmentFileHistoryService;
import com.legendshop.basic.service.convert.AttachmentFileFolderConverter;
import com.legendshop.basic.service.convert.AttachmentFileHistoryConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.product.api.ProductPropertyApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (AttachmentFileFolder)表服务实现类
 *
 * @author legendshop
 * @since 2021-07-07 11:12:52
 */
@Service
@AllArgsConstructor
@Slf4j
public class AttachmentFileFolderServiceImpl implements AttachmentFileFolderService {

	private final AttachmentFileFolderDao attachmentFileFolderDao;
	private final AttachmentFolderDao attachmentFolderDao;
	private final AttachmentDao attachmentDao;
	private final AttachmentFileFolderConverter attachmentFileFolderConverter;

	private final ProductPropertyApi productPropertyApi;
	private final ActiveBannerDao activeBannerDao;
	private final AttachmentFileHistoryService fileHistoryService;
	private final AttachmentFileHistoryConverter fileHistoryConverter;


	@Override
	public AttachmentFileFolderDTO getByParentId(Long parentId) {

		//查询当前文件夹id
		Long getIdByParentId = attachmentFileFolderDao.getIdByParentId(parentId);
		if (ObjectUtil.isNull(getIdByParentId) && null != parentId && -1 == parentId) {
			AttachmentFileFolder attachmentFileFolder = new AttachmentFileFolder();
			attachmentFileFolder.setParentId(-1L);
			attachmentFileFolder.setTypeId(0);
			attachmentFileFolder.setUserType(0);
			attachmentFileFolder.setFileName("平台根目录");
			attachmentFileFolder.setId(1L);
			attachmentFileFolder.setCreateTime(new Date());
			getIdByParentId = attachmentFileFolderDao.save(attachmentFileFolder);
		}
		//获取文件夹和图片列表
		List<AttachmentFileFolderDTO> attachmentFileFolders = attachmentFileFolderDao.queryByParentId(getIdByParentId);
		List<AttachmentDTO> attachmentDTOList = attachmentFileFolderDao.queryAttachment(getIdByParentId);

		AttachmentFileFolder fileFolderById = attachmentFileFolderDao.getById(getIdByParentId);
		AttachmentFileFolderDTO attachmentFileFolderDTO = attachmentFileFolderConverter.to(fileFolderById);

		attachmentFileFolderDTO.setFileFolderList(attachmentFileFolders);
		attachmentFileFolderDTO.setAttachmentList(attachmentDTOList);

		return attachmentFileFolderDTO;
	}

	@Override
	public AttachmentFileFolderDTO getFolderByFileId(Long fileId, Long shopId, Long userType) {
		List<AttachmentFileFolderDTO> fileFolderDTOList;

		//判断是否是商家类型文件夹
		if (userType == 1) {
			fileFolderDTOList = attachmentFileFolderDao.queryByFileIdAndShopId(fileId, shopId);
		} else {
			fileFolderDTOList = attachmentFileFolderDao.queryByParentId(fileId);
		}

		//获取当前商家文件夹的图片列表
		List<AttachmentDTO> attachmentDTOList = attachmentFileFolderDao.queryAttachmentByFileIdAndShopId(fileId, shopId);
		AttachmentFileFolder fileFolderDaoById = attachmentFileFolderDao.getById(fileId);
		AttachmentFileFolderDTO attachmentFileFolderDTO = attachmentFileFolderConverter.to(fileFolderDaoById);

		attachmentFileFolderDTO.setFileFolderList(fileFolderDTOList);
		attachmentFileFolderDTO.setAttachmentList(attachmentDTOList);

		return attachmentFileFolderDTO;
	}

	@Override
	public R saveFileFolder(AttachmentFileFolder attachmentFileFolder) {
		String fileName = attachmentFileFolder.getFileName();
		if (fileName.contains(".") || fileName.contains("/") || fileName.contains("\\")) {
			return R.fail("文件夹名称不能包括特殊字符");
		}

		if (ObjectUtil.isNull(attachmentFileFolder.getParentId()) && ObjectUtil.isNotNull(attachmentFileFolder.getShopId())) {
			attachmentFileFolder.setParentId(attachmentFileFolderDao.getByShopId(attachmentFileFolder.getShopId()));

		}

		if (ObjectUtil.isNull(attachmentFileFolder.getTypeId())) {
			if (ObjectUtil.isNull(attachmentFileFolder.getId()) && ObjectUtil.isNull(attachmentFileFolder.getShopId()) && ObjectUtil.isNull(attachmentFileFolder.getParentId())) {
				attachmentFileFolder.setTypeId(1);

			} else {
				if (attachmentFileFolder.getShopId() != null) {
					attachmentFileFolder.setId(attachmentFileFolderDao.getIdByParentId(attachmentFileFolder.getParentId(), attachmentFileFolder.getShopId()));

				} else {
					attachmentFileFolder.setId(attachmentFileFolderDao.getIdByParentId(attachmentFileFolder.getParentId()));

				}

				if (ObjectUtil.isNull(attachmentFileFolder.getId())) {
					int typeId = getIdList(attachmentFileFolder.getParentId()).size();
					attachmentFileFolder.setTypeId(typeId);

				} else {
					int typeId = getIdList(attachmentFileFolder.getId()).size();
					attachmentFileFolder.setTypeId(typeId);
				}
				attachmentFileFolder.setShopId(attachmentFileFolderDao.getById(attachmentFileFolder.getParentId()).getShopId());
			}
		}

		if (ObjectUtil.isNull(attachmentFileFolder.getShopId()) && ObjectUtil.isNull(attachmentFileFolder.getParentId())) {
			attachmentFileFolder.setParentId(attachmentFileFolderDao.getIdByParentId(-1L));
		}

		if (ObjectUtil.isNotNull(attachmentFileFolderDao.getByFileName(attachmentFileFolder.getFileName(), attachmentFileFolder.getShopId(), attachmentFileFolder.getParentId()))) {
			return R.fail("该名称已存在");
		}
		attachmentFileFolder.setCreateTime(new Date());
		return R.ok(attachmentFileFolderDao.save(attachmentFileFolder));
	}

	@Override
	public R checkAttachment(Long id, String url, String fileName) {


		//判断是否是图片
		if (fileName.contains(".")) {
			R<List<String>> urlList = productPropertyApi.queryAttachmentByUrl(url);
			List<String> activeList = activeBannerDao.queryAttachmentByUrl(url);
			if (urlList.getData().size() > 0 && activeList.size() > 0) {
				return R.fail("图片正在被使用，是否删除？");
			}
		}
		List<Long> ids = attachmentFolderDao.queryFileById(id);

		if (ids.size() > 0) {
			return R.fail("文件夹下有图片，无法删除");
		}
		return R.ok();
	}

	@Override
	public R updateFileFolderName(String fileName, Long id) {
		AttachmentFileFolder fileFolderDaoById = attachmentFileFolderDao.getById(id);
		Long parentId = fileFolderDaoById.getParentId();
		String nameByFileName = attachmentFileFolderDao.getNameByFileName(fileName, parentId);
		if (ObjectUtil.isNotNull(nameByFileName)) {
			return R.fail("文件名已存在，请重新命名");
		}
		AttachmentFileFolder attachmentFileFolder = fileFolderDaoById;
		if (attachmentFileFolder.getTypeId() == 1 && attachmentFileFolder.getShopId() != null) {
			return R.fail("文件夹为商家根目录,无法重命名");
		}

		return R.ok(attachmentFileFolderDao.updateFileFolderName(fileName, id));
	}

	@Override
	public R updateAttPathByFileFolderId(FileFolderUpdateDTO fileFolderUpdateDTO) {
		AttachmentFileFolder attachmentFileFolder = attachmentFileFolderDao.getById(fileFolderUpdateDTO.getFileFolderId());
		for (AttachmentFileFolderDTO attachmentFileFolderDTO : fileFolderUpdateDTO.getAttachmentFileFolderDTOList()) {

			//判断是否是文件夹
			if (attachmentFileFolderDTO.getType() == 1) {
				attachmentFileFolderDTO.setShopId(attachmentFileFolderDao.getById(attachmentFileFolderDTO.getId()).getShopId());
				//判断目标文件夹和移动文件夹是否是一个商家
				if (ObjectUtil.isNotNull(attachmentFileFolderDTO.getShopId())
						&& !(attachmentFileFolderDTO.getShopId().equals(attachmentFileFolderDao.getById(fileFolderUpdateDTO.getFileFolderId()).getShopId()))
						&& attachmentFileFolderDao.getById(attachmentFileFolderDTO.getId()).getShopId() != null) {
					return R.fail("商家文件夹无法移动到其他目录");
				}

				if (attachmentFileFolderDTO.getId().equals(fileFolderUpdateDTO.getFileFolderId())) {
					return R.fail("目标文件夹与移动文件夹相同,无法移动");
				}

				//设置属性
				AttachmentFileFolder attachmentFileFolderDaoById = attachmentFileFolderDao.getById(fileFolderUpdateDTO.getFileFolderId());
				attachmentFileFolderDTO.setParentId(fileFolderUpdateDTO.getFileFolderId());
				attachmentFileFolderDTO.setUserType(attachmentFileFolder.getUserType());
				attachmentFileFolderDTO.setShopId(attachmentFileFolderDaoById.getShopId());
				attachmentFileFolderDTO.setFileName(attachmentFileFolderDTO.getName());
				attachmentFileFolderDTO.setName(attachmentFileFolderDTO.getName());
				attachmentFileFolderDTO.setTypeId(attachmentFileFolderDaoById.getTypeId() + 1);

				//判断重命名
				for (AttachmentFileFolderDTO fileFolderDTO : attachmentFileFolderDao.queryByParentId(fileFolderUpdateDTO.getFileFolderId())) {

					if (attachmentFileFolderDTO.getFileName().equals(fileFolderDTO.getFileName())) {
						attachmentFileFolderDTO.setFileName(attachmentFileFolderDTO.getFileName() + "(副本)");
					}
				}
				attachmentFileFolderDao.update(attachmentFileFolderConverter.from(attachmentFileFolderDTO));

				//附件移动
			} else {
				Long attFileFolderId = attachmentFolderDao.getFileIdByAttId(attachmentFileFolderDTO.getId());
				if (attachmentFileFolderDao.getById(attFileFolderId).getShopId() != null && !attachmentFileFolderDao.getById(attFileFolderId).getShopId().equals(attachmentFileFolder.getShopId())) {
					return R.fail("该图片为商家图片, 无法将该图片移动到其他商家目录");
				}

				//判断重命名
				List<AttachmentDTO> attachmentDTOList = attachmentFileFolderDao.queryAttachment(fileFolderUpdateDTO.getFileFolderId());
				for (AttachmentDTO attachmentDTO : attachmentDTOList) {
					if (attachmentDTO.getName() != null && attachmentDTO.getName().equals(attachmentFileFolderDTO.getName())) {
						attachmentFileFolderDTO.setFileName(attachmentDTO.getName() + "(副本)");
						attachmentFileFolderDTO.setName(attachmentDTO.getName() + "(副本)");
					}
					attachmentDao.updateName(attachmentFileFolderDTO.getFileName(), attachmentFileFolderDTO.getId());
				}

				attachmentFolderDao.updateAttPathByFileFolderId(fileFolderUpdateDTO.getFileFolderId(), attachmentFileFolderDTO.getId());
			}
		}
		return R.ok();
	}

	@Override
	public Integer delFile(Long id, String fileNmae) {
		//判断是否是图片
		if (fileNmae.contains(".")) {
			//删除中间表
			attachmentFolderDao.deleteByAttachId(id);
			return attachmentDao.deleteById(id);
		}

		return attachmentFileFolderDao.deleteById(id);
	}


	@Override
	public R<PageSupport<AttachmentFileFolderDTO>> queryPage(AttachmentFileFolderQuery attachmentFileFolderQuery) {

		if (ObjectUtil.isEmpty(attachmentFileFolderQuery.getPageSize()) || attachmentFileFolderQuery.getPageSize() <= 0) {
			attachmentFileFolderQuery.setPageSize(10);
		}

		//文件夹总数
		Long count = attachmentFileFolderDao.getCountByQuery(attachmentFileFolderQuery);
		if (ObjectUtil.isNull(count)) {
			count = 0L;
		}
		//附件总数
		Long attCount = attachmentFileFolderDao.getAttCountByQuery(attachmentFileFolderQuery);
		if (ObjectUtil.isNull(attCount)) {
			attCount = 0L;
		}

		//文件夹 + 附件
		Long totalCount = count + attCount;
		//初始化总分页list
		PageSupport<AttachmentFileFolderDTO> attachmentFileFolderDtoPageSupport = attachmentFileFolderDao.queryPage(attachmentFileFolderQuery);

		PageSupport<AttachmentFileFolderDTO> pageSupport = new PageSupport<>(attachmentFileFolderDtoPageSupport.getResultList(), new DefaultPagerProvider(totalCount, attachmentFileFolderQuery.getCurPage(), attachmentFileFolderDtoPageSupport.getPageSize()));
		List<AttachmentFileFolderDTO> folderPage = new ArrayList<>();
		if (ObjectUtil.isNotNull(pageSupport.getResultList())) {
			folderPage.addAll(pageSupport.getResultList());
		}


		List<AttachmentFileFolderDTO> attFileFolderPage = new ArrayList<>();

		//当前页 > (文件夹总数 / 每页大小 +1) (查询文件夹页最后一页), 查询附件表
		if (attachmentFileFolderQuery.getCurPage() > (count / attachmentFileFolderQuery.getPageSize())) {

			// 计算当前页所查询的开始位置和结束位置
			// 例：当前页是2，每页数量是10，则结束位置为 20， 开始位置为 11
			int endLocation = attachmentFileFolderQuery.getCurPage() * attachmentFileFolderQuery.getPageSize();
			int startLocation = endLocation - attachmentFileFolderQuery.getPageSize() + 1;

			Long startLimit = 0L;
			Long endLimit = Long.valueOf(attachmentFileFolderQuery.getPageSize());

			// 如果刚好当前页存在文件夹与图片共存时
			// 则需要计算当前页素材结束位置
			// 例：12个文件夹和12个图片，当前页是第2页，则开始位置为11，结束位置为20
			// 因为共存，则当前页图片数量需要排除文档数量，即 20 - 12 = 8
			// limit 0,8
			if (startLocation <= count) {
				endLimit = endLocation - count;
			}
			// 如果全是图片，则需要计算当前页素材开始位置
			// 例：2个文件夹和12个图片，当前页是第2页，则开始位置为11，结束位置为20
			// 因为全是图片，则startLimit需要减去共存时的图片数量
			// 第一页是 2 个文件夹 和 8 个图片
			// 第二页是 4个图片
			// 则 11 - 1 - 2 = 8
			// limit 8,10
			else {
				startLimit = startLocation - 1 - count;
			}

			attachmentFileFolderQuery.setStartLimit(startLimit);
			attachmentFileFolderQuery.setEndLimit(endLimit);

			List<AttachmentFileFolderDTO> resultList = attachmentFileFolderDao.queryAttachmentPage(attachmentFileFolderQuery);

			if (ObjectUtil.isNotNull(resultList)) {
				attFileFolderPage.addAll(resultList);
			}
		}

		List<AttachmentFileFolderDTO> resultList = new ArrayList<>();
		resultList.addAll(folderPage);
		resultList.addAll(attFileFolderPage);

		return R.ok(new PageSupport<>(resultList, new DefaultPagerProvider(totalCount, attachmentFileFolderQuery.getCurPage(), attachmentFileFolderQuery.getPageSize())));

	}


	@Override
	public R checkAttachmentInFile(AttFileIdListDTO attFileIdListDTO) {

		AttachmentFileFolder fileFolder = attachmentFileFolderDao.getById(attFileIdListDTO.getId());
		if (null == fileFolder) {
			return R.fail("文件夹不存在");
		}

		if (fileFolder.getTypeId() == 1 && fileFolder.getShopId() != null) {
			return R.fail("文件为根目录无法删除");
		}

		List<AttachmentDTO> attachmentDtoList = attachmentFileFolderDao.queryAttachment(attFileIdListDTO.getId());

		if (ObjectUtil.isNotNull(attachmentDtoList) && attachmentDtoList.size() > 0) {
			return R.fail("文件夹下含有图片无法删除");
		} else {
			attachmentFileFolderDao.deleteById(attFileIdListDTO.getId());
			return R.ok("删除成功");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R delFileList(List<AttFileIdListDTO> attFileIdListDTOList, ShopUserDetail shopUserDetail) {
		List<Long> folderIds = attFileIdListDTOList.stream()
				.filter(e -> e.getType() == 1)
				.map(AttFileIdListDTO::getId)
				.collect(Collectors.toList());

		Long shopId = Optional.ofNullable(shopUserDetail).map(ShopUserDetail::getShopId).orElse(null);

		if (CollUtil.isNotEmpty(folderIds)) {
			List<AttachmentFileFolder> attachmentFileFolders = attachmentFileFolderDao.queryByFolderIdAndShopId(folderIds, shopId);
			for (AttachmentFileFolder attachmentFileFolder : attachmentFileFolders) {
				if (attachmentFileFolder.getTypeId() == 1 && attachmentFileFolder.getShopId() != null) {
					return R.fail("文件夹为商家文件夹，无法删除");
				}
				List<Long> attIdList = attachmentFolderDao.queryFileById(attachmentFileFolder.getId());

				boolean hasAttIdList = attIdList != null && attIdList.size() > 0;
				boolean hasAttachmentsInFolder = attachmentFileFolderDao.queryByParentId(attachmentFileFolder.getId()).size() > 0;
				if (hasAttIdList || hasAttachmentsInFolder) {
					return R.fail("文件夹下有内容，无法删除");
				}
			}

			attachmentFileFolderDao.deleteById(attachmentFileFolders.stream().map(AttachmentFileFolder::getId).collect(Collectors.toList()));
		}

		List<Long> attIds = attFileIdListDTOList.stream()
				.filter(e -> e.getType() == 0)
				.map(AttFileIdListDTO::getId)
				.collect(Collectors.toList());
		if (CollUtil.isNotEmpty(attIds)) {
			List<Attachment> attachments = attachmentDao.queryAllByIds(attIds);
			if (CollUtil.isEmpty(attachments)) {
				return R.ok();
			}

			Long shopUserId = Optional.ofNullable(shopUserDetail).map(ShopUserDetail::getUserId).orElse(null);

			// TODO 2021-08-11 因为上传接口没写好的原因，根本无法知道当前删除文件是否是当前用户的，为了兼容旧数据，只能根据时间进行不同处理
			// 如果不想这么判断的话，也可以先获取文件与文件夹的关联信息，再找到文件夹，通过文件夹就能获取到文件上传用户
			boolean isInvalidShopUserAttachment = shopUserId != null &&
					attachments.stream()
							.anyMatch(e -> DateUtil.parseDate("2021-08-13").after(e.getCreateTime())
									//确保附件的用户 ID 与 shopUserId 不相同。
									&& !shopUserId.equals(e.getUserId())
									//确保附件的文件来源是商家。
									&& FileSourceEnum.SHOP.getValue().equals(e.getFileSource()));

			if (isInvalidShopUserAttachment) {
				throw new BusinessException("删除失败~");
			}

			attachmentDao.deleteByAttId(attIds);
			attachmentFolderDao.deleteByAttachId(attIds);
		}
		return R.ok();
	}

	@Override
	public List<AttachmentFileFolder> getIdList(Long id) {
		List<AttachmentFileFolder> idList = new ArrayList<>();
		Long parentId = -99L;

		if (ObjectUtil.isNull(id)) {
			idList.add(attachmentFileFolderConverter.from(getByParentId(-1L)));
			Collections.reverse(idList);
			return idList;
		}

		while (parentId != -1) {
			AttachmentFileFolder attachmentFileFolder = attachmentFileFolderDao.getById(id);
			parentId = attachmentFileFolder.getParentId();
			if (id != -1) {
				idList.add(attachmentFileFolder);
			}
			id = parentId;
		}
		Collections.reverse(idList);
		return idList;
	}

	@Override
	public List<AttachmentFileFolder> getIdList(Long id, Long shopId) {
		List<AttachmentFileFolder> idList = new ArrayList<>();
		Long parentId = -99L;
		if (ObjectUtil.isNull(id)) {
			Long byShopId = attachmentFileFolderDao.getByShopId(shopId);
			idList.add(attachmentFileFolderDao.getById(byShopId));
			Collections.reverse(idList);
			return idList;
		}

		while (parentId != -1) {
			parentId = attachmentFileFolderDao.getParentIdById(id);
			if (id != -1) {

				idList.add(attachmentFileFolderDao.getById(id));
			}
			id = parentId;
		}
		Collections.reverse(idList);
		idList.remove(0);
		return idList;
	}

	@Override
	public R save(AttachmentFileFolderDTO attachmentFileFolderDTO) {
		AttachmentFileFolder attachmentFileFolder = attachmentFileFolderConverter.from(attachmentFileFolderDTO);
		return R.ok(attachmentFileFolderDao.save(attachmentFileFolder));
	}

	@Override
	public Long getAdminByParentId(Long parentId) {
		return attachmentFileFolderDao.getIdByParentId(parentId);
	}

	@Override
	public List<Long> queryIds() {
		return attachmentFileFolderDao.queryIds();
	}

	@Override
	public Long getByShopId(Long id) {
		return attachmentFileFolderDao.getIdByShopId(id);
	}

	@Override
	public R updateFilePath(FileFolderUpdateDTO fileFolderUpdateDTO) {
		return null;
	}

	@Override
	public List<AttachmentFileFolderDTO> queryShopFolder(Long shopId) {
		return attachmentFileFolderDao.queryShopFolder(shopId);
	}

	@Override
	public List<AttachmentFileFolderDTO> queryFileFolderList(Long id, Long shopId) {
		List<AttachmentFileFolderDTO> attachmentFileFolderDTOList = attachmentFileFolderDao.queryFileFolderList(id, shopId);

		return attachmentFileFolderDTOList;

	}

	@Override
	public List<AttachmentFileFolderDTO> queryPageAllFolder(Long id, Integer userType) {
		if (ObjectUtil.isNull(id)) {
			id = -1L;
		}

		return attachmentFileFolderDao.queryPageAllFolder(id, userType);
	}

	@Override
	public R<List<List<AttachmentFileFolderDTO>>> saveHistory(Long userId, String userType, List<Long> id) {
		//保存历史记录
		//获取文件夹否存在
		if (CollUtil.isEmpty(id)) {
			return queryHistoryIds(userId, userType);
		}
		List<AttachmentFileFolder> attachmentFileFolders = attachmentFileFolderDao.queryAllByIds(id);
		if (CollUtil.isEmpty(attachmentFileFolders)) {
			return R.ok();
		}
		List<Long> idList = attachmentFileFolders.stream().
				sorted(Comparator.comparing(AttachmentFileFolder::getTypeId)).map(AttachmentFileFolder::getId).
				collect(Collectors.toList());
		if (CollUtil.isNotEmpty(idList)) {
			Long fileId = idList.get(idList.size() - 1);
			AttachmentFileFolder fileFolder = attachmentFileFolderDao.getById(fileId);
			if (ObjectUtil.isNotEmpty(fileFolder) && !"SHOP_PIC".equals(userType)) {
				if (1 == fileFolder.getUserType()) {
					userType = "SHOP_FILE";
				}
			}
		}

		//筛选出文件夹id，比较
		String idStr = Convert.toStr(idList);

		//判断是否已经存在，存在则不作处理
		AttachmentFileHistoryDTO historyDTO = fileHistoryService.getFileId(userId, userType, idStr);
		log.info("获取当前用户:{},当前文件夹目录id:{}", userId, id);
		//当前历史记录已存在
		if (ObjectUtil.isNotEmpty(historyDTO)) {
			historyDTO.setUpdateTime(DateUtil.date());
			fileHistoryService.update(historyDTO);
			return queryHistoryIds(userId, userType);
		}
		AttachmentFileHistoryDTO userCount = fileHistoryService.getUserCount(userId, userType);
		log.info("获取当前用户:{},当前记录数量:{}", userId, userCount);
		//不存在则判断是否已经存在5条
		if (ObjectUtil.isNotEmpty(userCount) && userCount.getCounts() >= 5) {
			//存在5条则更新数据
			log.info("更新数据：获取当前用户:{},当前文件夹路径:{}", userId, idStr);
			fileHistoryService.updateFileHistory(userCount.getId(), idStr);
		} else {
			//不足5条则保存
			AttachmentFileHistoryDTO attachmentFileHistory = new AttachmentFileHistoryDTO();
			attachmentFileHistory.setCreateTime(new Date());
			attachmentFileHistory.setUpdateTime(new Date());
			attachmentFileHistory.setUserId(userId);
			attachmentFileHistory.setFolderId(idStr);
			attachmentFileHistory.setUserType(userType);
			log.info("保存数据：获取当前用户:{},当前文件夹路径:{}", userId, idStr);
			fileHistoryService.save(attachmentFileHistory);
		}

		//返回文件夹list集合
		return queryHistoryIds(userId, userType);
	}

	@Override
	public AttachmentFileFolder getByShopIdFirst(Long shopId) {
		return attachmentFileFolderDao.getByShopIdFirst(shopId);
	}

	@Override
	public void update(AttachmentFileFolder byShopIdFirst) {
		attachmentFileFolderDao.update(byShopIdFirst);
	}

	private R<List<List<AttachmentFileFolderDTO>>> queryHistoryIds(Long userId, String userType) {
		//获取用户的文件夹历史
		log.info("获取当前用户文件夹历史:{}", userId);
		List<AttachmentFileHistoryDTO> attachmentFileHistoryDTOList = fileHistoryService.queryByUserId(userId, userType);
		List<List<AttachmentFileFolderDTO>> fileFolders = new ArrayList<>();
		if (CollUtil.isEmpty(attachmentFileHistoryDTOList)) {
			return R.ok(fileFolders);
		}

		for (AttachmentFileHistoryDTO attachmentFileHistoryDTO : attachmentFileHistoryDTOList) {
			if (StrUtil.isNotBlank(attachmentFileHistoryDTO.getFolderId())) {
				List<Long> idList = Convert.toList(Long.class, attachmentFileHistoryDTO.getFolderId());
				//文件id为空无需处理
				if (CollUtil.isEmpty(idList)) {
					continue;
				}

				//根据文件夹id查找数据
				List<AttachmentFileFolderDTO> attachmentFileFolders = attachmentFileFolderConverter.to(attachmentFileFolderDao.queryAllByIds(idList));
				if (CollUtil.isEmpty(attachmentFileFolders)) {
					continue;
				}
				List<AttachmentFileFolderDTO> fileFolderList = attachmentFileFolders.stream().
						sorted(Comparator.comparing(AttachmentFileFolderDTO::getTypeId)).collect(Collectors.toList());
				AttachmentFileFolderDTO attachmentFileFolderDTO = fileFolderList.get(fileFolderList.size() - 1);
				Long id = attachmentFileFolderDao.getByParentId(attachmentFileFolderDTO.getId());
				if (ObjectUtil.isNotEmpty(id)) {
					attachmentFileFolderDTO.setIsNext(true);
				} else {
					attachmentFileFolderDTO.setIsNext(false);
				}
				if (idList.size() == fileFolderList.size()) {
					fileFolders.add(fileFolderList);
				}
			}
		}
		return R.ok(fileFolders);
	}


}

