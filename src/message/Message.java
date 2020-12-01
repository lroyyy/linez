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

	/** 构造系统消息 */
	public Message(String content, Player receiver) {
		setType(SYSTEM);
		setContent(content);
		setReceiver(receiver);
	}

	/** 构造私人消息 */
	public Message(String content, Player sender, Player receiver) {
		setType(PERSONAL);
		setContent(content);
		setSender(sender);
		setReceiver(receiver);
	}

	/** 构造动作消息 */
	public Message(Action action, Player sender, Player receiver) {
		setSender(sender);
		setReceiver(receiver);
		setAction(action);
	}

	@Override
	public String toString() {
		String str = "";
		if (action != null) {// 动作
			str += action.toString();
		} else {// 非动作
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
