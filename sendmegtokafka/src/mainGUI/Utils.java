package mainGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import kafka.BaseMsg;
import kafka.kafkaMsg;

public class Utils {

	// basecheck
	public boolean checkBase1(BaseMsg baseMsg, int i) {
		// not null ""
		if ("".equals(baseMsg.getKafkaIp()) || "".equals(baseMsg.getKafkaPort()) || "".equals(baseMsg.getKafkaGroupId())
				|| "".equals(baseMsg.getKafkaTopic())) {
			return false;
		}
		if (i == 0) {
			if ("".equals(baseMsg.getKafkaAccount()) || "".equals(baseMsg.getKafkaTimes())
					|| "".equals(baseMsg.getKafkaKey()) || "".equals(baseMsg.getKafkaStartId())) {
				return false;
			}
		}

		return true;
	}

	public boolean checkBase2(BaseMsg baseMsg, int i) {
		// only number
		if (!baseMsg.getKafkaPort().matches("^[0-9]*$")) {
			return false;
		}
		if (i == 0) {
			if (!baseMsg.getKafkaAccount().matches("^[0-9]*$") || !baseMsg.getKafkaTimes().matches("^[0-9]*$")
					|| !baseMsg.getKafkaStartId().matches("^[0-9]*$")) {
				return false;
			}
		}
		return true;
	}

	// checkip
	public boolean checkIp(String ip) {
		// 定义正则表达式
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if (ip.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAccTimeId(BaseMsg baseMsg) {
		if (baseMsg.getKafkaAccount().startsWith("0") || baseMsg.getKafkaTimes().startsWith("0")) {
			return false;
		}
		if (baseMsg.getKafkaAccount().length() > 9 || baseMsg.getKafkaTimes().length() > 9
				|| baseMsg.getKafkaStartId().length() > 9) {
			return false;
		}
		return true;
	}

	public boolean checkMsg(String kafkaMessage) {
		if ("".equals(kafkaMessage)) {
			return false;
		}
		if (!checkJson(kafkaMessage)) {
			return false;
		}
		return true;
	}

	private boolean checkJson(String kafkaMessage) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(kafkaMessage);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public BaseMsg getDefaultMsg(File f, int flag) throws Exception {
		Properties pro = new Properties();
		// InputStream inputStream =
		// this.getClass().getResourceAsStream("kafkamodel.properties");
		// RandomAccessFile ff = new RandomAccessFile(new
		// File("kafkalog20180329012249.txt"), "rw");
		FileInputStream fis = new FileInputStream(f);
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis, "utf-8"));
		pro.load(bf);
		BaseMsg bm = new BaseMsg();
		bm.setKafkaIp(pro.getProperty("ip"));
		bm.setKafkaPort(pro.getProperty("port"));
		bm.setKafkaGroupId(pro.getProperty("groupid"));
		bm.setKafkaTopic(pro.getProperty("topic"));

		// 0表示发送
		if (flag == 0) {
			bm.setKafkaAccount(pro.getProperty("account"));
			bm.setKafkaTimes(pro.getProperty("times"));
			bm.setKafkaMessage(formatJson(pro.getProperty("msg")));
			bm.setKafkaKey(pro.getProperty("key"));
			bm.setKafkaStartId(pro.getProperty("startid"));
			if (pro.getProperty("msgisdefault").equals("0")) {
				bm.setKafkaIsDefault(true);
			} else {
				bm.setKafkaIsDefault(false);
			}
		}
		fis.close();
		bf.close();
		return bm;
	}

	public boolean BaseMsgToPro(BaseMsg baseMsg3, int flag) throws Exception {
		File f;
		String content = "";
		if (flag == 0) {
			f = new File("sendkafkamodel.properties");
			content = "ip=" + baseMsg3.getKafkaIp() + "\r\nport=" + baseMsg3.getKafkaPort() + "\r\ngroupid="
					+ baseMsg3.getKafkaGroupId() + "\r\ntopic=" + baseMsg3.getKafkaTopic() + "\r\naccount="
					+ baseMsg3.getKafkaAccount() + "\r\ntimes=" + baseMsg3.getKafkaTimes() + "\r\nmsgisdefault="
					+ (baseMsg3.isKafkaIsDefault() ? "0" : "1") + "\r\nmsg="
					+ baseMsg3.getKafkaMessage().replaceAll("\n", "") + "\r\nkey=" + baseMsg3.getKafkaKey()
					+ "\r\nstartid=" + baseMsg3.getKafkaStartId();
		} else {
			f = new File("consumerkafkamodel.properties");
			content = "ip=" + baseMsg3.getKafkaIp() + "\r\nport=" + baseMsg3.getKafkaPort() + "\r\ngroupid="
					+ baseMsg3.getKafkaGroupId() + "\r\ntopic=" + baseMsg3.getKafkaTopic();
		}
		// 先删除隐藏文件再重新创建，隐藏文件不支持修改
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);// 创建文件输出流对象
			// 设置文件的隐藏属性
			String set = "attrib +H " + f.getAbsolutePath();
			Runtime.getRuntime().exec(set);
			// 将字符串写入到文件中
			fos.write(content.getBytes());
			return true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public boolean isFileExit(File file) {
		if (file.exists()) {
			return true;
		}
		return false;
	}

	// public void createHiddFile(File f, int flag) throws IOException {
	// String content = "";
	// if (flag == 0) {
	// content =
	// "ip=192.168.1.144\r\nport=9092\r\ngroupid=groupid\r\ntopic=test1\r\naccount=10\r\ntimes=1000\r\nmsgisdefault=0\r\nmsg=\r\nkey=key\r\nstartid=1";
	// } else {
	// content =
	// "ip=192.168.1.144\r\nport=9092\r\ngroupid=groupid\r\ntopic=test1";
	// }
	// FileOutputStream fos = null;
	// try {
	// fos = new FileOutputStream(f);// 创建文件输出流对象
	// // 设置文件的隐藏属性
	// String set = "attrib +H " + f.getAbsolutePath();
	// Runtime.getRuntime().exec(set);
	// // 将字符串写入到文件中
	// fos.write(content.getBytes());
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// } finally {
	// try {
	// fos.close();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }

	// format jsonstring
	public String formatJson(String content) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		int count = 0;
		while (index < content.length()) {
			char ch = content.charAt(index);
			if (ch == '{' || ch == '[') {
				sb.append(ch);
				sb.append('\n');
				count++;
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
			} else if (ch == '}' || ch == ']') {
				sb.append('\n');
				count--;
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
				sb.append(ch);
			} else if (ch == ',') {
				sb.append(ch);
				sb.append('\n');
				for (int i = 0; i < count; i++) {
					sb.append('\t');
				}
			} else {
				sb.append(ch);
			}
			index++;
		}
		return sb.toString();
	}

	// json变一行
	public static String compactJson(String content) {
		String regEx = "[\t\n]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(content);
		return m.replaceAll("").trim();
	}

}
