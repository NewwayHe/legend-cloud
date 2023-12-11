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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.UserMsgApi;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.ReceiverBusinessTypeEnum;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dao.PubDao;
import com.legendshop.shop.dao.PubReceiverDao;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.entity.Pub;
import com.legendshop.shop.enums.ReceiverEnum;
import com.legendshop.shop.query.PubQuery;
import com.legendshop.shop.service.PubService;
import com.legendshop.shop.service.convert.PubConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

/**
 * 公告服务
 *
 * @author legendshop
 */
@Service
public class PubServiceImpl implements PubService {

	@Autowired
	private PubDao pubDao;

	@Autowired
	private PubConverter converter;

	@Autowired
	private PubReceiverDao pubReceiverDao;

	@Autowired
	private UserMsgApi userMsgApi;

	@Override
	public R<PubDTO> getPubById(Long id, Long userId, ReceiverEnum receiverEnum) {

		Pub pub = pubDao.getById(id);
		if (ObjectUtil.isNull(pub)) {
			return R.fail("该公告不存在或已被删除，请刷新后重试");
		}

		if (userId != null) {

			Boolean exist = userMsgApi.isExist(userId, id, ReceiverBusinessTypeEnum.NOTICE.value());
			if (!exist) {
				ArrayList<ReceiverDTO> arrayList = new ArrayList<ReceiverDTO>();
				ReceiverDTO receiver = new ReceiverDTO();
				receiver.setUserType(MsgReceiverTypeEnum.ORDINARY_USER.value());
				receiver.setUserId(userId);
				receiver.setBusinessId(id);
				receiver.setCreateTime(new Date());
				receiver.setStatus(1);
				receiver.setBusinessType(ReceiverBusinessTypeEnum.NOTICE.value());
				arrayList.add(receiver);
				userMsgApi.saveReceivers(arrayList);
			}
		}
		return R.ok(converter.to(pub));
	}

	@Override
	@CacheEvict(value = CacheConstants.PUB_DETAILS, allEntries = true)
	public int deletePub(Long id) {
		return pubDao.deleteById(id);
	}

	@Override
	@CacheEvict(value = CacheConstants.PUB_DETAILS, allEntries = true)
	public Long save(PubDTO pub) {
		if (ObjectUtil.isNotEmpty(pub.getId())) {
			Pub entity = pubDao.getById(pub.getId());
			if (entity != null) {
				entity.setCreateTime(DateUtil.date());
				entity.setContent(pub.getContent());
				entity.setTitle(pub.getTitle());
				entity.setType(pub.getType());
				entity.setStartTime(pub.getStartTime());
				entity.setEndTime(pub.getEndTime());
				pubDao.update(entity);
				return pub.getId();
			} else {
				// can not find pub
				return null;
			}

		} else {
			pub.setCreateTime(DateUtil.date());
			pub.setStatus(CommonConstants.STATUS_NORMAL);
			return (Long) pubDao.save(converter.from(pub));
		}

	}

	@Override
	@CacheEvict(value = CacheConstants.PUB_DETAILS, allEntries = true)
	public int update(PubDTO pub) {
		return pubDao.update(converter.from(pub));
	}


	@Override
	public PageSupport<PubDTO> getPubListPage(PubQuery pubQuery) {
		return converter.page(pubDao.getPubListPage(pubQuery));
	}

	@Override
	public PubDTO getNewestPubByType(Integer type) {
		return converter.to(pubDao.getNewestPubByType(type));
	}

	@Override
	public PageSupport<PubDTO> queryPubPageListByType(PubQuery pubQuery) {
		return pubDao.queryPubPageListByType(pubQuery);
	}

	@Override
	public R updateStatus(Long id, Integer status) {

		Pub pub = pubDao.getById(id);
		if (ObjectUtil.isNull(pub)) {
			return R.fail("该公告不存在或已被删除，请刷新后重试");
		}
		pub.setStatus(status);
		int result = pubDao.update(pub);
		if (result <= 0) {
			return R.fail("更新失败");
		}
		return R.ok();
	}

	@Override
	public R<Integer> userUnreadMsg(Long userId) {
		Integer count = pubDao.userUnreadMsg(userId);
		if (count == null || count < 0) {
			count = 0;
		}
		return R.ok(count);
	}


}
