package audio;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import resource.Resource;

/**
 * wav������(java.applet.Applet.clip)
 * <p>
 * ͨ���ǲ���sound(��Ч)
 */
public class WavPlayer extends Thread {
	private AudioClip clip;
	private long delayTime;

	public WavPlayer() {
	}

	/** ������ȡָ��URL��WavPlayer */
	public WavPlayer(Resource resource) {
		this(resource.getURL());
	}

	/** ������ȡָ��URL��WavPlayer */
	public WavPlayer(URL url) {
		this(url, 0);
	}

	/** ������ȡָ��URL��WavPlayer,�����ӳ�ʱ��Ϊms */
	public WavPlayer(URL url, long ms) {
		if (AudioManager.canPlaySound)
			clip = Applet.newAudioClip(url);
	}

	public static void playSound(Resource resource) {
		WavPlayer wavPlayer = new WavPlayer(resource);
		wavPlayer.playMusic();
	}

	public void setURL(URL url) {
		if (AudioManager.canPlaySound)
			clip = Applet.newAudioClip(url);
	}

	@Override
	public void run() {
		try {
			sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		playMusic();
	}

	/** �������� */
	public boolean playMusic(boolean loopFlag) {
		if (!AudioManager.canPlaySound) {
			return false;
		}
		if (loopFlag) {
			clip.loop();
		} else {
			clip.play();
		}
		return true;
	}

	/** �������� */
	public void playMusic() {
		playMusic(false);
	}

	/** ��ͣ���� */
	public void stopMusic() {
		if (AudioManager.canPlaySound && clip != null) {
			clip.stop();
		}
	}

}