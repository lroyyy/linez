package thread;

public class MyThread extends Thread {

	// public MyThread(String str) {
	//
	// }

	protected void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
