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
 * �ۺϹ�����
 * <p>
 * ��Ϸ����ʵ��
 * <p>
 * 1.0.0.20151125<br>��һ��
 * */
public class GeneralManager {
	/** ��һ����ɫ */
	private static List<Color> nextColors;
	/** Ĭ��������� */
	public static String defaultPlayerName = "��ս��";
	/** ��� */
	private static Player player;
	/** ���� */
	private static Player model;
	/** ���أ������� */
	public static boolean isLocked;
	/** ���أ�û��Ŀ�� */
	private static boolean noMoreModel;
	/** ���أ���Ϸ�ѽ��� */
	public static boolean gameOver;
	/** ���أ����� */
	public static boolean online;
	/** ���أ�ԭʼ����Ϸ */
	public static boolean originalGame = true;
	/** ��һ��Ѫ */
	public static boolean firstBlood = true;
	/** ������ */
	private static int comboCount = 0;
	/** ����·�� */
	public static String dataPath = "./dat/";
	/** ���أ��ѳ�Խ�Լ� */
	public static boolean surpassOneself = false;

	/** Hello World */
	public static void main(String[] args) {
		ResourceManager.initResources();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd,HH:mm:ss");
					Debugger.out("����linez��ϵͳʱ�䣺"
							+ dateFormat.format(new Date()));
					init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** ��ʼ�� */
	public static void init() {
		init(null);
	}

	/** ��ʼ�� */
	public static void init(GameData gameData) {
		// ������Ϸ����
		resetArgs();
		// ��ʼ��ȫ�����壨���������н����ʼ��֮ǰ��
		SettingManager.preInitGlobalFont();
		// ��ʾ��ʼ����
		// UIManager.showWelcomeUI();
		// ��ʼ�����棨�������֣�
		UIManager.initMainUI(gameData);
		// ��ʼ����Ϸ����
		SettingManager.init();
		// ��ʼ����ҹ�����
		PlayerManager.init();
		// ��ʼ����Ƶ������
		AudioManager.init();
		// ��ʼ����Ϣ������
		MessageManager.init();
		// ��������UI
		SettingManager.updateUI();
		// Ӧ������
		SettingManager.applyAllSettings();
		// ��ʾ����
		UIManager.showMainUI();
		// ��¼
		if (!PlayerManager.loginSelfAdaptively(gameData)) {
			Debugger.out("��¼ʧ�ܣ�");
		}
		// ��ȡ��Ϸ�浵
		Debugger.out("׼������...");
		if (originalGame) {
			if (GameDataManager.loadGameByOption(SettingManager.gameMode
					.getValue())) {
				return;
			}
			Debugger.out("����������");
		}
		// ��ʼ��ը��������
		BombManager.init(gameData);
		// ��ʼ����һ����ɫ
		if (gameData != null) {// ��ȡ�˴浵��ָ����ɫ
			initNextColors(gameData.getNextColors());
			UIManager.getFrameMain()
					.setNextBtns(GeneralManager.getNextColors());
		} else {// ����Ϸ�������ɫ
			initNextColors();
			// ������
			generateBalls();
		}
		// �����ʱ�浵����
		if (gameData != null) {
			gameData = null;
		}
		// ���°���
		RecordManager.updateModel();
		// ���·�����ؽ���
		UIManager.updateScoreUI();
		Debugger.out("��Ϸ��ʼ����ϡ�");
	}

	/**
	 * �����£���ʼ��Ϸ
	 * <p>
	 * Դ������Ϸ
	 * */
	public static void restart(boolean notice) {
		restart(notice, null);
	}

	/**
	 * �����£���ʼ��Ϸ
	 * 
	 * @param noticeFlag
	 *            �Ƿ���ʾȷ��
	 * @param gameData
	 *            ��������ݣ���ֵΪnull��ʾ��ʼ����Ϸ
	 */
	public static void restart(boolean noticeFlag, GameData gameData) {
		int select = 1;
		if (noticeFlag) {
			select = JOptionPane.showConfirmDialog(null, "ȷ���ؿ�һ��?", "��ʾ",
					JOptionPane.OK_CANCEL_OPTION);
		}
		if (select == 0 || !noticeFlag) {// �û�ȷ����������ʾ
			if (UIManager.getCellsContainer() != null) {
				Debugger.out("׼�����¿�ʼ��Ϸ...");
				UIManager.getFrameMain().dispose(false, false);
				originalGame = false;
				// PlayerManager.setPreLoginPlayerName(player.getName());
				MessageManager.addSeparator();
				Debugger.out("��ǰ��Ϸ�ѹرգ���ʼ����Ϸ��");
				init(gameData);
			}
		}
	}

	/** ���ò��� */
	public static void resetArgs() {
		Debugger.out("�������ò���...");
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

	/** ��ʼ����һ����ɫ */
	public static void initNextColors() {
		initNextColors(null);
	}

	/** ��ʼ����һ����ɫ */
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

	/** ��ȡ���� */
	public static Player getModel() {
		return model;
	}

	/** ��ȡ�������� */
	public static int getModelScore() {
		return model.getBestRecord(SettingManager.gameMode.getValue())
				.getScore();
	}

	/**
	 * ���õ�ǰ����
	 * <p>
	 * 1.����player<br>
	 * 2.����UI
	 * */
	public static void setPresentScore(int score) {
		player.getRecord().setScore(score);
		// ��̬�ķֽ���
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
	 * �ӷ�
	 * <p>
	 * �ڵ�ǰ�����ϼӷ�
	 * 
	 * @param score
	 *            �ӵķ�
	 * */
	public static void addScore(int score) {
		int prescore = Integer.parseInt(UIManager.getFrameMain()
				.getLblPresentRecordString().getText().trim());
		setPresentScore(prescore + score);
		if (isNoMoreModel()) {// ��û��Ŀ�꣬�Ҳ�����������ͬ��
			UIManager.updateModelScoreUI();
		}
		// ���Ի�ȡը��
		if (BombManager.canGainBomb()) {
			BombManager.BombCountUp();
		}
	}

	/** ��ȡ��ǰ���� */
	public static int getPresentScore() {
		if (player.getRecord() != null) {
			return player.getRecord().getScore();
		} else {
			return 0;
		}
	}

	/** ��ȡ��һ����ɫ�� */
	public static List<Color> getNextColors() {
		if (nextColors == null || nextColors.isEmpty()) {
			Debugger.out("failed to getNextColors");
		}
		return nextColors;
	}

	/** �������� */
	public static boolean generateBalls() {
		// ������
		List<Cell> newBalls = UIManager.getFrameMain().getCellsContainer()
				.placeBalls(SettingManager.nextBallsCount);
		if (newBalls.size() < SettingManager.nextBallsCount) {// ���Ӳ��㣬δȫ������
			return false;
		}
		// �鿴���õ����Ƿ�պ�������飬������Щ����
		if (UIManager.getFrameMain().getCellsContainer()
				.dealWithLinez(newBalls)) {
			// setMessage("�����ˣ�");
		}
		// �����´η��õ���ɫ��
		initNextColors();
		// ������ʾ��ť��
		UIManager.getFrameMain().setNextBtns(GeneralManager.getNextColors());
		return true;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param linezSize
	 *            ���������ӵĸ���
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

	/** ��Ϸ���� */
	public static void gameOver() {
		isLocked = true;
		if (BombManager.bombActive) {
			UIManager.getFrameMain().getButtonBomb().setAimed(false);
		}
		// ���¼�¼
		if (RecordManager.updateRecords()) {
			JOptionPane.showMessageDialog(UIManager.getFrameMain(),
					"��ϲ��������Լ��ļ�¼��");
		}
	}

	/** ��ȡĿ������ */
	public static String getModelName() {
		return model.getName();
	}

	/** ��ȡ����� */
	public static String getPlayerName() {
		return player.getName();
	}

	/** ���ð��� */
	public static void setModel(Player player) {
		model = player;
	}

	/**
	 * ����player
	 * <p>
	 * ֱ��ָ��player����
	 * */
	public static void setPlayer(Player player) {
		Debugger.out("����player:" + player);
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
