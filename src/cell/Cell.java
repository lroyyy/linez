package cell;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import message.MessageManager;
import resource.Color;
import resource.Resource;
import setting.SettingManager;
import ui.UIManager;
import util.Debugger;
import audio.WavPlayer;
import bomb.BombManager;
import bomb.BombShiningThread;
import element.AnimationWorker;
import element.GeneralManager;

/** 格子 */
public class Cell extends JButton {
	private static final long serialVersionUID = 1L;
	/** 高 */
	public static int height = 50;
	/** 宽 */
	public static int width = 50;
	/** 容器 */
	private CellsContainer container;
	/** 状态 */
	private String state = SILENCE;
	/** 颜色 */
	private Color color;
	/** 状态：已激活 */
	private static final String ACTIVE = "active";
	/** 状态：未激活 */
	private static final String SILENCE = "silence";
	/** 是否是球 */
	private boolean hasBall;
	/** 是否是炸弹 */
	private boolean hasBomb;
	/** 是否是预览 */
	private boolean isPreview;
	/** 纵坐标 */
	private int i;
	/** 横坐标 */
	private int j;
	/** 炸弹闪烁线程 */
	private BombShiningThread bombShiningThread;
	/** 球出现线程 */
	private BallAppearThread ballAppearThread;
	/** 球消失线程 */
	private BallDisappearThread ballDisappearThread;
	/** 球跳动动画 */
	private AnimationWorker ballJumpingAnimationWorker;
	/** 代价 */
	private int costF;
	/** 实际代价 */
	private int costG;
	/** 估计代价 */
	private int costH;
	/** 父节点 */
	private Cell father;

	public Cell() {

	}

	/**
	 * 构造Cell
	 * 
	 * @param i
	 *            格子纵坐标
	 * @param j
	 *            格子横坐标
	 * @param container
	 *            容器
	 */
	public Cell(int i, int j, CellsContainer container) {
		this(i, j);
		this.container = container;
	}

	/**
	 * 构造Cell
	 * 
	 * @param i
	 *            格子纵坐标
	 * @param j
	 *            格子横坐标
	 * @param container
	 *            容器
	 */
	public Cell(int i, int j) {
		// this.id = id;
		this.i = i;
		this.j = j;
		setBounds(new Rectangle(width, height));
		initListeners();
	}

	public void setContainer(CellsContainer container) {
		this.container = container;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void startBallJumping() {
		ballJumpingAnimationWorker = new AnimationWorker(this, getColor()
				.getResource(), new int[] { 0, 1, 2, 1, 0 }, 80, true) {
		};
		ballJumpingAnimationWorker.execute();
	}

	public void stopBallJumping() {
		ballJumpingAnimationWorker.stop();
	}

	public void startBombShining() {
		bombShiningThread = new BombShiningThread(this);
		bombShiningThread.start();
	}

	public void stopBombShining() {
		bombShiningThread.setStop(true);
		setIcon(null);
	}

	public void appear() {
		ballAppearThread = new BallAppearThread(this);
		ballAppearThread.start();
	}

	public void disappear() {
		ballDisappearThread = new BallDisappearThread(this);
		ballDisappearThread.start();
	}

	private void initListeners() {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isPreview) {
					return;
				}
				leftClickAction();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (isPreview) {
					return;
				}
				mouseEnteredAction();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (isPreview) {
					return;
				}
				mouseExitedAction();
			}
		});
	}

	public void mouseEnteredAction() {
		if (hasBall() || !BombManager.bombActive) {
			return;
		}
		startBombShining();
		setBomb(true);
	}

	public void mouseExitedAction() {
		if (hasBall() || !BombManager.bombActive) {
			return;
		}
		stopBombShining();
		setBomb(false);
	}

	/** 左键动作 */
	public void leftClickAction() {
		if (GeneralManager.isLocked) {
			Debugger.out("locked!");
			return;
		}
		// 点选的cell为未激活的球->已激活的球
		if (hasBall() && state == SILENCE) {// 是球且状态为未激活
			// Debugger.out("点选的cell(" + Cell.this + ")为未激活的球");
			GeneralManager.isLocked = true;
			setActive(true);
			GeneralManager.isLocked = false;
			return;
		}
		// 点选的cell为已激活的球->未激活的球
		if (hasBall() && state == ACTIVE && container.isActive()) {// 是球且已激活，容器已激活
			// Debugger.out("点选的cell(" + Cell.this + ")为已激活的球");
			GeneralManager.isLocked = true;
			setActive(false);
			GeneralManager.isLocked = false;
			return;
		}
		// 点选的cell为空且炸弹不活跃，移动球
		if (!hasBall() && container.isActive() && !BombManager.bombActive) {
			// Debugger.out("点选的目标cell(" + Cell.this + ")为空");
			GeneralManager.isLocked = true;
			moveBall();
			if (!GeneralManager.isGameOver()) {
				GeneralManager.isLocked = false;
			}
			return;
		}
		// 点选的cell为待引爆的炸弹
		if (BombManager.bombActive && hasBomb()) {
			sourceBoom();
		}
	}

	/** 移动球 */
	public void moveBall() {
		// 获取移动路径
		final List<Cell> path = container.creatMovingPath(
				container.getActiveBall(), Cell.this);
		if (path != null && path.isEmpty()) {
			WavPlayer.playSound(Resource.blockSound);
			Debugger.out("can't touch");
			MessageManager.addMessage("对不起，无法到达。");
			return;
		}
		// activeBall停止闪烁
		final Cell activeBall = container.getActiveBall();
		container.getActiveBall().stopBallJumping();
		activeBall.setState(SILENCE);
		// 展示路径
		final Color activeBallColor = container.getActiveBall().getColor();
		container.showMovingPath(path, activeBallColor);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(SettingManager.ballMovingInterval.getValue()
							* path.size() + 5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						// try {
						activeBall.setBall(false);
						activeBall.setColor(null);
						setBall(true);
						setColor(activeBallColor);
						setBallIcon();
						container.setActive(false);// 取消容器活跃性
						List<Cell> cells = new ArrayList<Cell>();// 构造落子集
						cells.add(Cell.this);
						boolean hasLinez = container.dealWithLinez(cells);// 尝试处理连珠
						if (!hasLinez) {// 若无连珠
							GeneralManager.resetComboCount();// 重置连击数
							GeneralManager.generateBalls();// 生成球
							if (container.isFull()) {// 若容器满
								JOptionPane.showMessageDialog(
										UIManager.getFrameMain(), "游戏结束！");
								GeneralManager.gameOver();// 游戏结束
								return;
							}
						} else {// 若有连珠
							if (container.isEmpty()) {// 若容器空
								MessageManager.addMessage("好凶残，你清空了所有珠子！");
								GeneralManager.generateBalls();// 生成球
							}
						}
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
					}
				});
			}
		}).start();
	}

	public void setActive(boolean active) {
		if (active) {
			// 使已激活的球安静
			if (container.isActive()) {
				container.getActiveBall().setState(SILENCE);
				container.getActiveBall().stopBallJumping();
			}
			startBallJumping();
			WavPlayer.playSound(Resource.clickSound);
			setState(ACTIVE);
			container.setActive(true);
			container.setActiveBall(Cell.this);
		} else {
			stopBallJumping();
			setBallIcon();
			setState(SILENCE);
			container.setActive(false);
			container.setActiveBall(null);
		}
	}

	/** 爆炸 */
	public void boom() {
		if (hasBall()) {
			setBall(false);
			if (state == ACTIVE) {// 活跃的球
				stopBallJumping();
				setState(SILENCE);
				container.setActive(false);
			}
			disappear();
		}
	}

	/** 源头爆炸 */
	public void sourceBoom() {
		UIManager.shakeUI();
		BombManager.BombCountDown();
		BombManager.bombActive = false;
		UIManager.getFrameMain().getButtonBomb().setAimed(false);
		WavPlayer.playSound(Resource.boomSound);
		// 停止炸弹闪烁
		stopBombShining();
		// 爆炸动画
		AnimationWorker boomAnimationWorker = new AnimationWorker(this,
				Resource.boom, new int[] { 1, 2, 3, 4 }, 150, false) {
			@Override
			protected void done() {
				cell.setIcon(null);
			}
		};
		boomAnimationWorker.execute();
		// 爆炸扩散，十字
		Cell tmpCell = container.getCell(getI() + 1, getJ());
		if (tmpCell != null) {
			tmpCell.boom();
		}
		tmpCell = container.getCell(getI() - 1, getJ());
		if (tmpCell != null) {
			tmpCell.boom();
		}
		tmpCell = container.getCell(getI(), getJ() + 1);
		if (tmpCell != null) {
			tmpCell.boom();
		}
		tmpCell = container.getCell(getI(), getJ() - 1);
		if (tmpCell != null) {
			tmpCell.boom();
		}
		if (container.isEmpty()) {
			GeneralManager.generateBalls();
		}
	}

	/**
	 * 移动自（源点）
	 * 
	 * @param source
	 *            源点
	 * */
	public void moveFrom(Cell source) {

	}

	public void resetCost() {

	}

	public boolean hasBall() {
		return hasBall;
	}

	public void setBall(boolean ball) {
		this.hasBall = ball;
	}

	public boolean hasBomb() {
		return hasBomb;
	}

	public void setBomb(boolean bomb) {
		this.hasBomb = bomb;
	}

	public Color getColor() {
		if (color == null) {
			// Debugger.out("failed to getcolor:color=null");
			return null;
		}
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		if (color != null && SettingManager.colorStringVisible.getValue()) {
			setColorTooltip(true);
		} else {
			setColorTooltip(false);
		}
	}

	public void setColorTooltip(boolean b) {
		if (b && color != null) {
			setToolTipText(color.toString());
		} else {
			setToolTipText(null);
		}
	}

	/** 获取周围的空格 */
	public List<Cell> getAroundSpace() {
		List<Cell> cells = new ArrayList<Cell>();
		if (container.getCell(i - 1, j) != null
				&& !container.getCell(i - 1, j).hasBall()) {
			cells.add(container.getCell(i - 1, j));
		}
		if (container.getCell(i + 1, j) != null
				&& !container.getCell(i + 1, j).hasBall()) {
			cells.add(container.getCell(i + 1, j));
		}
		if (container.getCell(i, j - 1) != null
				&& !container.getCell(i, j - 1).hasBall()) {
			cells.add(container.getCell(i, j - 1));
		}
		if (container.getCell(i, j + 1) != null
				&& !container.getCell(i, j + 1).hasBall()) {
			cells.add(container.getCell(i, j + 1));
		}
		return cells;
	}

	public void setBallIcon() {
		if (color == null) {
			return;
		}
		if (isPreview) {
			setIcon(color.getResource().getImageIcon("16px"));
		} else {
			setIcon(color.getResource().getImageIcon("48px"));
		}
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	@Override
	public String toString() {
		String colorString;
		if (color == null) {
			colorString = null;
		} else {
			colorString = getColor().toString();
		}
		return "fix(" + i + "," + j + ") color:" + colorString + " costF="
				+ getCostG() + "+" + getCostH() + "="
				+ (getCostG() + getCostH());
	}

	public int getCostF() {
		return costF;
	}

	public void updateCostF() {
		costF = costG + costH;
	}

	public void setCostF(int cost) {
		costF = cost;
	}

	public int getCostG() {
		return costG;
	}

	public void updateCostG(Cell father) {
		if (father.getCostG() + 1 < costG) {
			costG = father.getCostG() + 1;
			setFather(father);
		}
	}

	public void setCostG(int cost) {
		costG = cost;
	}

	public int getCostH() {
		return costH;
	}

	public void setCostH(Cell destination) {
		costH = Math.abs(getI() - destination.getI())
				+ Math.abs(getJ() - destination.getJ());
	}

	public Cell getFather() {
		return father;
	}

	public void setFather(Cell father) {
		this.father = father;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
		setBallIcon();
	}

	// public boolean inLine(Cell source, Color exColor) {
	// Debugger.out(toString());
	// if (!hasBall()) {// 不是球
	// return false;
	// }
	// if (source.getColor().equals(Color.COLORFUL)) {// 若源点是彩球
	// if (!getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
	// if (exColor == null) {// 若前一点颜色未定义
	// exColor = getColor();
	// } else {// 若前一点颜色已定义
	// if (!exColor.equals(getColor())) {// 若下一点和前一点颜色不同
	// return false;
	// }
	// }
	// }
	// } else {// 若源点不是彩球
	// if (!getColor().equals(Color.COLORFUL)) {// 若下一点不是彩球
	// if (!source.getColor().equals(getColor())) {// 若源点和下一点不同色
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	/**
	 * 克隆的属性：
	 * <p>
	 * 横坐标i<br>
	 * 纵坐标j<br>
	 * 颜色color<br>
	 * 是否是球
	 * */
	@Override
	protected Cell clone() throws CloneNotSupportedException {
		Cell clonedCell = new Cell(i, j);
		clonedCell.setColor(getColor());
		clonedCell.setBall(hasBall());
		return clonedCell;
	}

}
