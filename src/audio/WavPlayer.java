package audio;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import resource.Resource;

/**
 * wav播放器(java.applet.Applet.clip)
 * <p>
 * 通常是播放sound(音效)
 */
public class WavPlayer extends Thread {
	private AudioClip clip;
	private long delayTime;

	public WavPlayer() {
	}

	/** 创建读取指定URL的WavPlayer */
	public WavPlayer(Resource resource) {
		this(resource.getURL());
	}

	/** 创建读取指定URL的WavPlayer */
	public WavPlayer(URL url) {
		this(url, 0);
	}

	/** 创建读取指定URL的WavPlayer,播放延迟时间为ms */
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

	/** 播放音乐 */
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

	/** 播放音乐 */
	public void playMusic() {
		playMusic(false);
	}

	/** 暂停音乐 */
	public void stopMusic() {
		if (AudioManager.canPlaySound && clip != null) {
			clip.stop();
		}
	}

}