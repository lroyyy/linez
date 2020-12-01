package element;

import util.Debugger;

public abstract class Manager {

	protected static String name;

	public static void init() {
		Debugger.out("正在初始化" + name);
	}

	public static void initFinished() {
		Debugger.out(name + "初始化完毕");
	}

	public static void setName(String name) {
		Manager.name = name;
	}
}
