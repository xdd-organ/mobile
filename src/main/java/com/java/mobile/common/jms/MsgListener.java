package com.java.mobile.common.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MsgListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(MsgListener.class);

	private MqService iService;

	@Override
	public void onMessage(final Message msg) {
		if (!(msg instanceof TextMessage)) {
			logger.warn("接收到的消息不是TextMessage类型的！");
			return;
		}

		TextMessage textMsg = (TextMessage) msg;
		String text;
		try {
			text = textMsg.getText();
		} catch (JMSException e) {
			logger.error("从消息中获取Json报文出错！" + e.getMessage());
			return;
		}
		logger.debug("接收到Json报文 -> " + text.trim());
		iService.init();
		iService.doService(text);
		iService.stop();

	}

	public MqService getiService() {
		return iService;
	}

	public void setiService(MqService iService) {
		this.iService = iService;
	}

}
