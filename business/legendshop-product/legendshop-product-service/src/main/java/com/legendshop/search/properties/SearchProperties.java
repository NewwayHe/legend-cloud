/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.legendshop.search.properties.SearchProperties.PREFIX;

/**
 * 搜索配置
 *
 * @author legendshop
 * @create: 2021-09-09 13:40
 */
@Data
@Configuration
@ConfigurationProperties(prefix = PREFIX)
public class SearchProperties {

	public static final String PREFIX = "legendshop.search";

	/**
	 * ES服务器地址
	 */
	private String host;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * ES密码
	 */
	private String password;


	/**
	 * 商品索引名称
	 */
	private String productIndexName = "dev70_sr1_new_product";


	/**
	 * 店铺索引名称
	 */
	private String shopIndexName = "dev70_sr1_new_shop";

	/**
	 * 营销索引名称
	 */
	private String activityIndexName = "dev70_sr1_new_activity";

	/**
	 * 优惠券引名称
	 */
	private String couponIndexName = "dev70_sr1_new_coupon";

	@Bean
	public String productIndexName() {
		return this.productIndexName;
	}

	@Bean
	public String shopIndexName() {
		return this.shopIndexName;
	}

	@Bean
	public String activityIndexName() {
		return this.activityIndexName;
	}

	@Bean
	public String couponIndexName() {
		return this.couponIndexName;
	}

}
