package record;

import java.util.Comparator;

/**
 * @author zhengfei.fjhg
 *         <p>
 *         ����˳�򣺽���
 */
public class RecordComparator implements Comparator<Record> {
	@Override
	public int compare(Record r1, Record r2) {
		return r1.compareTo(r2) * -1;
	}

}
