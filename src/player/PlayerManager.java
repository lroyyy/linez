package player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import message.MessageManager;
import setting.SettingManager;
import ui.UIManager;
import util.Debugger;
import element.GeneralManager;
import gamedata.GameData;

/**
 * ��ҹ�����
 * <p>
 * player��ע�ᡢ��¼����ȡ
 * */
public class PlayerManager {
	/** ���أ��ѵ�¼ */
	public static boolean login;
	// /** ���أ��Զ���¼ */
	// private static boolean autoLogin;
	/** Ԥ��¼����� */
	private static String preLoginPlayerName = "";

	private static String localPlayersPath = "./dat/";

	// private static String autoLoginPlayerName;
	/** ��Ҽ� */
	// public static List<Player> players;

	/**
	 * ��ʼ��PlayerMangager
	 * <p>
	 * ��ʼ��players
	 * */
	public static void init() {
		getPlayers();
	}

	/**
	 * ������Դ��ȡ������ң�����players
	 * */
	@SuppressWarnings("unchecked")
	public static List<Player> getPlayers() {
		PlayerReader playerReader = new PlayerReader();
		playerReader.execute();
		List<Player> players = null;
		try {
			players = (List<Player>) playerReader.get();
		} catch (Exception e) {
			Debugger.out("��ʼ��players:δ�ҵ��κ�player��players=null" + "��Ϣ��"
					+ e.getMessage());
		}
		return players;
	}

	/**
	 * ������Դ�л�ȡ�����ΪplayerName��player����
	 * 
	 * @return �ɹ� ������player��ʧ�� ������null
	 * */
	public static Player getPlayer(String playerName) {
		PlayerReader playerReader = new PlayerReader(playerName);
		playerReader.execute();
		Player tmpPlayer = null;
		try {
			tmpPlayer = (Player) playerReader.get();
		} catch (InterruptedException | ExecutionException e) {
			Debugger.out("��ȡ" + playerName + "ʱʧ�ܣ���Ϣ��" + e.getMessage());
			e.printStackTrace();
		}
		return tmpPlayer;
	}

	/**
	 * ��ȡ�������������
	 * <p>
	 * ȡ���ڴ��е�players
	 * */
	public static String[] getPlayersNames() {
		List<Player> players = getPlayers();
		if (players.isEmpty() || players == null) {
			return null;
		}
		List<String> playersNames = new ArrayList<String>();
		for (Player player : players) {
			playersNames.add(player.getName());
		}
		int size = players.size();
		return (String[]) playersNames.toArray(new String[size]);
	}

	/** ע�� */
	public static boolean register(final String playerName) {
			File file = new File(getPlayerPath(playerName));
			if (!file.exists()) {
				file.mkdirs();
			}
			return true;
		
	}

	/**
	 * ��¼��ָ��playerName
	 * <p>
	 * ������player
	 * 
	 * @param playerName
	 *            �����
	 * */
	public static boolean login(String playerName) {
		Debugger.out("��ʼ��¼��" + playerName);
		if (playerName == null) {
			Debugger.out("��¼ʧ�ܣ�playerName=null");
			return false;
		}
		Player tmpPlayer = getPlayer(playerName);
		if (tmpPlayer == null) {// ����Դ��û��
			Debugger.out("δ�ҵ�" + tmpPlayer + "����ʼע��");
			register(playerName);
			tmpPlayer = new Player(playerName);
		}
		tmpPlayer.initNewRecord();// ��ʼ����ǰ��¼
		GeneralManager.setPlayer(tmpPlayer);
		// GameManager.setPlayerName(playerName);
		login = true;
		Debugger.out("�ѵ�¼�� " + tmpPlayer);
		MessageManager.addMessage("��ã� " + playerName + " ��");
		return true;
	}

	/**
	 * ��¼��Ԥ��¼�����
	 * */
	public static boolean login() {
		if (!hasPreLoginPlayerName()) {
			return false;
		}
		return login(getPreLoginPlayerName());
	}

	/** ǿ���Ե�¼ */
	public static boolean loginOrDie() {
		if (login) {// �ѵ�¼������Ҫ��¼
			return true;
		}
		if (inputPlayerName()) {// ���������
			if (login()) {
				return true;
			}
		}
		Object[] options = { "���ص�¼", "�˳���Ϸ" };
		int option = JOptionPane.showOptionDialog(UIManager.getFrameMain(),
				"�����¼�˻����ܿ�ʼ��Ϸ���뷵�ص�¼��", "��ʾ", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (option == 0) {
			loginOrDie();
		} else {// ����¼���˳�
			UIManager.getFrameMain().exit(true);
		}
		return true;
	}

	/** ���������������Ԥ��¼���� */
	public static boolean inputPlayerName() {
		String tmpPlayerName = JOptionPane.showInputDialog(
				UIManager.getFrameMain(), "�����������", "����");
		if (tmpPlayerName == null) {// �û�ȡ��
			Debugger.out("�û�ȡ����������");
			return false;
		} else if (tmpPlayerName.equals("")) {// ���ַ�����
			JOptionPane.showMessageDialog(null, "��������Ϊ�գ����������롣");
			return inputPlayerName();
		} else {// ��Ч����
			setPreLoginPlayerName(tmpPlayerName);
			return true;
		}
	}

	public static boolean hasPreLoginPlayerName() {
		if (getPreLoginPlayerName() != null
				&& !getPreLoginPlayerName().equals("")) {// preLoginPlayerName��Ϊ���Ҳ�Ϊ""
			return true;
		} else {
			return false;
		}
	}

	public static void setPreLoginPlayerName(String preLoginPlayerName) {
		Debugger.out("����Ԥ��¼����" + preLoginPlayerName);
		PlayerManager.preLoginPlayerName = preLoginPlayerName;
	}

	public static String getPreLoginPlayerName() {
		return preLoginPlayerName;
	}

	/** ����Ӧ��¼ */
	public static boolean loginSelfAdaptively(GameData gameData) {
		if (!PlayerManager.hasPreLoginPlayerName()) {// ��Ԥ��¼���������Ƕ�ȡ����Ϸ��
			if (!SettingManager.autoLoginPlayerName.getValue().equals("")) {// ���Զ���¼�������Զ���¼
				if (!PlayerManager.login(SettingManager.autoLoginPlayerName
						.getValue())) {// �Զ���¼ʧ�ܣ�ת��ǿ�Ƶ�¼
					Debugger.out("�Զ���¼ʧ�ܣ�ת��ǿ�Ƶ�¼");
					return PlayerManager.loginOrDie();
				} else {
					Debugger.out("�Զ���¼�ɹ�");
					return true;
				}
			} else {// ���Զ���¼�� ��ǿ�Ƶ�¼
				return PlayerManager.loginOrDie();
			}
		} else {// ����Ԥ��¼��
			if (gameData != null) {// ����������
				PlayerManager.setPreLoginPlayerName(gameData.getPlayerName());
			}
			boolean success = PlayerManager.login();
			setPreLoginPlayerName("");
			return success;
		}
	}

	public static String getPlayerPath(String playerName) {
		return localPlayersPath + "/" + playerName + "/";
	}
}
