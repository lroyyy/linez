/**
 * 
 */
package ui;

import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import record.Record;

/**
 * @author zhengfei.fjhg
 * 
 */
public class MyTable extends JTable {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel tableModel;

	public MyTable() {
		// 行不可选
		setRowSelectionAllowed(false);
		// 单元格不可编辑
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// 设置表头
		tableModel
				.setColumnIdentifiers(new String[] { "排名", "姓名", "分数", "达成时间" });
		setModel(tableModel);
		// 设置各列宽
		getColumnModel().getColumn(0).setPreferredWidth(5);
		getColumnModel().getColumn(1).setPreferredWidth(60);
		getColumnModel().getColumn(2).setPreferredWidth(30);
		getColumnModel().getColumn(3).setPreferredWidth(100);
		// 单元格居中
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		setDefaultRenderer(Object.class, renderer);
	}

	public void addRows(List<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			Record score = records.get(i);
			Vector<String> v = new Vector<String>();
			v.add(Integer.toString(i + 1));
			v.add(score.getPlayerName());
			v.add(Integer.toString(score.getScore()));
			v.add(score.getDateString());
			tableModel.addRow(v);
		}
	}
}
