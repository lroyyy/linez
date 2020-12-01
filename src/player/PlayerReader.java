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
 * 玩家读取器
 * <p>
 * 找到，返回player或players<br>
 * 未找到，返回null
 * */
public class PlayerReader extends SwingWorker {

	private String playerName;
	private ArrayList<Player> players;

	/**
	 * 构造PlayerReader
	 * <p>
	 * 获取所有player
	 * */
	public PlayerReader() {

	}

	/**
	 * 构造PlayerReader
	 * <p>
	 * 获取指定player
	 * */
	public PlayerReader(String playerName) {
		Debugger.out("PlayerReader读取" + playerName);
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
		// 未找到，返回null
		if (players.isEmpty()) {
			return null;
		}
		if (playerName == null) {// 不指定
			return players;
		} else {// 指定
			return players.get(0);
		}
	}

	@Override
	protected void done() {
	}

}
