package player;

import record.Record;
import setting.SettingManager;

/**
 * 玩家
 * */
public class Player {

	/** 名称 */
	private String name;
	/** 当前记录 */
	private Record record;
	/** 经典模式纪录 */
	private Record classicalBestRecord;
	/** 增强模式纪录 */
	private Record enhancedBestRecord;

	// /** 移球次数 */
	// private int movingCount = 0;
	// /** 登录次数 */
	// private int loginCount = 0;
	// /** 完成连珠次数 */
	// private int linezCount = 0;
	// /** 完成成就数 */
	// private int achievementCount = 0;

	public Player() {
	}

	/**
	 * 构造一个新的Player
	 * 
	 * @param name
	 *            玩家名
	 * */
	public Player(String name) {
		this.name = name;
		initNewRecord();
	}

	public Player(String name, Record record) {
		this.name = name;
		this.record = record;
	}

	/** 初始化新的当前记录 */
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
	 * 玩家名：<br>
	 * 经典模式纪录 ：<br>
	 * 增强模式纪录：
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
		String str = "玩家名  " + name + " 经典模式纪录  " + classicalBestRecordString
				+ " 增强模式纪录  " + enhancedBestRecordString;
		return str;
	}

	/**
	 * 玩家名：<br>
	 * 经典模式纪录 ：<br>
	 * 增强模式纪录：
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
		String str = "玩家名  " + name + "\n经典模式纪录  " + classicalBestRecordString
				+ "\n增强模式纪录  " + enhancedBestRecordString;
		return str;
	}

}
