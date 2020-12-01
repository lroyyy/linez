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
				JLabel lblPlayerName = new JLabel("�����");
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
						.getBorder("TitledBorder.border"), "����ģʽ",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelNetworkMode.setLayout(new BoxLayout(panelNetworkMode,
						BoxLayout.Y_AXIS));
				panelSelections.add(panelNetworkMode);
				{
					rdbtnOnline = new JRadioButton("����ģʽ");
					panelNetworkMode.add(rdbtnOnline);
				}
				{
					rdbtnOffline = new JRadioButton("����ģʽ");
					panelNetworkMode.add(rdbtnOffline);
				}
				btnGroupNetworkMode = new MyButtonGroup("����ģʽ", rdbtnOnline,
						rdbtnOffline);
			}
			{
				JPanel panelGameMode = new JPanel();
				panelGameMode.setLayout(new BoxLayout(panelGameMode,
						BoxLayout.Y_AXIS));
				panelGameMode.setBorder(new TitledBorder(null, "��Ϸģʽ",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelSelections.add(panelGameMode);
				{
					rdbtnClassical = new JRadioButton("����ģʽ");
					panelGameMode.add(rdbtnClassical);
				}
				{
					rdbtnEnhanced = new JRadioButton("��ǿģʽ");
					panelGameMode.add(rdbtnEnhanced);
				}
				btnGroupGameMode = new MyButtonGroup("��Ϸģʽ", rdbtnClassical,
						rdbtnEnhanced);
			}
			{
				JPanel panelGameData = new JPanel();
				panelGameData.setBorder(new TitledBorder(null, "��Ϸ����",
						TitledBorder.LEADING, TitledBorder.TOP, null,
						Color.BLUE));
				panelSelections.add(panelGameData);
				panelGameData.setLayout(new BoxLayout(panelGameData,
						BoxLayout.Y_AXIS));
				{
					rdbtnNew = new JRadioButton("����Ϸ");
					rdbtnNew.setActionCommand(rdbtnNew.getText());
					panelGameData.add(rdbtnNew);
				}

				{
					rdbtnOld = new JRadioButton("��ȡ�浵");
					panelGameData.add(rdbtnOld);
				}
				btnGroupGameData = new MyButtonGroup("��Ϸģʽ", rdbtnNew, rdbtnOld);
			}
		}
		{
			JPanel panelClick = new JPanel();
			getContentPane().add(panelClick, BorderLayout.SOUTH);
			panelClick.setLayout(new GridLayout(3, 0, 0, 0));
			{
				JPanel panelData = new JPanel();
				panelData.setBorder(new TitledBorder(null, "�浵��Ϣ",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panelClick.add(panelData);
				{
					JLabel lblNewLabel = new JLabel("�浵��Ϣ");
					panelData.add(lblNewLabel);
				}
			}
			{
				JCheckBox chckbxAutoLogin = new JCheckBox(
						"�Ժ�Ҫ��ʾ�˴��ڣ�ֱ���Ա��ε�¼��ʽ��¼");
				chckbxAutoLogin.setHorizontalAlignment(SwingConstants.CENTER);
				panelClick.add(chckbxAutoLogin);
			}
			{
				JPanel panelButtons = new JPanel();
				panelClick.add(panelButtons);
				{
					JButton btnStart = new JButton("��ʼ");
					panelButtons.add(btnStart);
				}
				{
					JButton btnExit = new JButton("�˳�");
					panelButtons.add(btnExit);
				}
			}
		}
	}

	private void initListeners() {
		// TODO Auto-generated method stub

	}

//	public void setSettings() {
//		if (btnGroupNetworkMode.getSelectedString().equals("����ģʽ")) {
//			SettingManager.networkMode.setValue(SettingManager.OFFLINEMODE);
//		} else if (btnGroupNetworkMode.getSelectedString().equals("����ģʽ")) {
//			SettingManager.networkMode.setValue(SettingManager.ONLINEMODE);
//		}
//	}

	public void applySettings() {

	}

}
