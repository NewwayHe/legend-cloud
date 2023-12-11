/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.ShopBannerDTO;

/**
 * 商家默认的轮换图服务
 *
 * @author legendshop
 */
public interface ShopBannerService {

	/**
	 * 获取指定ID的商家轮换图信息
	 *
	 * @param id 轮换图ID
	 * @return 商家轮换图信息
	 */
	ShopBannerDTO getShopBanner(Long id);

	/**
	 * 删除指定ID的商家轮换图
	 *
	 * @param id        轮换图ID
	 * @param imageFile 图片文件
	 * @return 删除操作结果
	 */
	int deleteShopBanner(Long id, String imageFile);

	/**
	 * 保存商家轮换图信息
	 *
	 * @param shopBannerDto 商家轮换图DTO
	 * @return 保存后的商家轮换图ID
	 */
	Long saveShopBanner(ShopBannerDTO shopBannerDto);

	/**
	 * 更新商家轮换图信息
	 *
	 * @param shopBannerDto 商家轮换图DTO
	 * @return 更新操作结果
	 */
	int updateShopBanner(ShopBannerDTO shopBannerDto);

	/**
	 * 获取商家的轮换图列表
	 *
	 * @param curPageNo 当前页码
	 * @param shopId    商家ID
	 * @return 商家的轮换图列表
	 */
	PageSupport<ShopBannerDTO> getShopBanner(String curPageNo, Long shopId);

}
