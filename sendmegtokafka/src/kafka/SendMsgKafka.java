package kafka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.json.simple.JSONObject;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import mainGUI.KafkaMainGUI;
import scala.annotation.meta.field;

public class SendMsgKafka {
	public static int TIMES = 0;
	public ReturnMsg rm = new ReturnMsg();

	// 处理参数
	public ReturnMsg kafkaController(BaseMsg baseMsg) throws Exception {
		kafkaMsg kafkaMsg = new kafkaMsg();
		handleParams(baseMsg, kafkaMsg);

		// send
		ReturnMsg returnMsg = sendMsg(kafkaMsg);
		return returnMsg;

	}

	private void handleParams(BaseMsg baseMsg, kafkaMsg kafkaMsg) {
		kafkaMsg.setIpPort(baseMsg.getKafkaIp() + ":" + baseMsg.getKafkaPort());
		kafkaMsg.setGroupId(baseMsg.getKafkaGroupId());
		kafkaMsg.setTopic(baseMsg.getKafkaTopic());
		kafkaMsg.setKey(baseMsg.getKafkaKey());
		kafkaMsg.setAccount(Integer.valueOf(baseMsg.getKafkaAccount()));
		kafkaMsg.setTime(Integer.valueOf(baseMsg.getKafkaTimes()));
		kafkaMsg.setStartId(Integer.valueOf(baseMsg.getKafkaStartId()));

		if (baseMsg.isKafkaIsDefault()) {
			String msgJson = "{\"name\":\"testName\",\"age\":1,\"address\":\"address\",\"otherMsg\":\"msgtest\"}";
			kafkaMsg.setMessage(msgJson);
		} else {
			kafkaMsg.setMessage(baseMsg.getKafkaMessage());
		}

	}

	// send
	public ReturnMsg sendMsg(kafkaMsg kafkaMsg) throws IOException, Exception {
		PrintStream out = null;
		PrintStream mytxt = null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmmss");
			mytxt = new PrintStream("./kafkalog" + sd.format(new Date()) + ".txt");
			out = System.out;
			System.setOut(mytxt);
			System.out.println("Start：");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		TIMES = 0;
		Properties configProperties = new Properties();
		configProperties.put("group.id", kafkaMsg.getGroupId());
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaMsg.getIpPort());
		configProperties.put("key.serializer", StringSerializer.class);
		configProperties.put("value.serializer", JsonSerializer.class);

		KafkaProducer producer = new KafkaProducer(configProperties);

		ObjectMapper objectMapper = new ObjectMapper();
		int i = 0;
		int acc = kafkaMsg.getAccount();
		while (i < acc) {
			JSONObject data = new JSONObject();

			// handlemessage
			String msg = hanleMsg(kafkaMsg, i, acc);

			JsonNode jsonNode = objectMapper.readTree(msg);
			ProducerRecord rec = new ProducerRecord(kafkaMsg.getTopic(), kafkaMsg.getKey() + i, jsonNode);
			producer.send(rec);
			System.out.println(jsonNode);
			Thread.sleep(kafkaMsg.getTime());
			i++;
			TIMES++;
			rm.setComTimes(TIMES);
			if (!KafkaMainGUI.isRunning) {
				rm.setCompleted(false);
				System.setOut(out);
				mytxt.close();
				producer.close();
				return rm;
			}
			// System.out.println(jsonNode);
		}
		rm.setCompleted(true);
		System.out.println("End");
		System.setOut(out);
		mytxt.close();
		producer.close();
		return rm;

	}

	// handlemsg
	private String hanleMsg(kafkaMsg kafkaMsg, int i, int acc) throws IOException {
		com.alibaba.fastjson.JSONObject json = JSON.parseObject(kafkaMsg.getMessage());
		// 只发送一条信息则不做更改
		if (acc != 1) {
			for (Entry<String, Object> entry : json.entrySet()) {
				if ("age".equals(entry.getKey())) {
					int age = Integer.valueOf(entry.getValue().toString()) + i;
					entry.setValue(age);
				} else {
					entry.setValue(entry.getValue().toString() + i);
				}
			}
		}
		String s = json.toString();
		int idn = i + kafkaMsg.getStartId();
		s = "{" + "\"id\":" + idn + "," + s.substring(1);
		return s;
	}

	public Properties kafkaController2(BaseMsg baseMsg) {
		Properties props = new Properties();
		props.put("bootstrap.servers", baseMsg.getKafkaIp() + ":" + baseMsg.getKafkaPort());
		props.put("group.id", baseMsg.getKafkaGroupId());
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "earliest");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return props;
	}

	public void kafkaController3(BaseMsg baseMsg) {
		Properties props = new Properties();
		props.put("bootstrap.servers", baseMsg.getKafkaIp() + ":" + baseMsg.getKafkaPort());
		props.put("group.id", baseMsg.getKafkaGroupId());
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "earliest");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
		kafkaConsumer.subscribe(Arrays.asList(baseMsg.getKafkaTopic()));
		while (KafkaMainGUI.isRunning2) {
			ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("topic: " + record.topic() + " key: " + record.key() + " value: " + record.value()
						+ " partition: " + record.partition());
			}
		}

	}
}
