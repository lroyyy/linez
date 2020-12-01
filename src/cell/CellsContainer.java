package cell;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import message.MessageManager;
import record.RecordManager;
import resource.Color;
import resource.Resource;
import setting.SettingManager;
import util.Debugger;
import util.MyUtils;
import audio.WavPlayer;
import element.GeneralManager;

/**
 * ��������
 * */
public class CellsContainer extends JPanel {
	private static final long serialVersionUID = 1L;
	/** ���Ӽ� */
	public Cell[][] cells;
	private int markI = 1;
	private int markJ = 0;
	private int rowCount;
	private int columnCount;
	private boolean isActive;
	/** ��Ծ���� */
	private Cell activeBall;
	/** �Ƿ���Ԥ�� */
	private boolean isPreview;

	/**
	 * ����CellsContainer
	 * 
	 * @param rowCount
	 *            ����
	 * @param columnCount
	 *            ����
	 */
	public CellsContainer(int rowCount, int columnCount) {
		this(rowCount, columnCount, null);
	}

	/**
	 * ����CellsContainer
	 * 
	 * @param rowCount
	 *            ����
	 * @param columnCount
	 *            ����
	 * @param cells
	 *            ����ĸ��Ӽ�
	 */
	public CellsContainer(int rowCount, int columnCount, Cell[][] cells) {
		this(rowCount, columnCount, cells, false);
	}

	/**
	 * ����CellsContainer
	 * 
	 * @param rowCount
	 *            ����
	 * @param columnCount
	 *            ����
	 * @param cells
	 *            ����ĸ��Ӽ�
	 * @param isPreview
	 *            �Ƿ���Ԥ��
	 */
	public CellsContainer(int rowCount, int columnCount, Cell[][] cells,
			boolean isPreview) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		setLayout(new GridLayout(rowCount, columnCount, 0, 0));
		if (cells != null) {// ������cells
			setCells(cells);
			if (isPreview) {
				setPreview(isPreview);
			}
		} else {
			initCells();
		}
	}

	/**
	 * ��ʼ��cells
	 * */
	public void initCells() {
		cells = new Cell[rowCount][columnCount];
		for (int i = 1, j = 1, k = 1; k <= getCellsCount(); k++) {
			Cell cell = new Cell(i, j, this);
			add(cell);// �����Ӽӵ������У������ڽ��棩
			addToCells(cell);// �����Ӽӵ�cells�У��洢�����ݽṹ��
			j++;
			if (j > columnCount) {
				i++;
				j = 1;
			}
		}
	}

	private int getCellsCount() {
		return rowCount * columnCount;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
		for (int i = 1, j = 1, k = 1; k <= cells.length * cells[0].length; k++) {
			Cell cell = cells[i - 1][j - 1];
			cell.setContainer(this);
			add(cell);// �����Ӽӵ������У������ڽ��棩
			if (cell.getColor() != null) {
				cell.setBall(true);
				cell.setBallIcon();
			}
			j++;
			if (j > columnCount) {
				i++;
				j = 1;
			}
		}
	}

	/** add cell to cells */
	public void addToCells(Cell cell) {
		if (markJ != columnCount) {
			markJ++;
		} else if (markI != rowCount) {
			markI++;
			markJ = 1;
		} else {
			System.out
					.println("full!(markI=" + markI + ",markJ=" + markJ + ")");
			return;
		}
		cells[markI - 1][markJ - 1] = cell;
	}

	/**
	 * ������
	 * 
	 * @param ballCount
	 *            ���õĸ���
	 * @return ����λ��
	 * */
	public List<Cell> placeBalls(int ballCount) {
		int cellCount = SettingManager.columnCount * SettingManager.rowCount;
		Random randomBallIndex = new Random();
		List<Cell> newBalls = new ArrayList<Cell>();
		for (int i = 0; i < ballCount;) {
			if (isFull()) {
				break;
			}
			int tempId = randomBallIndex.nextInt(cellCount) + 1;
			if (!getCell(tempId).hasBall()) {
				getCell(tempId).setBall(true);
				getCell(tempId).setColor(GeneralManager.getNextColors().get(i));
				i++;
				getCell(tempId).appear();
				newBalls.add(getCell(tempId));
			}
		}
		return newBalls;
	}

	public boolean isFull() {
		for (int i = 0; i < columnCount * rowCount; i++) {
			if (!getCell(i + 1).hasBall()) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		for (int i = 0; i < columnCount * rowCount; i++) {
			if (getCell(i + 1).hasBall()) {
				return false;
			}
		}
		return true;
	}

	/** չʾ�ƶ�·�� */
	public void showMovingPath(final List<Cell> cells, final Color color) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < cells.size(); i++) {
					final Cell cell = cells.get(i);
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							cell.setIcon(color.getResource().getImageIcon(
									"48px"));
						}
					});
					try {
						Thread.sleep(SettingManager.ballMovingInterval
								.getValue());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i < cells.size() - 1) {
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								cell.setIcon(null);
							}
						});
					}
				}
			}
		}).start();
	}

	/**
	 * �����ƶ�·��
	 * <p>
	 * A*�㷨
	 * 
	 * @param source
	 *            Դ��
	 * @param destination
	 *            Ŀ���
	 * 
	 * */
	public List<Cell> creatMovingPath(Cell source, Cell destination) {
		/** ���� */
		List<Cell> open = new LinkedList<Cell>();
		/** �ռ� */
		List<Cell> close = new ArrayList<Cell>();
		/** �Ƿ��ҵ�Ŀ�� */
		boolean find = false;
		// Դ����뿪��
		source.setCostF(0);
		open.add(source);
		// Ѱ��Ŀ���
		while (!open.isEmpty()) {
			// �ڿ�����ѡ��F������͵ĸ���
			Cell costFlessCell = open.get(0);
			// �Ƿ��ҵ���Ŀ���
			if (costFlessCell == destination) {
				find = true;
				break;
			}
			// ��costFlessCell��Χ�Ŀո��ӵĴ���
			List<Cell> aroundSpace = costFlessCell.getAroundSpace();
			for (int i = 0; i < aroundSpace.size(); i++) {
				Cell tmpCell = aroundSpace.get(i);
				// ���ڱռ��У�����
				if (close.contains(tmpCell)) {
					continue;
				}
				// ָ����cell
				tmpCell.setFather(costFlessCell);
				if (open.contains(tmpCell)) {// ���ڿ����У����´���
					// ����ʵ�ʴ���
					tmpCell.updateCostG(costFlessCell);
					// ���´���
					tmpCell.updateCostF();
				} else {// ���ڿ����У����뿪�������ô���
					// ���뿪��
					open.add(tmpCell);
					// ���ù��ƴ���
					tmpCell.setCostH(destination);
					// ����ʵ�ʴ���
					tmpCell.setCostG(costFlessCell.getCostG() + 1);
					// ���´���
					tmpCell.updateCostF();
				}
			}
			// �ӿ����ƶ����ռ�
			open.remove(costFlessCell);
			close.add(costFlessCell);
			// ����
			Collections.sort(open, new CostComparator());
		}
		/** ·���� */
		List<Cell> path = new ArrayList<Cell>();
		if (!find) {
			return path;
		}
		Cell tmpCell = destination;
		path.add(destination);
		while (tmpCell != source) {
			if (tmpCell.getFather() != null) {
				tmpCell = tmpCell.getFather();
			}
			path.add(tmpCell);
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * ��������
	 * <p>
	 * �ж�cells���еĸ���cell�ܷ�ֱ�������
	 * 
	 * @param cells
	 *            ���Ӽ�
	 * @return true �ɹ���������<br>
	 *         false û�����飬�޷�����
	 * */
	public boolean dealWithLinez(List<Cell> cells) {
		/** �ܵ÷� */
		int totalScore = 0;
		/** �޷���������������� */
		int unLinezCount = 0;
		for (int i = 0; i < cells.size(); i++) {
			// ��ȡ����
			List<Cell> linez = getLinez(cells.get(i));
			if (linez == null || linez.isEmpty()) {
				// ������������
				unLinezCount++;
				continue;
			}
			// �״�����
			if (GeneralManager.firstBlood) {
				MessageManager.addMessage("��ϲ�����˵�һ��Ѫ��");
				GeneralManager.firstBlood = false;
			}
			// ������
			GeneralManager.addComboCount(1);
			if (GeneralManager.getComboCount() > 1) {
				MessageManager.addMessage(GeneralManager.getComboCount()
						+ "������");
			}
			// �÷�С��
			int score = linez.size() * 2
					+ (linez.size() - SettingManager.minLinezSize)
					+ (GeneralManager.getComboCount() - 1);
			totalScore += score;
			// ���������ʧ
			for (int j = 0; j < linez.size(); j++) {
				Cell tmpCell = linez.get(j);
				tmpCell.setBall(false);
				tmpCell.disappear();
			}
		}
		if (unLinezCount == cells.size()) {// ���޷����������������=�������������κ�����
			return false;
		} else {// ������
			// �÷�
			GeneralManager.addScore(totalScore);
			// ��ʾ��Ϣ
			MessageManager.addMessage("���" + totalScore + "�֡�");
			// ��Чֻ����һ��
			WavPlayer.playSound(Resource.linezSound);
			// ���Ը��°���
			RecordManager.updateModel();
			// if (GeneralManager.getPresentScore() > GeneralManager
			// .getModelScore()) {// ����ǰ�������ڰ�������
			// if (GeneralManager.getPlayerName().equals(
			// GeneralManager.getModelName())) {// ���������Լ�
			// if (!GeneralManager.isNoMoreModel()) {// �����а���
			// MessageManager.addMessage("�㳬Խ�����ң�");
			//
			// RecordManager.updateModel();// ���°���
			// }
			// } else {// �����������Լ�
			// MessageManager.addMessage("��ϲ�㳬Խ�� "
			// + GeneralManager.getModelName() + " ��");
			// RecordManager.updateModel();// ���°���
			// }
			// }
			return true;
		}
	}

	/**
	 * ��ȡ����cell����λ�õ���������
	 * */
	public List<Cell> getLinez(Cell cell) {
		if (cell.getColor() == null || !cell.hasBall()) {
			Debugger.out("failed to getLinez:cell " + cell
					+ " is not a ball/cell don't have color");
			return null;
		}
		List<Cell> linez = new ArrayList<Cell>();
		// ��
		List<Cell> line = getLine(cell, 0, 1);
		linez = MyUtils.merge(linez, line);
		// ��
		line = getLine(cell, 1, 0);
		linez = MyUtils.merge(linez, line);
		// ��б
		line = getLine(cell, 1, 1);
		linez = MyUtils.merge(linez, line);
		// ��б
		line = getLine(cell, 1, -1);
		linez = MyUtils.merge(linez, line);
		return linez;
	}

	/**
	 * ��ȡһ������
	 * 
	 * @param source
	 *            Դ��
	 * @param offsetI
	 *            ��ƫ����
	 * @param offsetJ
	 *            ��ƫ����
	 * */
	public List<Cell> getLine(Cell source, int offsetI, int offsetJ) {
		// ������
		List<Cell> tmpLine1 = new ArrayList<Cell>();// ��������
		tmpLine1.add(source);
		int count = 1;
		int tempOffsetI = offsetI;
		int tempOffsetJ = offsetJ;
		Color exColor = null;
		while (getCell(source.getI() + tempOffsetI, source.getJ() + tempOffsetJ) != null) {// cell�ڽ���
			Cell nextCell = getCell(source.getI() + tempOffsetI, source.getJ()
					+ tempOffsetJ);
			if (!nextCell.hasBall()) {// ������
				break;
			}
			if (source.getColor().equals(Color.COLORFUL)) {// ��Դ���ǲ���
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
					if (exColor == null) {// ��ǰһ����ɫδ����
						exColor = nextCell.getColor();
					} else {// ��ǰһ����ɫ�Ѷ���
						if (!exColor.equals(nextCell.getColor())) {// ����һ���ǰһ����ɫ��ͬ
							break;
						}
					}
				}
			} else {// ��Դ�㲻�ǲ���
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
					if (!source.getColor().equals(nextCell.getColor())) {//
						// ��Դ�����һ�㲻ͬɫ
						break;
					}
				}
			}
			// ��
			count++;
			tmpLine1.add(nextCell);
			// ƫ��
			if (offsetI > 0) {
				tempOffsetI++;
			}
			if (offsetI < 0) {
				tempOffsetI--;
			}
			if (offsetJ > 0) {
				tempOffsetJ++;
			}
			if (offsetJ < 0) {
				tempOffsetJ--;
			}
		}
		// ������
		List<Cell> tmpLine2 = new ArrayList<Cell>();
		tmpLine2.add(source);
		tempOffsetI = offsetI;
		tempOffsetJ = offsetJ;
		while (getCell(source.getI() - tempOffsetI, source.getJ() - tempOffsetJ) != null) {// cell�ڽ���
			Cell nextCell = getCell(source.getI() - tempOffsetI, source.getJ()
					- tempOffsetJ);
			if (!nextCell.hasBall()) {// ������
				break;
			}
			if (source.getColor().equals(Color.COLORFUL)) {// ��Դ���ǲ���
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
					if (exColor == null) {// ��ǰһ����ɫδ����
						exColor = nextCell.getColor();
					} else {// ��ǰһ����ɫ�Ѷ���
						if (!exColor.equals(nextCell.getColor())) {// ����һ���ǰһ����ɫ��ͬ
							break;
						}
					}
				}
			} else {// ��Դ�㲻�ǲ���
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
					if (!source.getColor().equals(nextCell.getColor())) {//
						// ��Դ�����һ�㲻ͬɫ
						break;
					}
				}
			}
			count++;
			tmpLine2.add(nextCell);
			if (offsetI > 0) {
				tempOffsetI++;
			}
			if (offsetI < 0) {
				tempOffsetI--;
			}
			if (offsetJ > 0) {
				tempOffsetJ++;
			}
			if (offsetJ < 0) {
				tempOffsetJ--;
			}
		}
		// �ܼ�
		if (count <= SettingManager.minLinezSize - 1) {// ������
			return new ArrayList<Cell>();
		} else {
			return MyUtils.merge(tmpLine1, tmpLine2);
		}
	}

	/**
	 * ��ȡcell
	 * 
	 * @param id
	 *            ��>=1��
	 * */
	public Cell getCell(int id) {
		id--;
		if (id >= 0 && id <= rowCount * columnCount) {
			int i = id / columnCount;
			int j = id % columnCount;
			return cells[i][j];
		} else {
			return null;
		}
	}

	/**
	 * ��ȡcell
	 * 
	 * @param i
	 *            �����꣨>=1��
	 * @param j
	 *            �����꣨>=1��
	 * @return ��Խ���޷���ȡ���򷵻�null
	 * */
	public Cell getCell(int i, int j) {
		if (i >= 1 && i <= rowCount && j >= 1 && j <= columnCount) {
			return cells[i - 1][j - 1];
		} else {
			return null;
		}
	}

	/** ������ɫ�ı����� */
	public void setColorStringVisible(boolean b) {
		for (int i = 1; i < rowCount * columnCount; i++) {
			Cell tmpCell = getCell(i);
			tmpCell.setColorTooltip(b);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
		if (!isActive) {
			setActiveBall(null);
		}
	}

	public Cell getActiveBall() {
		return activeBall;
	}

	public void setActiveBall(Cell activeBall) {
		this.activeBall = activeBall;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
		for (int i = 0; i < columnCount * rowCount; i++) {
			getCell(i + 1).setPreview(isPreview);
		}
	}

	/**
	 * ��¡����cell
	 * */
	@Override
	public CellsContainer clone() throws CloneNotSupportedException {
		Cell[][] clonedCells = new Cell[rowCount][columnCount];
		for (int i = 1, j = 1, k = 1; k <= clonedCells.length
				* clonedCells[0].length; k++) {
			clonedCells[i - 1][j - 1] = cells[i - 1][j - 1].clone();
			j++;
			if (j > columnCount) {
				i++;
				j = 1;
			}
		}
		CellsContainer clonedCellsContainer = new CellsContainer(rowCount,
				columnCount, clonedCells);
		return clonedCellsContainer;
	}

}
