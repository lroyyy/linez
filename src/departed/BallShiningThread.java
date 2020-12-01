package departed;

import java.awt.EventQueue;

import cell.Cell;
import thread.MyThread;

public class BallShiningThread extends MyThread {
	private volatile boolean isStop = false;
	private boolean needToFixIcon = true;
	// private ImageIcon ballIcon;
	private long time = 50;
	private Cell cell;

	// private Color color;

	public BallShiningThread(Cell cell) {
		this.cell = cell;
		// final Color color = cell.getColor();
		// if (color != null) {
		// ballIcon = color.getResource().getImageIcon("48px");
		// }
	}

	public BallShiningThread(Cell cell, boolean needToFixIcon) {
		this(cell);
		this.needToFixIcon = needToFixIcon;
	}

	@Override
	public void run() {
		// if (color == null) {
		// Debugger.out("failed to shining:color==null");
		// return;
		// }
		while (!isStop) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					cell.setBallIcon();
				}
			});
			delay(time * 5);
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					cell.setIcon(null);
				}
			});
			delay(time);
		}
		if (isNeedToFixIcon()) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					cell.setBallIcon();
				}
			});
		}
	}

	public boolean isNeedToFixIcon() {
		return needToFixIcon;
	}

	public void setNeedToFixIcon(boolean needToFixIcon) {
		this.needToFixIcon = needToFixIcon;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
}
