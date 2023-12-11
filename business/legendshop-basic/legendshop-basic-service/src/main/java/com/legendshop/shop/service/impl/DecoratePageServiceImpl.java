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
import com.alibaba.fastjson.JSON;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.DecoratePageBO;
import com.legendshop.shop.dao.DecoratePageDao;
import com.legendshop.shop.dto.DecoratePageDTO;
import com.legendshop.shop.dto.DecoratePageDateDTO;
import com.legendshop.shop.entity.DecoratePage;
import com.legendshop.shop.service.DecoratePageService;
import com.legendshop.shop.service.convert.DecoratePageConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 装修页面(DecoratePage)服务实现类
 *
 * @author legendshop
 */
@Slf4j
@Service
public class DecoratePageServiceImpl implements DecoratePageService {

	@Autowired
	private DecoratePageDao decoratePageDao;

	@Autowired
	private DecoratePageConverter converter;

	@Override
	public PageSupport<DecoratePageBO> queryPageList(DecoratePageQuery decoratePageQuery) {
		PageSupport<DecoratePage> ps = decoratePageDao.queryPageList(decoratePageQuery);
		return converter.convert2BoPageList(ps);
	}

	@Override
	public PageSupport<DecoratePageBO> queryPageListDesc(DecoratePageQuery decoratePageQuery) {
		PageSupport<DecoratePage> ps = decoratePageDao.queryPageListDesc(decoratePageQuery);
		return converter.convert2BoPageList(ps);
	}

	@Override
	@CacheEvict(value = CacheConstants.THEME_COLOR, allEntries = true)
	public R<Long> save(DecoratePageDTO decoratePageDTO) {

		DecoratePage originDecoratePage = decoratePageDao.getById(decoratePageDTO.getId());
		DecoratePage decoratePage = converter.from(decoratePageDTO);
		Long saveResult = null;
		// 保存
		if (ObjectUtil.isNull(originDecoratePage)) {

			decoratePage.setUseFlag(false);
			decoratePage.setRecDate(new Date());
			decoratePage.setStatus(DecoratePageStatusEnum.DRAFT.getNum());
			decoratePage.setType(2);
			Long result = decoratePageDao.save(decoratePage);
			if (result <= 0) {
				return R.fail("保存失败");
			}
			saveResult = result;
			//更新
		} else {
			if (ObjectUtil.isNotEmpty(originDecoratePage.getType()) && originDecoratePage.getType() == 1) {
				return R.fail("官方模板不能编辑!");
			}
			decoratePage.setRecDate(new Date());
			decoratePage.setUseFlag(originDecoratePage.getUseFlag());
			decoratePage.setStatus(DecoratePageStatusEnum.MODIFIED.getNum());
			decoratePage.setReleaseData(originDecoratePage.getReleaseData());
			int result = decoratePageDao.update(decoratePage);
			if (result <= 0) {
				return R.fail("更新失败");
			}
			saveResult = decoratePage.getId();
		}
		return R.ok(saveResult);
	}

	@Override
	@CacheEvict(value = CacheConstants.THEME_COLOR, allEntries = true)
	public R<Long> release(DecoratePageDTO decoratePageDTO) {

		DecoratePage originDecoratePage = decoratePageDao.getById(decoratePageDTO.getId());
		DecoratePage decoratePage = converter.from(decoratePageDTO);
		Long releaseResult = null;

		if (ObjectUtil.isNull(originDecoratePage)) {
			// 保存
			decoratePage.setReleaseData(decoratePageDTO.getData());
			decoratePage.setUseFlag(false);
			decoratePage.setRecDate(new Date());
			decoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
			decoratePage.setType(2);
			Long result = decoratePageDao.save(decoratePage);
			if (result <= 0) {
				return R.fail("发布失败");
			}
			releaseResult = result;
		} else {
			if (ObjectUtil.isNotEmpty(originDecoratePage.getType()) && originDecoratePage.getType() == 1) {
				return R.fail("官方模板不能编辑!");
			}
			//更新
			decoratePage.setReleaseData(decoratePageDTO.getData());
			decoratePage.setRecDate(new Date());
			decoratePage.setUseFlag(originDecoratePage.getUseFlag());
			decoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
			int result = decoratePageDao.update(decoratePage);
			if (result <= 0) {
				return R.fail("发布失败");
			}
			releaseResult = decoratePage.getId();
		}
		return R.ok(releaseResult);
	}

	@Override
	@CacheEvict(value = CacheConstants.THEME_COLOR, allEntries = true)
	public R releasePage(Long id) {

		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isNull(decoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		if (DecoratePageStatusEnum.RELEASED.getNum().equals(decoratePage.getStatus())) {
			return R.fail("页面已经被发布，请刷新确认");
		}

		decoratePage.setReleaseData(decoratePage.getData());
		decoratePage.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
		int result = decoratePageDao.update(decoratePage);
		if (result <= 0) {
			return R.fail("发布失败");
		}
		return R.ok();
	}

	@Override
	@CacheEvict(value = CacheConstants.THEME_COLOR, allEntries = true)
	public R usePage(Long id, String category) {

		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(decoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}

		if (decoratePage.getUseFlag()) {
			return R.fail("页面已经被设为首页，请刷新确认");
		}

		if (!decoratePageDao.updatePageToUnUse(decoratePage.getSource(), category)) {
			return R.fail("设为首页失败");
		}
		if (decoratePage.getCategory().equals(DecoratePageCategoryEnum.INDEX_T.value()) || decoratePage.getCategory().equals(DecoratePageCategoryEnum.POSTER_T.value())) {
			return R.fail("设为首页失败, 模板不能设置为首页");
		}
		decoratePage.setUseFlag(true);
		int result = decoratePageDao.update(decoratePage);
		if (result <= 0) {
			return R.fail("设为首页失败");
		}
		return R.ok();
	}

	@Override
	public R clone(Long id, String newName) {

		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(decoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		decoratePage.setId(null);
		decoratePage.setName(newName);
		decoratePage.setUseFlag(false);
		decoratePage.setReleaseData(null);
		decoratePage.setStatus(DecoratePageStatusEnum.DRAFT.getNum());
		decoratePage.setRecDate(new Date());

		Long result = decoratePageDao.save(decoratePage);
		if (result <= 0) {
			return R.fail("复制失败");
		}
		return R.ok();
	}

	@Override
	public R deleteById(Long id) {
		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(decoratePage)) {
			return R.fail("删除失败，该页面不存在或已被删除，请刷新确认");
		}
		if (null != decoratePage.getType() && decoratePage.getType() == 1) {
			R.fail("官方模板，无权限删除!");
		}
		int result = decoratePageDao.deleteById(id);
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

		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(decoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		decoratePage.setName(newName);
		int result = decoratePageDao.update(decoratePage);
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
		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isEmpty(decoratePage)) {
			return R.fail("页面不存在或已被删除，请刷新后重试");
		}
		decoratePage.setCoverPicture(coverPicture);
		int result = decoratePageDao.update(decoratePage);
		if (result <= 0) {
			return R.fail("更换封面图失败");
		}
		return R.ok();
	}

	@Override
	public R<String> getUsedIndexPage(String source) {

		DecoratePage decoratePage = decoratePageDao.getUsedIndexPage(source);
		if (ObjectUtil.isNull(decoratePage) || ObjectUtil.isNull(decoratePage.getReleaseData())) {
			return R.fail("暂无首页装修数据");
		}
		return R.ok(decoratePage.getReleaseData());
	}

	@Override
	public R<JSONObject> getPosterPageById(Long pageId, String source) {

		DecoratePage decoratePage = decoratePageDao.getPosterPageById(pageId, source);
		if (ObjectUtil.isNull(decoratePage) || ObjectUtil.isNull(decoratePage.getReleaseData())) {
			return R.fail("暂无装修数据或该页面未发布");
		}
		String releaseData = decoratePage.getReleaseData();
		JSONObject result = JSONUtil.parseObj(JSON.parse(releaseData), true);
		return R.ok(result);
	}

	@Override
	@Cacheable(value = CacheConstants.THEME_COLOR)
	public R<String> getThemeColor(String source) {

		R<String> usedIndexPage = this.getUsedIndexPage(source);
		if (ObjectUtil.isNull(usedIndexPage.getData())) {
			return R.ok(null);
		}
		String usedIndexPageData = usedIndexPage.getData();
		DecoratePageDateDTO decoratePageDateDTO = JSONUtil.toBean(usedIndexPageData, DecoratePageDateDTO.class);
		return R.ok(decoratePageDateDTO.getThemeColor());
	}

	@Override
	public R<DecoratePageDTO> edit(Long id) {
		DecoratePage decoratePage = decoratePageDao.getById(id);
		if (ObjectUtil.isNull(decoratePage)) {
			R.fail("该页面不存在或已被删除，请刷新后重试");
		}
		return R.ok(converter.to(decoratePage));
	}

	@Override
	public R<String> getUsedCenterPage(String source) {
		DecoratePage decoratePage = decoratePageDao.getUsedCenterPage(source);
		if (ObjectUtil.isNull(decoratePage) || ObjectUtil.isNull(decoratePage.getReleaseData())) {
			return R.fail("暂无首页装修数据");
		}
		return R.ok(decoratePage.getReleaseData());
	}

	@Override
	public R<Boolean> enableUserCenter(String source) {
		DecoratePage decoratePage = decoratePageDao.getUsedCenterPage(source);
		if (null == decoratePage || StringUtils.isBlank(decoratePage.getData())) {
			return R.ok(false);
		}
		DecoratePageDateDTO decoratePageDateDTO = JSONUtil.toBean(decoratePage.getData(), DecoratePageDateDTO.class);
		Boolean enable = decoratePageDateDTO.getEnable();
		if (null == enable || !enable) {
			return R.ok(false);
		}
		return R.ok(true);

	}

	@Override
	public R<String> getUsedDistributionCenterPage(String source) {
		DecoratePage decoratePage = decoratePageDao.getUsedDistributionCenterPage(source);
		if (ObjectUtil.isNull(decoratePage) || ObjectUtil.isNull(decoratePage.getReleaseData())) {
			return R.fail("暂无首页装修数据");
		}
		return R.ok(decoratePage.getReleaseData());
	}

	@Override
	public R<String> getUsedIndexPag() {
		DecoratePage usedIndexPag = decoratePageDao.getUsedIndexPag();
		if (ObjectUtil.isNull(usedIndexPag)) {
			return R.ok();
		}
		if (ObjectUtil.isNull(usedIndexPag.getData())) {
			return R.ok();
		}
		String tabbar = JSONUtil.parseObj(usedIndexPag.getData()).getStr("tabbar");
		return R.ok(tabbar);
	}
}
