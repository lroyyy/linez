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
	/** 配置文件路径 */
	private static String configFilePath = "./dat/config.ini";
	/** 配置文件项字符串 */
	private static String configFileSectionString = "Settings";
	/** 设置集 */
	public static List<Setting> settings;
	/** 列数 */
	public static int columnCount = 9;
	/** 最大列数 */
	public static final int maxColumnCount = 14;
	/** 最小列数 */
	public static int minColumnCount = 5;
	/** 行数 */
	public static int rowCount = 9;
	/** 最大行数 */
	public static final int maxRowCount = 14;
	/** 最小行数 */
	public static int minRowCount = 5;
	/** 下一组落子数 */
	public static int nextBallsCount = 3;
	/** 最小连珠长度 */
	public static int minLinezSize = 5;
	/** 单球分数 */
	public static int ballScore = 2;
	/** 球颜色数 */
	public static int ballColorsCount = 8;
	/** 彩球概率 */
	public static double probabilityOfColorfulBall = 0.02;
	/** 小球移动间隔时间 */
	public static Setting<Long> ballMovingInterval;
	public static ArrayList<Long> ballMovingIntervals;
	/** 小球移动间隔时间1 */
	public static final long BALL_MOVING_INTERVAL1 = 100;
	/** 小球移动间隔时间2 */
	public static final long BALL_MOVING_INTERVAL2 = 50;
	/** 小球移动间隔时间3 */
	public static final long BALL_MOVING_INTERVAL3 = 20;
	/** 游戏模式 */
	public static Setting<String> gameMode;
	public static ArrayList<String> gameModes;
	/** 经典模式 */
	public static final String CLASSICAL_MODE = "classical";
	/** 增强模式 */
	public static final String ENHANCED_MODE = "enhanced";
	/** 读取选项 */
	public static Setting<String> loadOption;
	public static ArrayList<String> loadOptions;
	/** 读取选项1：自动读取 */
	public static final String LOAD_OPTION1 = "auto";
	/** 读取选项2：询问 */
	public static final String LOAD_OPTION2 = "ask";
	/** 读取选项3：不读取 */
	public static final String LOAD_OPTION3 = "never";
	public static Setting<Boolean> colorStringVisible;
	public static Setting<String> autoLoginPlayerName;
	public static Setting<Boolean> sound;
	public static Setting<Boolean> bgm;
	/** 全局字体大小 */
//	public static Setting<Integer> globalFontSize;
//	public static ArrayList<Integer> globalFontSizes;
	public static final int GlOBAL_FONT_SIZE = 12;
//	public static final int globalFontSize1 = 12;
//	public static final int globalFontSize2 = 15;
//	public static final int globalFontSize3 = 18;

	public static void init() {
		Debugger.out("正在初始化游戏设置");
		// 初始化静态值
		initStaticValues();
		// 初始化设置
		initSettings();
		// 读取配置文件中的所有配置
		readConfigFile();
		// 更新UI
		updateUI();
	}

	/** 初始化静态值 */
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
	 * 初始化设置
	 * <p>
	 * 字体<br>
	 * 游戏模式<br>
	 * 颜色文本可视化<br>
	 * 游戏速度<br>
	 * 读取存档方式<br>
	 * 自动登录方式<br>
	 * 音效<br>
	 * 音乐
	 * */
	public static void initSettings() {
		// 初始化设置集
		if (settings == null) {
			settings = new ArrayList<Setting>();
		}
		// 字体
//		globalFontSize.addData(globalFontSizes, UIManager.getFrameMain()
//				.getBtnGroupFontSize());
//		settings.add(globalFontSize);
		// 游戏模式，默认增强模式
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
		// 颜色文本可视化，默认可视
		colorStringVisible = new Setting<Boolean>("colorStringVisible", false);
		colorStringVisible.addData(new Boolean(true), UIManager.getFrameMain()
				.getChckbxmntmColorString());
		settings.add(colorStringVisible);
		// 游戏速度，默认快速
		ballMovingInterval = new Setting<Long>("ballMovingInterval",
				BALL_MOVING_INTERVAL1);
		ballMovingInterval.addData(ballMovingIntervals, UIManager
				.getFrameMain().getBtnGroupBallMovingInterval());
		settings.add(ballMovingInterval);
		// 读取存档
		loadOption = new Setting<String>("loadOption", LOAD_OPTION2);
		loadOption.addData(loadOptions, UIManager.getFrameMain()
				.getBtnGroupLoadOption());
		settings.add(loadOption);
		// 自动登录，默认无
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
		// 音效，默认开
		sound = new Setting<Boolean>("sound", true) {
			@Override
			public void apply() {
				AudioManager.setCanPlaySound(value);
			}
		};
		sound.addData(true, UIManager.getFrameMain().getChckbxmntmSound());
		settings.add(sound);
		// 音乐，默认开
		bgm = new Setting<Boolean>("bgm", true) {
			@Override
			public void apply() {
				AudioManager.setCanPlayBGM(value);
			}
		};
		bgm.addData(true, UIManager.getFrameMain().getChckbxmntmBGM());
		settings.add(bgm);
	}

	/** 预初始化全局字体 */
	public static void preInitGlobalFont() {
		// 设置全局字体
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

	/** 应用所有设置 */
	public static void applyAllSettings() {
		for (Setting<?> setting : settings) {
			setting.apply();
		}
	}

	/** 读取配置文件中的所有配置 */
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
			return "经典模式";
		case 2:
			return "增强模式";
		}
		return null;
	}

	public static String getChineseGameMode(String gameMode) {
		switch (gameMode) {
		case CLASSICAL_MODE:
			return "经典模式";
		case ENHANCED_MODE:
			return "增强模式";
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
