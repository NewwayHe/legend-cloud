/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import com.legendshop.basic.enums.ThirdPartyAuthTypeEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.DistributionWithdrawAccountDTO;
import com.legendshop.pay.enums.WithdrawTypeEnum;
import com.legendshop.user.bo.PassportBO;
import com.legendshop.user.dao.PassportDao;
import com.legendshop.user.dao.PassportItemDao;
import com.legendshop.user.dto.PassportDTO;
import com.legendshop.user.dto.PassportItemDTO;
import com.legendshop.user.dto.WxUserInfo;
import com.legendshop.user.entity.Passport;
import com.legendshop.user.entity.PassportItem;
import com.legendshop.user.service.PassportService;
import com.legendshop.user.service.convert.PassportConverter;
import com.legendshop.user.service.convert.PassportItemConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

	final PassportDao passportDao;

	final PassportItemDao passportItemDao;

	final PassportConverter passportConverter;

	final PassportItemConverter passportItemConverter;

	@Override
	public R<String> getOpenIdByUserId(Long userId, String source) {
		return R.ok(this.passportDao.getOpenIdByUserId(userId, source));
	}

	@Override
	public R<PassportDTO> getByUserIdAndType(Long userId, String type) {
		return R.ok(this.passportConverter.to(passportDao.getByUserIdAndType(userId, type)));
	}

	@Override
	public R<PassportBO> updateAuthInfo(PassportBO bo) {
		PassportItem item = this.passportDao.authInfo(bo.getUserId(), bo.getType(), bo.getSource());
		//如果是找到绑定记录，则说明，用户已绑定过，需要先退出原来的微信才能再进行绑定，
		if (null != item && !bo.getThirdPartyIdentifier().equals(item.getThirdPartyIdentifier())) {
			log.info("找到绑定记录, 需要先退出原来的微信才能再进行绑定, 用户id:{}, 类型:{}, 来源:{}", bo.getUserId(), bo.getType(), bo.getSource());
			this.unboundUserPassport(bo.getUserId(), bo.getType(), bo.getSource());
		}

		Passport passport = this.passportDao.getPassByIdentifier(bo.getThirdPartyIdentifier());
		log.info("获取到通信证信息, passport:{}", passport);
		passport.setUserId(bo.getUserId());
		this.passportDao.update(passport);
		log.info("更新通信证信息, 用户id:{}", passport.getUserId());


		// 更新用户分销钱包微信账号
		DistributionWithdrawAccountDTO accountDTO = new DistributionWithdrawAccountDTO();
		accountDTO.setUserId(passport.getUserId());
		accountDTO.setRealName(passport.getNickName());
		accountDTO.setAccount(bo.getThirdPartyIdentifier());
		accountDTO.setWithdrawType(WithdrawTypeEnum.WECHAT_DISTRIBUTION_WALLET.name());
		log.info("微信第三方信息处理成功, 用户id:{}", passport.getUserId());
		return R.ok(bo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<PassportDTO> saveToWxUserInfo(WxUserInfo wxUserInfo) {
		Date now = new Date();
		PassportDTO passport = new PassportDTO();
		passport.setNickName(wxUserInfo.getNickname());
		passport.setHeadPortraitUrl(wxUserInfo.getHeadimgurl());
		passport.setSex(wxUserInfo.getSex());
		passport.setProvince(wxUserInfo.getProvince());
		passport.setCity(wxUserInfo.getCity());
		passport.setCountry(wxUserInfo.getCountry());
		passport.setType(ThirdPartyAuthTypeEnum.WE_CHAT.name());
		passport.setCreateTime(now);
		passport.setUpdateTime(now);
		Long passportId = this.passportDao.save(passportConverter.from(passport));
		passport.setId(passportId);
		PassportItemDTO passportItem = new PassportItemDTO();
		passportItem.setPassPortId(passportId);
		passportItem.setThirdPartyIdentifier(wxUserInfo.getOpenid());
		passportItem.setSource(wxUserInfo.getSource().name());
		passportItem.setCreateTime(now);
		passportItem.setUpdateTime(now);
		this.passportItemDao.save(passportItemConverter.from(passportItem));
		return R.ok(passport);
	}

	@Override
	public R<Long> savePassportItem(PassportItemDTO itemDTO) {
		return R.ok(this.passportItemDao.save(this.passportItemConverter.from(itemDTO)));
	}

	@Override
	public PassportDTO getPassByIdentifier(String identifier) {
		return this.passportConverter.to(this.passportDao.getPassByIdentifier(identifier));
	}

	@Override
	public int update(PassportDTO passportDTO) {
		return this.passportDao.update(this.passportConverter.from(passportDTO));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> updateItemBindParent(Long passportId, String thirdPartyIdentifier) {
		PassportItem passportItem = this.passportItemDao.getByThirdPartyIdentifier(thirdPartyIdentifier);
		if (null == passportItem) {
			log.error("查询授权记录失败 thirdPartyIdentifier：{}", thirdPartyIdentifier);
			return R.fail("错误的标识！");
		}
		Long uselessId = passportItem.getPassPortId();
		this.passportDao.deleteById(uselessId);
		passportItem.setPassPortId(passportId);
		this.passportItemDao.update(passportItem);
		return R.ok();
	}

	@Override
	public R<Void> clearPassportItem(Long userId, String source) {
		return R.process(this.passportDao.clearPassportItem(userId, source), "对用户第三方绑定账号解绑次失败");
	}

	@Override
	public R<List<String>> getOpenIdByUserIds(List<Long> userIds, String source) {
		return R.ok(passportDao.getOpensByUserIds(userIds, source));
	}

	/**
	 * 退出时，删除对应的第三方ID
	 *
	 * @param userId
	 * @param type
	 * @param source
	 * @return
	 */
	@Override
	public R<Boolean> unboundUserPassport(Long userId, String type, String source) {
		PassportItem item = this.passportDao.authInfo(userId, type, source);

		if (item == null) {
			log.info("获取不到通行证子表信息无需处理, 用户id:{}, 类型:{},来源:{}", userId, type, source);
			return R.ok(true);
		}

		//如果是找到绑定记录，则说明，用户已绑定过，需要先退出原来的微信才能再进行绑定，
		//删除对应的passportItem,以及对应的passport记录
		PassportItem passportItem = passportItemDao.getByThirdPartyIdentifier(item.getThirdPartyIdentifier());
		Passport passport = passportDao.getById(passportItem.getPassPortId());
		//TODO,可以考虑passport与passportItem是否可以一对多，则不需要删除passport
		log.info("获取到对应的通行证表信息, 用户id:{}, 类型:{},来源:{}", userId, type, source);
		passportDao.delete(passport);
		passportItemDao.delete(passportItem);
		log.info("删除对应的通行证表信息, 用户id:{}, 类型:{},来源:{}", userId, type, source);
		return R.ok(true);
	}

	@Override
	public PassportDTO getByUserId(Long userId) {
		if (null == userId) {
			return null;
		}
		return passportConverter.to(this.passportDao.getByUserId(userId));
	}
}
