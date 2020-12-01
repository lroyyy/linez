package cell;

import java.util.Comparator;

public class CostComparator implements Comparator<Cell> {

	@Override
	public int compare(Cell cell1, Cell cell2) {
		if (cell1.getCostF() < cell2.getCostF()) {
			// System.out.println(cell1.getCostF() + "<" + cell2.getCostF());
			return -1;

		} else if (cell1.getCostF() > cell2.getCostF()) {
			// System.out.println(cell1.getCostF() + ">" + cell2.getCostF());
			return 1;
		} else {
			return 0;
		}
	}

}
