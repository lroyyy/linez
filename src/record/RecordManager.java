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
 * ��¼������
 * <p>
 * getModel ��ȡ����<br>
 * updateModel ���°���<br>
 * 
 * */
public class RecordManager {

	/** ���ظ߷ְ��ļ�·�� */
	private static String localRecordsPath = GeneralManager.dataPath;
	private static String localRecordFile = "record.txt";

	/**
	 * ��ȡ����
	 * <p>
	 * ��ȡgameModeģʽ��player�İ���
	 * 
	 * @return ��¼���ջ�ǰ���Ѿ�����߷֣�����null
	 * */
	public static Player getModel(Player player, String gameMode) {
		List<Record> records = getRecords();
		if (records == null || records.isEmpty()) {// ����Դ�����κμ�¼
			return null;
		}
		// ��ȡ������¼
		records = RecordManager.filterRecords(records, gameMode);
		if (records == null || records.isEmpty()) {// ɸѡ�����κμ�¼
			return null;
		}
		Record modelRecord = null;
		boolean findFlag = false;
		for (int i = 0; i < records.size(); i++) {
			Record tmpRecord = records.get(i);
			if (player.getRecord().compareTo(tmpRecord) >= 0) {// �ҵ���player��recordС��record
				if (i == 0) {// player��record������record�����ް���
					modelRecord = null;
				} else {// ��������Ϊ�պñ�player��record��һ���record
					Record aRecord = records.get(i - 1);
					List<Record> aRecords = findRecords(records, aRecord);
					modelRecord = getBestRecord(aRecords);
				}
				findFlag = true;
				break;
			}
		}
		if (!findFlag) {// û���ҵ���playerС�ģ�player��С��recordȡ��С��
			modelRecord = records.get(records.size() - 1);
		}
		if (modelRecord != null) {// �а���
			Player model = PlayerManager.getPlayer(modelRecord.getPlayerName());
			return model;
		} else {// �ް���
			return null;
		}
	}

	/** �������� */
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
	 * ���°���
	 * <p>
	 * 1.�ж��Ƿ������������<br>
	 * 2.����model<br>
	 * 3.����UI
	 * */
	public static boolean updateModel() {
		if (GeneralManager.getModel() != null) {// ���а���
			if (GeneralManager.getPresentScore() <= GeneralManager
					.getModelScore()) {// ����ǰ�а����ҵ�ǰ����С�ڰ�������������ʧ��
				return false;
			}
			// ����ǰ��ʾ��Ϣ
			if (GeneralManager.getPlayerName().equals(
					GeneralManager.getModelName())) {// ����ǰ�������Լ�
				if (!GeneralManager.isNoMoreModel()) {// ��������һ������
					MessageManager.addMessage("�㳬Խ�����ң�");
				} else {// û���¸�������
					return false;
				}
			} else {// ����ǰ���������Լ�
				MessageManager.addMessage("��ϲ�㳬Խ�� "
						+ GeneralManager.getModelName() + " ��");
			}
		}
		// ��ȡplayer����һ������
		Player model = getModel(GeneralManager.getPlayer(),
				SettingManager.gameMode.getValue());
		Debugger.out("model=" + model);
		// ���ð�������ʾ��Ϣ
		if (model == null) {
			GeneralManager.setNoMoreModel(true);
			GeneralManager.setModel(GeneralManager.getPlayer());
		} else {
			GeneralManager.setNoMoreModel(false);
			GeneralManager.setModel(model);
			if (GeneralManager.getModelName().equals(
					GeneralManager.getPlayerName())) {// �¸��������Լ�
				MessageManager.addMessage("�Լ��ļ�¼������ǰ���ܷ��ٴγ�Խ�����أ�");
			} else {// �¸����������Լ�
				MessageManager.addMessage("�µ������߳����ˣ�TA�� "
						+ GeneralManager.getModelName() + " ��");
			}
		}
		// ���½���
		UIManager.updateModelNameUI();
		UIManager.updateModelScoreUI();
		return true;
	}

	/**
	 * ɸѡָ��ģʽ�ļ�¼��
	 * 
	 * @param records
	 *            ɸѡǰ�ļ�¼��
	 * @param gameMode
	 *            ָ������Ϸģʽ
	 * @return ɸѡ��ļ�¼��
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
	 * ��ȡ��¼
	 * <p>
	 * ��ȡ�����ΪplayerName�������gameModeģʽ�µļ�¼
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

	/** ��ȡ��¼�� */
	@SuppressWarnings("unchecked")
	public static List<Record> getRecords() {
		RecordReader reader = new RecordReader();
//		RecordReader reader = new RecordReader(buffer);
		reader.execute();
		try {
			return (List<Record>) reader.get();
		} catch (Exception e) {
			Debugger.out("�ڻ�ȡ���м�¼ʱ���ִ�����Ϣ��" + e.getMessage());
			return null;
		}
	}

	/** �õ�ǰ��¼���¼�¼�� */
	public static boolean updateRecords() {
		return updateRecords(GeneralManager.getPlayer().getRecord());
	}

	/** �ǼǼ�¼����¼�� */
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
