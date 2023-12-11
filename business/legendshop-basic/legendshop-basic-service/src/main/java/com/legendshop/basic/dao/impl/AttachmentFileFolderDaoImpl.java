/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.dao.AttachmentFileFolderDao;
import com.legendshop.basic.dto.AttachmentDTO;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.entity.AttachmentFileFolder;
import com.legendshop.basic.query.AttachmentFileFolderQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * (AttachmentFileFolder)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-07-06 14:19:23
 */
@Repository
public class AttachmentFileFolderDaoImpl extends GenericDaoImpl<AttachmentFileFolder, Long> implements AttachmentFileFolderDao {


	@Override
	public PageSupport<AttachmentFileFolder> queryPageByFolderId(AttachmentFileFolderQuery attachmentFileFolderQuery) {
		CriteriaQuery cq = new CriteriaQuery(AttachmentFileFolderQuery.class, attachmentFileFolderQuery.getPageSize(), attachmentFileFolderQuery.getCurPage());
		cq.eq("parentId", attachmentFileFolderQuery.getParentId());
		return queryPage(cq);
	}


	@Override
	public List<AttachmentFileFolderDTO> queryByParentId(Long parentId) {
		return this.query("select id, file_name, parent_id, type_id, create_time, update_time, shop_id, user_type from ls_attachment_file_folder where parent_id = ? ", AttachmentFileFolderDTO.class, parentId);
	}


	@Override
	public List<AttachmentDTO> queryAttachment(Long fileFolderId) {
		QueryMap map = new QueryMap();
		map.put("id", fileFolderId);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.queryAttachmentByFileFolder", map);
		List<AttachmentDTO> query = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(AttachmentDTO.class));
		return query;
	}

	@Override
	public List<AttachmentFileFolderDTO> queryAttachmentPage(AttachmentFileFolderQuery attachmentFileFolderQuery) {
		QueryMap queryMap = new QueryMap();

		if (ObjectUtil.isNull(attachmentFileFolderQuery.getId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getParentId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getShopId())) {
			queryMap.put("parentId", -1L);
			queryMap.put("id", getIdByParentId(-1L));

		} else {
			queryMap.put("parentId", attachmentFileFolderQuery.getParentId());
			queryMap.put("id", attachmentFileFolderQuery.getId());
		}
		queryMap.put("shopId", attachmentFileFolderQuery.getShopId());
		queryMap.put("type", attachmentFileFolderQuery.getType());
		queryMap.put("typeId", attachmentFileFolderQuery.getTypeId());
		queryMap.put("orderBy", "order by create_time desc");
		queryMap.like("keyWord", attachmentFileFolderQuery.getKeyWord(), MatchMode.ANYWHERE);
		queryMap.put("limit", "limit " + attachmentFileFolderQuery.getStartLimit() + "," + attachmentFileFolderQuery.getEndLimit());
		SQLOperation pageOperation = this.getSQLAndParams("AttachmentFileFolder.queryAttachmentByFileFolder", queryMap);
		return this.query(pageOperation.getSql(), AttachmentFileFolderDTO.class, pageOperation.getParams());
	}

	@Override
	public Long getByShopId(Long shopId) {
		return this.get("select id from ls_attachment_file_folder where shop_id = ? and type_id = 1 ", Long.class, shopId);
	}

	@Override
	public Long getIdByParentId(Long parentId, Long shopId) {
		return this.get("select id from ls_attachment_file_folder where parent_id = ? and shop_id = ?", Long.class, parentId, shopId);
	}

	@Override
	public Long getIdByParentId(Long parentId) {
		return this.get("select id from ls_attachment_file_folder where parent_id = ? and user_type = 0", Long.class, parentId);
	}

	@Override
	public Integer updateFileFolderName(String fileName, Long id) {
		return update("update ls_attachment_file_folder set file_name = ? where id = ?", fileName, id);
	}

	@Override
	public List<AttachmentFileFolderDTO> queryByFileIdAndShopId(Long fileId, Long shopId) {
		return this.query("select id, file_name, parent_id, type_id, create_time, update_time, shop_id, user_type from ls_attachment_file_folder where parent_id = ? and shop_id = ? ", AttachmentFileFolderDTO.class, fileId, shopId);
	}

	@Override
	public List<AttachmentDTO> queryAttachmentByFileIdAndShopId(Long fileId, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("fileFolderId", fileId);
		map.put("shopId", shopId);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.queryAttachmentByFileFolder", map);
		List<AttachmentDTO> query = query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(AttachmentDTO.class));
		return query;
	}

	@Override
	public String getNameByFileName(String fileName, Long id) {
		String sql = "select file_name from ls_attachment_file_folder where file_name = ? and parent_id = ?";
		List<Object> obj = new ArrayList<>();
		obj.add(fileName);
		obj.add(id);
		return this.get(sql, String.class, obj.toArray());
	}


	@Override
	public AttachmentFileFolder getByFileName(String fileName, Long shopId, Long parentId) {
		String sql = "select file_name from ls_attachment_file_folder where file_name = ? and parent_id = ?";
		List<Object> obj = new ArrayList<>();
		obj.add(fileName);
		obj.add(parentId);
		return this.get(sql, AttachmentFileFolder.class, obj.toArray());
	}

	@Override
	public Integer updateAllParentId(Long parentId) {
		return update("update ls_attachment_file_folder set parent_id = ? where user_type = 1 and type_id = 2", parentId);
	}


	@Override
	public PageSupport<AttachmentFileFolderDTO> queryPage(AttachmentFileFolderQuery attachmentFileFolderQuery) {
		SimpleSqlQuery sq = new SimpleSqlQuery(AttachmentFileFolderDTO.class, attachmentFileFolderQuery);
		QueryMap queryMap = new QueryMap();
		// 检查父 ID 是否为空或为 -1
		boolean isParentIdNullOrMinusOne = attachmentFileFolderQuery.getParentId() == null || attachmentFileFolderQuery.getParentId() == -1;
		// 检查 ID 是否为空
		boolean isIdNull = attachmentFileFolderQuery.getId() == null;
		// 检查店铺 ID 是否为空
		boolean isShopIdNull = attachmentFileFolderQuery.getShopId() == null;
		boolean checkParentAndId = isParentIdNullOrMinusOne && isIdNull;

		if (checkParentAndId && isShopIdNull) {
			Long idByParentId = getIdByParentId(-1L);
			queryMap.put("parentId", idByParentId);
		} else if (!isShopIdNull && isIdNull) {
			attachmentFileFolderQuery.setId(getByShopId(attachmentFileFolderQuery.getShopId()));
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
		} else if (isIdNull) {
			attachmentFileFolderQuery.setId(getIdByParentId(attachmentFileFolderQuery.getParentId()));
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
			queryMap.put("shopId", attachmentFileFolderQuery.getShopId());
			queryMap.put("id", attachmentFileFolderQuery.getId());
		} else if (ObjectUtil.isNull(attachmentFileFolderQuery.getId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getParentId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getShopId())) {
			Long idByParentId = getIdByParentId(-1L);
			queryMap.put("parentId", idByParentId);
		} else {
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
		}
		queryMap.put("type", attachmentFileFolderQuery.getType());
		queryMap.put("typeId", attachmentFileFolderQuery.getTypeId());
		queryMap.like("keyWord", attachmentFileFolderQuery.getKeyWord(), MatchMode.ANYWHERE);
		queryMap.put("userType", attachmentFileFolderQuery.getUserType());

		sq.setSqlAndParameter("AttachmentFileFolder.page", queryMap);
		return querySimplePage(sq);
	}


	@Override
	public Integer updateParentId(Long fileFolderId, Long id, Long typeId) {
		return update("update ls_attachment_file_folder set parent_id = ?, type_id = ? where id = ?", fileFolderId, typeId, id);
	}

	@Override
	public Long getCountByQuery(AttachmentFileFolderQuery attachmentFileFolderQuery) {

		QueryMap queryMap = new QueryMap();
		// 检查父 ID 是否为空或为 -1
		boolean isParentIdNullOrMinusOne = attachmentFileFolderQuery.getParentId() == null || attachmentFileFolderQuery.getParentId() == -1;
		// 检查 ID 是否为空
		boolean isIdNull = attachmentFileFolderQuery.getId() == null;
		// 检查店铺 ID 是否为空
		boolean isShopIdNull = attachmentFileFolderQuery.getShopId() == null;
		boolean checkParentAndId = isParentIdNullOrMinusOne && isIdNull;

		if (checkParentAndId && isShopIdNull) {
			Long idByParentId = getIdByParentId(-1L);
			queryMap.put("parentId", idByParentId);
		} else if (!isShopIdNull && isIdNull) {
			attachmentFileFolderQuery.setId(getByShopId(attachmentFileFolderQuery.getShopId()));
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
		} else if (isIdNull) {
			attachmentFileFolderQuery.setId(getIdByParentId(attachmentFileFolderQuery.getParentId()));
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
			queryMap.put("shopId", attachmentFileFolderQuery.getShopId());
			queryMap.put("id", attachmentFileFolderQuery.getId());
		} else if (ObjectUtil.isNull(attachmentFileFolderQuery.getId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getParentId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getShopId())) {
			Long idByParentId = getIdByParentId(-1L);
			queryMap.put("parentId", idByParentId);
		} else {
			queryMap.put("parentId", attachmentFileFolderQuery.getId());
		}
		queryMap.put("type", attachmentFileFolderQuery.getType());
		queryMap.put("typeId", attachmentFileFolderQuery.getTypeId());
		queryMap.put("userType", attachmentFileFolderQuery.getUserType());
		queryMap.like("keyWord", attachmentFileFolderQuery.getKeyWord(), MatchMode.ANYWHERE);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.pageCount", queryMap);
		return get(operation.getSql(), Long.class, operation.getParams());
	}

	@Override
	public Long getAttCountByQuery(AttachmentFileFolderQuery attachmentFileFolderQuery) {

		QueryMap queryMap = new QueryMap();
		if (ObjectUtil.isNull(attachmentFileFolderQuery.getId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getParentId()) && ObjectUtil.isNull(attachmentFileFolderQuery.getShopId())) {
			queryMap.put("parentId", -1L);
			queryMap.put("id", getIdByParentId(-1L));

		} else {
			queryMap.put("parentId", attachmentFileFolderQuery.getParentId());
			queryMap.put("id", attachmentFileFolderQuery.getId());
		}
		queryMap.put("shopId", attachmentFileFolderQuery.getShopId());
		queryMap.put("type", attachmentFileFolderQuery.getType());
		queryMap.put("userType", attachmentFileFolderQuery.getUserType());
		queryMap.put("orderBy", "order by create_time desc");
		queryMap.like("keyWord", attachmentFileFolderQuery.getKeyWord(), MatchMode.ANYWHERE);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.queryAttachmentByFileFolderCount", queryMap);

		return get(operation.getSql(), Long.class, operation.getParams());
	}


	@Override
	public Long getParentIdById(Long id) {
		return get("select parent_id from ls_attachment_file_folder where id = ?", Long.class, id);
	}

	@Override
	public Long getParentIdById(Long id, Long shopId) {
		return get("select parent_id from ls_attachment_file_folder where id = ? and shop_id = ?", Long.class, id, shopId);
	}

	@Override
	public List<AttachmentFileFolderDTO> queryFolder() {
		return query("select id, file_name, type_id, user_type, shop_id from ls_attachment_file_folder where shop_id is null", AttachmentFileFolderDTO.class);
	}

	@Override
	public List<AttachmentFileFolderDTO> queryShopFolder(Long shopId) {
		return query("select id, file_name, type_id, user_type, shop_id from ls_attachment_file_folder where shop_id = ?", AttachmentFileFolderDTO.class, shopId);
	}


	@Override
	public List<Long> queryIds() {
		return query("select id, shop_name from ls_shop_detail", Long.class);
	}

	@Override
	public Long getIdByShopId(Long id) {
		return get("select id from ls_attachment_file_folder where shop_id = ?", Long.class, id);
	}

	@Override
	public Long getIdByShopIdFirst(Long id) {
		return get("select id from ls_attachment_file_folder where shop_id = ? and type_id = 1", Long.class, id);
	}

	@Override
	public AttachmentFileFolder getByShopIdFirst(Long id) {
		return get("select * from ls_attachment_file_folder where shop_id = ? and type_id = 1", AttachmentFileFolder.class, id);
	}

	@Override
	public List<AttachmentFileFolderDTO> queryFileFolderList(Long id, Long shopId) {
		QueryMap queryMap = new QueryMap();
		if (id == null) {
			AttachmentFileFolderDTO attachmentFileFolderDTO = get("select * from ls_attachment_file_folder where shop_id = ? and type_id = 1", AttachmentFileFolderDTO.class, shopId);
			queryMap.put("id", attachmentFileFolderDTO.getParentId());

		} else {
			queryMap.put("id", id);
		}
		queryMap.put("shopId", shopId);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.queryPageAllFolder", queryMap);
		return query(operation.getSql(), AttachmentFileFolderDTO.class, operation.getParams());

	}

	@Override
	public List<AttachmentFileFolderDTO> queryPageAllFolder(Long id, Integer userType) {
		QueryMap queryMap = new QueryMap();
		if (id == null) {
			id = -1L;
		}
		queryMap.put("id", id);
		queryMap.put("userType", userType);
		SQLOperation operation = this.getSQLAndParams("AttachmentFileFolder.queryPageAllFolder", queryMap);
		return query(operation.getSql(), AttachmentFileFolderDTO.class, operation.getParams());
	}

	@Override
	public List<AttachmentFileFolder> queryShopAll(Long shopId) {
		return query("select * from ls_attachment_file_folder where shopId = ? and type_id = 1", AttachmentFileFolder.class, shopId);
	}

	@Override
	public List<AttachmentFileFolder> queryByFolderIdAndShopId(List<Long> folderIds, Long shopId) {
		LambdaEntityCriterion<AttachmentFileFolder> criterion = new LambdaEntityCriterion<>(AttachmentFileFolder.class, true);
		criterion.eq(AttachmentFileFolder::getShopId, shopId)
				.in(AttachmentFileFolder::getId, folderIds);
		return queryByProperties(criterion);
	}

	@Override
	public Long getByParentId(Long parentId) {
		return this.get("select id from ls_attachment_file_folder where parent_id = ?", Long.class, parentId);
	}

}
