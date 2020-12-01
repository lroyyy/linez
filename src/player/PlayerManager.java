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
 * 玩家管理器
 * <p>
 * player的注册、登录、获取
 * */
public class PlayerManager {
	/** 开关：已登录 */
	public static boolean login;
	// /** 开关：自动登录 */
	// private static boolean autoLogin;
	/** 预登录玩家名 */
	private static String preLoginPlayerName = "";

	private static String localPlayersPath = "./dat/";

	// private static String autoLoginPlayerName;
	/** 玩家集 */
	// public static List<Player> players;

	/**
	 * 初始化PlayerMangager
	 * <p>
	 * 初始化players
	 * */
	public static void init() {
		getPlayers();
	}

	/**
	 * 从数据源获取所有玩家，构造players
	 * */
	@SuppressWarnings("unchecked")
	public static List<Player> getPlayers() {
		PlayerReader playerReader = new PlayerReader();
		playerReader.execute();
		List<Player> players = null;
		try {
			players = (List<Player>) playerReader.get();
		} catch (Exception e) {
			Debugger.out("初始化players:未找到任何player，players=null" + "信息："
					+ e.getMessage());
		}
		return players;
	}

	/**
	 * 从数据源中获取玩家名为playerName的player对象
	 * 
	 * @return 成功 ，返回player；失败 ，返回null
	 * */
	public static Player getPlayer(String playerName) {
		PlayerReader playerReader = new PlayerReader(playerName);
		playerReader.execute();
		Player tmpPlayer = null;
		try {
			tmpPlayer = (Player) playerReader.get();
		} catch (InterruptedException | ExecutionException e) {
			Debugger.out("读取" + playerName + "时失败，信息：" + e.getMessage());
			e.printStackTrace();
		}
		return tmpPlayer;
	}

	/**
	 * 获取所有玩家姓名集
	 * <p>
	 * 取自内存中的players
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

	/** 注册 */
	public static boolean register(final String playerName) {
			File file = new File(getPlayerPath(playerName));
			if (!file.exists()) {
				file.mkdirs();
			}
			return true;
		
	}

	/**
	 * 登录到指定playerName
	 * <p>
	 * 设置了player
	 * 
	 * @param playerName
	 *            玩家名
	 * */
	public static boolean login(String playerName) {
		Debugger.out("开始登录到" + playerName);
		if (playerName == null) {
			Debugger.out("登录失败：playerName=null");
			return false;
		}
		Player tmpPlayer = getPlayer(playerName);
		if (tmpPlayer == null) {// 数据源中没有
			Debugger.out("未找到" + tmpPlayer + "，开始注册");
			register(playerName);
			tmpPlayer = new Player(playerName);
		}
		tmpPlayer.initNewRecord();// 初始化当前记录
		GeneralManager.setPlayer(tmpPlayer);
		// GameManager.setPlayerName(playerName);
		login = true;
		Debugger.out("已登录到 " + tmpPlayer);
		MessageManager.addMessage("你好， " + playerName + " 。");
		return true;
	}

	/**
	 * 登录到预登录玩家名
	 * */
	public static boolean login() {
		if (!hasPreLoginPlayerName()) {
			return false;
		}
		return login(getPreLoginPlayerName());
	}

	/** 强制性登录 */
	public static boolean loginOrDie() {
		if (login) {// 已登录，不需要登录
			return true;
		}
		if (inputPlayerName()) {// 输入玩家名
			if (login()) {
				return true;
			}
		}
		Object[] options = { "返回登录", "退出游戏" };
		int option = JOptionPane.showOptionDialog(UIManager.getFrameMain(),
				"必须登录账户才能开始游戏，请返回登录。", "提示", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (option == 0) {
			loginOrDie();
		} else {// 不登录，退出
			UIManager.getFrameMain().exit(true);
		}
		return true;
	}

	/** 输入玩家名（设置预登录名） */
	public static boolean inputPlayerName() {
		String tmpPlayerName = JOptionPane.showInputDialog(
				UIManager.getFrameMain(), "请输入玩家名", "匿名");
		if (tmpPlayerName == null) {// 用户取消
			Debugger.out("用户取消输入姓名");
			return false;
		} else if (tmpPlayerName.equals("")) {// 空字符输入
			JOptionPane.showMessageDialog(null, "姓名不能为空，请重新输入。");
			return inputPlayerName();
		} else {// 有效输入
			setPreLoginPlayerName(tmpPlayerName);
			return true;
		}
	}

	public static boolean hasPreLoginPlayerName() {
		if (getPreLoginPlayerName() != null
				&& !getPreLoginPlayerName().equals("")) {// preLoginPlayerName不为空且不为""
			return true;
		} else {
			return false;
		}
	}

	public static void setPreLoginPlayerName(String preLoginPlayerName) {
		Debugger.out("设置预登录名：" + preLoginPlayerName);
		PlayerManager.preLoginPlayerName = preLoginPlayerName;
	}

	public static String getPreLoginPlayerName() {
		return preLoginPlayerName;
	}

	/** 自适应登录 */
	public static boolean loginSelfAdaptively(GameData gameData) {
		if (!PlayerManager.hasPreLoginPlayerName()) {// 无预登录名（即不是读取的游戏）
			if (!SettingManager.autoLoginPlayerName.getValue().equals("")) {// 有自动登录名，则自动登录
				if (!PlayerManager.login(SettingManager.autoLoginPlayerName
						.getValue())) {// 自动登录失败，转入强制登录
					Debugger.out("自动登录失败，转入强制登录");
					return PlayerManager.loginOrDie();
				} else {
					Debugger.out("自动登录成功");
					return true;
				}
			} else {// 无自动登录名 ，强制登录
				return PlayerManager.loginOrDie();
			}
		} else {// 已有预登录名
			if (gameData != null) {// 若传入数据
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
