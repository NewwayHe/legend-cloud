/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dao.IndustryDirectoryDao;
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import com.legendshop.shop.query.IndustryDirectoryQuery;
import com.legendshop.shop.service.IndustryDirectoryService;
import com.legendshop.shop.service.convert.IndustryDirectoryConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表服务实现类
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndustryDirectoryServiceImpl implements IndustryDirectoryService {

	private final IndustryDirectoryDao industryDirectoryDao;

	private final IndustryDirectoryConverter converter;

	@Override
	public R<PageSupport<IndustryDirectoryDTO>> pageList(IndustryDirectoryQuery query) {
		return R.ok(this.converter.page(this.industryDirectoryDao.pageList(query)));
	}

	@Override
	public R<List<IndustryDirectoryDTO>> availableList(IndustryDirectoryQuery query) {
		return R.ok(this.converter.to(this.industryDirectoryDao.availableList(query)));
	}

	@Override
	public R<Void> deleteById(Long id) {
		return R.process(this.industryDirectoryDao.deleteById(id) > 0, "删除失败！");
	}

	@Override
	public R<Void> save(IndustryDirectoryDTO industryDirectoryDTO) {
		industryDirectoryDTO.setState(true);
		return R.process(this.industryDirectoryDao.save(this.converter.from(industryDirectoryDTO)) > 0, "保存失败！");
	}

	@Override
	public R<Void> update(IndustryDirectoryDTO industryDirectoryDTO) {
		industryDirectoryDTO.setState(true);
		return R.process(this.industryDirectoryDao.update(this.converter.from(industryDirectoryDTO)) > 0, "更新失败！");
	}

	@Override
	public IndustryDirectoryDTO getById(Long id) {
		return this.converter.to(this.industryDirectoryDao.getById(id));

	}

	@Override
	public List<IndustryDirectoryDTO> queryById(List<Long> ids) {
		return this.converter.to(this.industryDirectoryDao.queryById(ids));
	}

	@Override
	public List<IndustryDirectoryDTO> queryAll() {
		return this.converter.to(this.industryDirectoryDao.queryAll());
	}
}
