package ui;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class ScoreUpdatingWorker extends SwingWorker<Boolean, String> {

	private JLabel label;
	private int startValue;
	private int endValue;
	private long time;
	private static final long time1 = 5;
	private static final long time2 = 50;
	private static final long time3 = 100;

	public ScoreUpdatingWorker(JLabel label, int startValue, int endValue) {
		this.label = label;
		this.startValue = startValue;
		this.endValue = endValue;
		if (Math.abs(startValue - endValue) > 99) {// 100~
			time = time1;
		} else if (Math.abs(startValue - endValue) > 9) {// 10~99
			time = time2;
		} else {// 0~9
			time = time3;
		}
	}

	// @Override
	// public void run() {
	// int tmpValue = startValue;
	// // 初始>目标
	// while (tmpValue != endValue) {
	// delay(time);
	// if (tmpValue > endValue) {
	// tmpValue--;
	// } else if (tmpValue < endValue) {
	// tmpValue++;
	// }
	// String str = String.valueOf(tmpValue);
	// int length = String.valueOf(tmpValue).length();
	// for (int i = 0; i < 10 - length; i++) {
	// str = "  " + str;
	// }
	// final String formattedStr = str;
	// EventQueue.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// label.setText(formattedStr);
	// }
	// });
	// }
	// }

	@Override
	protected Boolean doInBackground() throws Exception {
		if (startValue == endValue) {
			return false;
		}
		int tmpValue = startValue;
		// 初始>目标
		while (tmpValue != endValue) {
			delay(time);
			if (tmpValue > endValue) {
				tmpValue--;
			} else if (tmpValue < endValue) {
				tmpValue++;
			}
			String str = String.valueOf(tmpValue);
			int length = String.valueOf(tmpValue).length();
			for (int i = 0; i < 10 - length; i++) {
				str = "  " + str;
			}
			String formattedStr = str;
			publish(formattedStr);
			// EventQueue.invokeLater(new Runnable() {
			// @Override
			// public void run() {
			// label.setText(formattedStr);
			// }
			// });
		}
		return true;
	}

	@Override
	protected void process(List<String> chunks) {
		for (String str : chunks) {
			label.setText(str);
		}
	}

	private void delay(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
