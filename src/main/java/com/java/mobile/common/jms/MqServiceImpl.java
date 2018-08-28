package com.java.mobile.common.jms;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code UpayService}作为service基类
 * <p>实现类必须实现doService(java.lang.String jsonStr)方法</p>
 * <p><strong>所有service的异常需要在service内部处理</strong></p>
 *
 */
public abstract class MqServiceImpl implements MqService {

	/**
	 * 进入service次数
	 */
	private AtomicInteger inCount;
	/**
	 * service处理完成次数
	 */
	private AtomicInteger outCount;
	public MqServiceImpl() {
		super();
		this.inCount = new AtomicInteger(0);
		this.outCount = new AtomicInteger(0);
	}

	public int getOutCount() {
		return outCount.get();
	}

	public void setOutCount(AtomicInteger outCount) {
		this.outCount = outCount;
	}
	
	public int getInCount() {
		return inCount.get();
	}
	
	public void setInCount(AtomicInteger inCount) {
		this.inCount = inCount;
	}

	@Override
	public abstract void doService(String jsonStr);

	@Override
	public void init() {
		inCount.incrementAndGet();
	}

	@Override
	public void stop() {
		outCount.incrementAndGet();
	}
	

}
