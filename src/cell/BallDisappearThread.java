package cell;

import java.awt.EventQueue;

import resource.Color;
import thread.MyThread;
import util.Debugger;

public class BallDisappearThread extends MyThread {

	private Cell cell;
	private long time = 50;

	public BallDisappearThread(Cell cell) {
		this.cell = cell;
	}

	@Override
	public void run() {
		final Color color = cell.getColor();
		if (color == null) {
			Debugger.out("failed to disappear:color==null");
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				cell.setIcon(color.getResource().getImageIcon("48px"));
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
				cell.setIcon(color.getResource().getImageIcon("16px"));
			}
		});
		delay(time);
		cell.setColor(null);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				cell.setIcon(null);
			}
		});
	}
}
