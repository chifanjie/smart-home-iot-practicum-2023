package com.ruoyi.iss.qpid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iss.socket.WebSocketServer;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.apache.qpid.jms.transports.TransportOptions;
import org.apache.qpid.jms.transports.TransportSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URI;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * qpid 中间平台消息监听
 *
 * @author 178535
 * @date 2023/8/19 3:56 PM
 * @since 1.8
 **/
@Component
@ConditionalOnProperty(prefix = "iot.platform.amqp", name = "enabled", havingValue = "true", matchIfMissing = false)
public class IotApplicationQpidListener implements ApplicationRunner
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IotApplicationQpidListener.class);

	/**
	 * 异步线程池，参数可以根据业务特点作调整，也可以用其他异步方式来处理。
	 */
	private final static ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
		Runtime.getRuntime().availableProcessors() * 2, 60,
		TimeUnit.SECONDS, new LinkedBlockingQueue<>(5000));

	/**
	 * 队列名，可以使用默认队列 DefaultQueue
	 */
	private static String queueName = System.getenv("IOT_AMQP_QUEUE");

	/**
	 * qpid-jms的账号和密码
	 */
	private static String username = System.getenv("IOT_AMQP_USERNAME");
	private static String password = System.getenv("IOT_AMQP_PASSWORD");

	/**
	 * 按照qpid-jms的规范，组装连接URL。
	 */
	private static String connectionUrl = System.getenv("IOT_AMQP_URL");

	private static Connection connection;
	private static Session session;
	private static MessageConsumer consumer;
	private static long lastReconnectTime = 0;
	private static AtomicBoolean isReconnecting = new AtomicBoolean(false);
	private static MessageListener messageListener = new MessageListener()
	{
		@Override
		public void onMessage(Message message)
		{
			try
			{
				// 建议异步处理收到的消息，确保onMessage函数里没有耗时逻辑。
				// 如果业务处理耗时过程过长阻塞住线程，可能会影响SDK收到消息后的正常回调。
				executorService.submit(() -> processMessage(message));
			}
			catch (Exception e)
			{
				LOGGER.info("submit task occurs exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	};
	private static JmsConnectionListener myJmsConnectionListener = new JmsConnectionListener()
	{
		/**
		 * 连接成功建立。
		 */
		@Override
		public void onConnectionEstablished(URI remoteURI)
		{
			LOGGER.info("AMQP connection established");
		}

		/**
		 * 尝试过最大重试次数之后，最终连接失败。
		 */
		@Override
		public void onConnectionFailure(Throwable error)
		{
			LOGGER.info("onConnectionFailure, " + error.getMessage());
		}

		/**
		 * 连接中断。
		 */
		@Override
		public void onConnectionInterrupted(URI remoteURI)
		{
			LOGGER.info("AMQP connection interrupted");
		}

		/**
		 * 连接中断后又自动重连上。
		 */
		@Override
		public void onConnectionRestored(URI remoteURI)
		{
			LOGGER.info("AMQP connection restored");
		}

		@Override
		public void onInboundMessage(JmsInboundMessageDispatch envelope)
		{
			LOGGER.debug("AMQP message received");
		}

		@Override
		public void onSessionClosed(Session session, Throwable cause)
		{
			LOGGER.info("onSessionClosed, session=" + session + ", cause =" + cause);
		}

		@Override
		public void onConsumerClosed(MessageConsumer consumer, Throwable cause)
		{
			LOGGER.info("MessageConsumer, consumer=" + consumer + ", cause =" + cause);
		}

		@Override
		public void onProducerClosed(MessageProducer producer, Throwable cause)
		{
			LOGGER.info("MessageProducer, producer=" + producer + ", cause =" + cause);
		}
	};

	private static void createConsumer() throws Exception
	{
		Hashtable<String, String> hashtable = new Hashtable<>();
		hashtable.put("connectionfactory.HwConnectionURL", connectionUrl);
		hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
		Context context = new InitialContext(hashtable);
		JmsConnectionFactory cf = (JmsConnectionFactory) context.lookup("HwConnectionURL");

		//信任服务端
		TransportOptions to = new TransportOptions();
		to.setTrustAll(false);
		cf.setSslContext(TransportSupport.createJdkSslContext(to));

		// 创建连接
		connection = cf.createConnection(username, password);
		((JmsConnection) connection).addConnectionListener(myJmsConnectionListener);

		// 创建 Session
		// Session.CLIENT_ACKNOWLEDGE: 收到消息后，需要手动调用message.acknowledge()。
		// Session.AUTO_ACKNOWLEDGE: SDK自动ACK（推荐）。
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		connection.start();
		connection.setExceptionListener(exceptionListener);
		// 创建 consumer
		consumer = session.createConsumer(new JmsQueue(queueName));
	}

	private static void receiveMessage()
	{
		while (true)
		{
			try
			{
				if (consumer != null)
				{
					Message message = consumer.receive();
					// 建议异步处理收到的消息，确保receiveMessage函数里没有耗时逻辑。
					executorService.execute(() -> processMessage(message));
				}
				else
				{
					Thread.sleep(1000L);
				}
			}
			catch (Exception e)
			{
				LOGGER.info("receiveMessage hand an exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 客户端断开需要重连
	 */
	private static void reconnect()
	{
		if (isReconnecting.compareAndSet(false, true))
		{
			while (true)
			{
				try
				{
					// 防止重连次数太多，重连时间间隔15s
					if (System.currentTimeMillis() - lastReconnectTime < 15 * 1000L)
					{
						Thread.sleep(15 * 1000L);
					}
					shutdown();
					createConsumer();
					lastReconnectTime = System.currentTimeMillis();
					isReconnecting.set(false);
					break;
				}
				catch (Exception e)
				{
					LOGGER.info("reconnect hand an exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 在这里处理您收到消息后的具体业务逻辑。
	 */
	private static void processMessage(Message message)
	{
		try
		{
			String body = message.getBody(String.class);
			String content = new String(body);
			JSONObject jsonObject = JSON.parseObject(content);
			LOGGER.info("resource=" + jsonObject.getString("resource"));
			LOGGER.info("product_id=" + jsonObject.getJSONObject("notify_data").getJSONObject("header").getString("product_id"));
			LOGGER.info("device_id=" + jsonObject.getJSONObject("notify_data").getJSONObject("header").getString("device_id"));
			//LOGGER.info("Temperature=" + jsonObject.getJSONObject("notify_data").getJSONObject("body").getJSONArray("services").getJSONObject(0).getJSONObject("properties").getIntValue("Temperature"));
			//WebSocketServer.sendGroupInfo(content);
			// Course adapter routes to an explicitly configured local demo user.
			String userId = System.getenv("IOT_AMQP_TARGET_USER_ID");
			WebSocketServer.sendInfo(content, userId);
		}
		catch (Exception e)
		{
			LOGGER.info("processMessage occurs error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void shutdown()
	{
		if (consumer != null)
		{
			try
			{
				consumer.close();
			}
			catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
		if (session != null)
		{
			try
			{
				session.close();
			}
			catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
		if (connection != null)
		{
			try
			{
				connection.setExceptionListener(null);
				connection.close();
			}
			catch (JMSException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * SpringBoot中，提供了一个接口：ApplicationRunner。接口中run方法执行时机：spring容器启动完成之后执行接口实现类run方法
	 *
	 * @param args : args
	 * @date 2023/8/19 4:07 PM
	 * @author 178535
	 * @since 1.8
	 **/
	@Override
	public void run(ApplicationArguments args) throws Exception
	{
        requireConfigured("IOT_AMQP_QUEUE", queueName);
        requireConfigured("IOT_AMQP_USERNAME", username);
        requireConfigured("IOT_AMQP_PASSWORD", password);
        requireConfigured("IOT_AMQP_URL", connectionUrl);
        requireConfigured("IOT_AMQP_TARGET_USER_ID", System.getenv("IOT_AMQP_TARGET_USER_ID"));
		createConsumer();
		// 处理消息有两种方式
		// 1，主动拉数据（推荐），参照receiveMessage()
		//receiveMessage();
		// 2, 添加监听，参照consumer.setMessageListener(messageListener), 服务端主动推数据给客户端，但得考虑接受的数据速率是客户能力能够承受住的
		consumer.setMessageListener(messageListener);
	}

    private static void requireConfigured(String name, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(name + " is required when IOT_AMQP_ENABLED=true");
        }
    }

	private static ExceptionListener exceptionListener = new ExceptionListener()
	{
		@Override
		public void onException(JMSException e)
		{
			e.printStackTrace();
			LOGGER.info("connection has an exception:" + e);
			// 链接发生异常，需要重连
			reconnect();
		}
	};


}
