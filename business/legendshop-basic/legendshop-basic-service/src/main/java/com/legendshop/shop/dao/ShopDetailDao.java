/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.ProductCommentStatisticsBO;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.*;
import com.legendshop.shop.entity.ShopDetail;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.vo.ShopDetailVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城信息lDao.
 *
 * @author legendshop
 */
public interface ShopDetailDao extends GenericDao<ShopDetail, Long> {


	/**
	 * 删除商城
	 *
	 * @param shopId
	 */
	int deleteShopDetailById(Long shopId);


	/**
	 * Gets the shop id by name.
	 *
	 * @param userName the user name
	 * @return the shop id by name
	 */
	Long getShopIdByName(String userName);

	Long getShopIdByUserId(Long userId);


	/**
	 * 开店用户找到自己的商城
	 *
	 * @param userId
	 * @return
	 */
	ShopDetail getByUserId(Long userId);

	ShopDetail getByShopId(Long shopId);

	List<ShopDetail> getAllOnline();

	Long getMaxShopId();

	Long getMinShopId();


	String getShopName(Long shopId);


	Integer updateShopType(Long shopId, Integer type);


	long getCountShopId();


	PageSupport<ShopDetail> getShopDetailPage(String curPageNo, ShopDetail shopDetail);

	PageSupport<ShopDetail> getShopDetailPage(ShopDetailQuery shopDetailQuery);

	/**
	 * 关联查询店铺及其类型信息
	 *
	 * @param userId
	 * @return
	 */
	ShopDetail getTypeShopDetailByUserId(Long userId);

	PageSupport<ShopDetail> page(ShopDetailQuery shopDetailQuery);

	Boolean updateOpStatus(List<Long> shopId, Integer opStatus);

	Boolean updateStatus(List<Long> shopId, Integer status);

	/**
	 * 通过shopUserId获取店铺数据
	 */
	ShopDetail getByShopUserId(Long shopUserId);

	/**
	 * 通过shopUserId获取店铺数据
	 */
	List<ShopDetail> getByShopUserId(List<Long> shopUserIds);

	/**
	 * 判断店铺名称是否存在
	 */
	boolean isShopNameExist(String shopName);

	/**
	 * 判断其他店铺名称是否存在,排除掉传递进来的那个账号
	 */
	boolean isOtherShopNameExist(Long shopId, String shopName);

	/**
	 * 判断用户Id是否存在
	 */
	boolean isShopUserIdExist(Long shopUserId);

	/**
	 * 判断店铺是否审核通过
	 *
	 * @param shopIds
	 * @return
	 */
	boolean isShopAuditPass(List<Long> shopIds);

	/**
	 * 商品详情店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	ShopDetailBO getUserShop(Long shopId);

	/**
	 * 获取商家基本信息
	 *
	 * @param shopId
	 * @return
	 */
	ShopDetailVO getShopDetailVO(Long shopId);

	/**
	 * 查询商家订单设置信息
	 *
	 * @param shopId
	 * @return
	 */
	ShopOrderSettingDTO getShopOrderSetting(Long shopId);

	/**
	 * 查询下拉框选择店铺
	 *
	 * @param shopDetailQuery
	 * @return
	 */
	PageSupport<ShopDetail> querySelect2(ShopDetailQuery shopDetailQuery);

	/**
	 * 批量修改状态
	 *
	 * @param ids
	 * @param status
	 * @return
	 */
	int batchUpdateStatus(List<Long> ids, Integer status);

	/**
	 * 修改店铺佣金比例
	 *
	 * @param shopId
	 * @param commissionRate
	 * @return
	 */
	int updateCommissionRate(Long shopId, Double commissionRate);

	/**
	 * 修改店铺商品是否需要审核
	 *
	 * @param shopId
	 * @param status
	 * @return
	 */
	int updateProdAudit(Long shopId, Integer status);

	/**
	 * 店铺搜索
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SearchShopDTO> searchShop(SearchShopQuery query);

	/**
	 * 更新购买数
	 *
	 * @param shopId
	 * @param buys
	 * @return
	 */
	int updateBuys(Long shopId, Integer buys);

	List<ShopDetailDTO> queryByIds(List<Long> shopIds);

	List<ShopListDTO> queryShopList(List<Long> ids);

	/**
	 * 根据店铺ID返回销量最高的4个商品图片路径
	 *
	 * @param shopIds 店铺ID
	 * @return
	 */
	List<SearchShopProductDTO> queryPicsByShopIds(Long shopIds);

	/**
	 * 根据店铺IDS 查询店铺物流平均分
	 *
	 * @param shopIds
	 * @return
	 */
	List<LogisticsCommentStatisticsBO> queryDvyTypeAvg(List<Long> shopIds);

	/**
	 * 根据店铺IDS 查询店铺服务平均分
	 *
	 * @param shopIds
	 * @return
	 */
	List<ShopCommentStatisticsDTO> queryShopCommAvg(List<Long> shopIds);

	/**
	 * 根据店铺IDS 查询店铺描述评价分
	 *
	 * @param shopIds
	 * @return
	 */
	List<ProductCommentStatisticsBO> queryProductCommentAvg(List<Long> shopIds);


	/**
	 * 商家修改默认分销比例
	 *
	 * @param defaultDisScale
	 * @param shopUserId
	 * @return
	 */
	int updateDefaultDisScale(BigDecimal defaultDisScale, Long shopUserId);

	int updateShopNewBieStatus(Integer shopNewBieStatus, Long shopId);

	Long searchAllShop(SearchShopQuery query);

	ShopMessageDTO getShopMessage(Long shopId);

	ShopDetailDocumentsDTO getshopDetailDocuments(Long shopId);
}
