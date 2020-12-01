package resource;

import java.net.URL;

import javax.swing.ImageIcon;

import util.Debugger;

public class Resource {

	/** ���� */
	private String name;
	/** ָ�� */
	private int index;
	/** ���� */
	private String type;
	/** ˮ�� */
	public static Resource aquaBall;
	/** ���� */
	public static Resource blueBall;
	/** ���� */
	public static Resource blackBall;
	/** ���� */
	public static Resource colorBall;
	/** ���� */
	public static Resource greenBall;
	/** ���� */
	public static Resource greyBall;
	/** ���� */
	public static Resource pinkBall;
	/** ���� */
	public static Resource redBall;
	/** ���� */
	public static Resource yellowBall;
	/** ը�� */
	public static Resource bomb;
	/** ����׼��ը�� */
	public static Resource aimedBomb;
	/** ��ը */
	public static Resource boom;
	/** �հ� */
	public static Resource empty;
	/** �����Ч */
	public static Resource clickSound;
	/** �ƶ���Ч */
	public static Resource moveSound;
	/** ������Ч */
	public static Resource blockSound;
	/** ������Ч */
	public static Resource linezSound;
	/** ��ף��Ч */
	public static Resource cheersSound;
	/** ��ը��Ч */
	public static Resource boomSound;
	/** �����Ч */
	public static Resource gainSound;
	/** �������� */
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
			Debugger.out("��Դ" + path + "�޷�ʶ��");
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
			Debugger.out("�޷��ҵ�����Դ��");
		}
		return new ImageIcon(getURL(arg));
	}
}
