/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.enums.ActivityEsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

	/**
	 * 标签ID
	 */
	private Long id;

	/**
	 * 营销活动ID
	 */
	private Long activityId;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * {@link ActivityEsTypeEnum}
	 */
	private Integer type;


	/**
	 * 处理标签
	 *
	 * @param tagList 标签集合
	 */
	public static List<Tag> disposeTagList(List<Tag> tagList) {
		if (ObjectUtil.isEmpty(tagList)) {
			return Collections.emptyList();
		}

		ArrayList<Tag> tags = new ArrayList<>();
		Map<Integer, List<Tag>> collect = tagList.stream().collect(Collectors.groupingBy(Tag::getType));

		List<Tag> couponTags = collect.get(ActivityEsTypeEnum.COUPON.getValue());
		if (CollUtil.isNotEmpty(couponTags)) {
			tags.add(couponTags.get(0));
		}
		return tags;
	}


}
