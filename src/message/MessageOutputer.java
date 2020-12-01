package message;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import util.Debugger;

/** 消息输出器 */
public class MessageOutputer extends SwingWorker<String, String> {

	private JLabel label;
	private String message;
	private long typeInterval = 60;
	private long delayTime = 300;
	private int delayCount = 5;
	public static boolean busy;

	public MessageOutputer(JLabel label, String message) {
		this.label = label;
		this.message = message;
	}

	private void type(String message) {
		String subMessage = "";
		for (int i = 0; i < message.length(); i++) {
			delay(typeInterval);
			subMessage = message.substring(0, i + 1) + "_";
			final String showedMessage = subMessage;
			publish(showedMessage);
			delay(typeInterval);
			subMessage = message.substring(0, i + 1);
			final String showedMessage2 = subMessage;
			publish(showedMessage2);
		}
	}

	private void typeDelay(String message) {
		for (int i = 0; i < delayCount; i++) {
			delay(delayTime);
			publish(message + "_");
			delay(delayTime);
			publish(message);
		}
	}

	@Override
	protected String doInBackground() throws Exception {
		busy = true;
		if (message.length() > MessageManager.lineSize) {
			int count = message.length() / MessageManager.lineSize;
			int beginIndex = 0;
			int endIndex = MessageManager.lineSize - 1;
			for (int i = 0; i < count + 1; i++) {
				String subMessage = message.substring(beginIndex, endIndex);
				Debugger.out(subMessage);
				type(subMessage);
				typeDelay(subMessage);
				beginIndex += MessageManager.lineSize - 1;
				if (i == count - 1) {
					endIndex = message.length();
				} else {
					endIndex += MessageManager.lineSize;
				}
			}
		} else {
			type(message);
			typeDelay(message);
		}
		// Debugger.out("消息：\"" + message + "\"显示完毕。");
		busy = false;
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		for (String message : chunks) {
			label.setText(message);
		}
	}

	protected void done() {
		// 继续显示下一条消息
		MessageManager.showMessage();
	}

	protected static void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
