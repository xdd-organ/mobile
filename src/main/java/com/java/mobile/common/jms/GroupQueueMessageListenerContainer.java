package com.java.mobile.common.jms;

import com.java.mobile.common.utils.IPUtil;
import com.java.mobile.common.utils.PortUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * {@code GroupQueueMessageListenerContainer}可以同时对多个broker中有相同前缀的队列进行消费。
 * <p><strong>设计思路</strong></p>
 * <p>多个Activemq Broker实例同时工作 </p>
 * <p>1.消息前置同时连接两个（或两个以上）MQ， 采用轮循机制发送消息到 MQ 1 或 MQ 2。
 *    2.核心同时配置多个MQ Broker的消费者，可以消费MQ 1 和MQ2 上的消息。 
 * </p>
 * <p>
 *    主要解决问题：
 *    1、解决MQ单点问题。两个或两个以上MQ可以同时工作。不存在MQ挂掉的风险。 
 *    2、MQ做为消息的传输管道， 增加MQ数量就可以拓宽管道的宽度。 
 *    3、去掉分组的概念， 所有消息前置以及核心的mq配置是相同的。 
 *    4、通过增加或减少 消息前置、MQ、核心应用的实例数量 系统能力易于扩展。    
 * </p>
 */

public class GroupQueueMessageListenerContainer /* implements SmartLifecycle */
{

	private static final Logger logger = LoggerFactory.getLogger(GroupQueueMessageListenerContainer.class);

	/**
	 * activeMq broker的链接 url列表
	 */
	private List<String> brokerURLs = null;

	/**
	 * 用于处理消息的 messageListener;
	 */
	private MessageListener messageListener;

	/**
	 * 消息选择器
	 */
	private String messageSelector;

	/**
	 * 是否消息选择器
	 */
	private boolean needSelector;

	/**
	 * 队列组名
	 */
	private String GroupQueueName = "Upay";

	/**
	 * 默认每个DefaultMessageListenerContainer启动的并发消费者数
	 */
	private int concurrentConsumers = 1;

	/**
	 * 默认每个DefaultMessageListenerContainer的最大消费者数
	 */
	private int maxConcurrentConsumers = 1;

	/**
	 * 每个broker的最大链接数，建议使用1
	 */
	private int maxConnection = 1;

	/**
	 * 队列组名称与子队列名称间的连接符
	 */
	private String jointMark = "-";

	/**
	 * 制定配置 子队列的consumer数量， 配置字符串为如 beijin:3,hebei:10 此时
	 * GroupName-beijin队列将配置3个consumer, GroupName-hebei队列将配置10个consumer
	 */
	private String subQueueConsumerCfg = null;

	/**
	 * 每个子队列的启动消费者数量记录 subname -> int
	 */
	private Map<String, Integer> consumerCfgMap = new HashMap<String, Integer>();

	/**
	 * 每个子队列的最大消费者数量记录 subname -> int
	 */
	private Map<String, Integer> maxConsumerCfgMap = new HashMap<String, Integer>();

	private volatile boolean isRunning = false;

	private String advisoryTopic = "ActiveMQ.Advisory.Queue";

	private AdvisoryListener advisoryListener = new AdvisoryListener();

	/**
	 * brokerUrl -> PooledConnectionFactory
	 */
	private Map<String, PooledConnectionFactory> pooledConnectionFactoryMap = new HashMap<String, PooledConnectionFactory>();
	/**
	 * 所有队列名称，默认队列和子队列
	 */
	private Set<String> AllQueueNames = new HashSet<String>();
	/**
	 * key(格式：brokerUrl + "," + queueName) -> DefaultMessageListenerContainer
	 */
	private Map<String, DefaultMessageListenerContainer> containerMap = new HashMap<String, DefaultMessageListenerContainer>();

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public int getConcurrentConsumers() {
		return concurrentConsumers;
	}

	public void setConcurrentConsumers(int concurrentConsumers) {
		this.concurrentConsumers = concurrentConsumers;
	}

	public boolean isNeedSelector() {
		return needSelector;
	}

	public void setNeedSelector(boolean needSelector) {
		this.needSelector = needSelector;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public List<String> getBrokerURLs() {
		return brokerURLs;
	}

	public void setBrokerURLs(List<String> brokerURLs) {
		this.brokerURLs = brokerURLs;
	}

	/**
	 * 
	 * 不采用jmx获取队列， 改用advisory message 获取队列名
	 *
	 * public List<String> getBrokerJmxURLs() { return brokerJmxURLs; }
	 * 
	 * public void setBrokerJmxURLs(List<String> brokerJmxURLs) {
	 * this.brokerJmxURLs = brokerJmxURLs; }
	 */

	public String getGroupQueueName() {
		return GroupQueueName;
	}

	public void setGroupQueueName(String groupQueueName) {
		GroupQueueName = groupQueueName;
	}

	/**
	 * 
	 * @Title: start
	 * @Description: 初始化
	 * @see org.springframework.context.Lifecycle#start()
	 */
	@PostConstruct
	public void start() throws JMSException {
		assert (brokerURLs != null && brokerURLs.size() > 0);
		assert (messageListener != null);
		brokerURLs = Arrays.asList(brokerURLs.get(0).split(","));
		logger.info("start GroupQueueMessageListenerContainer ...");

		/**
		 * 配置subqueue 的consumer 数量
		 */
		if (subQueueConsumerCfg != null) {
			String[] cfgs = subQueueConsumerCfg.split(",");
			for (String cfg : cfgs) {
				cfg = cfg.trim();
				String[] ca = cfg.split(":");
				if (ca.length != 2 && ca.length != 3) {
					logger.error("subqueue consumer number configuration error. {}", cfg);
					throw new IllegalArgumentException("subqueue consumer number configuration error: " + cfg);
				}
				try {
					ca[0] = ca[0].trim();
					ca[1] = ca[1].trim();
					Integer n = Integer.parseInt(ca[1]);
					Integer m;
					if (ca.length == 3) {
						m = Integer.parseInt(ca[2].trim());
					}else{
						m = n;
					}
					consumerCfgMap.put(ca[0], n);
					maxConsumerCfgMap.put(ca[0], m);

					String queueName = this.GroupQueueName + this.jointMark + ca[0];
					AllQueueNames.add(queueName);
					logger.debug("AllQueueNames.add({})", queueName);
				} catch (Exception e) {
					logger.error("subqueue consumer number is not a number . {}", cfg, e);
					throw e;
				}
			}
		} else {
			String queueName = this.GroupQueueName + this.jointMark + "default";
			AllQueueNames.add(queueName);
			logger.debug("AllQueueNames.add({})", queueName);
		}

		initConnectionFactory();
		updateContainers();

		/**
		 * 采用 advisory Message 获取broker上的所有队列
		 */
		startAdvisoryContainer();

		/**
		 * 注释掉，该用Advisor 方式获取 broker中的队列名
		 */
		// startMonitorThread();

		this.isRunning = true;
	}

	/**
	 * 
	 * @Title: startAdvisoryContainer
	 * @Description: ActiveMQTopic listenerContainer
	 * @return: void
	 */
	private void startAdvisoryContainer() {
		for (String brokerUrl : brokerURLs) {
			DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
			container.setConnectionFactory(pooledConnectionFactoryMap.get(brokerUrl));
			container.setConcurrentConsumers(1);
			container.setDestination(new ActiveMQTopic(advisoryTopic));
			container.setMessageListener(advisoryListener);
			container.setErrorHandler(new MqStateErrorHandler());
			container.initialize();
			container.start();

			String key = brokerUrl + "," + advisoryTopic;
			containerMap.put(key, container);
			logger.info("container for advisory queue topic [{}] had created", key);
		}
	}

	/**
	 * 
	 * @ClassName: AdvisoryListener
	 * @Description: 队列的topic监听器，监听队列的创建、删除以及收发消息等操作
	 * @date: 2016年3月29日 上午11:22:25
	 */
	class AdvisoryListener implements MessageListener {

		public void onMessage(Message message) {

			logger.info("AdvisoryListener receive message");
			if (message instanceof ActiveMQMessage) {
				ActiveMQMessage aMsg = (ActiveMQMessage) message;
				DestinationInfo desInf = (DestinationInfo) aMsg.getDataStructure();

				String queueName = desInf.getDestination().getPhysicalName();
				if (queueName != null && desInf.isAddOperation()) {
					addContainer(queueName);
				}
			} else {
				logger.error("AdvisoryListener recieve message is not ActiveMQMessage, that is abnormal.");
			}

		}

		// synchronized public void addContainer(String queueName)
		public void addContainer(String queueName) {
			String prefix = GroupQueueName + jointMark;
			String subname = queueName.replace(prefix, "");
			if (queueName.startsWith(prefix) && !AllQueueNames.contains(queueName)
					&& ((subname.equals("default") && subQueueConsumerCfg == null) || (subQueueConsumerCfg.contains(subname)))) {
				AllQueueNames.add(queueName);
				logger.info("discover new queue {}", queueName);
			}
			updateContainers();
		}
	}

	private void initConnectionFactory() throws JMSException {
		for (String url : brokerURLs) {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
			activeMQConnectionFactory.setBrokerURL(url);
			Connection connection = null;
			try {
				connection = activeMQConnectionFactory.createConnection();
				if (logger.isDebugEnabled()) {
					logger.debug("尝试获取" + url + "连接成功，地址有效。");
				}
			} catch (JMSException e) {
				logger.warn("尝试获取" + url + "连接失败，请确认地址是否有效!");
				throw e;
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (JMSException e) {
						throw e;
					}
				}
			}

			PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
			pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
			pooledConnectionFactory.setMaxConnections(maxConnection);
			pooledConnectionFactoryMap.put(url, pooledConnectionFactory);
		}
	}

	/**
	 * 
	 * @Title: updateContainers
	 * @Description: 队列lintenerContainer初始化
	 * @return: void
	 */
	synchronized private void updateContainers() {
		for (String brokerUrl : brokerURLs) {
			for (String name : AllQueueNames) {
				String key = brokerUrl + "," + name;
				DefaultMessageListenerContainer container = containerMap.get(key);
				if (container == null) {
					logger.warn(
							"there is no DefaultMessageListenerContainer for queue {}, new containner will be created",
							key);
					container = new DefaultMessageListenerContainer();
					container.setConnectionFactory(pooledConnectionFactoryMap.get(brokerUrl));

					/**
					 * 设置subQueue的consumers数量
					 */
					String subname = name.replaceFirst(GroupQueueName + jointMark, "");
					subname = subname.trim();
					Integer n = consumerCfgMap.get(subname);
					if (n != null) {
						container.setConcurrentConsumers(n);
					} else {
						container.setConcurrentConsumers(concurrentConsumers);
					}
					Integer m = maxConsumerCfgMap.get(subname);
					if (m != null) {
						container.setMaxConcurrentConsumers(m);
					} else {
					    if(n!=null){
                            container.setMaxConcurrentConsumers(n);
                        }else{
                            container.setMaxConcurrentConsumers(maxConcurrentConsumers);
                        }
					}
					if (needSelector) {
						container.setMessageSelector(
								"systemcode='" + IPUtil.getIp() + ":" + PortUtil.getPort() + "'");
					} else {
						container.setMessageSelector(messageSelector);
					}
					container.setDestination(new ActiveMQQueue(name));
					container.setMessageListener(messageListener);
					container.setErrorHandler(new MqStateErrorHandler());

					containerMap.put(key, container);
					logger.debug("containerMap.put({}{})", key, container);
					container.initialize();
					container.start();

					logger.info("DefaultMessageListenerContainer for queue {} had created", key);
				}
			}
		}
	}

	@PreDestroy
	synchronized public void stop() {
		for (DefaultMessageListenerContainer container : containerMap.values()) {
			container.destroy();
			container.stop();
		}

		for (PooledConnectionFactory factory : pooledConnectionFactoryMap.values()) {
			factory.stop();
		}
		this.isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getPhase() {
		return 0;
	}

	public boolean isAutoStartup() {
		return true;
	}

	public void stop(Runnable callback) {
		stop();
	}

	/**
	 * 获取maxConcurrentConsumers
	 * 
	 * @return the maxConcurrentConsumers
	 */
	public int getMaxConcurrentConsumers() {
		return maxConcurrentConsumers;
	}

	/**
	 * 设置 maxConcurrentConsumers
	 * 
	 * @param maxConcurrentConsumers
	 *            the maxConcurrentConsumers to set
	 */
	public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
		this.maxConcurrentConsumers = maxConcurrentConsumers;
	}

	public String getSubQueueConsumerCfg() {
		return subQueueConsumerCfg;
	}

	public void setSubQueueConsumerCfg(String subQueueConsumerCfg) {
		this.subQueueConsumerCfg = subQueueConsumerCfg;
	}

	public Map<String, Integer> getConsumerCfgMap() {
		return consumerCfgMap;
	}

	public void setConsumerCfgMap(Map<String, Integer> consumerCfgMap) {
		this.consumerCfgMap = consumerCfgMap;
	}

	/**
	 * 获取maxConsumerCfgMap
	 * 
	 * @return the maxConsumerCfgMap
	 */
	public Map<String, Integer> getMaxConsumerCfgMap() {
		return maxConsumerCfgMap;
	}

	/**
	 * 设置 maxConsumerCfgMap
	 * 
	 * @param maxConsumerCfgMap
	 *            the maxConsumerCfgMap to set
	 */
	public void setMaxConsumerCfgMap(Map<String, Integer> maxConsumerCfgMap) {
		this.maxConsumerCfgMap = maxConsumerCfgMap;
	}

}
