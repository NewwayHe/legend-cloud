/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import cn.legendshop.jpaplus.support.lambda.LambdaRestrictions;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.ProductCommentStatisticsBO;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dao.ShopDetailDao;
import com.legendshop.shop.dto.*;
import com.legendshop.shop.entity.ShopDetail;
import com.legendshop.shop.enums.SearchShopSortByEnum;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.vo.ShopDetailVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 商家详情Dao
 *
 * @author legendshop
 */
@Repository
public class ShopDetailDaoImpl extends GenericDaoImpl<ShopDetail, Long> implements ShopDetailDao {


	/**
	 * 根据用户名获取商城ID
	 */
	@Override
	public Long getShopIdByName(String userName) {
		List<Long> result = jdbcTemplate.query("select sd.shop_id as shopId from ls_shop_detail sd where sd.user_name = ?", new Object[]{userName}, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("shopId");
			}
		});
		if (CollUtil.isEmpty(result)) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * 根据创建人找到对应的shopId
	 */
	@Override
	public Long getShopIdByUserId(Long userId) {
		List<Long> result = jdbcTemplate.query("select sd.id as shopId from ls_shop_detail sd where sd.shop_user_id = ?", new Object[]{userId}, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("shopId");
			}
		});
		if (CollUtil.isEmpty(result)) {
			return null;
		}
		return result.get(0);
	}


	@Override
	public PageSupport<ShopDetail> page(ShopDetailQuery shopDetailQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(ShopDetail.class, shopDetailQuery, true);
		criteriaQuery.like("shopName", shopDetailQuery.getShopName(), MatchMode.ANYWHERE);
		criteriaQuery.like("contactPhone", shopDetailQuery.getContactPhone(), MatchMode.ANYWHERE);
		criteriaQuery.eq("status", shopDetailQuery.getStatus());
		criteriaQuery.eq("opStatus", shopDetailQuery.getOpStatus());
		criteriaQuery.eq("applyForType", shopDetailQuery.getApplyForType());
		//排除掉分销商店
		criteriaQuery.notEq("applyForType", shopDetailQuery.getExcludeApplyForType());
		criteriaQuery.addDescOrder("createTime");
		return this.queryPage(criteriaQuery);
	}

	@Override
	public Boolean updateOpStatus(List<Long> shopIds, Integer opStatus) {
		if (CollUtil.isEmpty(shopIds)) {
			return false;
		}
		StringBuilder sb = new StringBuilder("UPDATE ls_shop_detail SET op_status = ? WHERE id in (");
		for (Long shopId : shopIds) {
			sb.append(shopId);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return this.update(sb.toString(), opStatus) > 0;
	}

	@Override
	public Boolean updateStatus(List<Long> shopIds, Integer status) {
		if (CollUtil.isEmpty(shopIds)) {
			return false;
		}
		StringBuilder sb = new StringBuilder("UPDATE ls_shop_detail SET status = ? WHERE id in (");
		for (Long shopId : shopIds) {
			sb.append(shopId);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return this.update(sb.toString(), status) > 0;
	}

	@Override
	public ShopDetail getByShopUserId(Long shopUserId) {
		return getByProperties(new EntityCriterion().eq("shopUserId", shopUserId));
	}

	@Override
	public List<ShopDetail> getByShopUserId(List<Long> shopUserIds) {
		if (CollectionUtils.isEmpty(shopUserIds)) {
			return new ArrayList<>();
		}
		return queryByProperties(new EntityCriterion().in("shopUserId", shopUserIds.toArray()));
	}

	@Override
	public boolean isShopNameExist(String shopName) {
		if (ObjectUtil.isNull(shopName)) {
			return false;
		}
		return ObjectUtil.isNotNull(get("SELECT 1  FROM ls_shop_detail d WHERE shop_name=?", Integer.class, shopName.trim()));
	}

	@Override
	public boolean isOtherShopNameExist(Long shopId, String shopName) {
		if (ObjectUtil.isNull(shopName)) {
			return false;
		}
		return ObjectUtil.isNotNull(get("SELECT 1  FROM ls_shop_detail d WHERE shop_name=? and id <> ?", Integer.class, shopName, shopId));
	}

	@Override
	public boolean isShopUserIdExist(Long shopUserId) {
		return ObjectUtil.isNotNull(get("SELECT 1  FROM ls_shop_detail d WHERE shop_user_id=?", Integer.class, shopUserId));
	}

	@Override
	public boolean isShopAuditPass(List<Long> shopIds) {
		LambdaEntityCriterion<ShopDetail> entityCriterion = new LambdaEntityCriterion<>(ShopDetail.class);
		LambdaRestrictions<ShopDetail> restrictions = new LambdaRestrictions<>();
		entityCriterion.in(ShopDetail::getId, shopIds)
				.add(entityCriterion.or(
						restrictions.eq(ShopDetail::getOpStatus, OpStatusEnum.PASS.getValue()),
						restrictions.eq(ShopDetail::getOpStatus, OpStatusEnum.DENY.getValue()))
				);
		return this.getCount(entityCriterion) > 0;
	}

	/**
	 * The Class ShopDetailRowMapper.
	 */
	class ShopDetailRowMapper implements RowMapper<ShopDetailViewDTO> {

		@Override
		public ShopDetailViewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShopDetailViewDTO shopDetail = new ShopDetailViewDTO();
			shopDetail.setId(rs.getLong("id"));
			//	shopDetail.setShopId(rs.getLong("userId"));
//			shopDetail(rs.getString("siteName"));
//			shopDetail.setShopAddr(rs.getString("shopAddr"));
//			shopDetail.setBankCard(rs.getString("bankCard"));
//			shopDetail.setPayee(rs.getString("payee"));
//			shopDetail.setCode(rs.getString("code"));
//			shopDetail.setPostAddr(rs.getString("postAddr"));
//			shopDetail.setRecipient(rs.getString("recipient"));
			shopDetail.setStatus(rs.getInt("status"));
//			shopDetail.setVisitTimes(rs.getLong("visitTimes"));
//			shopDetail.setProductNum(rs.getLong("productNum"));
//			shopDetail.setCommNum(rs.getLong("commNum"));
			shopDetail.setBriefDesc(rs.getString("briefDesc"));
//			shopDetail.setFrontEndLanguage(rs.getString("frontEndLanguage"));
//			shopDetail.setBackEndLanguage(rs.getString("backEndLanguage"));
			shopDetail.setIdCardPic(rs.getString("idCardPic"));
			//shopDetail.setIdCardNum(rs.getString("idCardNum"));
//			shopDetail.setProvinceId(rs.getInt("provinceId"));
//			shopDetail.setCityId(rs.getInt("cityId"));
//			shopDetail.setAreaId(rs.getInt("areaId"));
//			shopDetail.setProvince(rs.getString("province"));
//			shopDetail.setCity(rs.getString("city"));
//			shopDetail.setArea(rs.getString("area"));
//			shopDetail.setUserTel(rs.getString("userTel"));
			shopDetail.setNickName(rs.getString("nickName"));
			shopDetail.setUserMobile(rs.getString("userMobile"));
			shopDetail.setQq(rs.getString("qq"));
//			shopDetail.setUserPostcode(rs.getString("userPostcode"));
			shopDetail.setFax(rs.getString("fax"));
//			shopDetail.setFrontEndStyle(rs.getString("frontEndStyle"));
//			shopDetail.setBackEndStyle(rs.getString("backEndStyle"));
//			shopDetail.setFrontEndTemplet(rs.getString("frontEndTemplet"));
//			shopDetail.setBackEndTemplet(rs.getString("backEndTemplet"));
//			shopDetail.setProdRequireAudit((Integer) rs.getObject("prodRequireAudit"));
			if (shopDetail.getQq() != null) {
				String[] qqs = shopDetail.getQq().split(",");
				List<String> qqList = new ArrayList<String>(qqs.length);
				for (int i = 0; i < qqs.length; i++) {
					if (qqs[i] != null && qqs[i].length() > 0) {
						qqList.add(qqs[i]);
					}
				}
//				shopDetail.setQqList(qqList);
			}
//			shopDetail.setDomainName(rs.getString("domainName"));
//			shopDetail.setIcpInfo(rs.getString("icpInfo"));
			return shopDetail;
		}

	}

	@Override
	public ShopDetail getByUserId(Long userId) {
		return getByProperties(new EntityCriterion().eq("shopUserId", userId));
	}


	@Override
	public ShopDetail getByShopId(Long shopId) {
		if (shopId == null) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("id", shopId));
	}

	@Override
	public List<ShopDetail> getAllOnline() {
		return query("select id,d.shop_name from ls_shop_detail d where d.status=1", ShopDetail.class);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "ShopDetailView", key = "#shopId"),
			@CacheEvict(value = "ShopDetail", key = "#shopId")
	})
	public int deleteShopDetailById(Long shopId) {
		return deleteById(shopId);
	}

	@Override
	public Long getMaxShopId() {
		return this.get("select max(shop_id) from ls_shop_detail ", Long.class);
	}

	@Override
	public long getCountShopId() {
		return this.getLongResult("select count(shop_id) from ls_shop_detail ");
	}

	@Override
	public Long getMinShopId() {
		return this.get("select min(shop_id) from ls_shop_detail ", Long.class);
	}


	@Override
	public String getShopName(Long shopId) {
		List<String> result = jdbcTemplate.query("select sd.shop_name as shopName from ls_shop_detail sd where sd.shop_user_id = ?", new Object[]{shopId}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("shopName");
			}
		});
		if (CollUtil.isEmpty(result)) {
			return null;
		}
		return result.get(0);
	}


	@Override
	public Integer updateShopType(Long shopId, Integer type) {
		return this.update("update ls_shop_detail set shop_type = ? where id=?", type, shopId);
	}

	@Override
	public PageSupport<ShopDetail> getShopDetailPage(String curPageNo, ShopDetail shopDetail) {
		CriteriaQuery cq = new CriteriaQuery(ShopDetail.class, curPageNo);
		cq.setPageSize(10);

		cq.isNotNull("secDomainName");

		if (StrUtil.isNotBlank(shopDetail.getShopName())) {
			cq.like("siteName", shopDetail.getShopName(), MatchMode.ANYWHERE);
		}
		return queryPage(cq);
	}

	@Override
	public PageSupport<ShopDetail> getShopDetailPage(ShopDetailQuery shopDetailQuery) {
		QueryMap map = new QueryMap();
		map.like("siteName", shopDetailQuery.getShopName(), MatchMode.ANYWHERE);
		map.like("contactName", shopDetailQuery.getContactName(), MatchMode.ANYWHERE);
		map.like("contactMobile", shopDetailQuery.getContactPhone(), MatchMode.ANYWHERE);
		map.put("status", shopDetailQuery.getStatus());
		map.put("opStatus", shopDetailQuery.getOpStatus());
		map.put("shopType", shopDetailQuery.getShopType());

		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(ShopDetail.class, shopDetailQuery.getPageSize(), shopDetailQuery.getCurPage());
		simpleSqlQuery.setSqlAndParameter("ShopDetail.getShopDetail", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public ShopDetail getTypeShopDetailByUserId(Long userId) {
		String sql = "SELECT s.*,c.company_name AS companyName,c.license_pic AS licensePicFilePath,c.license_number AS licenseNumber\n" +
				"FROM ls_shop_detail s LEFT JOIN ls_shop_company_detail c ON s.shop_id = c.shop_id WHERE s.user_id = ?";
		return this.get(sql, ShopDetail.class, userId);
	}


	/**
	 * 商品详情店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	@Override
	public ShopDetailBO getUserShop(Long shopId) {
		return get("SELECT id AS shopId,shop_name,shop_avatar,shop_type,apply_for_type,integral_status  FROM ls_shop_detail where  status=1 AND id = ?", ShopDetailBO.class, shopId);
	}

	@Override
	public ShopDetailVO getShopDetailVO(Long shopId) {
		return get(getSQL("ShopDetail.getShopDetailVO"), ShopDetailVO.class, shopId);
	}

	@Override
	public ShopOrderSettingDTO getShopOrderSetting(Long shopId) {
		return get(getSQL("ShopDetail.getShopOrderSetting"), ShopOrderSettingDTO.class, shopId);
	}

	@Override
	public PageSupport<ShopDetail> querySelect2(ShopDetailQuery shopDetailQuery) {

		CriteriaQuery cq = new CriteriaQuery(ShopDetail.class, shopDetailQuery.getPageSize(), shopDetailQuery.getCurPage());
		if (StrUtil.isNotBlank(shopDetailQuery.getShopName())) {
			cq.like("shopName", shopDetailQuery.getShopName(), MatchMode.ANYWHERE);
		}
		return queryPage(cq);
	}

	@Override
	public int batchUpdateStatus(List<Long> ids, Integer status) {
		StringBuilder sb = new StringBuilder("update ls_shop_detail set status =? where id in(");
		for (Long id : ids) {
			sb.append(id);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), status);
	}

	@Override
	public int updateCommissionRate(Long shopId, Double commissionRate) {
		return this.update("update ls_shop_detail set commission_rate = ? where id = ?", commissionRate, shopId);
	}

	@Override
	public int updateProdAudit(Long shopId, Integer status) {
		return this.update("update ls_shop_detail set prod_require_audit = ? where id = ?", status, shopId);
	}


	@Override
	public PageSupport<SearchShopDTO> searchShop(SearchShopQuery query) {
		QueryMap map = new QueryMap();

		map.put("shopStatus1", ShopDetailStatusEnum.ONLINE.getStatus());
		map.put("opStatus1", OpStatusEnum.PASS.getValue());
		map.put("shopStatus2", ShopDetailStatusEnum.ONLINE.getStatus());
		map.put("opStatus2", OpStatusEnum.PASS.getValue());
		map.put("userId", query.getUserId());
		map.put("id", query.getShopId());
		map.like("key", query.getKey(), MatchMode.ANYWHERE);
		if (SearchShopSortByEnum.CREDIT.getValue().equals(query.getSortBy())) {
			StringBuffer sortBy = new StringBuffer(", ROUND(IFNULL(sct.score,5)/IFNULL(sct.count,1),2)");
			sortBy.append(query.getDescending() ? " desc" : "asc");
			map.put("sortBy", sortBy);
		}
		if (SearchShopSortByEnum.BUYS.getValue().equals(query.getSortBy())) {
			StringBuffer sortBy = new StringBuffer(", sd.buys");
			sortBy.append(query.getDescending() ? " desc" : "asc");
			map.put("sortBy", sortBy);
		}
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(SearchShopDTO.class, query.getPageSize(), query.getCurPage());
		simpleSqlQuery.setSqlAndParameter("ShopDetail.searchShop", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public int updateBuys(Long shopId, Integer buys) {
		return update("update ls_shop_detail set buys = buys+? where id = ?", buys, shopId);
	}

	@Override
	public List<ShopDetailDTO> queryByIds(List<Long> shopIds) {
		if (CollUtil.isEmpty(shopIds)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<ShopDetailDTO> criterion = new LambdaEntityCriterion<>(ShopDetailDTO.class);
		criterion.in(ShopDetailDTO::getId, shopIds);
		return queryDTOByProperties(criterion);
	}

	@Override
	public List<ShopListDTO> queryShopList(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		StringBuilder sql = new StringBuilder("SELECT id, shop_name, shop_avatar, shop_brief AS briefDesc FROM ls_shop_detail WHERE id IN (");
		for (Long id : ids) {
			sql.append(id).append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(") and status = 1 and op_status = 1");
		return this.query(sql.toString(), ShopListDTO.class);
	}

	@Override
	public List<SearchShopProductDTO> queryPicsByShopIds(Long shopIds) {
		return query(getSQL("ShopDetail.queryPicsByShopIds"), SearchShopProductDTO.class, shopIds);
	}

	@Override
	public List<LogisticsCommentStatisticsBO> queryDvyTypeAvg(List<Long> shopIds) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("shopIds", shopIds);
		SQLOperation operation = getSQLAndParams("ShopDetail.queryDvyTypeAvg", queryMap);
		return query(operation.getSql(), LogisticsCommentStatisticsBO.class, operation.getParams());
	}

	@Override
	public List<ShopCommentStatisticsDTO> queryShopCommAvg(List<Long> shopIds) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("shopIds", shopIds);
		SQLOperation operation = getSQLAndParams("ShopDetail.queryShopCommAvg", queryMap);
		return query(operation.getSql(), ShopCommentStatisticsDTO.class, operation.getParams());
	}

	@Override
	public List<ProductCommentStatisticsBO> queryProductCommentAvg(List<Long> shopIds) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("shopIds", shopIds);
		SQLOperation sqlAndParams = getSQLAndParams("ShopDetail.queryProductCommentAvg", queryMap);
		return query(sqlAndParams.getSql(), ProductCommentStatisticsBO.class, sqlAndParams.getParams());
	}

	@Override
	public int updateDefaultDisScale(BigDecimal defaultDisScale, Long shopUserId) {
		return update("update ls_shop_detail set default_dis_scale = ? where shop_user_id = ?", defaultDisScale, shopUserId);
	}

	@Override
	public int updateShopNewBieStatus(Integer shopNewBieStatus, Long shopId) {
		return update("update ls_shop_detail set shop_new_bie_status = ? where id = ?", shopNewBieStatus, shopId);
	}

	@Override
	public Long searchAllShop(SearchShopQuery query) {
		QueryMap map = new QueryMap();
		SQLOperation operation = this.getSQLAndParams("ShopDetail.searchAllShop", map);
		Long count = get(operation.getSql(), Long.class, operation.getParams());
		return Optional.ofNullable(count).orElse(0L);
	}

	@Override
	public ShopMessageDTO getShopMessage(Long shopId) {
		return get("SELECT shop_name,qr_code,shop_avatar,shop_address,create_time,apply_for_type  FROM ls_shop_detail where  status=1 AND id = ?", ShopMessageDTO.class, shopId);
	}

	@Override
	public ShopDetailDocumentsDTO getshopDetailDocuments(Long shopId) {
		return get("SELECT company_name,shop_complete_address,business_start_time,business_end_time,unified_social_credit_code,registered_capital,corporate_name,business_license,business_scope  FROM ls_shop_detail where  status=1 AND id = ?", ShopDetailDocumentsDTO.class, shopId);
	}

}
