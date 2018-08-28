package com.java.mobile.common.jms;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@code AdvancedGroupQueueSender}主要利用了组队列模式，实现了对多个MQ Broker轮循发送消息。
 * <p>通过增加MQ Broker的数量，拓宽消息传输的通道。 单个MQ的接收消息能力大约为1万TPS,部署4个MQ时，可以达到4万。 可以实现MQ消息通道的横向扩展。<p>
 * <p>主要的发送方法如下：</p>
 * <p>发送延时消息:{@link #sendCache(String, String, long)}</p>
 * <p>发送普通消息:{@link #sendMsg(String, String)}</p>
 * <p>发送带有属性消息:{@link #sendMsg(String, String, String)}</p>
 * <p><strong>设计思路</strong></p>
 * <p>多个Activemq Broker实例同时工作 </p>
 * <p>1.消息前置同时连接两个（或两个以上）MQ， 采用轮循机制发送消息到 MQ 1 或 MQ 2。
 * 2.核心同时配置多个MQ Broker的消费者，可以消费MQ 1 和MQ2 上的消息。
 * </p>
 * <p>
 * 主要解决问题：
 * 1、解决MQ单点问题。两个或两个以上MQ可以同时工作。不存在MQ挂掉的风险。
 * 2、MQ做为消息的传输管道， 增加MQ数量就可以拓宽管道的宽度。
 * 3、去掉分组的概念， 所有消息前置以及核心的mq配置是相同的。
 * 4、通过增加或减少 消息前置、MQ、核心应用的实例数量 系统能力易于扩展。
 * </p>
 */
//@Component("chargeSender")
public class AdvancedGroupQueueSender /*implements SmartLifecycle*/ {

    private static final Logger logger = LoggerFactory.getLogger(AdvancedGroupQueueSender.class);

    /**
     * 多个broker的Url列表
     */
    private List<String> brokerURLs = null;

    /**
     * 每个broker的最大连接数
     */
    private int maxConnection = 1;

    /**
     * 某个broker连接不上或出现异常时，该broker的token被屏蔽的时间，单位毫秒，默认为30秒
     */
    private long freeTime = 30000;

    /**
     * 组队列名字
     */
    private String groupQueueName = "TestGroup";
    /**
     * 默认为20个
     */
    private int sessionCacheSize = 20;

    /**
     * 每个broker的token数量 = maxConnection * tokenFactor;
     */
    private int tokenFactor = sessionCacheSize;

    private volatile boolean isRunning = false;


    public List<String> getBrokerURLs() {
        return brokerURLs;
    }

    public void setBrokerURLs(List<String> brokerURLs) {
        this.brokerURLs = brokerURLs;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public long getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(long freeTime) {
        this.freeTime = freeTime;
    }

    public String getGroupQueueName() {
        return groupQueueName;
    }

    public void setGroupQueueName(String groupQueueName) {
        this.groupQueueName = groupQueueName;
    }

    public int getTokenFactor() {
        return tokenFactor;
    }

    public void setTokenFactor(int tokenFactor) {
        this.tokenFactor = tokenFactor;
    }

    private String jointMark = "-";

    private ActiveMQQueue defaultQueue = null;

    private Map<String, JmsTemplate> jmsTemplateMap = new HashMap<String, JmsTemplate>();

    private Map<String, CachingConnectionFactory> cachingConnectionFactoryMap = new HashMap<String, CachingConnectionFactory>();

    public Map<String, Set<Token>> TokenMap = new HashMap<String, Set<Token>>();

    public BlockingQueue<Token> blockingQueue = new LinkedBlockingQueue<Token>();

    public Timer timer = new Timer();

    class RevertTask extends TimerTask {
        Token freeToken;

        RevertTask(Token t) {
            this.freeToken = t;
        }

        @Override
        public void run() {
            try {
                freeToken.jmstemplate.getConnectionFactory().createConnection();
                // Test connection
                freeToken.inQueue();
                blockingQueue.offer(freeToken);
            } catch (JMSException e) {
                // to prevent the unnormal url inQueue
                String url = freeToken.brokerUrl;
                logger.warn("Try to connect to the broker failed! broker url is {}, retry after {} millseconds.", url, freeTime);
                // retry a bit later
                timer.schedule(new RevertTask(freeToken), freeTime);
            }
        }

    }

    class Token {
        int id;
        String brokerUrl;
        JmsTemplate jmstemplate;
        boolean inQueue = false;
        boolean isFree = true;

        long count = 0;
        long fcount = 0;

        Token(int id, String url, JmsTemplate template) {
            this.id = id;
            this.brokerUrl = url;
            this.jmstemplate = template;
        }

        void outQueue() {
            this.inQueue = false;
            this.count++;
        }

        void inQueue() {
            this.inQueue = true;
            this.isFree = false;
        }

        void free() {
            this.isFree = true;
            this.fcount++;
        }
    }

    /**
     * @param message
     * @param subQueueName
     * @throws JmsException
     * @Title: sendMsg
     * @Description: TODO    发送点对点mq异步消息
     * @return: void
     */
    public void sendMsg(final String message, String subQueueName) throws JmsException {
        this.sendMsg(message, subQueueName, null);
    }


    /**
     * @param message
     * @param subQueueName
     * @param delayTime    延迟时间
     * @Title: sendCache
     * @Description: TODO 使用缓存队列需要修改activemq.xml才能生效，在<broker>里添加属性schedulerSupport="true"
     * @return: void
     */

    public void sendCache(final String message, String subQueueName, final long delayTime) {

        logger.debug("send Cachemessage:{}", message);

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage txtmsg = session.createTextMessage();
                txtmsg.setText(message);
                txtmsg.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayTime);
                return txtmsg;
            }
        };

        this.send(subQueueName, messageCreator);

    }


    /**
     * @param message
     * @param subQueueName
     * @param systemId
     * @throws JmsException
     * @Title: sendMsg
     * @Description: TODO    发送mq同步消息
     * @return: void
     */

    public void sendMsg(final String message, String subQueueName, String systemId) throws JmsException {
        logger.debug("send message:{}", message);
        final Map<String, String> stringProperty = new HashMap<String, String>();
        if (systemId != null && systemId.length() > 0) {
            stringProperty.put("systemcode", systemId);
        }

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage txtmsg = session.createTextMessage();
                txtmsg.setText(message);
                if (stringProperty != null && stringProperty.size() > 0) {
                    for (String key : stringProperty.keySet()) {
                        txtmsg.setStringProperty(key, stringProperty.get(key));
                    }
                }
                return txtmsg;
            }
        };

        this.send(subQueueName, messageCreator);
    }

    public void send(String subName, MessageCreator messageCreator) throws JmsException {

        if (subName == null || subName.trim().length() <= 0) {
            throw new UncategorizedJmsException("queue's subname is null or blank");
        } else {
            subName = subName.trim();
        }

        Token token = null;
        boolean success = false;

        for (int k = 0; k < maxConnection * tokenFactor * brokerURLs.size(); k++) {
            try {
                token = blockingQueue.take();
                token.outQueue();

                token.jmstemplate.send(groupQueueName + jointMark + subName, messageCreator);
                success = true;

                token.inQueue();
                blockingQueue.offer(token);
                break;

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new UncategorizedJmsException("blockingQueue.take() interrupted, " + e.getMessage());
            } catch (JmsException e) {
                token.free();
                logger.warn("failed to send message to broker {}", token.brokerUrl);
                timer.schedule(new RevertTask(token), freeTime);
            }
        }

        if (success == false) {
            throw new UncategorizedJmsException("failed send message to  anyone of brokers, maybe brokers are shutdown");
        }
    }

    public boolean isRunning() {
        // TODO Auto-generated method stub
        return isRunning;
    }

    /**
     * @Title: start
     * @Description: TODO 初始化
     * @see org.springframework.context.Lifecycle#start()
     */
    @PostConstruct
    public void start() {
        assert (brokerURLs != null && brokerURLs.size() > 0);
        assert (groupQueueName != null && groupQueueName.trim().length() > 0);
        groupQueueName = groupQueueName.trim();
        assert (maxConnection > 0 && tokenFactor > 0);

        brokerURLs = Arrays.asList(brokerURLs.get(0).split(","));

        defaultQueue = new ActiveMQQueue(groupQueueName + jointMark + "default");

        for (String url : brokerURLs) {
            CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(new ActiveMQConnectionFactory(url));
            cachingConnectionFactory.setSessionCacheSize(getSessionCacheSize());
            JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
            jmsTemplate.setDefaultDestination(defaultQueue);

            cachingConnectionFactoryMap.put(url, cachingConnectionFactory);
            jmsTemplateMap.put(url, jmsTemplate);

            Set<Token> set = new HashSet<Token>();
            for (int i = 0; i < maxConnection * tokenFactor; i++) {
                Token token = new Token(i, url, jmsTemplate);
                set.add(token);
            }
            TokenMap.put(url, set);
        }

        for (String url : TokenMap.keySet()) {
            Set<Token> s = TokenMap.get(url);
            if (s != null) {
                for (Token t : s) {
                    blockingQueue.offer(t);
                }
            }
        }
        this.isRunning = true;
    }

    @PreDestroy
    public void stop() {
        for (CachingConnectionFactory c : cachingConnectionFactoryMap.values()) {
            c.destroy();
            c = null;
            //c.clear();
        }
        this.timer.cancel();
        this.isRunning = false;
    }

    public int getPhase() {
        return 0;
    }

    public boolean isAutoStartup() {
        return true;
    }

    public void stop(Runnable arg0) {
        stop();
    }

    public int getSessionCacheSize() {
        return sessionCacheSize;
    }

    public void setSessionCacheSize(int sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

	/*public static void main(String[] args)
	{
		List<String> urls = new ArrayList<String>();
		urls.add("tcp://127.0.0.1:61616?jms.useAsyncSend=true");
		urls.add("tcp://127.0.0.1:61616");
		urls.add("tcp://127.0.0.1:61617");

		final AdvancedGroupQueueSender sender = new AdvancedGroupQueueSender();
		sender.setBrokerURLs(urls);
		sender.setMaxConnection(1);
		sender.setFreeTime(10000L);
		sender.setGroupQueueName("TGG");
		sender.setTokenFactor(2);
		sender.start();

		final int cycleNum = 1000;
		int threadNum = 4;

		class SendThread extends Thread
		{
			public void run()
			{
				long start = System.currentTimeMillis();
				logger.info(Thread.currentThread().getName() + " tid:" + Thread.currentThread().getId()
						+ " start sending. at " + start);
				for (int j = 0; j < cycleNum; j++)
				{
					sender.sendMsg("test message", "default", "systemid");
				}
				long end = System.currentTimeMillis();
				logger.info(Thread.currentThread().getName() + " tid:" + Thread.currentThread().getId()
						+ " stop sending. at " + end);
				float time = (end - start) / 1000;
				float speed = cycleNum * 1000 / (end - start);
				logger.info(Thread.currentThread().getName() + " tid:" + Thread.currentThread().getId() + " sending use "
						+ time + " seconds. speed = " + speed);
				logger.info(sender.Statistics());
			}
		}

		for (int i = 0; i < threadNum; i++)
		{
			SendThread thd = new SendThread();
			thd.start();
		}

	}*/

}
