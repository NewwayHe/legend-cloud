/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author legendshop
 */
@Slf4j
public class IdWorker {

	// 下面两个每个5位，加起来就是10位的工作机器id

	/**
	 * 工作ID
	 */
	private long workerId;

	/**
	 * 数据中心ID
	 */
	private long datacenterId;

	/**
	 * 12位序列号
	 */
	private long sequence;


	public IdWorker(long workerId, long datacenterId, long sequence) {
		// sanity check for workerId
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		log.debug("worker starting. timestamp left shift {}, datacenter id bits {}, worker id bits {}, sequence bits {}, workerId {}",
				timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

		this.workerId = workerId;
		this.datacenterId = datacenterId;
		this.sequence = sequence;
	}

	public IdWorker() {
		this.datacenterId = getDatacenterId(datacenterIdBits);
		this.workerId = getMaxWorkerId(datacenterId, workerIdBits);
		this.sequence = RandomUtil.randomInt(11);
	}

	/**
	 * 初始时间戳
	 */
	private long initTimestamp = 1603354536000L;
	/**
	 * 长度为5位
	 */
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	/**
	 * 最大值
	 */
	private final long maxWorkerId = ~(-1L << workerIdBits);
	private final long maxDatacenterId = ~(-1L << datacenterIdBits);
	/**
	 * 序列号id长度
	 */
	private final long sequenceBits = 12L;
	/**
	 * 序列号最大值
	 */
	private final long sequenceMask = ~(-1L << sequenceBits);
	/**
	 * 工作id需要左移的位数，12位
	 */
	private final long workerIdShift = sequenceBits;
	/**
	 * 数据id需要左移位数 12+5=17位
	 */
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	/**
	 * 时间戳需要左移位数 12+5+5=22位
	 */
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	/**
	 * 上次时间戳，初始值为负数
	 */
	private long lastTimestamp = -1L;

	public long getWorkerId() {
		return workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	/**
	 * 下一个ID生成算法
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		// 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
		if (timestamp < lastTimestamp) {
			System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
					lastTimestamp - timestamp));
		}

		// 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		// 将上次时间戳值刷新
		lastTimestamp = timestamp;

		// 返回结果：
		// (timestamp - initTimestamp) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
		// (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
		// (workerId << workerIdShift) 表示将工作id左移相应位数
		// | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
		// 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
		return ((timestamp - initTimestamp) << timestampLeftShift) |
				(datacenterId << datacenterIdShift) |
				(workerId << workerIdShift) |
				sequence;
	}

	/**
	 * 获取时间戳，并与上次时间戳比较
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 获取系统时间戳
	 */
	private long timeGen() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得WorkerId
	 *
	 * @param datacenterId datacenterId
	 * @param maxWorkerId  maxWorkerId
	 */
	protected long getMaxWorkerId(long datacenterId, long maxWorkerId) {
		StringBuilder pid = new StringBuilder();
		pid.append(datacenterId);
		String name = ManagementFactory.getRuntimeMXBean().getName();
		if (StringUtils.isNotEmpty(name)) {
			//GET jvmPid
			pid.append(name.split("@")[0]);
		}
		//MAC + PID 的 hashcode 获取16个低位
		return (pid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
	}

	/**
	 * 获得DatacenterId
	 *
	 * @param maxDatacenterId maxDatacenterId
	 * @return long
	 */
	protected long getDatacenterId(long maxDatacenterId) {
		long id = 0L;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			if (network == null) {
				id = 1L;
			} else {
				byte[] mac = network.getHardwareAddress();
				if (null != mac) {
					id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
					id = id % (maxDatacenterId + 1);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return id;
	}


}
