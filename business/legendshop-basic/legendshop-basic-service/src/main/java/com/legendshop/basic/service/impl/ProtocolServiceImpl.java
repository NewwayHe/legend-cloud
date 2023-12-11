/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.ProtocolDao;
import com.legendshop.basic.dto.AdminProtocolDTO;
import com.legendshop.basic.dto.ProtocolDTO;
import com.legendshop.basic.entity.Protocol;
import com.legendshop.basic.query.ProtocolQuery;
import com.legendshop.basic.service.ProtocolService;
import com.legendshop.basic.service.convert.ProtocolConverter;
import com.legendshop.common.core.constant.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Protocol)服务实现类
 *
 * @author legendshop
 */
@Slf4j
@Service
public class ProtocolServiceImpl implements ProtocolService {

	@Autowired
	private ProtocolDao protocolDao;

	@Autowired
	private ProtocolConverter protocolConverter;

	@Override
	public PageSupport<ProtocolDTO> queryPageList(ProtocolQuery protocolQuery) {
		PageSupport<Protocol> ps = protocolDao.queryPageList(protocolQuery);
		return protocolConverter.page(ps);
	}

	@Override
	public R<Void> updateUrl(AdminProtocolDTO adminProtocolDTO) {
		Protocol protocol = protocolDao.getById(adminProtocolDTO.getId());
		if (ObjectUtil.isNull(protocol)) {
			return R.fail("该协议不存在或已被删除，刷新后重试");
		}
		Integer type = adminProtocolDTO.getType();
		if (type == null) {
			return R.fail("更新失败");
		}
		protocol.setType(type);
		if (type != 1 && type != 0) {
			return R.fail("协议类型无效");
		}
		if (type == 1) {
			protocol.setText(adminProtocolDTO.getText());
		}
		if (type == 0) {
			protocol.setUrl(adminProtocolDTO.getUrl());
		}
		protocol.setUpdateTime(DateUtil.date());
		int count = protocolDao.update(protocol);
		if (count <= 0) {
			return R.fail("更新失败");
		}
		return R.ok();
	}


	@Override
	public R<ProtocolDTO> getByCode(String code) {
		Protocol protocol = protocolDao.getByCode(code);
		return R.ok(protocolConverter.to(protocol));
	}

	@Override
	public R<ProtocolDTO> getById(Long id) {
		return R.ok(protocolConverter.to(protocolDao.getById(id)));
	}
}
