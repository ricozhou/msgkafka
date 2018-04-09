package kafka;

import javax.imageio.event.IIOReadProgressListener;

public class ReturnMsg {
	public boolean isCompleted;
	public int comTimes;

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getComTimes() {
		return comTimes;
	}

	public void setComTimes(int comTimes) {
		this.comTimes = comTimes;
	}

	@Override
	public String toString() {
		return "ReturnMsg [isCompleted=" + isCompleted + ", comTimes=" + comTimes + "]";
	}

}
