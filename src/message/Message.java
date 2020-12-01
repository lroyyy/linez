package message;

import player.Player;

public class Message {

	private String type;
	public static final String SYSTEM = "system";
	public static final String PERSONAL = "personal";
	private String content;
	private Player sender;
	private Player receiver;
	private Action action;

	/** ����ϵͳ��Ϣ */
	public Message(String content, Player receiver) {
		setType(SYSTEM);
		setContent(content);
		setReceiver(receiver);
	}

	/** ����˽����Ϣ */
	public Message(String content, Player sender, Player receiver) {
		setType(PERSONAL);
		setContent(content);
		setSender(sender);
		setReceiver(receiver);
	}

	/** ���춯����Ϣ */
	public Message(Action action, Player sender, Player receiver) {
		setSender(sender);
		setReceiver(receiver);
		setAction(action);
	}

	@Override
	public String toString() {
		String str = "";
		if (action != null) {// ����
			str += action.toString();
		} else {// �Ƕ���
			str += getContent();
		}
		return str;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Player getSender() {
		return sender;
	}

	public void setSender(Player sender) {
		this.sender = sender;
	}

	public Player getReceiver() {
		return receiver;
	}

	public void setReceiver(Player receiver) {
		this.receiver = receiver;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
		action.setReceiver(receiver);
		action.setSender(sender);
	}

}
