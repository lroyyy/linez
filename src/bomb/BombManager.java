package bomb;

import message.MessageManager;
import resource.Resource;
import setting.SettingManager;
import ui.UIManager;
import util.Debugger;
import audio.WavPlayer;
import element.GeneralManager;
import element.Manager;
import gamedata.GameData;

public class BombManager extends Manager {

	/** ը���� */
	public static int bombCount;
	/** Ĭ�ϳ�ʼը���� */
	public static int defaultBombCount = 1;
	/** ���ը��������� */
	public static int bombScore = 500;
	/** �ѻ��ը���� */
	public static int gainedBombCount;
	/** ���أ�ը����Ծ */
	public static boolean bombActive = false;

	/**
	 * ��ʼ��ը��������
	 * <p>
	 * 1.��ʼ���ѻ��ը����<br>
	 * 2.����UI
	 * */
	public static void init() {
		init(null);
	}

	public static void init(GameData gameData) {
		Debugger.out("���ڳ�ʼ��ը��������");
		if (SettingManager.gameMode.getValue().equals(
				SettingManager.CLASSICAL_MODE)) {
			return;
		}
		initGainedBombCount();
		if (gameData != null) {
			initBombCount(gameData.getBombCount());
		} else {
			initBombCount();
		}
	}

	public static void initBombCount() {
		setBombCount(defaultBombCount);
	}

	public static void initBombCount(int count) {
		setBombCount(count);
	}

	/** ��ʼ���ѻ��ը���� */
	public static void initGainedBombCount() {
		gainedBombCount = GeneralManager.getPresentScore() / bombScore;
	}

	public static boolean canGainBomb() {
		if (gainedBombCount < GeneralManager.getPresentScore() / bombScore) {
			return true;
		} else {
			return false;
		}
	}

	public static void setBombCount(int count) {
		bombCount = count;
		UIManager.getFrameMain().getLblBombCount().setText("��" + count);
	}

	public static void BombCountUp() {
		setBombCount(++bombCount);
		WavPlayer.playSound(Resource.gainSound);
		MessageManager.addMessage("��ϲ������һöը����");
		Debugger.out("ը��+1");
	}

	public static void BombCountDown() {
		if (bombCount >= 1) {
			setBombCount(--bombCount);
			MessageManager.addMessage("��ɹ�ʹ����һöը����");
			Debugger.out("ը��-1");
		}
	}

	public static int getBombCount() {
		return bombCount;
	}
}
