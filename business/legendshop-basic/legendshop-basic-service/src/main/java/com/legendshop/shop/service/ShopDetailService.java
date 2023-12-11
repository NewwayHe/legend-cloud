/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.*;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.vo.ShopDetailVO;
import com.legendshop.shop.vo.ShopInfoVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城信息服务.
 *
 * @author legendshop
 */
public interface ShopDetailService {

	/**
	 * 用缓存来读取商城信息.
	 *
	 * @param id the id
	 * @return the shop detail
	 */
	ShopDetailDTO getById(Long id);

	/**
	 * 审核商城.
	 *
	 * @param shopDetail the shop detail
	 * @return true, if successful
	 */
	boolean updateShop(ShopDetailDTO shopDetail);

	Long getShopIdByUserId(Long userId);

	ShopDetailDTO getByUserId(Long userId);


	List<ShopDetailDTO> queryByUserIds(List<Long> userIds);


	Long getMaxShopId();

	long getCountShopId();


	Long getMinShopId();


	Long saveContactInfo(ShopDetailDTO shopDetail, Long userId, String userName);


	String getShopName(Long shopId);


	Integer updateShopType(Long shopId, Integer type);


	PageSupport<ShopDetailDTO> getShopDetailPage(ShopDetailQuery shopDetailQuery);

	/**
	 * 关联查询店铺及其类型信息
	 */
	ShopDetailDTO getTypeShopDetailByUserId(Long userId);

	/**
	 * 获取所有在线的店铺列表
	 */
	List<ShopDetailDTO> queryOnlineList();

	PageSupport<ShopDetailDTO> page(ShopDetailQuery shopDetailQuery);

	R audit(AuditDTO shopDetailAuditDTO);

	PageSupport<AuditDTO> auditHistory(Long shopId, PageParams pageParams);

	Long saveShopDetail(ShopDetailDTO shopDetailDTO, String ip);

	/**
	 * 平台、商家更新店铺信息
	 *
	 * @param shopDetailDTO
	 * @return
	 */
	R<Void> updateShopDetail(ShopDetailDTO shopDetailDTO);

	/**
	 * 更新商家入驻信息
	 *
	 * @param shopDetailDTO
	 * @return
	 */
	R<Void> updateSettleInInfo(ShopDetailDTO shopDetailDTO);

	/**
	 * 通过shopUserId获取店铺数据
	 */
	ShopDetailVO getByShopUserId(Long shopUserId);

	/**
	 * 判断店铺名称是否存在
	 */
	boolean isShopNameExist(String shopName);

	/**
	 * 判断用户Id是否存在
	 */
	boolean isShopUserIdExist(Long shopUserId);


	/**
	 * 商品详情店铺信息
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	R<ShopDetailBO> getUserShop(Long userId, Long shopId);

	/**
	 * 获取商品详情
	 *
	 * @param shopId
	 * @return
	 */
	ShopDetailVO getShopDetailVO(Long shopId);

	/**
	 * 获取商家订单设置
	 *
	 * @param shopId
	 * @return
	 */
	ShopOrderSettingDTO getShopOrderSetting(Long shopId);

	/**
	 * 更新商家订单设置
	 *
	 * @param shopOrderSettingDTO
	 * @return
	 */
	R<Void> updateShopOrderSetting(ShopOrderSettingDTO shopOrderSettingDTO);

	/**
	 * 查询下拉框选择店铺
	 *
	 * @param shopDetailQuery
	 * @return
	 */
	PageSupport<ShopSelectDTO> querySelect2(ShopDetailQuery shopDetailQuery);

	/**
	 * 批量修改状态
	 *
	 * @param dto
	 * @return
	 */
	R batchUpdateStatus(BasicBatchUpdateStatusDTO dto);

	/**
	 * 修改店铺佣金比例
	 *
	 * @param shopId
	 * @param commissionRate
	 * @return
	 */
	Integer updateCommissionRate(Long shopId, Double commissionRate);

	/**
	 * 修改店铺商品是否需要审核
	 *
	 * @param shopId
	 * @param status
	 * @return
	 */
	Integer updateProdAudit(Long shopId, Integer status);

	/**
	 * 获取店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	ShopInfoVO getShopInfoVO(Long shopId);

	/**
	 * 店铺搜索
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SearchShopDTO> searchShop(SearchShopQuery query);

	/**
	 * 店铺搜索
	 *
	 * @param query
	 * @return
	 */
	Long searchAllShop(SearchShopQuery query);

	/**
	 * 更新购买数
	 *
	 * @param shopId
	 * @param buys
	 * @return
	 */
	R<Boolean> updateBuys(Long shopId, Integer buys);


	R<List<ShopDetailDTO>> queryAll();

	List<ShopDetailDTO> queryByIds(List<Long> shopIds);

	/**
	 * PC端首页展示的店铺列表查询
	 *
	 * @param ids
	 * @return
	 */
	List<ShopListDTO> queryShopList(List<Long> ids);

	/**
	 * 获得用户是否收藏商家标记
	 *
	 * @param userId 用户ID
	 * @param shopId 商家ID
	 * @return
	 */
	Boolean getUserShopFlag(Long userId, Long shopId);

	/**
	 * 根据店铺ID 返回店铺信息
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	ShopDetailBO getUserShopEs(Long userId, Long shopId);


	/**
	 * 修改商家默认分销比例
	 *
	 * @param defaultDisScale
	 * @param shopUserId
	 * @return
	 */
	R<ShopDetailDTO> updateDefaultDisScale(BigDecimal defaultDisScale, Long shopUserId);

	/**
	 * 获取商家默认分销比例
	 *
	 * @param shopUserId
	 * @return
	 */
	R<BigDecimal> getDefaultDisScale(Long shopUserId);

	int updateShopNewBieStatus(Integer shopNewBieStatus, Long shopUserId);

	/**
	 * 商家店铺详情
	 *
	 * @param shopId
	 * @return
	 */
	R<ShopMessageDTO> getshopDetail(Long shopId);


	R<ShopDetailDocumentsDTO> getshopDetailDocuments(Long userId, Long shopId);
}
