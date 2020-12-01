package element;

import java.util.List;

import javax.swing.SwingWorker;

import resource.Resource;
import cell.Cell;

public class AnimationWorker extends SwingWorker<Object, Object> {

	protected Cell cell;
	private int[] indexs;
	private Resource resource;
	private boolean working = true;
	private long time;
	private boolean loop;

	public AnimationWorker(Cell cell, Resource resource, int[] indexs,
			int time, boolean loop) {
		this.cell = cell;
		this.resource = resource;
		this.indexs = indexs;
		this.time = time;
		this.loop = loop;
	}

	@Override
	protected Object doInBackground() throws Exception {
		while (working) {
			for (int index : indexs) {
				publish(index);
				delay(time);
			}
			if (!loop) {
				break;
			}
		}
		return null;
	}

	@Override
	protected void process(List<Object> chunks) {
		for (Object object : chunks) {
			if (!working) {
				return;
			}
			cell.setIcon(resource.getImageIcon(object.toString()));
		}
	}

	public void stop() {
		working = false;
	}

	protected void delay(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
