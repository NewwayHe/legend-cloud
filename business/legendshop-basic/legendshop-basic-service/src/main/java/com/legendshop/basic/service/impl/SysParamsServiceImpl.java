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
import cn.hutool.core.util.RandomUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SysParamItemDao;
import com.legendshop.basic.dao.SysParamsDao;
import com.legendshop.basic.dto.*;
import com.legendshop.basic.entity.SysParams;
import com.legendshop.basic.enums.SysParamDataType;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.enums.SysParamsPushKeyEnum;
import com.legendshop.basic.query.SysParamPageQuery;
import com.legendshop.basic.query.SysParamQuery;
import com.legendshop.basic.service.SysParamCacheService;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.basic.service.WechatConfigNoticeService;
import com.legendshop.basic.service.convert.SysParamsConverter;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (SysParams)表服务实现类
 * <p>
 * 系统配置服务涉及大量缓存需要谨慎处理更新保存
 *
 * @author legendshop
 * @since 2020-08-28 12:00:46
 */
@Service
public class SysParamsServiceImpl extends BaseServiceImpl<SysParamsDTO, SysParamsDao, SysParamsConverter> implements SysParamsService {

	@Autowired
	private SysParamsDao sysParamsDao;

	@Autowired
	private SysParamItemDao sysParamItemDao;

	@Autowired
	private SysParamItemService sysParamItemService;

	@Autowired
	private SysParamsConverter sysParamsConverter;

	@Autowired
	private SysParamCacheService sysParamCacheService;

	@Autowired
	private WechatConfigNoticeService wechatConfigNoticeService;


	@Override
	public PageSupport<SysParamsDTO> queryPageList(SysParamQuery sysParamQuery) {
		PageSupport<SysParams> pageSupport = sysParamsDao.queryPageList(sysParamQuery);
		return sysParamsConverter.page(pageSupport);
	}

	@Override
	public SysParamsDTO getByName(String name) {
		SysParams params = sysParamsDao.getByName(name);
		if (null == params) {
			return null;
		}
		Boolean enabled = sysParamItemDao.getEnabledByParentId(params.getId());
		SysParamsDTO paramsDTO = sysParamsConverter.to(params);
		paramsDTO.setEnabled(enabled);
		return paramsDTO;
	}

	@Override
	public List<SysParamsDTO> getByNames(List<SysParamNameEnum> sysParamNameEnums) {
		List<SysParamsDTO> list = new ArrayList<>();
		sysParamNameEnums.forEach(sysParamNameEnum -> {
			list.add(getByName(sysParamNameEnum.name()));
		});
		return list;
	}

	@Override
	@Cacheable(value = CacheConstants.SYSTEM_PARAMS_DETAILS, key = "#name + '_sys'")
	public <T> T getConfigDtoByParamName(String name, Class<T> clazz) {
		SysParamsDTO dto = getByName(name);
		if (null == dto) {
			return null;
		}
		return sysParamItemService.getConfigDtoByParentId(dto.getId(), clazz);
	}

	@Override
	public <T> T getNotCacheConfigByName(String name, Class<T> clazz) {
		SysParamsDTO dto = getByName(name);
		if (null == dto) {
			return null;
		}
		return sysParamItemService.getConfigDtoByParentId(dto.getId(), clazz);
	}

	@Override
	public List<SysParamItemDTO> getSysParamItemsByParamName(String name) {
		SysParamsDTO dto = getByName(name);
		List<SysParamItemDTO> list = sysParamItemService.getListByParentId(dto.getId());
		List<SysParamItemDTO> checkBoxList = new ArrayList<>();
		//抽出checkBox
		list.forEach(sysParamItemDTO -> {
			if (SysParamDataType.CHECKBOX.value().equals(sysParamItemDTO.getDataType())) {
				checkBoxList.add(sysParamItemDTO);
			}
		});
		Map<String, List<SysParamItemDTO>> map = checkBoxList.stream().collect(Collectors.groupingBy(SysParamItemDTO::getRemark));
		//应前端要求组装checkbox
		for (Map.Entry<String, List<SysParamItemDTO>> entry : map.entrySet()) {
			SysParamItemDTO s = new SysParamItemDTO();
			s.setDataType(SysParamDataType.CHECKBOX.value());
			List<SysParamValueItemDTO> sysParamValueItemDTOS = new ArrayList<>();
			List<Long> checkBoxIds = new ArrayList<>();
			String key = entry.getKey();
			s.setDes(key);
			List<SysParamItemDTO> sysParamItemDTOS = entry.getValue();
			sysParamItemDTOS.forEach(d -> {
				SysParamValueItemDTO sysParamValueItemDTO = new SysParamValueItemDTO();
				sysParamValueItemDTO.setId(d.getId());
				s.setId(RandomUtil.randomLong());
				sysParamValueItemDTO.setValue(d.getDes());
				sysParamValueItemDTOS.add(sysParamValueItemDTO);
				if ("true".equals(d.getValue())) {
					checkBoxIds.add(d.getId());
				}
			});
			s.setSysParamValueItemDTOS(sysParamValueItemDTOS);
			s.setCheckBoxIds(checkBoxIds);
			list.add(0, s);
			list.removeAll(checkBoxList);
		}
		return list;
	}

	@Override
	public List<PayTypeDTO> getEnabledPayType() {

		List<PayTypeDTO> payTypeList = new ArrayList<>();

		//获取平台支付设置,是否为分账支付平台
		SysParamsDTO sysParamsDTO = getByName(SysParamNameEnum.PAY_SETTING.name());
		if (ObjectUtil.isNull(sysParamsDTO)) {
			return null;
		}

		List<SysParams> params;
		if (sysParamsDTO.getEnabled()) {
			// 获取支付分组的系统配置
			params = sysParamsDao.getByGroup(SysParamGroupEnum.SUB_PAY.name());
		} else {
			// 获取分账支付分组的系统配置
			params = sysParamsDao.getByGroup(SysParamGroupEnum.PAY.name());
		}

		if (ObjectUtil.isEmpty(params)) {
			return null;
		}
		List<Long> ids = params.stream().map(SysParams::getId).collect(Collectors.toList());
		List<SysParamItemDTO> enabledItems = sysParamItemDao.getEnabledByParentIds(ids);
		Map<Long, List<SysParamItemDTO>> map = enabledItems.stream().collect(Collectors.groupingBy(SysParamItemDTO::getParentId));

		for (SysParams sysParams : params) {
			// 只要已开启的支付方式
			if (null == map.get(sysParams.getId())) {
				continue;
			}
			SysParamItemDTO sysParamItemDTO = map.get(sysParams.getId()).get(0);
			if (Boolean.parseBoolean(sysParamItemDTO.getValue())) {
				PayTypeDTO payTypeDTO = new PayTypeDTO();
				payTypeDTO.setPayTypeId(sysParams.getName());
				payTypeDTO.setPayTypeName(sysParams.getDes());
				payTypeDTO.setMemo(sysParams.getRemark());
				payTypeList.add(payTypeDTO);
			}
		}
		return payTypeList;
	}

	@Override
	public List<PayTypeDTO> getUseEnabledPayType() {
		List<PayTypeDTO> enabledPayType = this.getEnabledPayType();
		enabledPayType.removeIf(e -> SysParamNameEnum.FREE_PAY.name().equals(e.getPayTypeId()));
		return enabledPayType;
	}

	@Override
	public List<SysParamsDTO> getByGroup(SysParamGroupEnum group) {
		return this.sysParamsConverter.to(sysParamsDao.getByGroup(group.name()));
	}

	@Override
	public PageSupport<SysParamsDTO> getByGroupPage(SysParamPageQuery query) {

		return this.sysParamsConverter.page(sysParamsDao.getByGroupPage(query));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long savePushSys(SysParamsDTO sysParamsDTO) {
		Long parentId = sysParamsDao.save(sysParamsConverter.from(sysParamsDTO));
		List<SysParamItemDTO> items = new ArrayList<>();
		for (SysParamsPushKeyEnum k : SysParamsPushKeyEnum.values()) {
			SysParamItemDTO item = new SysParamItemDTO();
			item.setParentId(parentId);
			item.setKeyWord(k.getKeyWord());
			item.setValue(k.getValue());
			item.setDes(k.getDesc());
			item.setDataType(k.getDateType());
			item.setUpdateTime(DateUtil.date());
			items.add(item);
		}
		sysParamItemDao.batchAddItems(items);
		return parentId;
	}


	@Override
	@Caching(evict = {
			@CacheEvict(value = CacheConstants.SYSTEM_PARAMS_DETAILS, allEntries = true),
			@CacheEvict(value = CacheConstants.SYSTEM_PARAMS_DETAILS_ITEM, allEntries = true)
	})
	public void cleanCache() {

	}

	@Override
	public void updateValueOnlyById(BatchUpdateSysParamDTO batchUpdateSysParamDTO) {
		this.sysParamItemDao.updateValueOnlyById(batchUpdateSysParamDTO.getSysParamValueItemDTOS());
		if (SysParamGroupEnum.WX.name().equals(batchUpdateSysParamDTO.getGroupBy())) {
			WxConfig wxConfig = this.getNotCacheConfigByName(batchUpdateSysParamDTO.getName(), WxConfig.class);
			this.wechatConfigNoticeService.weChatConfigPush(batchUpdateSysParamDTO.getName(), wxConfig);
		}
		this.sysParamCacheService.evictConfigDtoByParamName(batchUpdateSysParamDTO.getName());
	}
}
