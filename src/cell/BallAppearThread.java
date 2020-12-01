package cell;

import java.awt.EventQueue;

import resource.Color;
import thread.MyThread;
import util.Debugger;

public class BallAppearThread extends MyThread {

	private Cell cell;
	private long time = 50;

	public BallAppearThread(Cell cell) {
		this.cell = cell;
	}

	@Override
	public void run() {
		final Color color = cell.getColor();
		if (color == null) {
			Debugger.out("failed to appear:color==null");
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				cell.setIcon(color.getResource().getImageIcon("16px"));
			}
		});
		delay(time);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				cell.setIcon(color.getResource().getImageIcon("32px"));
			}
		});
		delay(time);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				cell.setIcon(color.getResource().getImageIcon("48px"));
			}
		});
	}
}
