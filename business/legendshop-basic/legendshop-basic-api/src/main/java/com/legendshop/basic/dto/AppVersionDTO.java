/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.ToString;

/**
 * @author legendshop
 */
@ToString
public class AppVersionDTO {

	/**
	 * 安卓用户版APK文件路径
	 */
	private String androidUserApk;

	/**
	 * 安卓商家版APK文件路径
	 */
	private String androidShopApk;

	/**
	 * 安卓用户版最新版本号
	 */
	private String androidUserVersion;

	/**
	 * 安卓商家版最新版本号
	 */
	private String androidShopVersion;

	/**
	 * 安卓用户版更新日志
	 */
	private String androidUserLog;

	/**
	 * 安卓商家版更新日志
	 */
	private String androidShopLog;

	/**
	 * IOS用户版Appstore地址
	 */
	private String iosUserAppstore;

	/**
	 * IOS商家版Appstore地址
	 */
	private String iosShopAppstore;

	public String getAndroidUserApk() {
		return androidUserApk;
	}

	public void setAndroidUserApk(String androidUserApk) {
		this.androidUserApk = androidUserApk;
	}

	public String getAndroidShopApk() {
		return androidShopApk;
	}

	public void setAndroidShopApk(String androidShopApk) {
		this.androidShopApk = androidShopApk;
	}

	public String getAndroidUserVersion() {
		return androidUserVersion;
	}

	public void setAndroidUserVersion(String androidUserVersion) {
		this.androidUserVersion = androidUserVersion;
	}

	public String getAndroidShopVersion() {
		return androidShopVersion;
	}

	public void setAndroidShopVersion(String androidShopVersion) {
		this.androidShopVersion = androidShopVersion;
	}

	public String getAndroidUserLog() {
		return androidUserLog;
	}

	public void setAndroidUserLog(String androidUserLog) {
		this.androidUserLog = androidUserLog;
	}

	public String getAndroidShopLog() {
		return androidShopLog;
	}

	public void setAndroidShopLog(String androidShopLog) {
		this.androidShopLog = androidShopLog;
	}

	public String getIosUserAppstore() {
		return iosUserAppstore;
	}

	public void setIosUserAppstore(String iosUserAppstore) {
		this.iosUserAppstore = iosUserAppstore;
	}

	public String getIosShopAppstore() {
		return iosShopAppstore;
	}

	public void setIosShopAppstore(String iosShopAppstore) {
		this.iosShopAppstore = iosShopAppstore;
	}
}
