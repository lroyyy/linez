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

/** ���� */
public class Cell extends JButton {
	private static final long serialVersionUID = 1L;
	/** �� */
	public static int height = 50;
	/** �� */
	public static int width = 50;
	/** ���� */
	private CellsContainer container;
	/** ״̬ */
	private String state = SILENCE;
	/** ��ɫ */
	private Color color;
	/** ״̬���Ѽ��� */
	private static final String ACTIVE = "active";
	/** ״̬��δ���� */
	private static final String SILENCE = "silence";
	/** �Ƿ����� */
	private boolean hasBall;
	/** �Ƿ���ը�� */
	private boolean hasBomb;
	/** �Ƿ���Ԥ�� */
	private boolean isPreview;
	/** ������ */
	private int i;
	/** ������ */
	private int j;
	/** ը����˸�߳� */
	private BombShiningThread bombShiningThread;
	/** ������߳� */
	private BallAppearThread ballAppearThread;
	/** ����ʧ�߳� */
	private BallDisappearThread ballDisappearThread;
	/** ���������� */
	private AnimationWorker ballJumpingAnimationWorker;
	/** ���� */
	private int costF;
	/** ʵ�ʴ��� */
	private int costG;
	/** ���ƴ��� */
	private int costH;
	/** ���ڵ� */
	private Cell father;

	public Cell() {

	}

	/**
	 * ����Cell
	 * 
	 * @param i
	 *            ����������
	 * @param j
	 *            ���Ӻ�����
	 * @param container
	 *            ����
	 */
	public Cell(int i, int j, CellsContainer container) {
		this(i, j);
		this.container = container;
	}

	/**
	 * ����Cell
	 * 
	 * @param i
	 *            ����������
	 * @param j
	 *            ���Ӻ�����
	 * @param container
	 *            ����
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

	/** ������� */
	public void leftClickAction() {
		if (GeneralManager.isLocked) {
			Debugger.out("locked!");
			return;
		}
		// ��ѡ��cellΪδ�������->�Ѽ������
		if (hasBall() && state == SILENCE) {// ������״̬Ϊδ����
			// Debugger.out("��ѡ��cell(" + Cell.this + ")Ϊδ�������");
			GeneralManager.isLocked = true;
			setActive(true);
			GeneralManager.isLocked = false;
			return;
		}
		// ��ѡ��cellΪ�Ѽ������->δ�������
		if (hasBall() && state == ACTIVE && container.isActive()) {// �������Ѽ�������Ѽ���
			// Debugger.out("��ѡ��cell(" + Cell.this + ")Ϊ�Ѽ������");
			GeneralManager.isLocked = true;
			setActive(false);
			GeneralManager.isLocked = false;
			return;
		}
		// ��ѡ��cellΪ����ը������Ծ���ƶ���
		if (!hasBall() && container.isActive() && !BombManager.bombActive) {
			// Debugger.out("��ѡ��Ŀ��cell(" + Cell.this + ")Ϊ��");
			GeneralManager.isLocked = true;
			moveBall();
			if (!GeneralManager.isGameOver()) {
				GeneralManager.isLocked = false;
			}
			return;
		}
		// ��ѡ��cellΪ��������ը��
		if (BombManager.bombActive && hasBomb()) {
			sourceBoom();
		}
	}

	/** �ƶ��� */
	public void moveBall() {
		// ��ȡ�ƶ�·��
		final List<Cell> path = container.creatMovingPath(
				container.getActiveBall(), Cell.this);
		if (path != null && path.isEmpty()) {
			WavPlayer.playSound(Resource.blockSound);
			Debugger.out("can't touch");
			MessageManager.addMessage("�Բ����޷����");
			return;
		}
		// activeBallֹͣ��˸
		final Cell activeBall = container.getActiveBall();
		container.getActiveBall().stopBallJumping();
		activeBall.setState(SILENCE);
		// չʾ·��
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
						container.setActive(false);// ȡ��������Ծ��
						List<Cell> cells = new ArrayList<Cell>();// �������Ӽ�
						cells.add(Cell.this);
						boolean hasLinez = container.dealWithLinez(cells);// ���Դ�������
						if (!hasLinez) {// ��������
							GeneralManager.resetComboCount();// ����������
							GeneralManager.generateBalls();// ������
							if (container.isFull()) {// ��������
								JOptionPane.showMessageDialog(
										UIManager.getFrameMain(), "��Ϸ������");
								GeneralManager.gameOver();// ��Ϸ����
								return;
							}
						} else {// ��������
							if (container.isEmpty()) {// ��������
								MessageManager.addMessage("���ײУ���������������ӣ�");
								GeneralManager.generateBalls();// ������
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
			// ʹ�Ѽ�����򰲾�
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

	/** ��ը */
	public void boom() {
		if (hasBall()) {
			setBall(false);
			if (state == ACTIVE) {// ��Ծ����
				stopBallJumping();
				setState(SILENCE);
				container.setActive(false);
			}
			disappear();
		}
	}

	/** Դͷ��ը */
	public void sourceBoom() {
		UIManager.shakeUI();
		BombManager.BombCountDown();
		BombManager.bombActive = false;
		UIManager.getFrameMain().getButtonBomb().setAimed(false);
		WavPlayer.playSound(Resource.boomSound);
		// ֹͣը����˸
		stopBombShining();
		// ��ը����
		AnimationWorker boomAnimationWorker = new AnimationWorker(this,
				Resource.boom, new int[] { 1, 2, 3, 4 }, 150, false) {
			@Override
			protected void done() {
				cell.setIcon(null);
			}
		};
		boomAnimationWorker.execute();
		// ��ը��ɢ��ʮ��
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
	 * �ƶ��ԣ�Դ�㣩
	 * 
	 * @param source
	 *            Դ��
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

	/** ��ȡ��Χ�Ŀո� */
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
	// if (!hasBall()) {// ������
	// return false;
	// }
	// if (source.getColor().equals(Color.COLORFUL)) {// ��Դ���ǲ���
	// if (!getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
	// if (exColor == null) {// ��ǰһ����ɫδ����
	// exColor = getColor();
	// } else {// ��ǰһ����ɫ�Ѷ���
	// if (!exColor.equals(getColor())) {// ����һ���ǰһ����ɫ��ͬ
	// return false;
	// }
	// }
	// }
	// } else {// ��Դ�㲻�ǲ���
	// if (!getColor().equals(Color.COLORFUL)) {// ����һ�㲻�ǲ���
	// if (!source.getColor().equals(getColor())) {// ��Դ�����һ�㲻ͬɫ
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	/**
	 * ��¡�����ԣ�
	 * <p>
	 * ������i<br>
	 * ������j<br>
	 * ��ɫcolor<br>
	 * �Ƿ�����
	 * */
	@Override
	protected Cell clone() throws CloneNotSupportedException {
		Cell clonedCell = new Cell(i, j);
		clonedCell.setColor(getColor());
		clonedCell.setBall(hasBall());
		return clonedCell;
	}

}
