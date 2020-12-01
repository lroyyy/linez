package message;

import player.Player;

public abstract class Action {

	private String name;
	private Player sender;
	private Player receiver;

	public Action(String name) {
		this.name = name;
	}

	@Override
	public abstract String toString();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
