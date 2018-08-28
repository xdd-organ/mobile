package com.java.mobile.common.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

/**
 * jms异常处理类
 *
 */
public class MqStateErrorHandler implements ErrorHandler {

	private static final Logger logger = LoggerFactory.getLogger(MqStateErrorHandler.class);
	@Override
	public void handleError(Throwable t) {

		if(t instanceof IllegalStateException){
			logger.error("Activemq State has already changed:"+t.getMessage());
		}
		else{
			try {
				throw t;
			} catch (Throwable e) {
				logger.error("error occured while throwing excption unnecessary to proccess:"+t.getMessage());
			}
		}
	}

}
