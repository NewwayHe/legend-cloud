/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.ShopDecoratePageBO;
import com.legendshop.shop.dao.ShopDecoratePageDao;
import com.legendshop.shop.dto.ShopDecoratePageDTO;
import com.legendshop.shop.entity.ShopDecoratePage;
import com.legendshop.shop.query.ShopDecoratePageQuery;
import com.legendshop.shop.service.ShopDecoratePageService;
import com.legendshop.shop.service.convert.ShopDecoratePageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 店铺主页装修服务实现类
 *
 * @author legendshop
 */
@Service
public class ShopDecoratePageServiceImpl implements ShopDecoratePageService {


	@Autowired
	private ShopDecoratePageDao shopDecoratePageDao;

	@Autowired
	private ShopDecoratePageConverter shopDecoratePageConverter;


	@Override
	public PageSupport<ShopDecoratePageBO> queryPageList(ShopDecoratePageQuery shopDecoratePageQuery) {
		return shopDecoratePageConverter.convert2BoPageList(shopDecoratePageDao.queryPageList(shopDecoratePageQuery));
	}

	@Override
	public PageSupport<ShopDecoratePageBO> queryPageListDesc(ShopDecoratePageQuery shopDecoratePageQuery) {
		PageSupport<ShopDecoratePage> shopDecoratePagePageSupport = shopDecoratePageDao.queryPageListDesc(shopDecoratePageQuery);
		if (ObjectUtil.isNotEmpty(shopDecoratePageQuery.getIsSetFirstEmpty())) {
			List<ShopDecoratePage> resultList = shopDecoratePagePageSupport.getResultList();
			List<ShopDecoratePage> result = new ArrayList<>();
			ShopDecoratePage shopDecorate = new ShopDecoratePage();
			result.add(shopDecorate);
			for (ShopDecoratePage shopDecoratePage : resultList) {
				result.add(shopDecoratePage);
			}
			shopDecoratePagePageSupport.setResultList(result);
		}
		return shopDecoratePageConverter.convert2BoPageList(shopDecoratePagePageSupport);
	}

	@Override
	public R save(ShopDecoratePageDTO shopDecoratePageDTO) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageConverter.from(shopDecoratePageDTO);
		shopDecoratePage.setRecDate(new Date());
		ShopDecoratePage originShopDecoratePage = shopDecoratePageDao.getById(shopDecoratePageDTO.getId());

		if (ObjectUtil.isNotNull(shopDecoratePageDTO.getId())) {
			if (originShopDecoratePage.getType() == 1) {
				return R.fail("官方模板不能编辑!");
			}
			//更新
			shopDecoratePage.setStatus(DecoratePageStatusEnum.MODIFIED.getNum());
			shopDecoratePage.setUseFlag(originShopDecoratePage.getUseFlag());
			shopDecoratePage.setType(originShopDecoratePage.getType());
			int result = shopDecoratePageDao.updateProperties(shopDecoratePage);
			if (result < 0) {
				return R.fail("更新失败");
			}
			return R.ok(shopDecoratePage.getId());
		} else {
			// 保存
			shopDecoratePage.setUseFlag(false);
			shopDecoratePage.setStatus(DecoratePageStatusEnum.DRAFT.getNum());
			shopDecoratePage.setType(2);
			Long result = shopDecoratePageDao.save(shopDecoratePage);
			if (result < 0) {
				return R.fail("保存失败");
			}
			return R.ok(result);
		}
	}

	@Override
	public R release(ShopDecoratePageDTO shopDecoratePageDTO) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageConverter.from(shopDecoratePageDTO);
		shopDecoratePage.setReleaseData(shopDecoratePageDTO.getData());
		shopDecoratePage.setRecDate(new Date());
		ShopDecoratePage originShopDecoratePage = shopDecoratePageDao.getById(shopDecoratePageDTO.getId());

		// 保存
		if (ObjectUtil.isNull(shopDecoratePageDTO.getId())) {
			shopDecoratePage.setUseFlag(false);
			shopDecoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
			shopDecoratePage.setType(2);
			Long result = shopDecoratePageDao.save(shopDecoratePage);
			if (result <= 0) {
				return R.fail("发布失败");
			}
			//更新
		} else {
			if (ObjectUtil.isNotEmpty(originShopDecoratePage.getType())) {
				if (originShopDecoratePage.getType() == 1) {
					return R.fail("官方模板不能编辑!");
				}
			}
			shopDecoratePage.setUseFlag(originShopDecoratePage.getUseFlag());
			shopDecoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
			shopDecoratePage.setType(2);
			int result = shopDecoratePageDao.updateProperties(shopDecoratePage);
			if (result <= 0) {
				return R.fail("发布失败");
			}
		}
		return R.ok();
	}

	@Override
	public R releasePage(Long id) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getById(id);
		if (ObjectUtil.isNull(shopDecoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		if (DecoratePageStatusEnum.RELEASED.getNum().equals(shopDecoratePage.getStatus())) {
			return R.fail("页面已经被发布，请刷新确认");
		}

		shopDecoratePage.setReleaseData(shopDecoratePage.getData());
		shopDecoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
		int result = shopDecoratePageDao.update(shopDecoratePage);
		if (result <= 0) {
			return R.fail("发布失败");
		}
		return R.ok();
	}

	@Override
	public R usePage(Long shopId, Long id) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(shopDecoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}

		if (shopDecoratePage.getUseFlag()) {
			return R.fail("页面已经被设为首页，请刷新确认");
		}
		if (shopDecoratePage.getCategory().equals(DecoratePageCategoryEnum.INDEX_T.value()) || shopDecoratePage.getCategory().equals(DecoratePageCategoryEnum.POSTER_T.value())) {
			return R.fail("设为首页失败, 模板不能设置为首页");
		}
		if (shopDecoratePage.getStatus().equals(DecoratePageStatusEnum.RELEASED.getNum())) {
			//全部设为非默认
			shopDecoratePageDao.updateDefaultPage(shopId, shopDecoratePage.getSource(), false);

			shopDecoratePageDao.updatePageToUnUse(shopId, shopDecoratePage.getSource());
			shopDecoratePage.setUseFlag(true);
			int result = shopDecoratePageDao.update(shopDecoratePage);
			if (result <= 0) {
				return R.fail("设为首页失败");
			}
		} else {
			return R.fail("需要先发布才能设为首页");
		}

		return R.ok();
	}

	@Override
	public R clone(Long id, String newName) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(shopDecoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		shopDecoratePage.setId(null);
		shopDecoratePage.setName(newName);
		shopDecoratePage.setUseFlag(false);
		shopDecoratePage.setReleaseData(null);
		shopDecoratePage.setStatus(DecoratePageStatusEnum.DRAFT.getNum());
		shopDecoratePage.setRecDate(new Date());

		Long result = shopDecoratePageDao.save(shopDecoratePage);
		if (result <= 0) {
			return R.fail("复制失败");
		}
		return R.ok();
	}

	@Override
	public R deleteById(Long id) {
		int result = shopDecoratePageDao.deleteById(id);
		if (result <= 0) {
			return R.fail("删除失败，该页面不存在或已被删除，请刷新确认");
		}
		return R.ok();
	}

	@Override
	public R updateName(Long id, String newName) {

		if (StrUtil.isBlank(newName.trim()) || (newName.trim()) == "") {
			return R.fail("页面名称不能为空，请确认后重试");
		}

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(shopDecoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		shopDecoratePage.setName(newName);
		int result = shopDecoratePageDao.update(shopDecoratePage);
		if (result <= 0) {
			return R.fail("修改名称失败");
		}
		return R.ok();
	}

	@Override
	public R updateCoverPicture(Long id, String coverPicture) {

		if (StrUtil.isBlank(coverPicture.trim()) || (coverPicture.trim()) == "") {
			return R.fail("封面图不能为空，请确认后重试");
		}
		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(shopDecoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		shopDecoratePage.setCoverPicture(coverPicture);
		int result = shopDecoratePageDao.update(shopDecoratePage);
		if (result <= 0) {
			return R.fail("更换封面图失败");
		}
		return R.ok();
	}

	@Override
	public R<JSONObject> getUsedIndexPage(Long shopId, String source) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getUsedIndexPage(shopId, source);
		if (ObjectUtil.isNull(shopDecoratePage) || ObjectUtil.isNull(shopDecoratePage.getReleaseData())) {
			return R.fail("暂无首页装修数据");
		}
		String releaseData = shopDecoratePage.getReleaseData();
		//忽略空值
		JSONObject result = JSONUtil.parseObj(releaseData, true);
		if (shopDecoratePage.getDefaultFlag()) {
			return R.ok();
		}
		return R.ok(result);
	}

	@Override
	public R<JSONObject> getPosterPageById(Long shopId, Long pageId, String source) {

		ShopDecoratePage shopDecoratePage = shopDecoratePageDao.getPosterPageById(shopId, pageId, source);
		if (ObjectUtil.isNull(shopDecoratePage) || ObjectUtil.isNull(shopDecoratePage.getReleaseData())) {
			return R.fail("暂无装修数据或该页面未发布");
		}
		String releaseData = shopDecoratePage.getReleaseData();
		JSONObject result = JSONUtil.parseObj(releaseData);
		return R.ok(result);
	}

	@Override
	public R<ShopDecoratePageBO> edit(Long pageId, Long shopId) {
		return R.ok(shopDecoratePageConverter.convert2BoPageList(shopDecoratePageDao.getByPageIdAndShopId(pageId, shopId)));
	}

	@Override
	public R<ShopDecoratePageBO> edit(Long pageId) {
		return R.ok(shopDecoratePageConverter.convert2BoPageList(shopDecoratePageDao.getById(pageId)));
	}

	@Override
	public R updateDefaultState(Long shopId, String source) {

		shopDecoratePageDao.updatePageToUnUse(shopId, source);
		//全部设为默认
		shopDecoratePageDao.updateDefaultPage(shopId, source, true);
		return R.ok();
	}


}
