package audio;

import resource.Resource;
import ui.FrameMain;
import ui.UIManager;
import util.Debugger;

public class AudioManager {
	/** �Ƿ񲥷���Ч */
	public static boolean canPlaySound;
	/** �Ƿ񲥷ű������� */
	public static boolean canPlayBGM;
	/** ������ */
	public static FrameMain frameMain;
	public static WavPlayer BGMPlayer;
	public static WavPlayer wavPlayer;

	public static void init() {
		Debugger.out("���ڳ�ʼ����Ƶ������");
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
		Debugger.out("��ʼ����BGM");
		BGMPlayer.playMusic(true);
	}

	public static void stopBGM() {
		Debugger.out("ֹͣ����BGM");
		if (BGMPlayer != null) {
			BGMPlayer.stopMusic();
		}
	}

	public static void playSound(Resource resource) {

	}
}
