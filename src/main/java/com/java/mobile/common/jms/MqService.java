package com.java.mobile.common.jms;

public interface MqService {
	void doService(String jsonStr);
	void init();
	void stop();
}
