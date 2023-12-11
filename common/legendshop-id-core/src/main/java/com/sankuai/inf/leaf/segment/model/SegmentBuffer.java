/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.segment.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双buffer
 *
 * @author legendshop
 */
public class SegmentBuffer {
	/**
	 * 键值
	 */
	private String key;
	/**
	 * 双缓冲区
	 */
	private Segment[] segments;
	/**
	 * 当前使用的段（segment）的索引
	 */
	private volatile int currentPos;
	/**
	 * 下一个缓冲区是否处于可切换状态
	 */
	private volatile boolean nextReady;
	/**
	 * 是否初始化完成
	 */
	private volatile boolean initOk;
	/**
	 * 线程是否正在运行中
	 */
	private final AtomicBoolean threadRunning;
	/**
	 * 读写锁
	 */
	private final ReadWriteLock lock;
	/**
	 * 步长
	 */
	private volatile int step;
	/**
	 * 最小步长
	 */
	private volatile int minStep;
	/**
	 * 更新时间戳
	 */
	private volatile long updateTimestamp;

	public SegmentBuffer() {
		segments = new Segment[]{new Segment(this), new Segment(this)};
		currentPos = 0;
		nextReady = false;
		initOk = false;
		threadRunning = new AtomicBoolean(false);
		lock = new ReentrantReadWriteLock();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Segment[] getSegments() {
		return segments;
	}

	public Segment getCurrent() {
		return segments[currentPos];
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public int nextPos() {
		return (currentPos + 1) % 2;
	}

	public void switchPos() {
		currentPos = nextPos();
	}

	public boolean isInitOk() {
		return initOk;
	}

	public void setInitOk(boolean initOk) {
		this.initOk = initOk;
	}

	public boolean isNextReady() {
		return nextReady;
	}

	public void setNextReady(boolean nextReady) {
		this.nextReady = nextReady;
	}

	public AtomicBoolean getThreadRunning() {
		return threadRunning;
	}

	public Lock rLock() {
		return lock.readLock();
	}

	public Lock wLock() {
		return lock.writeLock();
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getMinStep() {
		return minStep;
	}

	public void setMinStep(int minStep) {
		this.minStep = minStep;
	}

	public long getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(long updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SegmentBuffer{");
		sb.append("key='").append(key).append('\'');
		sb.append(", segments=").append(Arrays.toString(segments));
		sb.append(", currentPos=").append(currentPos);
		sb.append(", nextReady=").append(nextReady);
		sb.append(", initOk=").append(initOk);
		sb.append(", threadRunning=").append(threadRunning);
		sb.append(", step=").append(step);
		sb.append(", minStep=").append(minStep);
		sb.append(", updateTimestamp=").append(updateTimestamp);
		sb.append('}');
		return sb.toString();
	}
}
