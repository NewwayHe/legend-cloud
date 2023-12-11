/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * oss aws 公共配置信息
 *
 * @author legendshop
 */
@Data
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@ConfigurationProperties(prefix = "legendshop.oss")
public class OssProperties {

	/**
	 * 是否启用
	 */
	private Boolean enable;

	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;

	/**
	 * 自定义域名
	 */
	private String customDomain;

	/**
	 * true: monio nginx反向代理和S3默认支持,url访问为:{http://endpoint/bucketname}
	 * false: 云服务需要配置为false,url访问为:{http://bucketname.endpoint}
	 */
	private Boolean pathStyleAccess = true;

	/**
	 * 应用ID
	 */
	private String appId;

	/**
	 * 地区
	 */
	private String region;

	/**
	 * accessKey
	 */
	private String accessKey;

	/**
	 * secretKey
	 */
	private String secretKey;

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "bbc-public";

	/**
	 * 私有的储存桶名称
	 * 只允许拥有账号密码的用户下载此文件，或者在控制台分享
	 */
	private String privateBucketName = "bbc-private";


}
