package record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import message.MessageManager;
import player.Player;
import player.PlayerManager;
import resource.Resource;
import setting.SettingManager;
import ui.DialogBuffer;
import ui.UIManager;
import util.Debugger;
import audio.WavPlayer;
import element.GeneralManager;

/**
 * 记录管理器
 * <p>
 * getModel 获取榜样<br>
 * updateModel 更新榜样<br>
 * 
 * */
public class RecordManager {

	/** 本地高分榜文件路径 */
	private static String localRecordsPath = GeneralManager.dataPath;
	private static String localRecordFile = "record.txt";

	/**
	 * 获取榜样
	 * <p>
	 * 获取gameMode模式下player的榜样
	 * 
	 * @return 记录集空或当前分已经是最高分，返回null
	 * */
	public static Player getModel(Player player, String gameMode) {
		List<Record> records = getRecords();
		if (records == null || records.isEmpty()) {// 数据源中无任何记录
			return null;
		}
		// 获取榜样记录
		records = RecordManager.filterRecords(records, gameMode);
		if (records == null || records.isEmpty()) {// 筛选后无任何记录
			return null;
		}
		Record modelRecord = null;
		boolean findFlag = false;
		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (player.getRecord().compareTo(tmpRecord) >= 0) {// 找到比player的record小的record
				if (i == 0) {// player的record比最大的record还大，无榜样
					modelRecord = null;
				} else {// 榜样设置为刚好比player的record大一点的record
					Record aRecord = records.get(i - 1);
					List<Record> aRecords = findRecords(records, aRecord);
					modelRecord = getBestRecord(aRecords);
				}
				findFlag = true;
				break;
			}
		}
		if (!findFlag) {// 没有找到比player小的，player最小，record取最小的
			modelRecord = records.get(records.size() - 1);
		}
		if (modelRecord != null) {// 有榜样
			Player model = PlayerManager.getPlayer(modelRecord.getPlayerName());
			return model;
		} else {// 无榜样
			return null;
		}
	}

	/** 降序排列 */
	public static List<Record> sort(List<Record> records) {
		Collections.sort(records, new RecordComparator());
		return records;
	}

	public static List<Record> findRecords(List<Record> records, Record record) {
		List<Record> result = new ArrayList<Record>();
		for (Record tmpRecord : records) {
			if (tmpRecord.getScore() == record.getScore()) {
				Debugger.out("same:" + tmpRecord);
				result.add(tmpRecord);
			}
		}
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public static Record getBestRecord(List<Record> records) {
		sort(records);
		return records.get(0);
	}

	/**
	 * 更新榜样
	 * <p>
	 * 1.判断是否满足更新条件<br>
	 * 2.更新model<br>
	 * 3.更新UI
	 * */
	public static boolean updateModel() {
		if (GeneralManager.getModel() != null) {// 若有榜样
			if (GeneralManager.getPresentScore() <= GeneralManager
					.getModelScore()) {// 若当前有榜样且当前分数小于榜样分数，更新失败
				return false;
			}
			// 更新前提示信息
			if (GeneralManager.getPlayerName().equals(
					GeneralManager.getModelName())) {// 若当前榜样是自己
				if (!GeneralManager.isNoMoreModel()) {// 若仍有下一个榜样
					MessageManager.addMessage("你超越了自我！");
				} else {// 没有下个榜样了
					return false;
				}
			} else {// 若当前榜样不是自己
				MessageManager.addMessage("恭喜你超越了 "
						+ GeneralManager.getModelName() + " ！");
			}
		}
		// 获取player的下一个榜样
		Player model = getModel(GeneralManager.getPlayer(),
				SettingManager.gameMode.getValue());
		Debugger.out("model=" + model);
		// 设置榜样，显示消息
		if (model == null) {
			GeneralManager.setNoMoreModel(true);
			GeneralManager.setModel(GeneralManager.getPlayer());
		} else {
			GeneralManager.setNoMoreModel(false);
			GeneralManager.setModel(model);
			if (GeneralManager.getModelName().equals(
					GeneralManager.getPlayerName())) {// 下个榜样是自己
				MessageManager.addMessage("自己的纪录就在眼前，能否再次超越自我呢？");
			} else {// 下个榜样不是自己
				MessageManager.addMessage("新的守擂者出现了，TA是 "
						+ GeneralManager.getModelName() + " 。");
			}
		}
		// 更新界面
		UIManager.updateModelNameUI();
		UIManager.updateModelScoreUI();
		return true;
	}

	/**
	 * 筛选指定模式的记录集
	 * 
	 * @param records
	 *            筛选前的记录集
	 * @param gameMode
	 *            指定的游戏模式
	 * @return 筛选后的记录集
	 * */
	public static List<Record> filterRecords(List<Record> records,
			String gameMode) {
		if (records == null) {
			return null;
		}
		if (records.isEmpty()) {
			return records;
		}
		List<Record> newRecords = new ArrayList<Record>();
		for (int i = 0; i < records.size(); i++) {
			Record record = records.get(i);
			if (record.getGameMode().equals(gameMode)) {
				newRecords.add(record);
			}
		}
		sort(newRecords);
		return newRecords;
	}

	/**
	 * 获取记录
	 * <p>
	 * 获取玩家名为playerName的玩家在gameMode模式下的记录
	 * */
	public static Record getRecord(String playerName, String gameMode) {
		RecordReader reader = new RecordReader(playerName, gameMode);
		reader.execute();
		try {
			return (Record) reader.get();
		} catch (Exception e) {
			return null;
		}
	}

	/** 获取记录集 */
	@SuppressWarnings("unchecked")
	public static List<Record> getRecords() {
		RecordReader reader = new RecordReader();
//		RecordReader reader = new RecordReader(buffer);
		reader.execute();
		try {
			return (List<Record>) reader.get();
		} catch (Exception e) {
			Debugger.out("在获取所有记录时出现错误，信息：" + e.getMessage());
			return null;
		}
	}

	/** 用当前记录更新记录集 */
	public static boolean updateRecords() {
		return updateRecords(GeneralManager.getPlayer().getRecord());
	}

	/** 登记记录到记录集 */
	public static boolean updateRecords(Record newRecord) {
		RecordWriter recordWriter = new RecordWriter(newRecord);
		recordWriter.execute();
		boolean success = false;
		try {
			success = (boolean) recordWriter.get();
		} catch (Exception e) {
			success = false;
		}
		if (success) {
			WavPlayer.playSound(Resource.cheersSound);

		}
		return success;
	}

	public static String getRecordsPath() {
		return localRecordsPath;
	}

	public static String getRecordPath(String playerName) {
		return localRecordsPath + playerName + "/" + localRecordFile;
	}

	public static String getRecordPath() {
		return localRecordFile;
	}
}
