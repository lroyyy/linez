package message;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import ui.UIManager;
import util.Debugger;
import element.GeneralManager;

/** 消息管理器 */
public class MessageManager {

	/** 消息队列 */
	private static List<Message> messages;
	/** 历史消息队列 */
	private static List<Message> messageHistory;
	/** 行字数 */
	public static int lineSize = 33;
	private static Action sayHello;
	private static Action provoke;

	public static void init() {
		Debugger.out("正在初始化消息管理器");
		messages = new LinkedList<Message>();
		if (messageHistory == null) {
			messageHistory = new LinkedList<Message>();
		}
	}

	/** 添加消息 */
	public synchronized static void addMessage(String message) {
		Debugger.out("消息队列中加入了一条新消息：" + message);
		messages.add(new Message(message, GeneralManager.getPlayer()));
		messageHistory.add(new Message(message, GeneralManager.getPlayer()));
		if (!MessageOutputer.busy) {
			showMessage();
		}
	}

	/** 添加消息 */
	public synchronized static void addMessage(Message message) {
		Debugger.out("消息队列中加入了一条新消息：" + message);
		messages.add(message);
		messageHistory.add(message);
		if (!MessageOutputer.busy) {
			showMessage();
		}
	}

	public static void addSeparator() {
		String separatorString = "--------------------------------------------------------------------------------------------------------";
		messageHistory.add(new Message((separatorString), GeneralManager
				.getPlayer()));
	}

	/** 显示消息 */
	public static void showMessage() {
		if (messages.isEmpty()) {
			return;
		}
		Message message = ((LinkedList<Message>) messages).poll();
		JLabel messageContainer = UIManager.getFrameMain()
				.getMessageContainer();
		MessageOutputer messageUpdatingWorker = new MessageOutputer(
				messageContainer, message.toString());
		messageUpdatingWorker.execute();
	}

	/** 获取消息历史 */
	public static String getMessageHistory() {
		String tmpStr = "无历史消息。";
		if (!messageHistory.isEmpty()) {
			tmpStr = "";
			for (Message message : messageHistory) {
				tmpStr += message + "\n";
			}
		}
		return tmpStr;
	}

	/** 清空消息历史 */
	public static void resetMessageHistory() {
		if (!messageHistory.isEmpty()) {
			messageHistory.clear();
		}
	}

	public static void initActions() {
		sayHello = new Action("问好") {
			@Override
			public String toString() {
				return getSender().getName() + " 对  " + getReceiver().getName()
						+ " 打了一声招呼。";
			}
		};
		provoke = new Action("挑衅") {
			@Override
			public String toString() {
				return getSender().getName() + " 向  " + getReceiver().getName()
						+ " 轻蔑地勾了勾手指，说：”小样，来咬我？“";
			}
		};
	}
}
