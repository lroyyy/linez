package element;

import gamedata.GameData;
import gamedata.GameDataManager;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import message.MessageManager;
import player.Player;
import player.PlayerManager;
import record.RecordManager;
import resource.Color;
import resource.ResourceManager;
import setting.SettingManager;
import ui.ScoreUpdatingWorker;
import ui.UIManager;
import util.Debugger;
import audio.AudioManager;
import bomb.BombManager;
import cell.Cell;

/**
 * 综合管理器
 * <p>
 * 游戏流程实现
 * <p>
 * 1.0.0.20151125<br>第一版
 * */
public class GeneralManager {
	/** 下一组颜色 */
	private static List<Color> nextColors;
	/** 默认玩家名称 */
	public static String defaultPlayerName = "挑战者";
	/** 玩家 */
	private static Player player;
	/** 榜样 */
	private static Player model;
	/** 开关：已锁定 */
	public static boolean isLocked;
	/** 开关：没有目标 */
	private static boolean noMoreModel;
	/** 开关：游戏已结束 */
	public static boolean gameOver;
	/** 开关：在线 */
	public static boolean online;
	/** 开关：原始的游戏 */
	public static boolean originalGame = true;
	/** 第一滴血 */
	public static boolean firstBlood = true;
	/** 连击数 */
	private static int comboCount = 0;
	/** 数据路径 */
	public static String dataPath = "./dat/";
	/** 开关：已超越自己 */
	public static boolean surpassOneself = false;

	/** Hello World */
	public static void main(String[] args) {
		ResourceManager.initResources();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd,HH:mm:ss");
					Debugger.out("运行linez，系统时间："
							+ dateFormat.format(new Date()));
					init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** 初始化 */
	public static void init() {
		init(null);
	}

	/** 初始化 */
	public static void init(GameData gameData) {
		// 重置游戏参数
		resetArgs();
		// 初始化全局字体（必须在所有界面初始化之前）
		SettingManager.preInitGlobalFont();
		// 显示开始界面
		// UIManager.showWelcomeUI();
		// 初始化界面（基本布局）
		UIManager.initMainUI(gameData);
		// 初始化游戏设置
		SettingManager.init();
		// 初始化玩家管理器
		PlayerManager.init();
		// 初始化音频管理器
		AudioManager.init();
		// 初始化消息管理器
		MessageManager.init();
		// 更新设置UI
		SettingManager.updateUI();
		// 应用设置
		SettingManager.applyAllSettings();
		// 显示界面
		UIManager.showMainUI();
		// 登录
		if (!PlayerManager.loginSelfAdaptively(gameData)) {
			Debugger.out("登录失败！");
		}
		// 读取游戏存档
		Debugger.out("准备读档...");
		if (originalGame) {
			if (GameDataManager.loadGameByOption(SettingManager.gameMode
					.getValue())) {
				return;
			}
			Debugger.out("跳过读档。");
		}
		// 初始化炸弹管理器
		BombManager.init(gameData);
		// 初始化下一组颜色
		if (gameData != null) {// 读取了存档，指定颜色
			initNextColors(gameData.getNextColors());
			UIManager.getFrameMain()
					.setNextBtns(GeneralManager.getNextColors());
		} else {// 新游戏，随机颜色
			initNextColors();
			// 生成球
			generateBalls();
		}
		// 清除临时存档数据
		if (gameData != null) {
			gameData = null;
		}
		// 更新榜样
		RecordManager.updateModel();
		// 更新分数相关界面
		UIManager.updateScoreUI();
		Debugger.out("游戏初始化完毕。");
	}

	/**
	 * （重新）开始游戏
	 * <p>
	 * 源生新游戏
	 * */
	public static void restart(boolean notice) {
		restart(notice, null);
	}

	/**
	 * （重新）开始游戏
	 * 
	 * @param noticeFlag
	 *            是否提示确认
	 * @param gameData
	 *            载入的数据，若值为null表示开始新游戏
	 */
	public static void restart(boolean noticeFlag, GameData gameData) {
		int select = 1;
		if (noticeFlag) {
			select = JOptionPane.showConfirmDialog(null, "确定重开一局?", "提示",
					JOptionPane.OK_CANCEL_OPTION);
		}
		if (select == 0 || !noticeFlag) {// 用户确定或无需提示
			if (UIManager.getCellsContainer() != null) {
				Debugger.out("准备重新开始游戏...");
				UIManager.getFrameMain().dispose(false, false);
				originalGame = false;
				// PlayerManager.setPreLoginPlayerName(player.getName());
				MessageManager.addSeparator();
				Debugger.out("当前游戏已关闭，开始新游戏。");
				init(gameData);
			}
		}
	}

	/** 重置参数 */
	public static void resetArgs() {
		Debugger.out("正在重置参数...");
		model = null;
		player = null;
		isLocked = false;
		setNoMoreModel(false);
		gameOver = false;
		BombManager.bombActive = false;
		surpassOneself = false;
		firstBlood = true;
		noMoreModel = false;
		resetComboCount();
	}

	/** 初始化下一组颜色 */
	public static void initNextColors() {
		initNextColors(null);
	}

	/** 初始化下一组颜色 */
	public static void initNextColors(List<Color> colors) {
		if (colors != null) {
			nextColors = colors;
			return;
		}
		Random randomColorIndex = new Random();
		nextColors = new ArrayList<Color>();
		for (int i = 0; i < SettingManager.nextBallsCount; i++) {
			int tempColorIndex;
			if (Math.random() < SettingManager.probabilityOfColorfulBall) {
				tempColorIndex = 0;
			} else {
				tempColorIndex = randomColorIndex
						.nextInt(SettingManager.ballColorsCount) + 1;
			}
			nextColors.add(Color.values()[tempColorIndex]);
		}
	}

	/** 获取榜样 */
	public static Player getModel() {
		return model;
	}

	/** 获取榜样分数 */
	public static int getModelScore() {
		return model.getBestRecord(SettingManager.gameMode.getValue())
				.getScore();
	}

	/**
	 * 设置当前分数
	 * <p>
	 * 1.更新player<br>
	 * 2.更新UI
	 * */
	public static void setPresentScore(int score) {
		player.getRecord().setScore(score);
		// 动态改分进程
		int startValue;
		if (UIManager.getFrameMain().getLblPresentRecordString().getText() == null
				|| UIManager.getFrameMain().getLblPresentRecordString()
						.getText().equals("")) {
			startValue = score;
		} else {
			startValue = Integer.parseInt(UIManager.getFrameMain()
					.getLblPresentRecordString().getText().trim());
		}
		new ScoreUpdatingWorker(UIManager.getFrameMain()
				.getLblPresentRecordString(), startValue, score).execute();
	}

	/**
	 * 加分
	 * <p>
	 * 在当前分数上加分
	 * 
	 * @param score
	 *            加的分
	 * */
	public static void addScore(int score) {
		int prescore = Integer.parseInt(UIManager.getFrameMain()
				.getLblPresentRecordString().getText().trim());
		setPresentScore(prescore + score);
		if (isNoMoreModel()) {// 若没有目标，右侧分数将与左侧同步
			UIManager.updateModelScoreUI();
		}
		// 尝试获取炸弹
		if (BombManager.canGainBomb()) {
			BombManager.BombCountUp();
		}
	}

	/** 获取当前分数 */
	public static int getPresentScore() {
		if (player.getRecord() != null) {
			return player.getRecord().getScore();
		} else {
			return 0;
		}
	}

	/** 获取下一组颜色集 */
	public static List<Color> getNextColors() {
		if (nextColors == null || nextColors.isEmpty()) {
			Debugger.out("failed to getNextColors");
		}
		return nextColors;
	}

	/** 生成珠子 */
	public static boolean generateBalls() {
		// 放置球
		List<Cell> newBalls = UIManager.getFrameMain().getCellsContainer()
				.placeBalls(SettingManager.nextBallsCount);
		if (newBalls.size() < SettingManager.nextBallsCount) {// 格子不足，未全部落子
			return false;
		}
		// 查看放置的球是否刚好组成连珠，处理这些连珠
		if (UIManager.getFrameMain().getCellsContainer()
				.dealWithLinez(newBalls)) {
			// setMessage("真幸运！");
		}
		// 设置下次放置的颜色集
		initNextColors();
		// 设置提示按钮集
		UIManager.getFrameMain().setNextBtns(GeneralManager.getNextColors());
		return true;
	}

	/**
	 * 获取连珠分数
	 * 
	 * @param linezSize
	 *            连珠中珠子的个数
	 * */
	public int getLinezScore(int linezSize) {
		int score = 0;
		if (linezSize == SettingManager.minLinezSize) {
			score = SettingManager.minLinezSize * SettingManager.ballScore;
		}
		if (linezSize >= SettingManager.minLinezSize
				&& linezSize < SettingManager.minLinezSize + 2) {
			score = SettingManager.minLinezSize * SettingManager.ballScore
					+ (linezSize - SettingManager.minLinezSize);
		}
		if (linezSize >= SettingManager.minLinezSize + 2
				&& linezSize < SettingManager.minLinezSize + 4) {
			score = SettingManager.minLinezSize * SettingManager.ballScore
					+ (linezSize - SettingManager.minLinezSize) * 2;
		}
		return score;
	}

	/** 游戏结束 */
	public static void gameOver() {
		isLocked = true;
		if (BombManager.bombActive) {
			UIManager.getFrameMain().getButtonBomb().setAimed(false);
		}
		// 更新记录
		if (RecordManager.updateRecords()) {
			JOptionPane.showMessageDialog(UIManager.getFrameMain(),
					"恭喜你打破了自己的纪录！");
		}
	}

	/** 获取目标姓名 */
	public static String getModelName() {
		return model.getName();
	}

	/** 获取玩家名 */
	public static String getPlayerName() {
		return player.getName();
	}

	/** 设置榜样 */
	public static void setModel(Player player) {
		model = player;
	}

	/**
	 * 设置player
	 * <p>
	 * 直接指定player对象
	 * */
	public static void setPlayer(Player player) {
		Debugger.out("设置player:" + player);
		GeneralManager.player = player;
	}

	public static boolean isGameOver() {
		return gameOver;
	}

	public static void setGameOver(boolean gameOver) {
		GeneralManager.gameOver = gameOver;
		if (gameOver) {
			gameOver();
		}
	}

	public static Player getPlayer() {
		return player;
	}

	public static void showPlayerMessage() {
		JOptionPane.showMessageDialog(UIManager.getFrameMain(), getPlayer()
				.toStringList());
	}

	public static void showModelMessage() {
		JOptionPane.showMessageDialog(UIManager.getFrameMain(), getModel()
				.toStringList());
	}

	public static void addComboCount(int count) {
		Debugger.out("combocount:" + comboCount + "->" + (comboCount + count));
		comboCount += count;
	}

	public static int getComboCount() {
		return comboCount;
	}

	public static void resetComboCount() {
		comboCount = 0;
	}

	public static boolean isNoMoreModel() {
		return noMoreModel;
	}

	public static void setNoMoreModel(boolean noMoreModel) {
		GeneralManager.noMoreModel = noMoreModel;
//		Debugger.out("setNoMoreModel", noMoreModel);
	}
}
