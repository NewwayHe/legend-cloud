/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.legendshop.search.properties.SearchProperties;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author legendshop
 */
@AllArgsConstructor
@Configuration
public class ElasticSearchConfig {

	private final SearchProperties searchProperties;

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		System.out.println("Host = " + searchProperties.getHost());

//		//有账号密码：
//		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

//		credentialsProvider.setCredentials(
//				AuthScope.ANY, new UsernamePasswordCredentials(searchProperties.getUsername(), searchProperties.getPassword()));
//
//		RestClient restClient = RestClient
//				//这个HttpHost对象包含了Elasticsearch服务器的地址信息，它将被用来指定RestClient应该连接到哪个Elasticsearch节点。
//				.builder(HttpHost.create(searchProperties.getHost()))
//				//方法用于设置默认的凭据提供者
//				.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
//						.setDefaultCredentialsProvider(credentialsProvider)
//				)
//
//				.build();

		//无账号密码：
		RestClient restClient = RestClient.builder(HttpHost.create(searchProperties.getHost())).build();

		// 创建传输层，这里使用Jackson映射器
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// 创建并返回ElasticsearchClient实例
		return new ElasticsearchClient(transport);
	}


	/**
	 * restHighLevelClient 相当于spring中的id  RestHighLevelClient相当于class
	 */
//    @Bean
//    public RestHighLevelClient restHighLevelClient(ElasticsearchRestClientProperties properties) {
//        return createRestHighLevelClient(properties, 60 * 1000L * 10);
//    }

//    public RestHighLevelClient createRestHighLevelClient(ElasticsearchRestClientProperties properties, Long keepAlive) {
//        HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
//        RestClientBuilder clientBuilder = RestClient.builder(hosts)
//                .setHttpClientConfigCallback(requestConfig -> requestConfig.setKeepAliveStrategy(
//                        (response, context) -> keepAlive));
//        return new RestHighLevelClient(clientBuilder);
//    }

//	private HttpHost createHttpHost(String uri) {
//		try {
//			return createHttpHost(URI.create(uri));
//		} catch (IllegalArgumentException ex) {
//			return HttpHost.create(uri);
//		}
//	}
//
//	private HttpHost createHttpHost(URI uri) {
//		if (!StringUtils.hasLength(uri.getUserInfo())) {
//			return HttpHost.create(uri.toString());
//		}
//		try {
//			return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
//					uri.getQuery(), uri.getFragment()).toString());
//		} catch (URISyntaxException ex) {
//			throw new IllegalStateException(ex);
//		}
//	}

//	@Override
//	@Bean
//	public RestHighLevelClient elasticsearchClient() {
//		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//				.connectedTo(searchProperties.getHost())
//				.build();
//
//		return RestClients.create(clientConfiguration).rest();
//	}

}
