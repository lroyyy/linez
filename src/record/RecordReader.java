package record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.SwingWorker;

import ui.DialogBuffer;
import util.Debugger;
import element.GeneralManager;

/**
 * 记录读取器
 * <p>
 * 读取符合条件的记录，返回降序后的List&lt;Record>对象<br>
 * 若无符合条件的记录，返回null
 * */
public class RecordReader extends SwingWorker {

	private String playerName;
	private String gameMode;
	private ArrayList<Record> records;

	public RecordReader() {
		super();
	}

	/** 读取所有记录，有缓冲对话框 */
//	public RecordReader(DialogBuffer dialogBuffer) {
//		super(dialogBuffer);
//	}

	/** 读取玩家名为playerName的指定游戏模式的记录，无缓冲对话框 */
	public RecordReader(String playerName, String gameMode) {
		this.playerName = playerName;
		this.gameMode = gameMode;
	}

	/** 读取玩家名为playerName的指定游戏模式的记录，有缓冲对话框 */
//	public RecordReader(DialogBuffer dialogBuffer, String playerName,
//			String gameMode) {
//		this(dialogBuffer);
//		this.playerName = playerName;
//		this.gameMode = gameMode;
//	}

	protected Object doInBackground() throws Exception {
		records = new ArrayList<Record>();
			doOffline();
		if (records.isEmpty()) {
			Debugger.out("获取所有记录时，找不到任何记录。");
			return null;
		}
		RecordManager.sort(records);
		if (playerName == null) {
			return records;
		} else {
			return records.get(0);
		}
	}

	private void doOffline() {
		try {
			// 创建文件夹筛选器
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.isDirectory()) {
						return true;
					} else {
						return false;
					}
				}
			};
			// 读取文件夹
			File[] files = null;
			if (playerName == null) {// 未指定玩家名
				files = new File(GeneralManager.dataPath).listFiles(filter);
				if (files == null) {
					// 无记录
					return;
				}
				for (int i = 0; i < files.length; i++) {
					String tmpPath = files[i].getPath() + "\\"
							+ RecordManager.getRecordPath();
					files[i] = new File(tmpPath);
				}
			} else {// 指定玩家名
				File file = new File(RecordManager.getRecordPath(playerName));
				files = new File[1];
				files[0] = file;
			}
			for (File file : files) {
				if (file == null || !file.exists()) {
					continue;
				}
				String line = "null";
				BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(file));
				reader.mark(1000);
				// String content = "";
				if ((line = reader.readLine()) == null) {
					Debugger.out(file.getAbsolutePath() + "为空文件");
					continue;
				} else {
					reader.reset();
				}
				while ((line = reader.readLine()) != null) {
					// content += line;
					String[] msgs = line.split(" ");
					// 姓名
					String tmpName = msgs[0];
					// 游戏模式
					String tmpGameMode = msgs[1];
					int tmpScoreInt = Integer.parseInt(msgs[2]);
					// 日期
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd,HH:mm:ss");
					Date tmpDate = null;
					tmpDate = sdf.parse(msgs[3]);
					// 构造record
					Record tmpRecord = new Record(tmpGameMode, tmpName,
							tmpScoreInt, tmpDate);
					records.add(tmpRecord);
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Debugger.out("读取本地记录时出错：" + e.getMessage());
		}
	}
}
