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
	 * 构造GameData
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
	 * 游戏模式： <br>
	 * 玩家名： <br>
	 * 分数： <br>
	 * 雷数：<br>
	 * 时间：
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
		str = "玩家名：" + player.getName() + "；游戏模式：" + gameModeString + "；分数："
				+ scoreString + "；雷数：" + bombCountString + "；时间：" + dateString;
		return str;
	}

}
