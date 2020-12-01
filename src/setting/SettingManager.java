package setting;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.plaf.FontUIResource;

import ui.UIManager;
import util.Debugger;
import audio.AudioManager;

@SuppressWarnings("rawtypes")
public class SettingManager {
	/** �����ļ�·�� */
	private static String configFilePath = "./dat/config.ini";
	/** �����ļ����ַ��� */
	private static String configFileSectionString = "Settings";
	/** ���ü� */
	public static List<Setting> settings;
	/** ���� */
	public static int columnCount = 9;
	/** ������� */
	public static final int maxColumnCount = 14;
	/** ��С���� */
	public static int minColumnCount = 5;
	/** ���� */
	public static int rowCount = 9;
	/** ������� */
	public static final int maxRowCount = 14;
	/** ��С���� */
	public static int minRowCount = 5;
	/** ��һ�������� */
	public static int nextBallsCount = 3;
	/** ��С���鳤�� */
	public static int minLinezSize = 5;
	/** ������� */
	public static int ballScore = 2;
	/** ����ɫ�� */
	public static int ballColorsCount = 8;
	/** ������� */
	public static double probabilityOfColorfulBall = 0.02;
	/** С���ƶ����ʱ�� */
	public static Setting<Long> ballMovingInterval;
	public static ArrayList<Long> ballMovingIntervals;
	/** С���ƶ����ʱ��1 */
	public static final long BALL_MOVING_INTERVAL1 = 100;
	/** С���ƶ����ʱ��2 */
	public static final long BALL_MOVING_INTERVAL2 = 50;
	/** С���ƶ����ʱ��3 */
	public static final long BALL_MOVING_INTERVAL3 = 20;
	/** ��Ϸģʽ */
	public static Setting<String> gameMode;
	public static ArrayList<String> gameModes;
	/** ����ģʽ */
	public static final String CLASSICAL_MODE = "classical";
	/** ��ǿģʽ */
	public static final String ENHANCED_MODE = "enhanced";
	/** ��ȡѡ�� */
	public static Setting<String> loadOption;
	public static ArrayList<String> loadOptions;
	/** ��ȡѡ��1���Զ���ȡ */
	public static final String LOAD_OPTION1 = "auto";
	/** ��ȡѡ��2��ѯ�� */
	public static final String LOAD_OPTION2 = "ask";
	/** ��ȡѡ��3������ȡ */
	public static final String LOAD_OPTION3 = "never";
	public static Setting<Boolean> colorStringVisible;
	public static Setting<String> autoLoginPlayerName;
	public static Setting<Boolean> sound;
	public static Setting<Boolean> bgm;
	/** ȫ�������С */
//	public static Setting<Integer> globalFontSize;
//	public static ArrayList<Integer> globalFontSizes;
	public static final int GlOBAL_FONT_SIZE = 12;
//	public static final int globalFontSize1 = 12;
//	public static final int globalFontSize2 = 15;
//	public static final int globalFontSize3 = 18;

	public static void init() {
		Debugger.out("���ڳ�ʼ����Ϸ����");
		// ��ʼ����ֵ̬
		initStaticValues();
		// ��ʼ������
		initSettings();
		// ��ȡ�����ļ��е���������
		readConfigFile();
		// ����UI
		updateUI();
	}

	/** ��ʼ����ֵ̬ */
	private static void initStaticValues() {
		ballMovingIntervals = new ArrayList<Long>();
		ballMovingIntervals.add(BALL_MOVING_INTERVAL1);
		ballMovingIntervals.add(BALL_MOVING_INTERVAL2);
		ballMovingIntervals.add(BALL_MOVING_INTERVAL3);
		gameModes = new ArrayList<String>();
		gameModes.add(CLASSICAL_MODE);
		gameModes.add(ENHANCED_MODE);
		loadOptions = new ArrayList<String>();
		loadOptions.add(LOAD_OPTION1);
		loadOptions.add(LOAD_OPTION2);
		loadOptions.add(LOAD_OPTION3);
//		globalFontSizes = new ArrayList<Integer>();
//		globalFontSizes.add(globalFontSize1);
//		globalFontSizes.add(globalFontSize2);
//		globalFontSizes.add(globalFontSize3);
	}

	/**
	 * ��ʼ������
	 * <p>
	 * ����<br>
	 * ��Ϸģʽ<br>
	 * ��ɫ�ı����ӻ�<br>
	 * ��Ϸ�ٶ�<br>
	 * ��ȡ�浵��ʽ<br>
	 * �Զ���¼��ʽ<br>
	 * ��Ч<br>
	 * ����
	 * */
	public static void initSettings() {
		// ��ʼ�����ü�
		if (settings == null) {
			settings = new ArrayList<Setting>();
		}
		// ����
//		globalFontSize.addData(globalFontSizes, UIManager.getFrameMain()
//				.getBtnGroupFontSize());
//		settings.add(globalFontSize);
		// ��Ϸģʽ��Ĭ����ǿģʽ
		gameMode = new Setting<String>("gameMode", ENHANCED_MODE) {
			@Override
			public boolean updateUI() {
				if (value.equals(SettingManager.ENHANCED_MODE)) {
					UIManager.getFrameMain().addPanelBomb();
				}
				return super.updateUI();
			}
		};
		gameMode.addData(gameModes, UIManager.getFrameMain()
				.getBtnGroupGameMode());
		settings.add(gameMode);
		// ��ɫ�ı����ӻ���Ĭ�Ͽ���
		colorStringVisible = new Setting<Boolean>("colorStringVisible", false);
		colorStringVisible.addData(new Boolean(true), UIManager.getFrameMain()
				.getChckbxmntmColorString());
		settings.add(colorStringVisible);
		// ��Ϸ�ٶȣ�Ĭ�Ͽ���
		ballMovingInterval = new Setting<Long>("ballMovingInterval",
				BALL_MOVING_INTERVAL1);
		ballMovingInterval.addData(ballMovingIntervals, UIManager
				.getFrameMain().getBtnGroupBallMovingInterval());
		settings.add(ballMovingInterval);
		// ��ȡ�浵
		loadOption = new Setting<String>("loadOption", LOAD_OPTION2);
		loadOption.addData(loadOptions, UIManager.getFrameMain()
				.getBtnGroupLoadOption());
		settings.add(loadOption);
		// �Զ���¼��Ĭ����
		autoLoginPlayerName = new Setting<String>("autoLoginPlayerName", "") {
			@Override
			public boolean updateUI() {
				if (value.equals("")) {
					return false;
				}
				UIManager.getFrameMain().getChckbxmntmAutoLogin()
						.setSelected(true);
				return true;
			}
		};
		settings.add(autoLoginPlayerName);
		// ��Ч��Ĭ�Ͽ�
		sound = new Setting<Boolean>("sound", true) {
			@Override
			public void apply() {
				AudioManager.setCanPlaySound(value);
			}
		};
		sound.addData(true, UIManager.getFrameMain().getChckbxmntmSound());
		settings.add(sound);
		// ���֣�Ĭ�Ͽ�
		bgm = new Setting<Boolean>("bgm", true) {
			@Override
			public void apply() {
				AudioManager.setCanPlayBGM(value);
			}
		};
		bgm.addData(true, UIManager.getFrameMain().getChckbxmntmBGM());
		settings.add(bgm);
	}

	/** Ԥ��ʼ��ȫ������ */
	public static void preInitGlobalFont() {
		// ����ȫ������
		FontUIResource fontRes = new FontUIResource(new Font("Dialog",
				Font.PLAIN, GlOBAL_FONT_SIZE));
		for (Enumeration<Object> keys = javax.swing.UIManager
				.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = javax.swing.UIManager.get(key);
			if (value instanceof FontUIResource)
				javax.swing.UIManager.put(key, fontRes);
		}
	}

	/** Ӧ���������� */
	public static void applyAllSettings() {
		for (Setting<?> setting : settings) {
			setting.apply();
		}
	}

	/** ��ȡ�����ļ��е��������� */
	public static void readConfigFile() {
		for (Setting<?> setting : settings) {
			setting.readConfigFile();
		}
	}

	public static void updateUI() {
		for (Setting<?> setting : settings) {
			setting.updateUI();
		}
	}

	public static String getChineseGameMode(int index) {
		switch (index) {
		case 1:
			return "����ģʽ";
		case 2:
			return "��ǿģʽ";
		}
		return null;
	}

	public static String getChineseGameMode(String gameMode) {
		switch (gameMode) {
		case CLASSICAL_MODE:
			return "����ģʽ";
		case ENHANCED_MODE:
			return "��ǿģʽ";
		}
		return null;
	}

	public static String getConfigFilePath() {
		return configFilePath;
	}

	public static int getCellsCount() {
		return columnCount * rowCount;
	}

	public static String getConfigFileSectionString() {
		return configFileSectionString;
	}
}
