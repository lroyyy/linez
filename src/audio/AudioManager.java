package audio;

import resource.Resource;
import ui.FrameMain;
import ui.UIManager;
import util.Debugger;

public class AudioManager {
	/** 是否播放音效 */
	public static boolean canPlaySound;
	/** 是否播放背景音乐 */
	public static boolean canPlayBGM;
	/** 主窗体 */
	public static FrameMain frameMain;
	public static WavPlayer BGMPlayer;
	public static WavPlayer wavPlayer;

	public static void init() {
		Debugger.out("正在初始化音频管理器");
		frameMain = UIManager.getFrameMain();
		BGMPlayer = new WavPlayer();
		wavPlayer = new WavPlayer();
	}

	public static void setCanPlaySound(boolean b) {
		canPlaySound = b;
	}

	public static void setCanPlayBGM(boolean b) {
		canPlayBGM = b;
		if (b) {
			BGMPlayer.setURL(Resource.BGM.getURL());
			playBGM();
		} else {
			stopBGM();
		}
	}

	public static void playBGM() {
		Debugger.out("开始播放BGM");
		BGMPlayer.playMusic(true);
	}

	public static void stopBGM() {
		Debugger.out("停止播放BGM");
		if (BGMPlayer != null) {
			BGMPlayer.stopMusic();
		}
	}

	public static void playSound(Resource resource) {

	}
}
