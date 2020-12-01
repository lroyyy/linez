package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import message.MessageManager;
import record.Record;
import setting.SettingManager;
import bomb.BombManager;
import cell.CellsContainer;
import element.GeneralManager;
import gamedata.GameData;
import gamedata.GameDataManager;

public class DialogGameData extends MyDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private FrameMain frameMain;
	private JButton btnOverrideClassical;
	private JButton btnDeleteClassical;
	private JButton btnOverrideEnhanced;
	private JButton btnDeleteEnhanced;
	private JTextArea txtrClasscial;
	private JTextArea txtrEnhanced;
	private JButton btnLoadClassical;
	private JButton btnLoadEnhanced;
	private JTextArea txtrPresent;
	private JPanel panelClasscial;
	private JPanel panelPresent;
	private JPanel panelEnhanced;
	private JPanel panelCancel;

	public DialogGameData(FrameMain frameMain) {
		super(frameMain, "游戏存档管理", 390 + SettingManager.GlOBAL_FONT_SIZE * 16,
				225 + SettingManager.GlOBAL_FONT_SIZE * 10);
		initLayout();
		setListeners();
		showPresentGameData();
		showClassicalGameData();
		showEnhancedGameData();
	}

	public void initLayout() {
		getContentPane().setLayout(new BorderLayout());
		{
			JLabel lblPlayerName = new JLabel("玩家名：" + GeneralManager.getPlayerName());
			lblPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(lblPlayerName, BorderLayout.NORTH);
		}
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 3, 0, 0));
		{
			panelPresent = new JPanel();
			panelPresent.setBorder(new TitledBorder(null, "当前游戏数据", TitledBorder.CENTER, TitledBorder.TOP, null, null));
			contentPanel.add(panelPresent);
			panelPresent.setLayout(new BorderLayout(0, 0));
			{
				txtrPresent = new JTextArea();
				txtrPresent.setEditable(false);
				txtrPresent.setLineWrap(true);
				panelPresent.add(txtrPresent, BorderLayout.NORTH);
			}
		}
		{
			panelClasscial = new JPanel();
			panelClasscial
					.setBorder(new TitledBorder(null, "经典模式存档", TitledBorder.CENTER, TitledBorder.TOP, null, null));
			contentPanel.add(panelClasscial);
			panelClasscial.setLayout(new BorderLayout(5, 5));
			{
				JPanel panelClasscialBtns = new JPanel();
				panelClasscial.add(panelClasscialBtns, BorderLayout.SOUTH);
				panelClasscialBtns.setLayout(new GridLayout(0, 3, 0, 0));
				{
					btnOverrideClassical = new JButton("保存");
					btnOverrideClassical.setMargin(new Insets(1, 1, 1, 1));
					btnOverrideClassical.setEnabled(false);
					panelClasscialBtns.add(btnOverrideClassical);

				}
				{
					btnLoadClassical = new JButton("读取");
					btnLoadClassical.setMargin(new Insets(1, 1, 1, 1));
					btnLoadClassical.setEnabled(false);
					panelClasscialBtns.add(btnLoadClassical);
				}
				{
					btnDeleteClassical = new JButton("删除");
					btnDeleteClassical.setMargin(new Insets(1, 1, 1, 1));
					btnDeleteClassical.setEnabled(false);
					panelClasscialBtns.add(btnDeleteClassical);
				}
			}
			{
				txtrClasscial = new JTextArea();
				txtrClasscial.setLineWrap(true);
				txtrClasscial.setEditable(false);
				panelClasscial.add(txtrClasscial, BorderLayout.NORTH);
			}
		}
		{
			panelEnhanced = new JPanel();
			panelEnhanced
					.setBorder(new TitledBorder(null, "增强模式存档", TitledBorder.CENTER, TitledBorder.TOP, null, null));
			contentPanel.add(panelEnhanced);
			panelEnhanced.setLayout(new BorderLayout(5, 5));
			{
				txtrEnhanced = new JTextArea();
				txtrEnhanced.setLineWrap(true);
				txtrEnhanced.setEditable(false);
				panelEnhanced.add(txtrEnhanced, BorderLayout.NORTH);
			}
			{
				JPanel panelEnhancedBtns = new JPanel();
				panelEnhanced.add(panelEnhancedBtns, BorderLayout.SOUTH);
				panelEnhancedBtns.setLayout(new GridLayout(0, 3, 0, 0));
				{
					btnOverrideEnhanced = new JButton("保存");
					btnOverrideEnhanced.setMargin(new Insets(1, 1, 1, 1));
					btnOverrideEnhanced.setEnabled(false);
					panelEnhancedBtns.add(btnOverrideEnhanced);
				}
				{
					btnLoadEnhanced = new JButton("读取");
					btnLoadEnhanced.setMargin(new Insets(1, 1, 1, 1));
					btnLoadEnhanced.setEnabled(false);
					panelEnhancedBtns.add(btnLoadEnhanced);
				}
				{
					btnDeleteEnhanced = new JButton("删除");
					btnDeleteEnhanced.setMargin(new Insets(1, 1, 1, 1));
					btnDeleteEnhanced.setEnabled(false);
					panelEnhancedBtns.add(btnDeleteEnhanced);
				}
			}
		}
		panelCancel = new JPanel();
		getContentPane().add(panelCancel, BorderLayout.SOUTH);
		{
			panelCancel.add(addBtnBack());
		}
	}

	private void setListeners() {
		btnOverrideClassical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		btnOverrideEnhanced.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		btnLoadClassical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadAction(SettingManager.CLASSICAL_MODE);
			}
		});
		btnLoadEnhanced.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadAction(SettingManager.ENHANCED_MODE);
			}
		});
		btnDeleteClassical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAction(SettingManager.CLASSICAL_MODE);
			}
		});
		btnDeleteEnhanced.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAction(SettingManager.ENHANCED_MODE);
			}
		});
	}

	private void saveAction() {
		if (GameDataManager.saveGame()) {
			MessageManager.addMessage("保存成功！");
			DialogGameData.this.dispose();
			new DialogGameData(frameMain).setVisible(true);
		} else {
			MessageManager.addMessage("保存失败！");
		}
	}

	private void loadAction(String gameMode) {
		if (!GameDataManager.loadGame(gameMode)) {
			MessageManager.addMessage("读取失败！");
		} else {
			DialogGameData.this.dispose();
		}
	}

	private void deleteAction(String gameMode) {
		if (GameDataManager.delete(gameMode)) {
			MessageManager.addMessage("删除成功！");
			DialogGameData.this.dispose();
			new DialogGameData(frameMain).setVisible(true);
		} else {
			MessageManager.addMessage("删除失败！");
		}
	}

	private void showPresentGameData() {
		String str = "游戏模式：" + SettingManager.getChineseGameMode(SettingManager.gameMode.getValue()) + "\n分数："
				+ GeneralManager.getPlayer().getRecord().getScore() + "\n雷数：";
		if (SettingManager.gameMode.getValue().equals(SettingManager.ENHANCED_MODE)) {
			str += BombManager.getBombCount();
		} else {
			str += "不支持";
		}
		str += "\n时间：" + GeneralManager.getPlayer().getRecord().getDateString();
		txtrPresent.setText(str);
		// 预览
		CellsContainer cellsContainer = null;
		try {
			cellsContainer = UIManager.getCellsContainer().clone();
			cellsContainer.setPreview(true);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		panelPresent.add(cellsContainer, BorderLayout.CENTER);
	}

	private void showClassicalGameData() {
		String msg = "无";
		if (SettingManager.gameMode.getValue().equals(SettingManager.CLASSICAL_MODE)) {
			btnOverrideClassical.setEnabled(true);
		}
		try {
			GameData gameData = GameDataManager.getGameDataFromFile(SettingManager.CLASSICAL_MODE);
			Record classicalRecord = gameData.getRecord();
			msg = "分数：" + classicalRecord.getScore() + "\n时间：" + classicalRecord.getDateString();
			btnOverrideClassical.setText("覆盖");
			btnLoadClassical.setEnabled(true);
			btnDeleteClassical.setEnabled(true);
			// 预览
			CellsContainer cellsContainer = new CellsContainer(SettingManager.rowCount, SettingManager.columnCount,
					gameData.getCells(), true);
			panelClasscial.add(cellsContainer, BorderLayout.CENTER);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		txtrClasscial.setText(msg);
	}

	private void showEnhancedGameData() {
		String msg = "无";
		if (SettingManager.gameMode.getValue().equals(SettingManager.ENHANCED_MODE)) {
			btnOverrideEnhanced.setEnabled(true);
		}
		try {
			GameData gameData = GameDataManager.getGameDataFromFile(SettingManager.ENHANCED_MODE);
			Record enhancedRecord = gameData.getRecord();
			msg = "分数：" + enhancedRecord.getScore() + "\t雷数：" + gameData.getBombCount() + "\n时间："
					+ enhancedRecord.getDateString();
			btnOverrideEnhanced.setText("覆盖");
			btnLoadEnhanced.setEnabled(true);
			btnDeleteEnhanced.setEnabled(true);
			// 预览
			CellsContainer cellsContainer = new CellsContainer(SettingManager.rowCount, SettingManager.columnCount,
					gameData.getCells(), true);
			panelEnhanced.add(cellsContainer, BorderLayout.CENTER);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		txtrEnhanced.setText(msg);
	}

}
