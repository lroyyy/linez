package resource;

import util.Debugger;

public class ResourceManager {

	/** ��ʼ����Դ */
	public static void initResources() {
		Debugger.out("���ڳ�ʼ����Դ");
		// ͼ��
		Resource.aquaBall = new Resource("graphics", "aqua_ball");
		Resource.blueBall = new Resource("graphics", "blue_ball");
		Resource.blackBall = new Resource("graphics", "black_ball");
		Resource.colorBall = new Resource("graphics", "color_ball");
		Resource.greenBall = new Resource("graphics", "green_ball");
		Resource.greyBall = new Resource("graphics", "grey_ball");
		Resource.pinkBall = new Resource("graphics", "pink_ball");
		Resource.redBall = new Resource("graphics", "red_ball");
		Resource.yellowBall = new Resource("graphics", "yellow_ball");
		Resource.bomb = new Resource("graphics", "bomb");
		Resource.aimedBomb = new Resource("graphics", "aimedBomb");
		Resource.boom = new Resource("graphics", "boom");
		Resource.empty = new Resource("graphics", "empty");
		// ��Ч
		Resource.clickSound = new Resource("audio/sound", "click2");
		Resource.moveSound = new Resource("audio/sound", "qiu");
		Resource.blockSound = new Resource("audio/sound", "funny1");
		Resource.cheersSound = new Resource("audio/sound", "cheers");
		Resource.linezSound = new Resource("audio/sound", "linez");
		Resource.boomSound = new Resource("audio/sound", "boom");
		Resource.gainSound = new Resource("audio/sound", "gain");
		// ����
		Resource.BGM = new Resource("audio/BGM", "bgm_piano");
		// Debugger.out("��Դ��ʼ�����");
	}
}
