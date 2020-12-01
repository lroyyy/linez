package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import record.Record;
import record.RecordManager;
import setting.SettingManager;

public class DialogRecords extends MyDialog {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnClear;
	private List<Record> records;
	private List<Record> classcicalRecords;
	private List<Record> enhancedRecords;
	protected FrameMain frameMain;
	private JButton btnAdd;
	private PanelScore panelScore;
	private JButton btnRefresh;

	public DialogRecords(FrameMain frameMain) {
		super(frameMain, "排行榜", 353, 371);
		updateRecords();
		initLayout();
		setListeners();
	}

	class PanelScore extends JPanel {
		private static final long serialVersionUID = 1L;
		private String title;
		private List<Record> records;
		private MyTable table;
		private JScrollPane scrollPane;

		public PanelScore(String title, List<Record> records) {
			super();
			this.title = title;
			this.records = records;
			initLayout();
		}

		public void initLayout() {
			setBorder(new TitledBorder(null, title, TitledBorder.CENTER,
					TitledBorder.TOP, null, null));
			setLayout(new BorderLayout(0, 0));
			{
				table = new MyTable();
				if (records != null) {
					table.addRows(records);
				}
			}
			{
				scrollPane = new JScrollPane(table);
				add(scrollPane);
			}
		}
	}

	private void updateRecords() {
		records = RecordManager.getRecords();
		classcicalRecords = RecordManager.filterRecords(records,
				SettingManager.CLASSICAL_MODE);
		enhancedRecords = RecordManager.filterRecords(records,
				SettingManager.ENHANCED_MODE);
	}

	public void initLayout() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		{
			JPanel panelCenter = new JPanel();
			// panelCenter.setLayout(new BoxLayout(target, axis));
			// panelCenter.setLayout(new GridLayout(4, 0, 0, 0));
			contentPane.add(panelCenter, BorderLayout.CENTER);
			{
				panelScore = new PanelScore("经典模式", classcicalRecords);
				panelCenter.add(panelScore);
			}
			{
				panelScore = new PanelScore("增强模式", enhancedRecords);
				panelCenter.add(panelScore);
			}
			panelCenter.setLayout(new GridLayout(2, 0, 0, 0));
		}
		{
			JPanel panelButtom = new JPanel();
			contentPane.add(panelButtom, BorderLayout.SOUTH);
			{
				btnAdd = new JButton("添加");
				// panelButtom.add(btnAdd);
			}
			{
				btnClear = new JButton("清空");
				// panelButtom.add(btnClear);
			}
			{
				btnRefresh = new JButton("刷新");
				panelButtom.add(btnRefresh);
			}
			{
				panelButtom.add(addBtnBack());
			}
		}

	}

	private void setListeners() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// String name = JOptionPane.showInputDialog(frameMain, "姓名",
				// "输入姓名", JOptionPane.INFORMATION_MESSAGE);
				// if (name == null) {
				// return;
				// }
				// if (name.equals("")) {
				// name = "匿名";
				// }
				// String scoreString = JOptionPane.showInputDialog(frameMain,
				// "分数", "输入分数", JOptionPane.INFORMATION_MESSAGE);
				// if (name == scoreString) {
				// return;
				// }
				// Record record = new Record(name,
				// Integer.parseInt(scoreString),
				// new Date());
				// RecordManager.updateRecords(record);
				// DialogRecords.this.dispose();
				// new DialogRecords(frameMain).setVisible(true);
			}
		});
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogRecords.this.dispose();
				new DialogRecords(frameMain).setVisible(true);
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "确定清空？", "清空排行榜",
						JOptionPane.OK_CANCEL_OPTION) != 0) {
					return;
				}
				File file = new File(RecordManager.getRecordsPath());
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(file, false));
					writer.write("");
					writer.close();
					DialogRecords.this.dispose();
					new DialogRecords(frameMain).setVisible(true);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "抱歉，清空失败。");
					e1.printStackTrace();
				}
			}
		});
	}
}
