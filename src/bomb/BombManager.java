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

	/** 炸弹数 */
	public static int bombCount;
	/** 默认初始炸弹数 */
	public static int defaultBombCount = 1;
	/** 获得炸弹所需分数 */
	public static int bombScore = 500;
	/** 已获得炸弹数 */
	public static int gainedBombCount;
	/** 开关：炸弹活跃 */
	public static boolean bombActive = false;

	/**
	 * 初始化炸弹管理器
	 * <p>
	 * 1.初始化已获得炸弹数<br>
	 * 2.更新UI
	 * */
	public static void init() {
		init(null);
	}

	public static void init(GameData gameData) {
		Debugger.out("正在初始化炸弹管理器");
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

	/** 初始化已获得炸弹数 */
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
		UIManager.getFrameMain().getLblBombCount().setText("×" + count);
	}

	public static void BombCountUp() {
		setBombCount(++bombCount);
		WavPlayer.playSound(Resource.gainSound);
		MessageManager.addMessage("恭喜你获得了一枚炸弹！");
		Debugger.out("炸弹+1");
	}

	public static void BombCountDown() {
		if (bombCount >= 1) {
			setBombCount(--bombCount);
			MessageManager.addMessage("你成功使用了一枚炸弹。");
			Debugger.out("炸弹-1");
		}
	}

	public static int getBombCount() {
		return bombCount;
	}
}
