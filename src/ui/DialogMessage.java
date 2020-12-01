package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import message.MessageManager;
import player.PlayerManager;
import element.GeneralManager;

public class DialogMessage extends MyDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	private JButton btnClear;
	private JPanel panelSend;
	private JButton btnSend;
	private JPanel panelTo;
	private JLabel lblTo;
	private JComboBox<String> comboBoxTo;
	private JComboBox<String> comboBoxMessage;
	private JTextField textFieldMessage;
	private JPanel panel;

	public DialogMessage(FrameMain frameMain) {
		super(frameMain, "消息管理器", 450, 300);
		initLayout();
		initListeners();
		updateMesssages();
	}

	private void initLayout() {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelMessageHistory = new JPanel();
			panelMessageHistory.setBorder(new TitledBorder(null, "消息历史",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panelMessageHistory, BorderLayout.CENTER);
			panelMessageHistory.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panelMessageHistory.add(scrollPane);
				{
					textArea = new JTextArea();
					textArea.setLineWrap(true);
					textArea.setEditable(false);
					scrollPane.setViewportView(textArea);
				}
			}
			{
				panel = new JPanel();
				panelMessageHistory.add(panel, BorderLayout.SOUTH);
				{
					btnClear = new JButton("清空历史消息");
					panel.add(btnClear);
					panel.add(addBtnBack());
				}
			}
		}
		if (!GeneralManager.online) {
			return;
		}
		{
			panelSend = new JPanel();
			panelSend.setBorder(new TitledBorder(null, "发送消息",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(panelSend, BorderLayout.SOUTH);
			panelSend.setLayout(new BorderLayout(10, 10));
			{
				btnSend = new JButton("发送");
				panelSend.add(btnSend, BorderLayout.EAST);
			}
			{
				panelTo = new JPanel();
				panelSend.add(panelTo, BorderLayout.WEST);
				panelTo.setLayout(new BoxLayout(panelTo, BoxLayout.X_AXIS));
				{
					lblTo = new JLabel("对");
					panelTo.add(lblTo);
				}
				{
					comboBoxTo = new JComboBox<String>();
					if (GeneralManager.online) {
						initPlayerList();
					}
					comboBoxTo.setEditable(true);
					panelTo.add(comboBoxTo);
				}
			}
			{
				comboBoxMessage = new JComboBox<String>();
				comboBoxMessage.setModel(new DefaultComboBoxModel<String>(
						new String[] { "自定义消息", "问好", "挑衅" }));
				comboBoxMessage.setEditable(true);
				panelSend.add(comboBoxMessage, BorderLayout.CENTER);
				textFieldMessage = (JTextField) comboBoxMessage.getEditor()
						.getEditorComponent();
			}
		}
	}

	private void initPlayerList() {
		if (PlayerManager.getPlayersNames() == null) {
			return;
		}
		comboBoxTo.setModel(new DefaultComboBoxModel<String>(PlayerManager
				.getPlayersNames()));
	}

	private void initListeners() {
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessageManager.resetMessageHistory();
				updateMesssages();
			}
		});
		if (GeneralManager.online) {
			textFieldMessage.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (textFieldMessage.getText().equals("自定义消息")) {
						textFieldMessage.setText("");
					}
				}
			});
			comboBoxMessage.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (comboBoxMessage.getSelectedItem().equals("自定义消息")) {
						comboBoxMessage.setEditable(true);
					} else {
						comboBoxMessage.setEditable(false);
					}
				}
			});
			btnSend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

				}
			});
		}
	}

	public void updateMesssages() {
		textArea.setText(MessageManager.getMessageHistory());
	}
}
