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
 * 格子容器
 * */
public class CellsContainer extends JPanel {
	private static final long serialVersionUID = 1L;
	/** 格子集 */
	public Cell[][] cells;
	private int markI = 1;
	private int markJ = 0;
	private int rowCount;
	private int columnCount;
	private boolean isActive;
	/** 活跃的球 */
	private Cell activeBall;
	/** 是否是预览 */
	private boolean isPreview;

	/**
	 * 构造CellsContainer
	 * 
	 * @param rowCount
	 *            行数
	 * @param columnCount
	 *            列数
	 */
	public CellsContainer(int rowCount, int columnCount) {
		this(rowCount, columnCount, null);
	}

	/**
	 * 构造CellsContainer
	 * 
	 * @param rowCount
	 *            行数
	 * @param columnCount
	 *            列数
	 * @param cells
	 *            传入的格子集
	 */
	public CellsContainer(int rowCount, int columnCount, Cell[][] cells) {
		this(rowCount, columnCount, cells, false);
	}

	/**
	 * 构造CellsContainer
	 * 
	 * @param rowCount
	 *            行数
	 * @param columnCount
	 *            列数
	 * @param cells
	 *            传入的格子集
	 * @param isPreview
	 *            是否是预览
	 */
	public CellsContainer(int rowCount, int columnCount, Cell[][] cells,
			boolean isPreview) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		setLayout(new GridLayout(rowCount, columnCount, 0, 0));
		if (cells != null) {// 传入了cells
			setCells(cells);
			if (isPreview) {
				setPreview(isPreview);
			}
		} else {
			initCells();
		}
	}

	/**
	 * 初始化cells
	 * */
	public void initCells() {
		cells = new Cell[rowCount][columnCount];
		for (int i = 1, j = 1, k = 1; k <= getCellsCount(); k++) {
			Cell cell = new Cell(i, j, this);
			add(cell);// 将格子加到容器中（体现在界面）
			addToCells(cell);// 将格子加到cells中（存储在数据结构）
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
			add(cell);// 将格子加到容器中（体现在界面）
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
	 * 放置球
	 * 
	 * @param ballCount
	 *            放置的个数
	 * @return 所放位置
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

	/** 展示移动路径 */
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
	 * 创建移动路径
	 * <p>
	 * A*算法
	 * 
	 * @param source
	 *            源点
	 * @param destination
	 *            目标点
	 * 
	 * */
	public List<Cell> creatMovingPath(Cell source, Cell destination) {
		/** 开集 */
		List<Cell> open = new LinkedList<Cell>();
		/** 闭集 */
		List<Cell> close = new ArrayList<Cell>();
		/** 是否找到目标 */
		boolean find = false;
		// 源点加入开集
		source.setCostF(0);
		open.add(source);
		// 寻找目标点
		while (!open.isEmpty()) {
			// 在开集中选出F代价最低的格子
			Cell costFlessCell = open.get(0);
			// 是否找到了目标点
			if (costFlessCell == destination) {
				find = true;
				break;
			}
			// 求costFlessCell周围的空格子的代价
			List<Cell> aroundSpace = costFlessCell.getAroundSpace();
			for (int i = 0; i < aroundSpace.size(); i++) {
				Cell tmpCell = aroundSpace.get(i);
				// 若在闭集中，跳过
				if (close.contains(tmpCell)) {
					continue;
				}
				// 指定父cell
				tmpCell.setFather(costFlessCell);
				if (open.contains(tmpCell)) {// 若在开集中，更新代价
					// 更新实际代价
					tmpCell.updateCostG(costFlessCell);
					// 更新代价
					tmpCell.updateCostF();
				} else {// 不在开集中，加入开集，设置代价
					// 加入开集
					open.add(tmpCell);
					// 设置估计代价
					tmpCell.setCostH(destination);
					// 设置实际代价
					tmpCell.setCostG(costFlessCell.getCostG() + 1);
					// 更新代价
					tmpCell.updateCostF();
				}
			}
			// 从开集移动到闭集
			open.remove(costFlessCell);
			close.add(costFlessCell);
			// 排序
			Collections.sort(open, new CostComparator());
		}
		/** 路径集 */
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
	 * 处理连珠
	 * <p>
	 * 判断cells集中的各个cell能否分别处于连珠
	 * 
	 * @param cells
	 *            落子集
	 * @return true 成功处理连珠<br>
	 *         false 没有连珠，无法处理
	 * */
	public boolean dealWithLinez(List<Cell> cells) {
		/** 总得分 */
		int totalScore = 0;
		/** 无法构成连珠的落子数 */
		int unLinezCount = 0;
		for (int i = 0; i < cells.size(); i++) {
			// 获取连珠
			List<Cell> linez = getLinez(cells.get(i));
			if (linez == null || linez.isEmpty()) {
				// 该落子无连珠
				unLinezCount++;
				continue;
			}
			// 首次连珠
			if (GeneralManager.firstBlood) {
				MessageManager.addMessage("恭喜你获得了第一滴血！");
				GeneralManager.firstBlood = false;
			}
			// 连击数
			GeneralManager.addComboCount(1);
			if (GeneralManager.getComboCount() > 1) {
				MessageManager.addMessage(GeneralManager.getComboCount()
						+ "连击！");
			}
			// 得分小计
			int score = linez.size() * 2
					+ (linez.size() - SettingManager.minLinezSize)
					+ (GeneralManager.getComboCount() - 1);
			totalScore += score;
			// 连珠的球消失
			for (int j = 0; j < linez.size(); j++) {
				Cell tmpCell = linez.get(j);
				tmpCell.setBall(false);
				tmpCell.disappear();
			}
		}
		if (unLinezCount == cells.size()) {// 若无法构成连珠的落子数=落子数，即无任何连珠
			return false;
		} else {// 有连珠
			// 得分
			GeneralManager.addScore(totalScore);
			// 显示消息
			MessageManager.addMessage("获得" + totalScore + "分。");
			// 音效只播放一次
			WavPlayer.playSound(Resource.linezSound);
			// 尝试更新榜样
			RecordManager.updateModel();
			// if (GeneralManager.getPresentScore() > GeneralManager
			// .getModelScore()) {// 若当前分数大于榜样分数
			// if (GeneralManager.getPlayerName().equals(
			// GeneralManager.getModelName())) {// 若榜样是自己
			// if (!GeneralManager.isNoMoreModel()) {// 若仍有榜样
			// MessageManager.addMessage("你超越了自我！");
			//
			// RecordManager.updateModel();// 更新榜样
			// }
			// } else {// 若榜样不是自己
			// MessageManager.addMessage("恭喜你超越了 "
			// + GeneralManager.getModelName() + " ！");
			// RecordManager.updateModel();// 更新榜样
			// }
			// }
			return true;
		}
	}

	/**
	 * 获取经过cell所在位置的所有连珠
	 * */
	public List<Cell> getLinez(Cell cell) {
		if (cell.getColor() == null || !cell.hasBall()) {
			Debugger.out("failed to getLinez:cell " + cell
					+ " is not a ball/cell don't have color");
			return null;
		}
		List<Cell> linez = new ArrayList<Cell>();
		// 横
		List<Cell> line = getLine(cell, 0, 1);
		linez = MyUtils.merge(linez, line);
		// 竖
		line = getLine(cell, 1, 0);
		linez = MyUtils.merge(linez, line);
		// 正斜
		line = getLine(cell, 1, 1);
		linez = MyUtils.merge(linez, line);
		// 反斜
		line = getLine(cell, 1, -1);
		linez = MyUtils.merge(linez, line);
		return linez;
	}

	/**
	 * 获取一条连珠
	 * 
	 * @param source
	 *            源点
	 * @param offsetI
	 *            纵偏移量
	 * @param offsetJ
	 *            横偏移量
	 * */
	public List<Cell> getLine(Cell source, int offsetI, int offsetJ) {
		// 正方向
		List<Cell> tmpLine1 = new ArrayList<Cell>();// 假想连珠
		tmpLine1.add(source);
		int count = 1;
		int tempOffsetI = offsetI;
		int tempOffsetJ = offsetJ;
		Color exColor = null;
		while (getCell(source.getI() + tempOffsetI, source.getJ() + tempOffsetJ) != null) {// cell在界内
			Cell nextCell = getCell(source.getI() + tempOffsetI, source.getJ()
					+ tempOffsetJ);
			if (!nextCell.hasBall()) {// 不是球
				break;
			}
			if (source.getColor().equals(Color.COLORFUL)) {// 若源点是彩球
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
					if (exColor == null) {// 若前一点颜色未定义
						exColor = nextCell.getColor();
					} else {// 若前一点颜色已定义
						if (!exColor.equals(nextCell.getColor())) {// 若下一点和前一点颜色不同
							break;
						}
					}
				}
			} else {// 若源点不是彩球
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
					if (!source.getColor().equals(nextCell.getColor())) {//
						// 若源点和下一点不同色
						break;
					}
				}
			}
			// 连
			count++;
			tmpLine1.add(nextCell);
			// 偏移
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
		// 反方向
		List<Cell> tmpLine2 = new ArrayList<Cell>();
		tmpLine2.add(source);
		tempOffsetI = offsetI;
		tempOffsetJ = offsetJ;
		while (getCell(source.getI() - tempOffsetI, source.getJ() - tempOffsetJ) != null) {// cell在界内
			Cell nextCell = getCell(source.getI() - tempOffsetI, source.getJ()
					- tempOffsetJ);
			if (!nextCell.hasBall()) {// 不是球
				break;
			}
			if (source.getColor().equals(Color.COLORFUL)) {// 若源点是彩球
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
					if (exColor == null) {// 若前一点颜色未定义
						exColor = nextCell.getColor();
					} else {// 若前一点颜色已定义
						if (!exColor.equals(nextCell.getColor())) {// 若下一点和前一点颜色不同
							break;
						}
					}
				}
			} else {// 若源点不是彩球
				if (!nextCell.getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
					if (!source.getColor().equals(nextCell.getColor())) {//
						// 若源点和下一点不同色
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
		// 总计
		if (count <= SettingManager.minLinezSize - 1) {// 不够长
			return new ArrayList<Cell>();
		} else {
			return MyUtils.merge(tmpLine1, tmpLine2);
		}
	}

	/**
	 * 获取cell
	 * 
	 * @param id
	 *            （>=1）
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
	 * 获取cell
	 * 
	 * @param i
	 *            纵坐标（>=1）
	 * @param j
	 *            横坐标（>=1）
	 * @return 若越界无法获取，则返回null
	 * */
	public Cell getCell(int i, int j) {
		if (i >= 1 && i <= rowCount && j >= 1 && j <= columnCount) {
			return cells[i - 1][j - 1];
		} else {
			return null;
		}
	}

	/** 设置颜色文本可视 */
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
	 * 克隆所有cell
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
