package resource;

import java.net.URL;

import javax.swing.ImageIcon;

import util.Debugger;

public class Resource {

	/** 名称 */
	private String name;
	/** 指数 */
	private int index;
	/** 类型 */
	private String type;
	/** 水球 */
	public static Resource aquaBall;
	/** 蓝球 */
	public static Resource blueBall;
	/** 黑球 */
	public static Resource blackBall;
	/** 彩球 */
	public static Resource colorBall;
	/** 绿球 */
	public static Resource greenBall;
	/** 灰球 */
	public static Resource greyBall;
	/** 粉球 */
	public static Resource pinkBall;
	/** 红球 */
	public static Resource redBall;
	/** 黄球 */
	public static Resource yellowBall;
	/** 炸弹 */
	public static Resource bomb;
	/** 被瞄准的炸弹 */
	public static Resource aimedBomb;
	/** 爆炸 */
	public static Resource boom;
	/** 空白 */
	public static Resource empty;
	/** 点击音效 */
	public static Resource clickSound;
	/** 移动音效 */
	public static Resource moveSound;
	/** 阻塞音效 */
	public static Resource blockSound;
	/** 连珠音效 */
	public static Resource linezSound;
	/** 庆祝音效 */
	public static Resource cheersSound;
	/** 爆炸音效 */
	public static Resource boomSound;
	/** 获得音效 */
	public static Resource gainSound;
	/** 背景音乐 */
	public static Resource BGM;

	public Resource(String type, String name) {
		this(type, name, -1);
	}

	public Resource(String type, String name, int index) {
		this.type = type;
		this.name = name;
		this.index = index;
	}

	public URL getURL() {
		return getURL("");
	}

	public URL getURL(String arg) {
		String indexString = "";
		String sizeString = "";
		String extension = "";
		// size/index
		if (arg.endsWith("px")) {
			sizeString = "_" + arg;
		} else if (!arg.equals("")) {
			indexString = "_" + arg;
		}
		// extension
		if (type.equals("graphics")) {
			extension = ".png";
		}
		if (type.equals("audio/sound")) {
			extension = ".wav";
		}
		if (type.equals("audio/BGM")) {
			extension = ".wav";
		}
		String path = "/" + type + "/" + name + indexString + sizeString
				+ extension;
		URL url = getClass().getResource(path);
		if (url == null) {
			Debugger.out("资源" + path + "无法识别。");
		}
		return url;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ImageIcon getImageIcon() {
		return getImageIcon("");
	}

	public ImageIcon getImageIcon(String arg) {
		if (getURL(arg) == null) {
			Debugger.out("无法找到该资源。");
		}
		return new ImageIcon(getURL(arg));
	}
}
