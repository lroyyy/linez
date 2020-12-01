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
 * ��¼��ȡ��
 * <p>
 * ��ȡ���������ļ�¼�����ؽ�����List&lt;Record>����<br>
 * ���޷��������ļ�¼������null
 * */
public class RecordReader extends SwingWorker {

	private String playerName;
	private String gameMode;
	private ArrayList<Record> records;

	public RecordReader() {
		super();
	}

	/** ��ȡ���м�¼���л���Ի��� */
//	public RecordReader(DialogBuffer dialogBuffer) {
//		super(dialogBuffer);
//	}

	/** ��ȡ�����ΪplayerName��ָ����Ϸģʽ�ļ�¼���޻���Ի��� */
	public RecordReader(String playerName, String gameMode) {
		this.playerName = playerName;
		this.gameMode = gameMode;
	}

	/** ��ȡ�����ΪplayerName��ָ����Ϸģʽ�ļ�¼���л���Ի��� */
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
			Debugger.out("��ȡ���м�¼ʱ���Ҳ����κμ�¼��");
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
			// �����ļ���ɸѡ��
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
			// ��ȡ�ļ���
			File[] files = null;
			if (playerName == null) {// δָ�������
				files = new File(GeneralManager.dataPath).listFiles(filter);
				if (files == null) {
					// �޼�¼
					return;
				}
				for (int i = 0; i < files.length; i++) {
					String tmpPath = files[i].getPath() + "\\"
							+ RecordManager.getRecordPath();
					files[i] = new File(tmpPath);
				}
			} else {// ָ�������
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
					Debugger.out(file.getAbsolutePath() + "Ϊ���ļ�");
					continue;
				} else {
					reader.reset();
				}
				while ((line = reader.readLine()) != null) {
					// content += line;
					String[] msgs = line.split(" ");
					// ����
					String tmpName = msgs[0];
					// ��Ϸģʽ
					String tmpGameMode = msgs[1];
					int tmpScoreInt = Integer.parseInt(msgs[2]);
					// ����
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd,HH:mm:ss");
					Date tmpDate = null;
					tmpDate = sdf.parse(msgs[3]);
					// ����record
					Record tmpRecord = new Record(tmpGameMode, tmpName,
							tmpScoreInt, tmpDate);
					records.add(tmpRecord);
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Debugger.out("��ȡ���ؼ�¼ʱ����" + e.getMessage());
		}
	}
}
