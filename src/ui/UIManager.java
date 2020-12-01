package ui;

import element.GeneralManager;
import gamedata.GameData;

import javax.swing.border.TitledBorder;

import util.Debugger;
import cell.CellsContainer;

public class UIManager {
	/** 主窗体 */
	private static FrameMain frameMain;
	/** 格子容器 */
	private static CellsContainer cellsContainer;

	public static void showWelcomeUI() {
		DialogLogin dialogLogin = new DialogLogin();
		dialogLogin.setVisible(true);
	}

	/** 初始化界面 */
	public static void initMainUI(GameData gameData) {
		Debugger.out("正在初始化界面");
		frameMain = new FrameMain(gameData);
		cellsContainer = frameMain.getCellsContainer();
	}

	public static void initUI() {
		initMainUI(null);
	}

	/** 显示主界面 */
	public static void showMainUI() {
		Debugger.out("显示UI");
		frameMain.setVisible(true);
	}

	/** 更新分数相关的界面 */
	public static void updateScoreUI() {
		updatePlayerNameUI();
		GeneralManager.setPresentScore(GeneralManager.getPresentScore());
		updateModelNameUI();
		updateModelScoreUI();
	}

	/**
	 * 更新玩家名UI
	 * */
	public static void updatePlayerNameUI() {
		TitledBorder border = (TitledBorder) (frameMain.getPanelScore()
				.getBorder());
		border.setTitle(GeneralManager.getPlayerName());
		frameMain.getPanelScore().updateUI();
	}

	/**
	 * 更新榜样姓名
	 * */
	public static void updateModelNameUI() {
		TitledBorder border = (TitledBorder) (frameMain.getPanelGoal()
				.getBorder());
		border.setTitle(GeneralManager.getModelName());
		frameMain.getPanelGoal().updateUI();
	}

	/**
	 * 更新榜样分数UI
	 * <p>
	 * 若有榜样，则更新至榜样的纪录；<br>
	 * 否则更新至当前分数
	 * */
	public static void updateModelScoreUI() {
		// 动态改分进程
		int startValue;
		int score;
		if (GeneralManager.isNoMoreModel()) {
			score = GeneralManager.getPresentScore();
		} else {
			score = GeneralManager.getModelScore();
		}
		if (frameMain.getLblGoalScoreString().getText() == null
				|| frameMain.getLblGoalScoreString().getText().equals("")) {
			startValue = score;
		} else {
			startValue = Integer.parseInt(frameMain.getLblGoalScoreString()
					.getText().trim());
		}
		new ScoreUpdatingWorker(frameMain.getLblGoalScoreString(), startValue,
				score).execute();
	}

	public static void shakeUI() {
		FrameShaker frameShaker = new FrameShaker(frameMain);
		frameShaker.startShake();
	}

	public static FrameMain getFrameMain() {
		return frameMain;
	}

	public static CellsContainer getCellsContainer() {
		return cellsContainer;
	}
}
