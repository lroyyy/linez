package bomb;

import java.awt.EventQueue;

import javax.swing.ImageIcon;

import cell.Cell;
import resource.Resource;
import thread.MyThread;

public class BombShiningThread extends MyThread {
	private volatile boolean isStop = false;
	private boolean needToFixIcon = true;
	private long time = 50;
	private ImageIcon bombIcon;

	private Cell cell;

	public BombShiningThread(Cell cell) {
		this.cell = cell;
		bombIcon = Resource.bomb.getImageIcon("48px");
	}

	public BombShiningThread(Cell cell, boolean needToFixIcon) {
		this.cell = cell;
		this.needToFixIcon = needToFixIcon;
	}

	@Override
	public void run() {
		while (!isStop) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					cell.setIcon(bombIcon);
				}
			});
			delay(time * 2);
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
					cell.setIcon(null);
				}
			});
		}
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public boolean isNeedToFixIcon() {
		return needToFixIcon;
	}

	public void setNeedToFixIcon(boolean needToFixIcon) {
		this.needToFixIcon = needToFixIcon;
	}
}
