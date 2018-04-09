package kafka;

public class BaseMsg {
	// ip
	public String kafkaIp;
	// port
	public String kafkaPort;
	// groupid
	public String kafkaGroupId;
	// topic
	public String kafkaTopic;
	// account
	public String kafkaAccount;
	// times
	public String kafkaTimes;
	// is msg type
	public boolean kafkaIsDefault;
	// msg
	public String kafkaMessage;
	// key
	public String kafkaKey;
	// startid
	public String kafkaStartId;

	public String getKafkaStartId() {
		return kafkaStartId;
	}

	public void setKafkaStartId(String kafkaStartId) {
		this.kafkaStartId = kafkaStartId;
	}

	public String getKafkaIp() {
		return kafkaIp;
	}

	public void setKafkaIp(String kafkaIp) {
		this.kafkaIp = kafkaIp;
	}

	public String getKafkaPort() {
		return kafkaPort;
	}

	public void setKafkaPort(String kafkaPort) {
		this.kafkaPort = kafkaPort;
	}

	public String getKafkaGroupId() {
		return kafkaGroupId;
	}

	public void setKafkaGroupId(String kafkaGroupId) {
		this.kafkaGroupId = kafkaGroupId;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public String getKafkaAccount() {
		return kafkaAccount;
	}

	public void setKafkaAccount(String kafkaAccount) {
		this.kafkaAccount = kafkaAccount;
	}

	public String getKafkaTimes() {
		return kafkaTimes;
	}

	public void setKafkaTimes(String kafkaTimes) {
		this.kafkaTimes = kafkaTimes;
	}

	public boolean isKafkaIsDefault() {
		return kafkaIsDefault;
	}

	public void setKafkaIsDefault(boolean kafkaIsDefault) {
		this.kafkaIsDefault = kafkaIsDefault;
	}

	public String getKafkaMessage() {
		return kafkaMessage;
	}

	public void setKafkaMessage(String kafkaMessage) {
		this.kafkaMessage = kafkaMessage;
	}

	public String getKafkaKey() {
		return kafkaKey;
	}

	public void setKafkaKey(String kafkaKey) {
		this.kafkaKey = kafkaKey;
	}

	@Override
	public String toString() {
		return "BaseMsg [kafkaIp=" + kafkaIp + ", kafkaPort=" + kafkaPort + ", kafkaGroupId=" + kafkaGroupId
				+ ", kafkaTopic=" + kafkaTopic + ", kafkaAccount=" + kafkaAccount + ", kafkaTimes=" + kafkaTimes
				+ ", kafkaIsDefault=" + kafkaIsDefault + ", kafkaMessage=" + kafkaMessage + ", kafkaKey=" + kafkaKey
				+ ", kafkaStartId=" + kafkaStartId + "]";
	}

}
