package bomb;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import resource.Resource;
import cell.Cell;

public class BoomWorker extends SwingWorker {

	private Cell cell;
	private ImageIcon boomIcon;
	private ImageIcon emptyIcon;

	public BoomWorker(Cell cell) {
		this.cell = cell;
		emptyIcon = Resource.empty.getImageIcon("48px");
	}

	@Override
	protected Object doInBackground() throws Exception {
		for (int i = 0; i < 6; i++) {
			boomIcon = Resource.boom.getImageIcon("" + (i + 1));
			publish(boomIcon);
			// delay(50);
		}

		publish(emptyIcon);
		return null;
	}

	@Override
	protected void process(List chunks) {
		for (Object object : chunks) {
			cell.setIcon((ImageIcon) object);
		}
	}

}
