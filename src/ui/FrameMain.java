package ui;

import element.GeneralManager;
import gamedata.GameData;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import player.PlayerManager;
import record.RecordManager;
import resource.Color;
import resource.Resource;
import setting.Setting;
import setting.SettingManager;
import util.Debugger;
import audio.AudioManager;
import bomb.BombManager;
import bomb.ButtonBomb;
import cell.Cell;
import cell.CellsContainer;

public class FrameMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private CellsContainer cellsContainer;
	private JPanel contentPane;
	private List<JButton> nextBtns;
	private JPanel panelMessage;
	private JPanel panelNextBtns;
	private JLabel lblPresentScoreString;
	private JLabel lblGoalString;
	private JMenuItem mntmRestart;
	private JMenuItem mntmRecords;
	private JMenuItem mntmExit;
	private JMenuItem mntmAbout;
	private JMenu mnSettings;
	private JMenu mnGameSpeed;
	private JRadioButtonMenuItem rdbtnmntmSpeed1;
	private JRadioButtonMenuItem rdbtnmntmSpeed2;
	private JRadioButtonMenuItem rdbtnmntmSpeed3;
	private JMenu mnVoice;
	private JCheckBoxMenuItem chckbxmntmSound;
	private JPanel panelModel;
	private JPanel panelPlayer;
	private JCheckBoxMenuItem chckbxmntmColorString;
	private GameData gameData;
	private JSeparator separator_1;
	private JMenuItem mntmSwitchAccount;
	private JCheckBoxMenuItem chckbxmntmBgm;
	private JMenuItem mntmShowPlayerMessage;
	private JCheckBoxMenuItem chckbxmntmAutoLogin;
	private JMenu mnLoadOption;
	private JRadioButtonMenuItem rdbtnmntmLoadOption1;
	private JRadioButtonMenuItem rdbtnmntmLoadOption2;
	private JRadioButtonMenuItem rdbtnmntmLoadOption3;
	private JPanel panelBomb;
	private ButtonBomb buttonBomb;
	private JLabel lblBombCount;
	private JMenuItem mntmCreateLnk;
	private JMenu mnGameMode;
	private JRadioButtonMenuItem rdbtnmntmGameMode1;
	private JRadioButtonMenuItem rdbtnmntmGameMode2;
	private JPanel panelNext;
	private JMenuItem mnGameData;
	private JLabel lblRecieve;
	private JButton btnMessage;
	private JPanel panelBtns;
	private MyButtonGroup btnGroupLoadOption;
	private MyButtonGroup btnGroupBallMovingInterval;
	private MyButtonGroup btnGroupGameMode;
	// private JMenu mnGlobalFontSize;
	// private JRadioButtonMenuItem rdbtnmntmGlobalFontSize1;
	// private JRadioButtonMenuItem rdbtnmntmGlobalFontSize2;
	// private JRadioButtonMenuItem rdbtnmntmGlobalFontSize3;
	private MyButtonGroup btnGroupGlobalFontSize;

	public FrameMain() {
		this(null);
	}

	public FrameMain(GameData gameData) {
		this.gameData = gameData;
		init();
	}

	public void init() {
		initLayout();
		initListeners();
	}

	private void initLayout() {
		setIconImage(Resource.colorBall.getImageIcon("48px").getImage());
		setTitle("linez");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(new Rectangle(SettingManager.columnCount * Cell.width + 50,
				SettingManager.rowCount * Cell.height + 140 + 3 * SettingManager.GlOBAL_FONT_SIZE));
		setLocationRelativeTo(null);// ����
		setResizable(false);// ��С���ɵ�
		initMenus();
		initContentPane();
	}

	private void initContentPane() {
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);
		{// �ϲ�����
			JPanel panelTop = new JPanel();
			panelTop.setLayout(new BorderLayout(3, 3));
			contentPane.add(panelTop, BorderLayout.NORTH);
			{// ��һ�鴰��
				panelBtns = new JPanel();
				panelBtns.setLayout(new BorderLayout(0, 0));
				panelTop.add(panelBtns);
				{
					panelNext = new JPanel();
					panelBtns.add(panelNext, BorderLayout.CENTER);
					{
						panelNextBtns = new JPanel();
						panelNext.add(panelNextBtns);
						panelNextBtns.setLayout(new GridLayout(0, SettingManager.nextBallsCount, 0, 0));
					}
				}
				{
					{// ��һ�鰴ť
						nextBtns = new ArrayList<JButton>();
						for (int i = 0; i < SettingManager.nextBallsCount; i++) {
							JButton tmpCell = new JButton();
							// tmpCell.setBounds(new Rectangle(34, 34));
							tmpCell.setIcon(Resource.colorBall.getImageIcon("32px"));
							tmpCell.setMargin(new Insets(0, 0, 0, 0));
							tmpCell.setFocusable(false);
							nextBtns.add(tmpCell);
							panelNextBtns.add(tmpCell);
						}
					}
				}
			}
			{// ��ҵ�ǰ��������
				panelPlayer = new JPanel();
				panelPlayer.setBorder(new TitledBorder(null, "��ս��", TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panelPlayer.setLayout(new BoxLayout(panelPlayer, BoxLayout.X_AXIS));
				panelTop.add(panelPlayer, BorderLayout.EAST);
				{
					lblPresentScoreString = new JLabel("                    0");
					lblPresentScoreString.setHorizontalAlignment(SwingConstants.CENTER);
					panelPlayer.add(lblPresentScoreString);
				}
			}
			{// Ŀ�괰��
				panelModel = new JPanel();
				panelModel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "������",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panelModel.setLayout(new BoxLayout(panelModel, BoxLayout.X_AXIS));
				panelTop.add(panelModel, BorderLayout.WEST);
				{
					lblGoalString = new JLabel("                    0");
					panelModel.add(lblGoalString);
				}
			}
		}
		// ��������
		if (gameData != null) {
			initCellsContainer(gameData.getCells());
		} else {
			initCellsContainer();
		}
		{// ��Ϣ����
			panelMessage = new JPanel();
			panelMessage.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panelMessage.setLayout(new BorderLayout(5, 5));
			contentPane.add(panelMessage, BorderLayout.SOUTH);
			{
				lblRecieve = new JLabel(" ");
				panelMessage.add(lblRecieve, BorderLayout.CENTER);
			}
			{
				btnMessage = new JButton("����Ϣ������");
				panelMessage.add(btnMessage, BorderLayout.EAST);
			}
		}

	}

	private void initMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		{
			JMenu mnGame = new JMenu("��Ϸ");
			menuBar.add(mnGame);
			{
				mntmSwitchAccount = new JMenuItem();
				mntmSwitchAccount.setText("�л��˻�");
				mnGame.add(mntmSwitchAccount);
			}
			{
				mntmShowPlayerMessage = new JMenuItem("�鿴�����Ϣ");
				mnGame.add(mntmShowPlayerMessage);
			}
			{
				separator_1 = new JSeparator();
				mnGame.add(separator_1);
			}
			{
				mntmRestart = new JMenuItem("�ؿ�һ��");
				mnGame.add(mntmRestart);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mnGameData = new JMenuItem("��Ϸ�浵");
				mnGame.add(mnGameData);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mntmRecords = new JMenuItem("���а�");
				mnGame.add(mntmRecords);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mntmExit = new JMenuItem("�˳�");
				mnGame.add(mntmExit);
			}
		}
		{
			mnSettings = new JMenu("����");
			menuBar.add(mnSettings);
			{// ��Ϸģʽ
				mnGameMode = new JMenu("��Ϸģʽ");
				mnSettings.add(mnGameMode);
				{
					rdbtnmntmGameMode1 = new JRadioButtonMenuItem("����ģʽ");
					mnGameMode.add(rdbtnmntmGameMode1);
				}
				{
					rdbtnmntmGameMode2 = new JRadioButtonMenuItem("��ǿģʽ");
					mnGameMode.add(rdbtnmntmGameMode2);
				}
				btnGroupGameMode = new MyButtonGroup("��Ϸģʽ", rdbtnmntmGameMode1, rdbtnmntmGameMode2);
			}
			// {// ȫ�������С
			// mnGlobalFontSize = new JMenu("ȫ�������С");
			// mnSettings.add(mnGlobalFontSize);
			// {
			// rdbtnmntmGlobalFontSize1 = new JRadioButtonMenuItem("С");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize1);
			// }
			// {
			// rdbtnmntmGlobalFontSize2 = new JRadioButtonMenuItem("��");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize2);
			// }
			// {
			// rdbtnmntmGlobalFontSize3 = new JRadioButtonMenuItem("��");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize3);
			// }
			// btnGroupGlobalFontSize = new MyButtonGroup("ȫ�������С",
			// rdbtnmntmGlobalFontSize1, rdbtnmntmGlobalFontSize2,
			// rdbtnmntmGlobalFontSize3);
			// }
			{// С���ƶ�ʱ����
				mnGameSpeed = new JMenu("��Ϸ�ٶ�");
				mnSettings.add(mnGameSpeed);
				{
					rdbtnmntmSpeed1 = new JRadioButtonMenuItem("����");
					mnGameSpeed.add(rdbtnmntmSpeed1);
				}
				{
					rdbtnmntmSpeed2 = new JRadioButtonMenuItem("����");
					mnGameSpeed.add(rdbtnmntmSpeed2);
				}
				{
					rdbtnmntmSpeed3 = new JRadioButtonMenuItem("����");
					mnGameSpeed.add(rdbtnmntmSpeed3);
				}
				btnGroupBallMovingInterval = new MyButtonGroup("С���ƶ�ʱ����", rdbtnmntmSpeed1, rdbtnmntmSpeed2,
						rdbtnmntmSpeed3);
			}
			{
				mnVoice = new JMenu("����");
				mnSettings.add(mnVoice);
				{// ��Ч
					chckbxmntmSound = new JCheckBoxMenuItem("��Ч");
					mnVoice.add(chckbxmntmSound);
				}
				{// ��������
					chckbxmntmBgm = new JCheckBoxMenuItem("��������");
					mnVoice.add(chckbxmntmBgm);
				}
			}
			{// �Զ���ȡ�浵��ʽ
				mnLoadOption = new JMenu("�Զ���ȡ�浵��ʽ");
				mnSettings.add(mnLoadOption);
				{
					rdbtnmntmLoadOption1 = new JRadioButtonMenuItem("�Զ���ȡ");
					mnLoadOption.add(rdbtnmntmLoadOption1);
				}
				{
					rdbtnmntmLoadOption2 = new JRadioButtonMenuItem("ѯ�ʶ�ȡ");
					mnLoadOption.add(rdbtnmntmLoadOption2);
				}
				{
					rdbtnmntmLoadOption3 = new JRadioButtonMenuItem("����ȡ");
					mnLoadOption.add(rdbtnmntmLoadOption3);
				}
				btnGroupLoadOption = new MyButtonGroup("�Զ���ȡ�浵��ʽ", rdbtnmntmLoadOption1, rdbtnmntmLoadOption2,
						rdbtnmntmLoadOption3);
			}
			{// �Զ���¼�˻�
				chckbxmntmAutoLogin = new JCheckBoxMenuItem("�Զ���¼�˻�");
				mnSettings.add(chckbxmntmAutoLogin);
			}
			{// ��ʾ/������ɫ�ı�
				chckbxmntmColorString = new JCheckBoxMenuItem("��ʾ/������ɫ�ı�");
				mnSettings.add(chckbxmntmColorString);
			}
			{
				mntmCreateLnk = new JMenuItem("�������ϴ�����ݷ�ʽ");
				mnSettings.add(mntmCreateLnk);
			}
		}
		{
			JMenu mnHelp = new JMenu("����");
			menuBar.add(mnHelp);
			{
				mntmAbout = new JMenuItem("����");
				mnHelp.add(mntmAbout);
			}
		}
	}

	private void initCellsContainer() {
		initCellsContainer(null);
	}

	private void initCellsContainer(Cell[][] cells) {
		cellsContainer = new CellsContainer(SettingManager.columnCount, SettingManager.rowCount, cells);
		contentPane.add(cellsContainer, BorderLayout.CENTER);
	}

	public JCheckBoxMenuItem getChckbxmntmColorString() {
		return chckbxmntmColorString;
	}

	public MyButtonGroup getBtnGroupLoadOption() {
		return btnGroupLoadOption;
	}

	public MyButtonGroup getBtnGroupBallMovingInterval() {
		return btnGroupBallMovingInterval;
	}

	public MyButtonGroup getBtnGroupGameMode() {
		return btnGroupGameMode;
	}

	public MyButtonGroup getBtnGroupFontSize() {
		return btnGroupGlobalFontSize;
	}

	private void initListeners() {
		mntmSwitchAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(FrameMain.this, "�л��˻�����Ϸ�ؿ����Ƿ�ȷ����", "��ʾ",
						JOptionPane.YES_NO_OPTION);
				if (option == 0) {
					if (PlayerManager.inputPlayerName()) {
						GeneralManager.restart(false);
					}
				}
			}
		});
		mntmRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GeneralManager.restart(true, null);
			}
		});
		mntmShowPlayerMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GeneralManager.showPlayerMessage();
			}
		});
		mnGameData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogGameData(FrameMain.this).setVisible(true);
			}
		});
		chckbxmntmAutoLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tmpPlayerName = "";
				if (chckbxmntmAutoLogin.getState()) {
					tmpPlayerName = GeneralManager.getPlayerName();
				}
				settingAction(SettingManager.autoLoginPlayerName, tmpPlayerName);
			}
		});
		chckbxmntmSound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settingAction(SettingManager.sound, chckbxmntmSound.getState());
			}
		});
		chckbxmntmBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settingAction(SettingManager.bgm, chckbxmntmBgm.getState());
			}
		});
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mntmRecords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogRecords(FrameMain.this).setVisible(true);
			}
		});
		chckbxmntmColorString.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settingAction(SettingManager.colorStringVisible, chckbxmntmColorString.getState());
			}
		});
		// btnGroupGlobalFontSize.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// globalFontSizeAction(e);
		// }
		// });
		btnGroupGameMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameModeAction(e);
			}
		});
		btnGroupBallMovingInterval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ballMovingIntervalAction(e);
			}
		});
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(FrameMain.this, "���ߣ�֣��\n�汾�ţ�1.0.0.20151125", "����",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panelPlayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GeneralManager.showPlayerMessage();
			}
		});
		panelModel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GeneralManager.showModelMessage();
			}
		});
		btnMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogMessage(FrameMain.this).setVisible(true);
			}
		});
	}

	/**
	 * ���ö���
	 * <p>
	 * 1.��ֵ<br>
	 * 2.Ӧ������<br>
	 * 3.д������
	 */
	private <T> void settingAction(Setting<T> setting, T value) {
		setting.setValue(value);
		setting.apply();
		setting.writeConfigFile();
	}

	/**
	 * ��ť�鶯��
	 * <p>
	 * �ֱ�԰�ť���еİ�ťִ�У�
	 * <p>
	 * 1.��ֵ<br>
	 * 2.Ӧ������<br>
	 * 3.д������
	 * 
	 * @param e
	 *            �¼�
	 * @param btnGroup
	 *            d
	 * @see settingAction
	 * 
	 */
	private <T> void btnGroupAction(ActionEvent e, MyButtonGroup btnGroup, Setting<T> setting, List<T> settingValues) {
		for (int i = 0; i < btnGroup.getButtonCount(); i++) {
			if (e.getSource().equals(btnGroup.get(i))) {
				settingAction(setting, settingValues.get(i));
				break;
			}
		}
	}

	// private void globalFontSizeAction(ActionEvent e) {
	// btnGroupAction(e, btnGroupGlobalFontSize,
	// SettingManager.globalFontSize, SettingManager.globalFontSizes);
	// int option = JOptionPane.showConfirmDialog(FrameMain.this,
	// "�����С���޸ģ��ؿ���Ϸ����Ч���Ƿ������ؿ���Ϸ��", "��ʾ", JOptionPane.YES_NO_OPTION);
	// if (option == 0) {
	// GeneralManager.restart(false);
	// }
	// }

	/** С���ƶ�������� */
	private void ballMovingIntervalAction(ActionEvent e) {
		btnGroupAction(e, btnGroupBallMovingInterval, SettingManager.ballMovingInterval,
				SettingManager.ballMovingIntervals);
	}

	/** ��Ϸģʽ���� */
	private void gameModeAction(ActionEvent e) {
		int option = JOptionPane.showConfirmDialog(FrameMain.this, "������Ϸģʽ����Ϸ�ؿ����Ƿ�ȷ����", "��ʾ",
				JOptionPane.YES_NO_OPTION);
		if (option != 0) {// UI�޸ĳ���
			if (e.getSource().equals(rdbtnmntmGameMode1)) {
				rdbtnmntmGameMode2.setSelected(true);
			}
			if (e.getSource().equals(rdbtnmntmGameMode2)) {
				rdbtnmntmGameMode1.setSelected(true);
			}
			return;
		}
		btnGroupAction(e, btnGroupGameMode, SettingManager.gameMode, SettingManager.gameModes);
		GeneralManager.restart(false);
	}

	/** ���ը������ */
	public void addPanelBomb() {
		if (panelBomb != null) {
			return;
		}
		panelBomb = new JPanel();
		panelBtns.add(panelBomb, BorderLayout.EAST);
		{// ը����ť
			buttonBomb = new ButtonBomb();
			buttonBomb.setIcon(Resource.bomb.getImageIcon("32px"));
			// btnBomb.setBounds(new Rectangle(26, 26));
			buttonBomb.setMargin(new Insets(0, 0, 0, 0));
			buttonBomb.setFocusable(false);
			panelBomb.add(buttonBomb);
		}
		{// ը������ǩ
			lblBombCount = new JLabel("��" + BombManager.getBombCount());
			lblBombCount.setFont(new Font("Dialog", Font.PLAIN, 20));
			panelBomb.add(lblBombCount);
		}
	}

	public JLabel getMessageContainer() {
		return lblRecieve;
	}

	public JLabel getLblPresentRecordString() {
		return lblPresentScoreString;
	}

	public JLabel getLblGoalScoreString() {
		return lblGoalString;
	}

	public JPanel getPanelGoal() {
		return panelModel;
	}

	public JPanel getPanelScore() {
		return panelPlayer;
	}

	public CellsContainer getCellsContainer() {
		return cellsContainer;
	}

	public void setNextBtns(List<Color> colors) {
		if (colors.size() != nextBtns.size()) {
			Debugger.out("failed to setNextBtns:colors.size!=nextBtns.size");
			return;
		}
		for (int i = 0; i < nextBtns.size(); i++) {
			JButton tmpBtn = nextBtns.get(i);
			tmpBtn.setIcon(GeneralManager.getNextColors().get(i).getResource().getImageIcon("32px"));
		}
	}

	public ButtonBomb getButtonBomb() {
		return buttonBomb;
	}

	public JLabel getLblBombCount() {
		return lblBombCount;
	}

	public JMenuItem getMntmLogin() {
		return mntmSwitchAccount;
	}

	public JCheckBoxMenuItem getChckbxmntmAutoLogin() {
		return chckbxmntmAutoLogin;
	}

	public JCheckBoxMenuItem getChckbxmntmSound() {
		return chckbxmntmSound;
	}

	public JCheckBoxMenuItem getChckbxmntmBGM() {
		return chckbxmntmBgm;
	}

	public void exit(boolean exitFlag) {
		AudioManager.stopBGM();
		super.dispose();
		if (exitFlag) {
			System.exit(0);
		}
	}

	/**
	 * ���
	 * 
	 * @param noticeFlag
	 *            �Ƿ���ʾ
	 * @param exitFlag
	 *            �Ƿ����
	 */
	public void dispose(boolean noticeFlag, boolean exitFlag) {
		if (!noticeFlag) {// �������˳�
			exit(exitFlag);
			return;
		}
		// ��ʾ�˳�
		String noticeStr = "ȷ���˳���Ϸ��";
		if (PlayerManager.login) {// �ѵ�¼
			GeneralManager.getPlayer().getRecord().updateDate();
			if (RecordManager.updateRecords(GeneralManager.getPlayer().getRecord())) {
				noticeStr = "��ϲ�㣬�������Լ��ļ�¼";
			} else {
				noticeStr = "��Ŀǰ��û�д����Լ��ļ�¼";
			}
		}
		int option = JOptionPane.showConfirmDialog(FrameMain.this, noticeStr + "���Ƿ��˳���Ϸ��", "ȷ���˳�",
				JOptionPane.YES_NO_OPTION);
		if (option == 0) {
			exit(exitFlag);
		}
		return;
	}

	/**
	 * �����Ĭ�ϴ��ˣ�
	 */
	@Override
	public void dispose() {
		dispose(true, true);
	}

	public void setNextBtnsColorStringVisible(boolean b) {
		for (int i = 0; i < nextBtns.size(); i++) {
			JButton tmpBtn = nextBtns.get(i);
			if (b && GeneralManager.getNextColors() != null) {
				tmpBtn.setToolTipText(GeneralManager.getNextColors().get(i).toString());
			} else {
				tmpBtn.setToolTipText(null);
			}
		}
	}
}
