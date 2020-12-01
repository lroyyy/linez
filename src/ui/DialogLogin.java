package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import setting.SettingManager;

public class DialogLogin extends MyDialog {
	private JTextField txtPlayerName;
	private JRadioButton rdbtnOnline;
	private JRadioButton rdbtnOffline;
	private JRadioButton rdbtnClassical;
	private JRadioButton rdbtnEnhanced;
	private JRadioButton rdbtnOld;
	private JRadioButton rdbtnNew;
	private MyButtonGroup btnGroupGameData;
	private MyButtonGroup btnGroupGameMode;
	private MyButtonGroup btnGroupNetworkMode;

	public DialogLogin() {
		super(null, "linez", 300, 310);
		initLayout();
		initListeners();
	}

	private void initLayout() {
		{
			JPanel panelTop = new JPanel();
			getContentPane().add(panelTop, BorderLayout.NORTH);
			JPanel panelPlayerName = new JPanel();
			panelTop.add(panelPlayerName);
			{
				JLabel lblPlayerName = new JLabel("玩家名");
				panelPlayerName.add(lblPlayerName);
			}
			{
				txtPlayerName = new JTextField();
				txtPlayerName.setColumns(10);
				panelPlayerName.add(txtPlayerName);
			}
		}
		{
			JPanel panelSelections = new JPanel();
			getContentPane().add(panelSelections, BorderLayout.CENTER);
			panelSelections.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

			{
				JPanel panelNetworkMode = new JPanel();
				panelNetworkMode.setBorder(new TitledBorder(UIManager
						.getBorder("TitledBorder.border"), "网络模式",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelNetworkMode.setLayout(new BoxLayout(panelNetworkMode,
						BoxLayout.Y_AXIS));
				panelSelections.add(panelNetworkMode);
				{
					rdbtnOnline = new JRadioButton("联网模式");
					panelNetworkMode.add(rdbtnOnline);
				}
				{
					rdbtnOffline = new JRadioButton("单机模式");
					panelNetworkMode.add(rdbtnOffline);
				}
				btnGroupNetworkMode = new MyButtonGroup("网络模式", rdbtnOnline,
						rdbtnOffline);
			}
			{
				JPanel panelGameMode = new JPanel();
				panelGameMode.setLayout(new BoxLayout(panelGameMode,
						BoxLayout.Y_AXIS));
				panelGameMode.setBorder(new TitledBorder(null, "游戏模式",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelSelections.add(panelGameMode);
				{
					rdbtnClassical = new JRadioButton("经典模式");
					panelGameMode.add(rdbtnClassical);
				}
				{
					rdbtnEnhanced = new JRadioButton("增强模式");
					panelGameMode.add(rdbtnEnhanced);
				}
				btnGroupGameMode = new MyButtonGroup("游戏模式", rdbtnClassical,
						rdbtnEnhanced);
			}
			{
				JPanel panelGameData = new JPanel();
				panelGameData.setBorder(new TitledBorder(null, "游戏数据",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelSelections.add(panelGameData);
				panelGameData.setLayout(new BoxLayout(panelGameData,
						BoxLayout.Y_AXIS));
				{
					rdbtnNew = new JRadioButton("新游戏");
					rdbtnNew.setActionCommand(rdbtnNew.getText());
					panelGameData.add(rdbtnNew);
				}

				{
					rdbtnOld = new JRadioButton("读取存档");
					panelGameData.add(rdbtnOld);
				}
				btnGroupGameData = new MyButtonGroup("游戏模式", rdbtnNew, rdbtnOld);
			}
		}
		{
			JPanel panelClick = new JPanel();
			getContentPane().add(panelClick, BorderLayout.SOUTH);
			panelClick.setLayout(new GridLayout(3, 0, 0, 0));
			{
				JPanel panelData = new JPanel();
				panelData.setBorder(new TitledBorder(null, "存档信息",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panelClick.add(panelData);
				{
					JLabel lblNewLabel = new JLabel("存档信息");
					panelData.add(lblNewLabel);
				}
			}
			{
				JCheckBox chckbxAutoLogin = new JCheckBox(
						"以后不要显示此窗口，直接以本次登录方式登录");
				chckbxAutoLogin.setHorizontalAlignment(SwingConstants.CENTER);
				panelClick.add(chckbxAutoLogin);
			}
			{
				JPanel panelButtons = new JPanel();
				panelClick.add(panelButtons);
				{
					JButton btnStart = new JButton("开始");
					panelButtons.add(btnStart);
				}
				{
					JButton btnExit = new JButton("退出");
					panelButtons.add(btnExit);
				}
			}
		}
	}

	private void initListeners() {
		// TODO Auto-generated method stub

	}

//	public void setSettings() {
//		if (btnGroupNetworkMode.getSelectedString().equals("单机模式")) {
//			SettingManager.networkMode.setValue(SettingManager.OFFLINEMODE);
//		} else if (btnGroupNetworkMode.getSelectedString().equals("联网模式")) {
//			SettingManager.networkMode.setValue(SettingManager.ONLINEMODE);
//		}
//	}

	public void applySettings() {

	}

}
