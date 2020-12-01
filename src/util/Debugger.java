package util;

public class Debugger {

	static boolean isAvailable = true;

	public static void out(String msg, Object o) {
		if (!isAvailable) {
			return;
		}

		String value = o.toString();
		if (o instanceof Boolean || o instanceof Integer || o instanceof Double) {
			value = String.valueOf(o);
		}
		System.out.println("Debug:[" + msg + "]ֵΪ" + value);
	}

	public static void out(String msg) {
		if (!isAvailable) {
			return;
		}
		System.out.println("Debug:[" + msg + "]");
	}
}
