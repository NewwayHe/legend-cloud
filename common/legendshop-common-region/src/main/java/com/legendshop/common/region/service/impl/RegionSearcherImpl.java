/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.service.impl;

import com.legendshop.common.core.util.ExceptionUtil;
import com.legendshop.common.region.core.DbConfig;
import com.legendshop.common.region.core.DbSearcher;
import com.legendshop.common.region.core.IpInfoDTO;
import com.legendshop.common.region.properties.RegionProperties;
import com.legendshop.common.region.service.RegionSearcherService;
import com.legendshop.common.region.util.IpInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 地区查找实现类
 *
 * @author legendshop
 */
@RequiredArgsConstructor
public class RegionSearcherImpl implements InitializingBean, RegionSearcherService {
	private final ResourceLoader resourceLoader;
	private final RegionProperties regionProperties;
	private DbSearcher searcher;

	@Override
	public void afterPropertiesSet() throws Exception {
		DbConfig config = new DbConfig();
		Resource resource = resourceLoader.getResource(regionProperties.getDbPath());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = new DbSearcher(config, new ByteArrayDBReader(StreamUtils.copyToByteArray(inputStream)));
		}
	}

	@Override
	public IpInfoDTO memorySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO memorySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO getByIndexPtr(long ptr) {
		try {
			return IpInfoUtil.toIpInfo(searcher.getByIndexPtr(ptr));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO btreeSearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO btreeSearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO binarySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	@Override
	public IpInfoDTO binarySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}
}
