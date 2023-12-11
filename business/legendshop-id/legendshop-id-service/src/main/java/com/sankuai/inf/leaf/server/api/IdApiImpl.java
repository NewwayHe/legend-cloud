package com.sankuai.inf.leaf.server.api;

import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Results;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.id.api.IdApi;
import com.sankuai.inf.leaf.server.service.SegmentService;
import com.sankuai.inf.leaf.server.service.SnowflakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class IdApiImpl implements IdApi {

	private final SegmentService segmentService;
	private final SnowflakeService snowflakeService;

	@Override
	public R<Result> getSegmentId(String key) {
		return R.ok(segmentService.getId(key));
	}

	@Override
	public R<Result> getSnowflakeId(String key) {
		return R.ok(snowflakeService.getId(key));
	}

	@Override
	public R<Results> getSegmentIds(String key, int total) {
		return R.ok(segmentService.getSegmentIds(key, total));
	}

	@Override
	public R<Results> getSnowflakeIds(String key, int total) {
		return R.ok(snowflakeService.getSnowflakeIds(key, total));
	}
}
