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
		setLocationRelativeTo(null);// 居中
		setResizable(false);// 大小不可调
		initMenus();
		initContentPane();
	}

	private void initContentPane() {
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);
		{// 上部窗格
			JPanel panelTop = new JPanel();
			panelTop.setLayout(new BorderLayout(3, 3));
			contentPane.add(panelTop, BorderLayout.NORTH);
			{// 下一组窗格
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
					{// 下一组按钮
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
			{// 玩家当前分数窗格
				panelPlayer = new JPanel();
				panelPlayer.setBorder(new TitledBorder(null, "挑战者", TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panelPlayer.setLayout(new BoxLayout(panelPlayer, BoxLayout.X_AXIS));
				panelTop.add(panelPlayer, BorderLayout.EAST);
				{
					lblPresentScoreString = new JLabel("                    0");
					lblPresentScoreString.setHorizontalAlignment(SwingConstants.CENTER);
					panelPlayer.add(lblPresentScoreString);
				}
			}
			{// 目标窗格
				panelModel = new JPanel();
				panelModel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "守擂者",
						TitledBorder.CENTER, TitledBorder.TOP, null, null));
				panelModel.setLayout(new BoxLayout(panelModel, BoxLayout.X_AXIS));
				panelTop.add(panelModel, BorderLayout.WEST);
				{
					lblGoalString = new JLabel("                    0");
					panelModel.add(lblGoalString);
				}
			}
		}
		// 格子容器
		if (gameData != null) {
			initCellsContainer(gameData.getCells());
		} else {
			initCellsContainer();
		}
		{// 消息窗格
			panelMessage = new JPanel();
			panelMessage.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panelMessage.setLayout(new BorderLayout(5, 5));
			contentPane.add(panelMessage, BorderLayout.SOUTH);
			{
				lblRecieve = new JLabel(" ");
				panelMessage.add(lblRecieve, BorderLayout.CENTER);
			}
			{
				btnMessage = new JButton("打开消息管理器");
				panelMessage.add(btnMessage, BorderLayout.EAST);
			}
		}

	}

	private void initMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		{
			JMenu mnGame = new JMenu("游戏");
			menuBar.add(mnGame);
			{
				mntmSwitchAccount = new JMenuItem();
				mntmSwitchAccount.setText("切换账户");
				mnGame.add(mntmSwitchAccount);
			}
			{
				mntmShowPlayerMessage = new JMenuItem("查看玩家信息");
				mnGame.add(mntmShowPlayerMessage);
			}
			{
				separator_1 = new JSeparator();
				mnGame.add(separator_1);
			}
			{
				mntmRestart = new JMenuItem("重开一局");
				mnGame.add(mntmRestart);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mnGameData = new JMenuItem("游戏存档");
				mnGame.add(mnGameData);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mntmRecords = new JMenuItem("排行榜");
				mnGame.add(mntmRecords);
			}
			{
				JSeparator separator = new JSeparator();
				mnGame.add(separator);
			}
			{
				mntmExit = new JMenuItem("退出");
				mnGame.add(mntmExit);
			}
		}
		{
			mnSettings = new JMenu("设置");
			menuBar.add(mnSettings);
			{// 游戏模式
				mnGameMode = new JMenu("游戏模式");
				mnSettings.add(mnGameMode);
				{
					rdbtnmntmGameMode1 = new JRadioButtonMenuItem("经典模式");
					mnGameMode.add(rdbtnmntmGameMode1);
				}
				{
					rdbtnmntmGameMode2 = new JRadioButtonMenuItem("增强模式");
					mnGameMode.add(rdbtnmntmGameMode2);
				}
				btnGroupGameMode = new MyButtonGroup("游戏模式", rdbtnmntmGameMode1, rdbtnmntmGameMode2);
			}
			// {// 全局字体大小
			// mnGlobalFontSize = new JMenu("全局字体大小");
			// mnSettings.add(mnGlobalFontSize);
			// {
			// rdbtnmntmGlobalFontSize1 = new JRadioButtonMenuItem("小");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize1);
			// }
			// {
			// rdbtnmntmGlobalFontSize2 = new JRadioButtonMenuItem("中");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize2);
			// }
			// {
			// rdbtnmntmGlobalFontSize3 = new JRadioButtonMenuItem("大");
			// mnGlobalFontSize.add(rdbtnmntmGlobalFontSize3);
			// }
			// btnGroupGlobalFontSize = new MyButtonGroup("全局字体大小",
			// rdbtnmntmGlobalFontSize1, rdbtnmntmGlobalFontSize2,
			// rdbtnmntmGlobalFontSize3);
			// }
			{// 小球移动时间间隔
				mnGameSpeed = new JMenu("游戏速度");
				mnSettings.add(mnGameSpeed);
				{
					rdbtnmntmSpeed1 = new JRadioButtonMenuItem("慢速");
					mnGameSpeed.add(rdbtnmntmSpeed1);
				}
				{
					rdbtnmntmSpeed2 = new JRadioButtonMenuItem("正常");
					mnGameSpeed.add(rdbtnmntmSpeed2);
				}
				{
					rdbtnmntmSpeed3 = new JRadioButtonMenuItem("快速");
					mnGameSpeed.add(rdbtnmntmSpeed3);
				}
				btnGroupBallMovingInterval = new MyButtonGroup("小球移动时间间隔", rdbtnmntmSpeed1, rdbtnmntmSpeed2,
						rdbtnmntmSpeed3);
			}
			{
				mnVoice = new JMenu("声音");
				mnSettings.add(mnVoice);
				{// 音效
					chckbxmntmSound = new JCheckBoxMenuItem("音效");
					mnVoice.add(chckbxmntmSound);
				}
				{// 背景音乐
					chckbxmntmBgm = new JCheckBoxMenuItem("背景音乐");
					mnVoice.add(chckbxmntmBgm);
				}
			}
			{// 自动读取存档方式
				mnLoadOption = new JMenu("自动读取存档方式");
				mnSettings.add(mnLoadOption);
				{
					rdbtnmntmLoadOption1 = new JRadioButtonMenuItem("自动读取");
					mnLoadOption.add(rdbtnmntmLoadOption1);
				}
				{
					rdbtnmntmLoadOption2 = new JRadioButtonMenuItem("询问读取");
					mnLoadOption.add(rdbtnmntmLoadOption2);
				}
				{
					rdbtnmntmLoadOption3 = new JRadioButtonMenuItem("不读取");
					mnLoadOption.add(rdbtnmntmLoadOption3);
				}
				btnGroupLoadOption = new MyButtonGroup("自动读取存档方式", rdbtnmntmLoadOption1, rdbtnmntmLoadOption2,
						rdbtnmntmLoadOption3);
			}
			{// 自动登录账户
				chckbxmntmAutoLogin = new JCheckBoxMenuItem("自动登录账户");
				mnSettings.add(chckbxmntmAutoLogin);
			}
			{// 显示/隐藏颜色文本
				chckbxmntmColorString = new JCheckBoxMenuItem("显示/隐藏颜色文本");
				mnSettings.add(chckbxmntmColorString);
			}
			{
				mntmCreateLnk = new JMenuItem("在桌面上创建快捷方式");
				mnSettings.add(mntmCreateLnk);
			}
		}
		{
			JMenu mnHelp = new JMenu("帮助");
			menuBar.add(mnHelp);
			{
				mntmAbout = new JMenuItem("关于");
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
				int option = JOptionPane.showConfirmDialog(FrameMain.this, "切换账户后游戏重开，是否确定？", "提示",
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
				JOptionPane.showMessageDialog(FrameMain.this, "作者：郑非\n版本号：1.0.0.20151125", "关于",
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
	 * 设置动作
	 * <p>
	 * 1.赋值<br>
	 * 2.应用设置<br>
	 * 3.写入配置
	 */
	private <T> void settingAction(Setting<T> setting, T value) {
		setting.setValue(value);
		setting.apply();
		setting.writeConfigFile();
	}

	/**
	 * 按钮组动作
	 * <p>
	 * 分别对按钮组中的按钮执行：
	 * <p>
	 * 1.赋值<br>
	 * 2.应用设置<br>
	 * 3.写入配置
	 * 
	 * @param e
	 *            事件
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
	// "字体大小已修改，重开游戏后生效，是否现在重开游戏？", "提示", JOptionPane.YES_NO_OPTION);
	// if (option == 0) {
	// GeneralManager.restart(false);
	// }
	// }

	/** 小球移动间隔动作 */
	private void ballMovingIntervalAction(ActionEvent e) {
		btnGroupAction(e, btnGroupBallMovingInterval, SettingManager.ballMovingInterval,
				SettingManager.ballMovingIntervals);
	}

	/** 游戏模式动作 */
	private void gameModeAction(ActionEvent e) {
		int option = JOptionPane.showConfirmDialog(FrameMain.this, "更改游戏模式后游戏重开，是否确定？", "提示",
				JOptionPane.YES_NO_OPTION);
		if (option != 0) {// UI修改撤销
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

	/** 添加炸弹窗格 */
	public void addPanelBomb() {
		if (panelBomb != null) {
			return;
		}
		panelBomb = new JPanel();
		panelBtns.add(panelBomb, BorderLayout.EAST);
		{// 炸弹按钮
			buttonBomb = new ButtonBomb();
			buttonBomb.setIcon(Resource.bomb.getImageIcon("32px"));
			// btnBomb.setBounds(new Rectangle(26, 26));
			buttonBomb.setMargin(new Insets(0, 0, 0, 0));
			buttonBomb.setFocusable(false);
			panelBomb.add(buttonBomb);
		}
		{// 炸弹数标签
			lblBombCount = new JLabel("×" + BombManager.getBombCount());
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
	 * 清除
	 * 
	 * @param noticeFlag
	 *            是否提示
	 * @param exitFlag
	 *            是否大退
	 */
	public void dispose(boolean noticeFlag, boolean exitFlag) {
		if (!noticeFlag) {// 无条件退出
			exit(exitFlag);
			return;
		}
		// 提示退出
		String noticeStr = "确定退出游戏？";
		if (PlayerManager.login) {// 已登录
			GeneralManager.getPlayer().getRecord().updateDate();
			if (RecordManager.updateRecords(GeneralManager.getPlayer().getRecord())) {
				noticeStr = "恭喜你，打破了自己的记录";
			} else {
				noticeStr = "你目前还没有打破自己的记录";
			}
		}
		int option = JOptionPane.showConfirmDialog(FrameMain.this, noticeStr + "，是否退出游戏？", "确认退出",
				JOptionPane.YES_NO_OPTION);
		if (option == 0) {
			exit(exitFlag);
		}
		return;
	}

	/**
	 * 清除（默认大退）
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
