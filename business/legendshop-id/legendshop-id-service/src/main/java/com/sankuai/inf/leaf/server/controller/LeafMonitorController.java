/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.server.controller;

import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import com.sankuai.inf.leaf.segment.model.LeafAlloc;
import com.sankuai.inf.leaf.segment.model.SegmentBuffer;
import com.sankuai.inf.leaf.server.config.AuthAdminData;
import com.sankuai.inf.leaf.server.model.SegmentBufferView;
import com.sankuai.inf.leaf.server.service.SegmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Controller
public class LeafMonitorController {
	private Logger logger = LoggerFactory.getLogger(LeafMonitorController.class);

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private AuthAdminData authAdminData;

	private static String userLogined = "userLogined";

	@SneakyThrows
	@RequestMapping(value = "login")
	@ResponseBody
	public String login(HttpServletRequest request) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		if (authAdminData.getPassword().equals(password) && authAdminData.getUsername().equals(name)) {
			request.getSession().setAttribute(userLogined, true);
			return "Login success";
		}

		return "Login failed";
	}


	@SneakyThrows
	@RequestMapping(value = "cache")
	public String getCache(HttpServletRequest request) {

		if (request.getSession().getAttribute(userLogined) == null) {
			throw new IllegalArgumentException("No logined");
		}

		Map<String, SegmentBufferView> data = new HashMap<>(16);
		SegmentIDGenImpl segmentIdGen = segmentService.getIdGen();
		if (segmentIdGen == null) {
			throw new IllegalArgumentException("You should config leaf.segment.enable=true first");
		}
		Map<String, SegmentBuffer> cache = segmentIdGen.getCache();
		for (Map.Entry<String, SegmentBuffer> entry : cache.entrySet()) {
			SegmentBufferView sv = new SegmentBufferView();
			SegmentBuffer buffer = entry.getValue();
			sv.setInitOk(buffer.isInitOk());
			sv.setKey(buffer.getKey());
			sv.setPos(buffer.getCurrentPos());
			sv.setNextReady(buffer.isNextReady());
			sv.setMax0(buffer.getSegments()[0].getMax());
			sv.setValue0(buffer.getSegments()[0].getValue().get());
			sv.setStep0(buffer.getSegments()[0].getStep());

			sv.setMax1(buffer.getSegments()[1].getMax());
			sv.setValue1(buffer.getSegments()[1].getValue().get());
			sv.setStep1(buffer.getSegments()[1].getStep());

			data.put(entry.getKey(), sv);

		}
		logger.info("Current Host {}", InetAddress.getLocalHost().getHostAddress());
		request.setAttribute("data", data);
		return "segment";
	}

	@SneakyThrows
	@RequestMapping(value = "db")
	public String getDb(HttpServletRequest request) {
		SegmentIDGenImpl segmentIdGen = segmentService.getIdGen();
		if (segmentIdGen == null) {
			throw new IllegalArgumentException("You should config leaf.segment.enable=true first");
		}

		if (request.getSession().getAttribute(userLogined) == null) {
			throw new IllegalArgumentException("No logined");
		}

		List<LeafAlloc> items = segmentIdGen.getAllLeafAllocs();
		request.setAttribute("items", items);
		logger.info("Current Host {}", InetAddress.getLocalHost().getHostAddress());
		return "db";
	}

	/**
	 * the output is like this:
	 * {
	 * "timestamp": "1567733700834(2019-09-06 09:35:00.834)",
	 * "sequenceId": "3448",
	 * "workerId": "39"
	 * }
	 */
	@RequestMapping(value = "decodeSnowflakeId")
	@ResponseBody
	public Map<String, String> decodeSnowflakeId(HttpServletRequest request, @RequestParam("snowflakeId") String snowflakeIdStr) {
		Map<String, String> map = new HashMap<>(16);

		if (request.getSession().getAttribute(userLogined) == null) {
			throw new IllegalArgumentException("No logined");
		}

		try {
			long snowflakeId = Long.parseLong(snowflakeIdStr);

			long originTimestamp = (snowflakeId >> 22) + 1288834974657L;
			Date date = new Date(originTimestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			map.put("timestamp", String.valueOf(originTimestamp) + "(" + sdf.format(date) + ")");

			long workerId = (snowflakeId >> 12) ^ (snowflakeId >> 22 << 10);
			map.put("workerId", String.valueOf(workerId));

			long sequence = snowflakeId ^ (snowflakeId >> 12 << 12);
			map.put("sequenceId", String.valueOf(sequence));
		} catch (NumberFormatException e) {
			map.put("errorMsg", "snowflake Id反解析发生异常!");
		}
		return map;
	}
}
