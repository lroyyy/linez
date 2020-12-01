package gamedata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import player.Player;
import record.Record;
import resource.Color;
import setting.SettingManager;
import cell.Cell;

public class GameData {

	private Player player;
	private Cell[][] cells;
	private List<Color> nextColors;
	private Date date;
	private int bombCount;

	public GameData() {
	}

	/**
	 * ����GameData
	 * 
	 * @param player
	 * @param cells
	 * @param nextColors
	 */
	public GameData(Player player, Cell[][] cells, List<Color> nextColors,
			Date date) {
		this.player = player;
		this.cells = cells;
		this.nextColors = nextColors;
		this.date = date;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public List<Color> getNextColors() {
		return nextColors;
	}

	public Record getRecord() {
		return player.getRecord();
	}

	public void setRecord(Record record) {
		player.setRecord(record);
	}

	public String getPlayerName() {
		return player.getName();
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public int getBombCount() {
		return bombCount;
	}

	public Date getDate() {
		return date;
	}

	/**
	 * ��Ϸģʽ�� <br>
	 * ������� <br>
	 * ������ <br>
	 * ������<br>
	 * ʱ�䣺
	 * */
	@Override
	public String toString() {
		String str = "";
		String gameModeString = SettingManager.getChineseGameMode(player
				.getRecord().getGameMode());
		String scoreString = Integer.toString(player.getRecord().getScore());
		String bombCountString = Integer.toString(bombCount);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd,HH:mm:ss");
		String dateString = dateFormat.format(date);
		str = "�������" + player.getName() + "����Ϸģʽ��" + gameModeString + "��������"
				+ scoreString + "��������" + bombCountString + "��ʱ�䣺" + dateString;
		return str;
	}

}
