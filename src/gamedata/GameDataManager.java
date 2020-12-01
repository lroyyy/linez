package gamedata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import message.MessageManager;
import player.Player;
import record.Record;
import resource.Color;
import setting.SettingManager;
import ui.UIManager;
import util.Debugger;
import util.MyUtils;
import util.TxtProcesser;
import bomb.BombManager;
import cell.Cell;
import element.GeneralManager;

public class GameDataManager {

	/** 保存文件父路径 */
	private static String saveFileParentPath = "./dat/";
	/** 经典模式保存文件名 */
	private static String classcialSaveFileName = "save1.dat";
	/** 增强模式保存文件名 */
	private static String enhancedSaveFileName = "save2.dat";

	// /** 读取选项 */
	// public static String loadOption;
	// /** 读取选项1：自动读取 */
	// public static final String loadOption1 = "auto";
	// /** 读取选项2：询问 */
	// public static final String loadOption2 = "ask";
	// /** 读取选项3：不读取 */
	// public static final String loadOption3 = "never";

	// private GameData gameData;

	/**
	 * 保存游戏
	 * <p>
	 * 生成保存文件， 内容依次如下：<br>
	 * 玩家名<br>
	 * 分数<br>
	 * 格子颜色<br>
	 * 下一组颜色<br>
	 * 时间<br>
	 * （炸弹数）
	 * */
	public static boolean saveGame() {
		// 创建写入文件
		File file = new File(getSavePath());
		if (file.exists()) {
			int option = JOptionPane.showConfirmDialog(
					UIManager.getFrameMain(), "已有存档，是否覆盖？", "提示",
					JOptionPane.YES_NO_OPTION);
			if (option != 0) {
				return false;
			}
		}
		MyUtils.initFile(getSavePath());
		// 构造数据文本
		// String gameModeString = "";
		String cellsString = "";
		String playerNameString = "";
		String scoreString = "";
		String nextColorsString = "";
		String dateString = "";
		String bombCountString = "";
		// 游戏模式
		// gameModeString = Integer.toString(GameManager
		// .getGameModeIndex(GameManager.getGameMode()));
		// 格子颜色
		for (int i = 0; i < SettingManager.getCellsCount(); i++) {
			Cell tmpCell = UIManager.getCellsContainer().getCell(i + 1);
			int tmpColorNumber = 0;
			if (tmpCell.getColor() != null) {
				tmpColorNumber = tmpCell.getColor().ordinal() + 1;
			}
			cellsString += tmpColorNumber;
			if (i == SettingManager.getCellsCount() - 1) {
				cellsString += "\n";
			} else {
				cellsString += ",";
			}

		}
		// 玩家名
		playerNameString = GeneralManager.getPlayerName() + "\n";
		// 分数
		scoreString = String.valueOf(GeneralManager.getPresentScore()) + "\n";
		// 下一组颜色
		for (int i = 0; i < GeneralManager.getNextColors().size(); i++) {
			int tmpColorNumber = GeneralManager.getNextColors().get(i)
					.ordinal();
			nextColorsString += tmpColorNumber;
			if (i == GeneralManager.getNextColors().size() - 1) {
				nextColorsString += "\n";
			} else {
				nextColorsString += ",";
			}
		}
		// 时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		dateString = sdf.format(new Date()) + "\n";
		// 雷数
		if (SettingManager.gameMode.getValue() == SettingManager.ENHANCED_MODE) {
			bombCountString = String.valueOf(BombManager.getBombCount()) + "\n";
		}
		// 总文本
		String saveString = playerNameString + scoreString + cellsString
				+ nextColorsString + dateString + bombCountString;
		// 写入文件
		Debugger.out("开始写入保存文件");
		MyUtils.initFile(getSavePath());
		return TxtProcesser.write(getSavePath(), saveString);
		// BufferedWriter writer = null;
		// try {
		// writer = new BufferedWriter(new FileWriter(file));
		// Debugger.out(saveString);
		// writer.write(saveString);
		// writer.close();
		// } catch (IOException e) {
		// JOptionPane.showMessageDialog(UIManager.getFrameMain(),
		// "保存失败：文件写入时出错。");
		// e.printStackTrace();
		// return false;
		// }
		// return true;
	}

	/** 从文件获取游戏数据 */
	public static GameData getGameDataFromFile(String gameMode)
			throws Exception {
		// 验证存档
		File file = new File(getSavePath(gameMode));
		if (!file.exists()) {
			Debugger.out("未找到存档:" + getSavePath(gameMode));
			// JOptionPane.showMessageDialog(GameManager.frameMain,
			// "读取失败，未找到"
			// + GameManager.getPlayer().getName() + "的存档。");
			// return null;
			throw new Exception("未找到存档。");
		}
		if (!file.canRead()) {// 文件不存在或无法读取
			Debugger.out("无法读取存档:" + getSavePath(gameMode));
			// JOptionPane
			// .showMessageDialog(GameManager.frameMain, "读取失败，存档无法读取。");
			// return null;
			throw new Exception("该存档无法读取。");
		}
		// 读取
		// String gameMode = null;
		String playerName = null;
		int score = 0;
		Cell[][] cells = null;
		List<Color> tmpNextColors = null;
		Date date = null;
		int bombCount = 0;
		BufferedReader reader = null;
		Debugger.out("gamedata:" + file.getPath());
		try {
			reader = new BufferedReader(new FileReader(file.getPath()));
			// 游戏模式
			// gameMode = GameManager.getGameMode(Integer.parseInt(reader
			// .readLine()));
			// 玩家名
			playerName = reader.readLine();
			if (playerName.equals("")) {
				Debugger.out("保存文件内容为空");
				reader.close();
				return null;
			}
			// 分数
			String scoreString = reader.readLine();
			score = Integer.parseInt(scoreString);
			// 格子颜色
			String cellsString = reader.readLine();
			String[] colorNumberString = cellsString.split(",");
			cells = new Cell[SettingManager.rowCount][SettingManager.columnCount];
			for (int i = 1, j = 1, k = 1; k <= SettingManager.getCellsCount(); k++) {
				cells[i - 1][j - 1] = new Cell(i, j);
				int colorNumber = Integer.parseInt(colorNumberString[k - 1]);
				if (colorNumber != 0) {
					cells[i - 1][j - 1]
							.setColor(Color.values()[colorNumber - 1]);
				}
				// add(cell);// 将格子加到容器中（体现在界面）
				j++;
				if (j > SettingManager.columnCount) {
					i++;
					j = 1;
				}
			}
			// 下一组颜色
			String nextColorString = reader.readLine();
			String[] nextColorNumberString = nextColorString.split(",");
			tmpNextColors = new ArrayList<Color>();
			for (int i = 0; i < nextColorNumberString.length; i++) {
				int colorNumber = Integer.parseInt(nextColorNumberString[i]);
				Color tmpColor = Color.values()[colorNumber];
				tmpNextColors.add(tmpColor);
			}
			// 时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
			date = sdf.parse(reader.readLine());
			// 雷数
			if (gameMode.equals(SettingManager.ENHANCED_MODE)) {
				bombCount = Integer.parseInt(reader.readLine());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			reader.close();
			e.printStackTrace();
		} catch (IOException e) {
			reader.close();
			e.printStackTrace();
		}
		Record tmpRecord = new Record(gameMode, playerName, score);
		Player tmpPlayer = new Player(playerName, tmpRecord);
		GameData gameData = new GameData(tmpPlayer, cells, tmpNextColors, date);
		if (gameMode.equals(SettingManager.ENHANCED_MODE)) {
			gameData.setBombCount(bombCount);
		}
		return gameData;
	}

	/**
	 * 读取游戏
	 * <p>
	 * 不提示，不确认
	 * */
	public static boolean loadGame(String gameMode) {
		return loadGame(gameMode, false, false);
	}

	/** 读取游戏 */
	public static boolean loadGame(String gameMode, boolean noticeFlag,
			boolean confirmFlag) {
		Debugger.out("开始读取游戏");
		GameData gameData = null;
		try {
			gameData = getGameDataFromFile(gameMode);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (gameData == null) {
			if (noticeFlag) {
				JOptionPane.showMessageDialog(UIManager.getFrameMain(), "未找到 "
						+ GeneralManager.getPlayer().getName() + " 的存档。");
			}
			return false;
		}
		if (confirmFlag) {
			int option = JOptionPane.showConfirmDialog(
					UIManager.getFrameMain(), "存档信息：\n" + gameData + "\n是否读取？",
					"读取游戏", JOptionPane.YES_NO_OPTION);
			if (option != 0) {
				return false;
			}
		}
		// 读取成功
		GeneralManager.originalGame = false;
		SettingManager.gameMode.setValue(gameMode);
		SettingManager.gameMode.writeConfigFile();
		GeneralManager.restart(false, gameData);
		MessageManager.addMessage("已读取游戏存档：" + gameData);
		Debugger.out("已读取游戏存档：" + gameData);
		return true;
	}

	/** 根据选项读取游戏 */
	public static boolean loadGameByOption(String loadOption) {
		String gameMode = SettingManager.gameMode.getValue();
		try {
			if (getGameDataFromFile(gameMode) == null) {// 若当前模式无存档
				// 切换到存档的游戏模式
				if (gameMode.equals(SettingManager.CLASSICAL_MODE)) {
					gameMode = SettingManager.ENHANCED_MODE;
				} else {
					gameMode = SettingManager.CLASSICAL_MODE;
				}
				// 读取另一个模式的存档
				if (getGameDataFromFile(gameMode) == null) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		switch (loadOption) {
		case SettingManager.LOAD_OPTION1:
			return loadGame(gameMode);
		case SettingManager.LOAD_OPTION2:
			return loadGame(gameMode, false, true);
		case SettingManager.LOAD_OPTION3:
			break;
		}
		return false;
	}

	// public static void setLoadOption(String loadOption) {
	// GameDataManager.loadOption = loadOption;
	// MyUtils.initFile(SettingManager.getConfigFilePath());
	// try {
	// IniProcesser.WritePrivateProfileString(
	// SettingManager.getConfigFilePath(), "Settings",
	// "loadOption", loadOption);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// }

	/** 删除 */
	public static boolean delete(String gameMode) {
		File file = new File(getSavePath(gameMode));
		if (file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	public static String getSavePath() {
		return getSavePath(SettingManager.gameMode.getValue());
	}

	public static String getSavePath(String gameMode) {
		String savePath = saveFileParentPath + GeneralManager.getPlayerName()
				+ "/";
		switch (gameMode) {
		case SettingManager.CLASSICAL_MODE:
			savePath += classcialSaveFileName;
			break;
		case SettingManager.ENHANCED_MODE:
			savePath += enhancedSaveFileName;
			break;
		}
		return savePath;
	}
}
