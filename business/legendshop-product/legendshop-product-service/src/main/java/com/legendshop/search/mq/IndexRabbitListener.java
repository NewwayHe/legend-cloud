/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.mq;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.search.constants.AmqpConst;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.service.SearchCouponIndexService;
import com.legendshop.search.service.SearchProductIndexService;
import com.legendshop.search.service.SearchProductService;
import com.legendshop.search.service.SearchShopIndexService;
import com.legendshop.search.service.strategy.IndexServiceContext;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IndexRabbitListener {

	final IndexServiceContext indexServiceContext;

	final SearchProductIndexService searchProductIndexService;

	final SearchProductService searchProductService;

	final SearchShopIndexService searchShopIndexService;

	final SearchCouponIndexService searchCouponIndexService;

	private final StringRedisTemplate redisTemplate;


	/**
	 * TODO 应合并到统一重建索引方法 reBuildIndex
	 * 创建索引
	 *
	 * @param productIds the 创建索引的商品ID
	 * @throws IOException channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.INDEX_CREATE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.INDEX_EXCHANGE), key = {AmqpConst.INDEX_CREATE_ROUTING_KEY}
	))
	public void consumeCreateIndex(List<Long> productIds, Message message, Channel channel) throws IOException {
		if (CollUtil.isEmpty(productIds)) {
			log.error("接收到的消息为空");
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			return;
		}
		log.info("接收消息体：" + productIds.toString());
		try {
			searchProductIndexService.initByProductIdListIndex(productIds);
		} catch (Exception e) {
			// 消息推送要求重推
			log.error("consumeCreateIndex error", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}


	/**
	 * TODO 应合并到统一重建索引方法 reBuildIndex
	 * 删除索引监听
	 *
	 * @param productIds the 删除索引的商品ID
	 * @throws IOException channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.INDEX_DELETE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.INDEX_EXCHANGE), key = {AmqpConst.INDEX_DELETE_ROUTING_KEY}
	))
	public void consumeDeleteIndex(List<Long> productIds, Message message, Channel channel) throws IOException {
		if (CollUtil.isEmpty(productIds)) {
			log.error("接收到的消息为空");
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			return;
		}
		log.info("接收消息体：" + productIds.toString());
		try {
			productIds.forEach(searchProductIndexService::delByProductIdIndex);
		} catch (Exception e) {
			// 拒绝接收消息
			e.printStackTrace();
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}


	/**
	 * TODO 应合并到统一重建索引方法 reBuildIndex
	 * 删除整个类目下的索引的监听
	 *
	 * @param categoryId the 类目ID
	 * @throws IOException channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.INDEX_DELETE_BY_CATEGORY_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.INDEX_EXCHANGE), key = {AmqpConst.INDEX_DELETE_BY_CATEGORY_ROUTING_KEY}
	))
	public void consumeDeleteCategoryIndex(Long categoryId, Message message, Channel channel) throws IOException {
		if (categoryId == null) {
			log.error("接收到的消息为空");
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			return;
		}
		log.info("接收消息体：" + categoryId);
		try {
			List<ProductDocumentDTO> result = searchProductService.searchProductByCategoryId(categoryId, 10000);
			if (CollectionUtils.isEmpty(result)) {
				log.info("没有需要删除索引的商品：" + categoryId);
				return;
			}
			result.forEach(e -> searchProductIndexService.delByProductIdIndex(e.getProductId()));

		} catch (Exception e) {
			// 拒绝接收消息
			log.error("", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	/**
	 * TODO 应合并到统一重建索引方法 reBuildIndex
	 * 创建索引
	 *
	 * @param shopIds 重建店铺索引
	 * @throws IOException channel
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.INDEX_CREATE_BY_SHOP_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.INDEX_EXCHANGE), key = {AmqpConst.INDEX_CREATE_BY_SHOP_ROUTING_KEY}
	))
	public void consumeCreateShopIndex(List<Long> shopIds, Message message, Channel channel) throws IOException {

		log.info("接收重建店铺索引");
		try {
			searchShopIndexService.initAllShopIndex();
		} catch (Exception e) {
			// 消息推送要求重推
			log.error("consumeCreateIndex error", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}


//   统一重建索引方法------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 重建索引
	 */
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.REBUILD_ES_INDEX_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.INDEX_EXCHANGE), key = {AmqpConst.REBUILD_ES_INDEX_ROUTING_KEY}
	))
	public void rebuildIndex(Long reIndexId, Message message, Channel channel) throws Exception {
		log.info("rebuildIndex 接收到重建索引信息");
		try {
			indexServiceContext.exec(reIndexId);
		} catch (Exception e) {
			// 拒绝接收消息
			log.error("", e);
		} finally {
			redisTemplate.delete(CacheConstants.INDEX_INIT);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
		log.info("rebuildIndex 重建索引信息完成");
	}

}
