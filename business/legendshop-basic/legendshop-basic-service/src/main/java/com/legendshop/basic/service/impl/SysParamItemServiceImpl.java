/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SysParamItemDao;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.entity.SysParamItem;
import com.legendshop.basic.query.SysParamItemQuery;
import com.legendshop.basic.service.SysParamCacheService;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.basic.service.convert.SysParamItemConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (SysParamItem)表服务实现类
 * <p>
 * 系统配置服务涉及大量缓存需要谨慎处理更新保存
 *
 * @author legendshop
 * @since 2020-08-28 14:17:39
 */
@Service
public class SysParamItemServiceImpl extends BaseServiceImpl<SysParamItemDTO, SysParamItemDao, SysParamItemConverter> implements SysParamItemService {

	@Autowired
	private SysParamItemDao sysParamItemDao;

	@Autowired
	private SysParamCacheService sysParamCacheService;

	@Autowired
	private SysParamItemConverter sysParamItemConverter;

	@Override
	public PageSupport<SysParamItemDTO> queryPageList(SysParamItemQuery sysParamItemQuery) {
		PageSupport<SysParamItem> pageSupport = sysParamItemDao.queryPageList(sysParamItemQuery);
		return sysParamItemConverter.page(pageSupport);
	}

	@Override
	public <T> T getConfigDtoByParentId(Long parentId, Class<T> config) {
		List<SysParamItem> paramItems = sysParamItemDao.getListByParentId(parentId);

		Map<String, String> map = new HashMap<>(16);
		paramItems.forEach(item -> {
			map.put(item.getKeyWord(), item.getValue());
		});

		return BeanUtil.mapToBean(map, config, true);
	}

	@Override
	public List<SysParamItemDTO> getListByParentId(Long paramId) {
		return sysParamItemConverter.to(sysParamItemDao.getListByParentId(paramId));
	}

	@Override
	public void updateSysParamItems(List<SysParamItemDTO> sysParamItemDTOS) {
		sysParamItemDTOS.forEach(sysParamItemDTO -> {
			sysParamItemDTO.setUpdateTime(DateUtil.date());
			update(sysParamItemDTO);
		});
	}


	@Override
	public R<SysParamItemDTO> getSysParamId(Long id) {
		return R.ok(sysParamItemConverter.to(sysParamItemDao.getById(id)));
	}
}
