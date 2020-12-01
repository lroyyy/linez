package message;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import ui.UIManager;
import util.Debugger;
import element.GeneralManager;

/** ��Ϣ������ */
public class MessageManager {

	/** ��Ϣ���� */
	private static List<Message> messages;
	/** ��ʷ��Ϣ���� */
	private static List<Message> messageHistory;
	/** ������ */
	public static int lineSize = 33;
	private static Action sayHello;
	private static Action provoke;

	public static void init() {
		Debugger.out("���ڳ�ʼ����Ϣ������");
		messages = new LinkedList<Message>();
		if (messageHistory == null) {
			messageHistory = new LinkedList<Message>();
		}
	}

	/** �����Ϣ */
	public synchronized static void addMessage(String message) {
		Debugger.out("��Ϣ�����м�����һ������Ϣ��" + message);
		messages.add(new Message(message, GeneralManager.getPlayer()));
		messageHistory.add(new Message(message, GeneralManager.getPlayer()));
		if (!MessageOutputer.busy) {
			showMessage();
		}
	}

	/** �����Ϣ */
	public synchronized static void addMessage(Message message) {
		Debugger.out("��Ϣ�����м�����һ������Ϣ��" + message);
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

	/** ��ʾ��Ϣ */
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

	/** ��ȡ��Ϣ��ʷ */
	public static String getMessageHistory() {
		String tmpStr = "����ʷ��Ϣ��";
		if (!messageHistory.isEmpty()) {
			tmpStr = "";
			for (Message message : messageHistory) {
				tmpStr += message + "\n";
			}
		}
		return tmpStr;
	}

	/** �����Ϣ��ʷ */
	public static void resetMessageHistory() {
		if (!messageHistory.isEmpty()) {
			messageHistory.clear();
		}
	}

	public static void initActions() {
		sayHello = new Action("�ʺ�") {
			@Override
			public String toString() {
				return getSender().getName() + " ��  " + getReceiver().getName()
						+ " ����һ���к���";
			}
		};
		provoke = new Action("����") {
			@Override
			public String toString() {
				return getSender().getName() + " ��  " + getReceiver().getName()
						+ " ����ع��˹���ָ��˵����С������ҧ�ң���";
			}
		};
	}
}
