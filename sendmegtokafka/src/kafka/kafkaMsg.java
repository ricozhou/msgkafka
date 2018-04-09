package kafka;

public class kafkaMsg {
	// ip:port
	public String ipPort;
	// groupid
	public String groupId;
	// topic
	public String topic;
	// key
	public String key;
	// account
	public int account;
	// time
	public int time;
	// msg
	public String message;
	// startid
	public int startId;

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	@Override
	public String toString() {
		return "kafkaMsg [ipPort=" + ipPort + ", groupId=" + groupId + ", topic=" + topic + ", key=" + key
				+ ", account=" + account + ", time=" + time + ", message=" + message + ", startId=" + startId + "]";
	}

}
