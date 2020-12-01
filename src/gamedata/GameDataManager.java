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

	/** �����ļ���·�� */
	private static String saveFileParentPath = "./dat/";
	/** ����ģʽ�����ļ��� */
	private static String classcialSaveFileName = "save1.dat";
	/** ��ǿģʽ�����ļ��� */
	private static String enhancedSaveFileName = "save2.dat";

	// /** ��ȡѡ�� */
	// public static String loadOption;
	// /** ��ȡѡ��1���Զ���ȡ */
	// public static final String loadOption1 = "auto";
	// /** ��ȡѡ��2��ѯ�� */
	// public static final String loadOption2 = "ask";
	// /** ��ȡѡ��3������ȡ */
	// public static final String loadOption3 = "never";

	// private GameData gameData;

	/**
	 * ������Ϸ
	 * <p>
	 * ���ɱ����ļ��� �����������£�<br>
	 * �����<br>
	 * ����<br>
	 * ������ɫ<br>
	 * ��һ����ɫ<br>
	 * ʱ��<br>
	 * ��ը������
	 * */
	public static boolean saveGame() {
		// ����д���ļ�
		File file = new File(getSavePath());
		if (file.exists()) {
			int option = JOptionPane.showConfirmDialog(
					UIManager.getFrameMain(), "���д浵���Ƿ񸲸ǣ�", "��ʾ",
					JOptionPane.YES_NO_OPTION);
			if (option != 0) {
				return false;
			}
		}
		MyUtils.initFile(getSavePath());
		// ���������ı�
		// String gameModeString = "";
		String cellsString = "";
		String playerNameString = "";
		String scoreString = "";
		String nextColorsString = "";
		String dateString = "";
		String bombCountString = "";
		// ��Ϸģʽ
		// gameModeString = Integer.toString(GameManager
		// .getGameModeIndex(GameManager.getGameMode()));
		// ������ɫ
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
		// �����
		playerNameString = GeneralManager.getPlayerName() + "\n";
		// ����
		scoreString = String.valueOf(GeneralManager.getPresentScore()) + "\n";
		// ��һ����ɫ
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
		// ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		dateString = sdf.format(new Date()) + "\n";
		// ����
		if (SettingManager.gameMode.getValue() == SettingManager.ENHANCED_MODE) {
			bombCountString = String.valueOf(BombManager.getBombCount()) + "\n";
		}
		// ���ı�
		String saveString = playerNameString + scoreString + cellsString
				+ nextColorsString + dateString + bombCountString;
		// д���ļ�
		Debugger.out("��ʼд�뱣���ļ�");
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
		// "����ʧ�ܣ��ļ�д��ʱ����");
		// e.printStackTrace();
		// return false;
		// }
		// return true;
	}

	/** ���ļ���ȡ��Ϸ���� */
	public static GameData getGameDataFromFile(String gameMode)
			throws Exception {
		// ��֤�浵
		File file = new File(getSavePath(gameMode));
		if (!file.exists()) {
			Debugger.out("δ�ҵ��浵:" + getSavePath(gameMode));
			// JOptionPane.showMessageDialog(GameManager.frameMain,
			// "��ȡʧ�ܣ�δ�ҵ�"
			// + GameManager.getPlayer().getName() + "�Ĵ浵��");
			// return null;
			throw new Exception("δ�ҵ��浵��");
		}
		if (!file.canRead()) {// �ļ������ڻ��޷���ȡ
			Debugger.out("�޷���ȡ�浵:" + getSavePath(gameMode));
			// JOptionPane
			// .showMessageDialog(GameManager.frameMain, "��ȡʧ�ܣ��浵�޷���ȡ��");
			// return null;
			throw new Exception("�ô浵�޷���ȡ��");
		}
		// ��ȡ
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
			// ��Ϸģʽ
			// gameMode = GameManager.getGameMode(Integer.parseInt(reader
			// .readLine()));
			// �����
			playerName = reader.readLine();
			if (playerName.equals("")) {
				Debugger.out("�����ļ�����Ϊ��");
				reader.close();
				return null;
			}
			// ����
			String scoreString = reader.readLine();
			score = Integer.parseInt(scoreString);
			// ������ɫ
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
				// add(cell);// �����Ӽӵ������У������ڽ��棩
				j++;
				if (j > SettingManager.columnCount) {
					i++;
					j = 1;
				}
			}
			// ��һ����ɫ
			String nextColorString = reader.readLine();
			String[] nextColorNumberString = nextColorString.split(",");
			tmpNextColors = new ArrayList<Color>();
			for (int i = 0; i < nextColorNumberString.length; i++) {
				int colorNumber = Integer.parseInt(nextColorNumberString[i]);
				Color tmpColor = Color.values()[colorNumber];
				tmpNextColors.add(tmpColor);
			}
			// ʱ��
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
			date = sdf.parse(reader.readLine());
			// ����
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
	 * ��ȡ��Ϸ
	 * <p>
	 * ����ʾ����ȷ��
	 * */
	public static boolean loadGame(String gameMode) {
		return loadGame(gameMode, false, false);
	}

	/** ��ȡ��Ϸ */
	public static boolean loadGame(String gameMode, boolean noticeFlag,
			boolean confirmFlag) {
		Debugger.out("��ʼ��ȡ��Ϸ");
		GameData gameData = null;
		try {
			gameData = getGameDataFromFile(gameMode);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (gameData == null) {
			if (noticeFlag) {
				JOptionPane.showMessageDialog(UIManager.getFrameMain(), "δ�ҵ� "
						+ GeneralManager.getPlayer().getName() + " �Ĵ浵��");
			}
			return false;
		}
		if (confirmFlag) {
			int option = JOptionPane.showConfirmDialog(
					UIManager.getFrameMain(), "�浵��Ϣ��\n" + gameData + "\n�Ƿ��ȡ��",
					"��ȡ��Ϸ", JOptionPane.YES_NO_OPTION);
			if (option != 0) {
				return false;
			}
		}
		// ��ȡ�ɹ�
		GeneralManager.originalGame = false;
		SettingManager.gameMode.setValue(gameMode);
		SettingManager.gameMode.writeConfigFile();
		GeneralManager.restart(false, gameData);
		MessageManager.addMessage("�Ѷ�ȡ��Ϸ�浵��" + gameData);
		Debugger.out("�Ѷ�ȡ��Ϸ�浵��" + gameData);
		return true;
	}

	/** ����ѡ���ȡ��Ϸ */
	public static boolean loadGameByOption(String loadOption) {
		String gameMode = SettingManager.gameMode.getValue();
		try {
			if (getGameDataFromFile(gameMode) == null) {// ����ǰģʽ�޴浵
				// �л����浵����Ϸģʽ
				if (gameMode.equals(SettingManager.CLASSICAL_MODE)) {
					gameMode = SettingManager.ENHANCED_MODE;
				} else {
					gameMode = SettingManager.CLASSICAL_MODE;
				}
				// ��ȡ��һ��ģʽ�Ĵ浵
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

	/** ɾ�� */
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
