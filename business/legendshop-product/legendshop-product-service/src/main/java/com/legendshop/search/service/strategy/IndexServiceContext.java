/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.search.constants.AmqpConst;
import com.legendshop.search.dto.SearchRebuildIndexLogDTO;
import com.legendshop.search.enmus.IndexStatusEnum;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.service.IndexService;
import com.legendshop.search.service.SearchRebuildIndexLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class IndexServiceContext {

	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final List<IndexService> indexServiceList;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final SearchRebuildIndexLogService searchRebuildIndexLogService;

	public Boolean createReIndexLog(String indexType, Integer targetMethod, Integer targetType, String targetId) {
		SearchRebuildIndexLogDTO searchRebuildIndexLogDTO = new SearchRebuildIndexLogDTO();
		searchRebuildIndexLogDTO.setIndexType(indexType);
		searchRebuildIndexLogDTO.setTargetMethod(targetMethod);
		if (ObjectUtil.isNotEmpty(targetType)) {
			searchRebuildIndexLogDTO.setTargetType(targetType);
		}
		if (StringUtil.isNotBlank(targetId)) {
			searchRebuildIndexLogDTO.setTargetId(targetId);
		}
		searchRebuildIndexLogDTO.setStatus(IndexStatusEnum.WAIT.getValue());
		searchRebuildIndexLogDTO.setCreateTime(new Date());
		return createReIndexLog(searchRebuildIndexLogDTO);
	}

	public Boolean createReIndexLog(SearchRebuildIndexLogDTO searchRebuildIndexLogDTO) {
		//创建重建索引日志
		R<Long> result = searchRebuildIndexLogService.save(searchRebuildIndexLogDTO);
		if (result.success()) {
			//发起MQ消息队列
			amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.REBUILD_ES_INDEX_ROUTING_KEY, result.getData());
			return true;
		}
		return false;
	}


	/**
	 * 根据重建索引的ID，找到重建索引的要求，调用对应的IndexService子类来更新索引
	 *
	 * @param reIndexId
	 * @return
	 */
	public Boolean exec(Long reIndexId) throws IOException {
		//读取更新日志
		SearchRebuildIndexLogDTO rebuildIndexLogDTO = searchRebuildIndexLogService.getById(reIndexId);

		//执行
		Optional<IndexService> handlerOptional = indexServiceList.stream().filter(u -> u.isSupport(rebuildIndexLogDTO.getIndexType())).findFirst();
		if (!handlerOptional.isPresent()) {
			throw new BusinessException("没有可用的重建索引方式！");
		}
		IndexService indexService = handlerOptional.get();

		//targetId格式为逗号分隔符
		List<Long> idListResult = null;
		if (ObjectUtil.isNotNull(rebuildIndexLogDTO.getTargetId())) {
			List<String> idList = Arrays.asList(rebuildIndexLogDTO.getTargetId().split(","));
			idListResult = idList.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		}

		IndexTargetMethodEnum targetMethod = IndexTargetMethodEnum.fromCode(rebuildIndexLogDTO.getTargetMethod());
		if (null == targetMethod) {
			throw new BusinessException("没有可用的重建索引方法！");
		}

		if (indexService.getSupportClass() != null) {
			IndexOperations indexOperations = elasticsearchTemplate.indexOps(indexService.getSupportClass());
			if (!indexOperations.exists()) {
				log.info("正在重建索引，发现当前索引不存在，{}", indexService.getSupportClass());
				boolean createFlag = indexOperations.create();
				log.info("索引创建完成，{}", createFlag);
				boolean putMappingFlag = indexOperations.putMapping(indexOperations.createMapping());
				log.info("索引映射完成，{}", putMappingFlag);
			}
		}

		String remark = null;
		switch (targetMethod) {
			case CREATE:
				remark = indexService.initIndex(rebuildIndexLogDTO.getTargetType(), idListResult);
				break;
			case UPDATE:
				remark = indexService.updateIndex(rebuildIndexLogDTO.getTargetType(), idListResult);
				break;
			case DELETE:
				remark = indexService.delIndex(rebuildIndexLogDTO.getTargetType(), idListResult);
				break;
		}

		//更新日志状态为 已执行完成 20
		rebuildIndexLogDTO.setStatus(20);
		rebuildIndexLogDTO.setRemark(remark);

		return searchRebuildIndexLogService.updateStatus(rebuildIndexLogDTO).getSuccess();
	}
}
