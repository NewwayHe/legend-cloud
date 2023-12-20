package com.legendshop.common.data.cache;

import com.legendshop.common.data.cache.properties.RedisCacheProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedissonClient初始化
 *
 * @author legendshop
 */
@Configuration
@EnableConfigurationProperties({RedisCacheProperties.class})
public class RedissonConfig {


	private final RedisCacheProperties redisCacheProperties;

	public RedissonConfig(RedisCacheProperties redisCacheProperties) {
		this.redisCacheProperties = redisCacheProperties;
	}

	@Bean
	public RedissonClient redissonclient() {
		// 创建配置 指定redis地址及节点信息
		Config config = new Config();
		config.useSingleServer()
				.setAddress("redis://" + redisCacheProperties.getHost() + ":" + redisCacheProperties.getPort())
				.setPassword(redisCacheProperties.getPassword());
		return Redisson.create(config);
	}
}
