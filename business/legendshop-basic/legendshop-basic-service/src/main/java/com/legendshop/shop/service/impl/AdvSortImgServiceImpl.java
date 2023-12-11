/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.api.CategoryApi;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.shop.dao.AdvSortImgDao;
import com.legendshop.shop.dto.AdvSortImgDTO;
import com.legendshop.shop.entity.AdvSortImg;
import com.legendshop.shop.query.AdvSortImgQuery;
import com.legendshop.shop.service.AdvSortImgService;
import com.legendshop.shop.service.convert.AdvSortImgConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (AdvSortImg)表服务实现类
 *
 * @author legendshop
 * @since 2021-07-09 15:28:05
 */
@Service
@AllArgsConstructor
public class AdvSortImgServiceImpl extends BaseServiceImpl<AdvSortImgDTO, AdvSortImgDao, AdvSortImgConverter> implements AdvSortImgService {

	private final AdvSortImgDao advSortImgDao;
	private final AdvSortImgConverter advSortImgConverter;

	private final CategoryApi categoryApi;

	@Override
	public List<AdvSortImgDTO> queryByAdvSortId(Long sortId) {

		List<AdvSortImg> advSortImgs = advSortImgDao.queryByAdvSortId(sortId);

		List<AdvSortImgDTO> advSortImgDTOList = new ArrayList<>();
		for (AdvSortImg advSortImg : advSortImgs) {
			advSortImgDTOList.add(advSortImgConverter.to(advSortImg));
		}
		return advSortImgDTOList;
	}

	@Override
	public R<PageSupport<AdvSortImgDTO>> queryPage(AdvSortImgQuery advSortImgQuery) {
		PageSupport<AdvSortImg> advSortImgPageSupport = advSortImgDao.queryPageImg(advSortImgQuery);
		PageSupport<AdvSortImgDTO> advSortImgDtoPageSupport = advSortImgConverter.page(advSortImgPageSupport);
		advSortImgDtoPageSupport.getResultList().forEach(a -> {
			R<String> categoryNameR = categoryApi.getCategoryNameById(a.getCategoryId());
			if (categoryNameR.success()) {
				a.setCategoryName(categoryNameR.getData());
			}
		});
		return R.ok(advSortImgDtoPageSupport);
	}

	@Override
	public R<List<CategoryBO>> getTopCategory() {
		return R.ok(advSortImgDao.getTopCategory());
	}

	@Override
	public R batchUpdateStatus(List<Long> ids, Integer status) {
		advSortImgDao.batchUpdateStatus(ids, status);
		return R.ok();
	}

	@Override
	public R<List<AdvSortImgDTO>> queryByCategoryId(Long categoryId) {
		List<AdvSortImg> list = advSortImgDao.queryByCategoryId(categoryId);
		return R.ok(advSortImgConverter.to(list));
	}

	@Override
	public R saveCategoryAdv(AdvSortImgDTO advSortImgDTO) {
		return save(advSortImgDTO);
	}

	@Override
	public R deleteCategoryAdv(Long id) {
		AdvSortImg advSortImg = advSortImgDao.getById(id);
		if (ObjectUtil.isNull(advSortImg)) {
			return R.fail("分类广告不存在!");
		}
		if (Objects.equals(advSortImg.getStatus(), 1)) {
			return R.fail("当前分类广告上线状态,无法删除!");
		}
		advSortImgDao.deleteById(id);
		return R.ok();
	}

	@Override
	public R updateCategoryAdv(AdvSortImgDTO advSortImgDTO) {
		AdvSortImg advSortImg = advSortImgDao.getById(advSortImgDTO.getId());
		if (advSortImg == null) {
			return R.fail("更新失败，该分类广告不存在！");
		}

		advSortImg.setUpdateTime(DateUtil.date());
		advSortImg.setCategoryId(advSortImgDTO.getCategoryId());
		advSortImg.setAdvPath(advSortImgDTO.getAdvPath());
		advSortImg.setAdvUrl(JSONUtil.toJsonStr(advSortImgDTO.getAdvUrl()));
		advSortImg.setAdvImgName(advSortImgDTO.getAdvImgName());
		advSortImg.setStatus(advSortImgDTO.getStatus());

		advSortImgDao.update(advSortImg);
		return R.ok();
	}
}
