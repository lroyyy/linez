package record;

import java.text.SimpleDateFormat;
import java.util.Date;

import setting.SettingManager;

/**
 * 记录
 * <p>
 * 游戏模式<br>
 * 玩家名<br>
 * 分数<br>
 * 时间
 * 
 * @author zhengfei.fjhg
 * 
 */
public class Record implements Comparable<Record> {

	// /**编号*/
	// private int id;
	/** 游戏模式 */
	private String gameMode;
	/** 玩家名 */
	private String playerName;
	/** 日期 */
	private Date date;
	private int score;

	public Record() {
		// this(GameManager.getPlayer().getName(),
		// GameManager.getPresentScore());
	}

	/**
	 * @param playerName
	 *            玩家姓名
	 * @param score
	 *            分数
	 */
	public Record(String gameMode, String playerName, int score) {
		this(gameMode, playerName, score, new Date());
	}

	/**
	 * @param playerName
	 *            玩家姓名
	 * @param score
	 *            分数
	 * @param date
	 *            日期
	 */
	public Record(String gameMode, String playerName, int score, Date date) {
		// this.rank = rank;
		this.gameMode = gameMode;
		this.playerName = playerName;
		this.score = score;
		this.date = date;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/** 本地文件字符串 */
	public String toLocalFileString() {
		String gameModeString = "unknownGameMode";
		String playerNameString = "unknownName";
		String dateString = "unknownDate";
		if (gameMode != null) {
			gameModeString = gameMode;
		}
		if (playerName != null) {
			playerNameString = playerName;
		}
		if (date != null) {
			dateString = getDateString();
		}
		return playerNameString + " " + gameModeString + " " + score + " "
				+ dateString;
	}

	/**
	 * 玩家名：<br>
	 * 游戏模式：<br>
	 * 分数：<br>
	 * 时间：<br>
	 * */
	@Override
	public String toString() {
		String gameModeString = "未知游戏模式";
		String playerNameString = "未知玩家名";
		String dateString = "未知日期";
		if (gameMode != null) {
			gameModeString = SettingManager.getChineseGameMode(gameMode);
		}
		if (playerName != null) {
			playerNameString = playerName;
		}
		if (date != null) {
			dateString = getDateString();
		}
		return playerNameString + " " + gameModeString + " " + score + " "
				+ dateString;
	}

	/**
	 * 玩家名：<br>
	 * 游戏模式：<br>
	 * 分数：<br>
	 * 时间：<br>
	 * */
	public String toString(boolean tag) {
		String gameModeString = "未知游戏模式";
		String playerNameString = "未知玩家名";
		String dateString = "未知日期";
		if (gameMode != null) {
			gameModeString = SettingManager.getChineseGameMode(gameMode);
		}
		if (playerName != null) {
			playerNameString = playerName;
		}
		if (date != null) {
			dateString = getDateString();
		}
		if (tag) {
			return "玩家名：" + playerNameString + " 游戏模式：" + gameModeString
					+ " 分数：" + score + " 时间：" + dateString;
		} else {
			return toString();
		}
	}

	public boolean hasSameName(Record score) {
		if (getPlayerName().equals(score.getPlayerName())) {
			return true;
		} else {
			return false;
		}
	}

	public void updateDate() {
		date = new Date();
	}

	public String getPlayerName() {
		return playerName;
	}

	public Date getDate() {
		return date;
	}

	public String getDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd,HH:mm:ss");
		return dateFormat.format(date);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * 1.若分数大，则大<br>
	 * 2.若分数小，则小<br>
	 * 3.若分数相等<br>
	 * (1)若日期小，则大<br>
	 * (2)若日期大，则小
	 * */
	@Override
	public int compareTo(Record record) {
		if (getScore() > record.getScore()) {// 分数大，返回1
			return 1;
		} else if (getScore() < record.getScore()) {// 分数小，返回-1
			return -1;
		} else {// 分数相等
			if (getDate().compareTo(record.getDate()) < 0) {// 日期早，返回1
				return 1;
			} else if (getDate().compareTo(record.getDate()) > 0) {// 日期晚，返回1
				return -1;
			} else {// 分数相等，日期相等
				return 0;
			}
		}
	}

	public String getGameMode() {
		return gameMode;
	}

}
