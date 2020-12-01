package record;

import util.Debugger;
import util.MyUtils;
import util.TxtProcesser;

import javax.swing.SwingWorker;

import element.GeneralManager;

public class RecordWriter extends SwingWorker {

	private Record newRecord;
	private String SQLString;
	private Record oldRecord;

	public RecordWriter(Record record) {
		this.newRecord = record;
	}

	protected Object doInBackground() throws Exception {
		if (newRecord.getScore() == 0) {// 0�ֲ��Ǽ�
			return false;
		}
		// ��ȡ�ɼ�¼
		RecordReader reader = new RecordReader(newRecord.getPlayerName(),
				newRecord.getGameMode());
		reader.execute();
		oldRecord = (Record) reader.get();
		// ִ��д����
			return doOffline();
		
	}

	private boolean doOffline() {
		MyUtils.initFile(RecordManager.getRecordPath(newRecord.getPlayerName()));
		if (oldRecord == null) {
			Debugger.out("��ʼ�ڱ���������¼");
			return TxtProcesser.writeLine(
					RecordManager.getRecordPath(newRecord.getPlayerName()),
					newRecord.toLocalFileString());
		} else if (newRecord.compareTo(oldRecord) > 1) {
			Debugger.out("��ʼ�ڱ��ظ��¼�¼");
			return TxtProcesser.updateLine(
					RecordManager.getRecordPath(newRecord.getPlayerName()),
					oldRecord.toLocalFileString(),
					newRecord.toLocalFileString());
		} else {
			return false;
		}
	}

	private void setSQLUpdatingString() {
		SQLString = "update record set r_score=" + newRecord.getScore()
				+ ",r_date='" + newRecord.getDateString()
				+ "' where r_playerName='" + newRecord.getPlayerName() + "'";
	}

	private void setSQLInsertingLString() {
		SQLString = "insert into record values ('" + newRecord.getGameMode()
				+ "','" + newRecord.getPlayerName() + "',"
				+ newRecord.getScore() + ",'" + newRecord.getDateString()
				+ "')";
	}
}
