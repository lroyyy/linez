package record;

import java.text.SimpleDateFormat;
import java.util.Date;

import setting.SettingManager;

/**
 * ��¼
 * <p>
 * ��Ϸģʽ<br>
 * �����<br>
 * ����<br>
 * ʱ��
 * 
 * @author zhengfei.fjhg
 * 
 */
public class Record implements Comparable<Record> {

	// /**���*/
	// private int id;
	/** ��Ϸģʽ */
	private String gameMode;
	/** ����� */
	private String playerName;
	/** ���� */
	private Date date;
	private int score;

	public Record() {
		// this(GameManager.getPlayer().getName(),
		// GameManager.getPresentScore());
	}

	/**
	 * @param playerName
	 *            �������
	 * @param score
	 *            ����
	 */
	public Record(String gameMode, String playerName, int score) {
		this(gameMode, playerName, score, new Date());
	}

	/**
	 * @param playerName
	 *            �������
	 * @param score
	 *            ����
	 * @param date
	 *            ����
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

	/** �����ļ��ַ��� */
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
	 * �������<br>
	 * ��Ϸģʽ��<br>
	 * ������<br>
	 * ʱ�䣺<br>
	 * */
	@Override
	public String toString() {
		String gameModeString = "δ֪��Ϸģʽ";
		String playerNameString = "δ֪�����";
		String dateString = "δ֪����";
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
	 * �������<br>
	 * ��Ϸģʽ��<br>
	 * ������<br>
	 * ʱ�䣺<br>
	 * */
	public String toString(boolean tag) {
		String gameModeString = "δ֪��Ϸģʽ";
		String playerNameString = "δ֪�����";
		String dateString = "δ֪����";
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
			return "�������" + playerNameString + " ��Ϸģʽ��" + gameModeString
					+ " ������" + score + " ʱ�䣺" + dateString;
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
	 * 1.�����������<br>
	 * 2.������С����С<br>
	 * 3.���������<br>
	 * (1)������С�����<br>
	 * (2)�����ڴ���С
	 * */
	@Override
	public int compareTo(Record record) {
		if (getScore() > record.getScore()) {// �����󣬷���1
			return 1;
		} else if (getScore() < record.getScore()) {// ����С������-1
			return -1;
		} else {// �������
			if (getDate().compareTo(record.getDate()) < 0) {// �����磬����1
				return 1;
			} else if (getDate().compareTo(record.getDate()) > 0) {// ����������1
				return -1;
			} else {// ������ȣ��������
				return 0;
			}
		}
	}

	public String getGameMode() {
		return gameMode;
	}

}
