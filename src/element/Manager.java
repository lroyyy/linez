package element;

import util.Debugger;

public abstract class Manager {

	protected static String name;

	public static void init() {
		Debugger.out("���ڳ�ʼ��" + name);
	}

	public static void initFinished() {
		Debugger.out(name + "��ʼ�����");
	}

	public static void setName(String name) {
		Manager.name = name;
	}
}
