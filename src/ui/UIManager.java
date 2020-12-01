package ui;

import element.GeneralManager;
import gamedata.GameData;

import javax.swing.border.TitledBorder;

import util.Debugger;
import cell.CellsContainer;

public class UIManager {
	/** ������ */
	private static FrameMain frameMain;
	/** �������� */
	private static CellsContainer cellsContainer;

	public static void showWelcomeUI() {
		DialogLogin dialogLogin = new DialogLogin();
		dialogLogin.setVisible(true);
	}

	/** ��ʼ������ */
	public static void initMainUI(GameData gameData) {
		Debugger.out("���ڳ�ʼ������");
		frameMain = new FrameMain(gameData);
		cellsContainer = frameMain.getCellsContainer();
	}

	public static void initUI() {
		initMainUI(null);
	}

	/** ��ʾ������ */
	public static void showMainUI() {
		Debugger.out("��ʾUI");
		frameMain.setVisible(true);
	}

	/** ���·�����صĽ��� */
	public static void updateScoreUI() {
		updatePlayerNameUI();
		GeneralManager.setPresentScore(GeneralManager.getPresentScore());
		updateModelNameUI();
		updateModelScoreUI();
	}

	/**
	 * ���������UI
	 * */
	public static void updatePlayerNameUI() {
		TitledBorder border = (TitledBorder) (frameMain.getPanelScore()
				.getBorder());
		border.setTitle(GeneralManager.getPlayerName());
		frameMain.getPanelScore().updateUI();
	}

	/**
	 * ���°�������
	 * */
	public static void updateModelNameUI() {
		TitledBorder border = (TitledBorder) (frameMain.getPanelGoal()
				.getBorder());
		border.setTitle(GeneralManager.getModelName());
		frameMain.getPanelGoal().updateUI();
	}

	/**
	 * ���°�������UI
	 * <p>
	 * ���а�����������������ļ�¼��<br>
	 * �����������ǰ����
	 * */
	public static void updateModelScoreUI() {
		// ��̬�ķֽ���
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
