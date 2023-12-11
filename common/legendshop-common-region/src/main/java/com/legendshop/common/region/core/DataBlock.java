/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

/**
 * 数据块
 *
 * @author legendshop
 */
public class DataBlock {
	/**
	 * city id
	 */
	private int city_id;

	/**
	 * region address
	 */
	private String region;

	/**
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * construct method
	 *
	 * @param city_id
	 * @param region  region string
	 * @param dataPtr data ptr
	 */
	public DataBlock(int city_id, String region, int dataPtr) {
		this.city_id = city_id;
		this.region = region;
		this.dataPtr = dataPtr;
	}

	public DataBlock(int city_id, String region) {
		this(city_id, region, 0);
	}

	public int getCityId() {
		return city_id;
	}

	public DataBlock setCityId(int city_id) {
		this.city_id = city_id;
		return this;
	}

	public String getRegion() {
		return region;
	}

	public DataBlock setRegion(String region) {
		this.region = region;
		return this;
	}

	public int getDataPtr() {
		return dataPtr;
	}

	public DataBlock setDataPtr(int dataPtr) {
		this.dataPtr = dataPtr;
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(city_id) + '|' + region + '|' + dataPtr;
	}
}
