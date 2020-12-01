package player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import record.Record;
import record.RecordManager;
import record.RecordReader;
import setting.SettingManager;
import util.Debugger;
import element.GeneralManager;

/**
 * ��Ҷ�ȡ��
 * <p>
 * �ҵ�������player��players<br>
 * δ�ҵ�������null
 * */
public class PlayerReader extends SwingWorker {

	private String playerName;
	private ArrayList<Player> players;

	/**
	 * ����PlayerReader
	 * <p>
	 * ��ȡ����player
	 * */
	public PlayerReader() {

	}

	/**
	 * ����PlayerReader
	 * <p>
	 * ��ȡָ��player
	 * */
	public PlayerReader(String playerName) {
		Debugger.out("PlayerReader��ȡ" + playerName);
		this.playerName = playerName;
	}

	private boolean doOffline() {
		List<Record> records = RecordManager.getRecords();
		if (records == null || records.isEmpty()) {
			return false;
		}
		for (Record record : records) {
			Debugger.out("record=" + record.toString(true));
			if (playerName != null
					&& !record.getPlayerName().equals(playerName)) {
				continue;
			}
			Player player = new Player(record.getPlayerName());
			if (record.getGameMode().equals(SettingManager.CLASSICAL_MODE)) {
				player.setClassicalBestRecord(record);
			} else if (record.getGameMode().equals(SettingManager.ENHANCED_MODE)) {
				player.setEnhancedBestRecord(record);
			}
			players.add(player);
		}
		if (players.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected Object doInBackground() throws Exception {
		players = new ArrayList<Player>();
		doOffline();
		// δ�ҵ�������null
		if (players.isEmpty()) {
			return null;
		}
		if (playerName == null) {// ��ָ��
			return players;
		} else {// ָ��
			return players.get(0);
		}
	}

	@Override
	protected void done() {
	}

}
