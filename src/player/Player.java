package player;

import record.Record;
import setting.SettingManager;

/**
 * ���
 * */
public class Player {

	/** ���� */
	private String name;
	/** ��ǰ��¼ */
	private Record record;
	/** ����ģʽ��¼ */
	private Record classicalBestRecord;
	/** ��ǿģʽ��¼ */
	private Record enhancedBestRecord;

	// /** ������� */
	// private int movingCount = 0;
	// /** ��¼���� */
	// private int loginCount = 0;
	// /** ���������� */
	// private int linezCount = 0;
	// /** ��ɳɾ��� */
	// private int achievementCount = 0;

	public Player() {
	}

	/**
	 * ����һ���µ�Player
	 * 
	 * @param name
	 *            �����
	 * */
	public Player(String name) {
		this.name = name;
		initNewRecord();
	}

	public Player(String name, Record record) {
		this.name = name;
		this.record = record;
	}

	/** ��ʼ���µĵ�ǰ��¼ */
	public void initNewRecord() {
		setRecord(new Record(SettingManager.gameMode.getValue(), name, 0));
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Record getClassicalBestRecord() {
		if (classicalBestRecord != null) {
			return classicalBestRecord;
		} else {
			return new Record(SettingManager.CLASSICAL_MODE, name, 0);
		}
	}

	public void setClassicalBestRecord(Record classicalBestRecord) {
		this.classicalBestRecord = classicalBestRecord;
	}

	public Record getEnhancedBestRecord() {
		if (enhancedBestRecord != null) {
			return enhancedBestRecord;
		} else {
			return new Record(SettingManager.ENHANCED_MODE, name, 0);
		}
	}

	public void setEnhancedBestRecord(Record enhancedBestRecord) {
		this.enhancedBestRecord = enhancedBestRecord;
	}

	public Record getBestRecord(String gameMode) {
		if (gameMode.equals(SettingManager.CLASSICAL_MODE)) {
			return getClassicalBestRecord();
		}
		if (gameMode.equals(SettingManager.ENHANCED_MODE)) {
			return getEnhancedBestRecord();
		}
		return null;
	}

	public void setBestRecord(Record modelRecord, String gameMode) {
		if (gameMode.equals(SettingManager.CLASSICAL_MODE)) {
			setClassicalBestRecord(modelRecord);
		}
		if (gameMode.equals(SettingManager.ENHANCED_MODE)) {
			setEnhancedBestRecord(modelRecord);
		}
	}

	/**
	 * @param loginCount
	 * @param movingCount
	 * @param linezCount
	 * @param achievementCount
	 */
	// public void setCounts(int loginCount, int movingCount, int linezCount,
	// int achievementCount) {
	// this.loginCount = loginCount;
	// this.movingCount = movingCount;
	// this.linezCount = linezCount;
	// this.achievementCount = achievementCount;
	// }

	/**
	 * �������<br>
	 * ����ģʽ��¼ ��<br>
	 * ��ǿģʽ��¼��
	 * */
	@Override
	public String toString() {
		String classicalBestRecordString = "0";
		String enhancedBestRecordString = "0";
		if (classicalBestRecord != null) {
			classicalBestRecordString = String.valueOf(classicalBestRecord
					.getScore());
		}
		if (enhancedBestRecord != null) {
			enhancedBestRecordString = String.valueOf(enhancedBestRecord
					.getScore());
		}
		String str = "�����  " + name + " ����ģʽ��¼  " + classicalBestRecordString
				+ " ��ǿģʽ��¼  " + enhancedBestRecordString;
		return str;
	}

	/**
	 * �������<br>
	 * ����ģʽ��¼ ��<br>
	 * ��ǿģʽ��¼��
	 * */
	public String toStringList() {
		String classicalBestRecordString = "0";
		String enhancedBestRecordString = "0";
		if (classicalBestRecord != null) {
			classicalBestRecordString = String.valueOf(classicalBestRecord
					.getScore());
		}
		if (enhancedBestRecord != null) {
			enhancedBestRecordString = String.valueOf(enhancedBestRecord
					.getScore());
		}
		String str = "�����  " + name + "\n����ģʽ��¼  " + classicalBestRecordString
				+ "\n��ǿģʽ��¼  " + enhancedBestRecordString;
		return str;
	}

}
