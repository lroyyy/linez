package ui;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FrameShaker{

	// 窗口距离中心左右晃动的最大距离

	public static final int SHAKE_DISTANCE = 10;

	// 窗口晃动一个循环（中间，右，中间，左， 中间）所需要的时间(ms),

	// 这个值越小， 晃动的就越快。

	public static final double SHAKE_CYCLE = 80;

	// 整个晃动所需要的时间。

	public static final int SHAKE_DURATION = 300;

	// 这个是设定Swing多长时间（ms）更新窗口的位置。

	public static final int SHAKE_UPDATE = 5;

	// private JDialog dialog;
	private JFrame frame;

	private Point naturalLocation;

	private long startTime;

	private Timer shakeTimer;

	@SuppressWarnings("unused")
	private final double HALF_PI = Math.PI / 2.0;

	private final double TWO_PI = Math.PI * 2.0;

	public FrameShaker(JFrame frame) {

		this.frame = frame;

	}

	public void startShake() {

		// 保存窗口的原始位置

		naturalLocation = frame.getLocation();

		// 保存开始的时间

		startTime = System.currentTimeMillis();

		ActionListener listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 计算倒目前为止花费的时间

				long elapsed = System.currentTimeMillis() -

				startTime;

				// 下了三角公式是为了利用时间计算出某一时刻晃动的幅度，举例见A.
				double waveOffset = (elapsed % SHAKE_CYCLE) /

				SHAKE_CYCLE;

				double angle = waveOffset * TWO_PI;

				double angley = waveOffset * TWO_PI;

				int shakenX = (int) ((Math.sin(angle) *

				SHAKE_DISTANCE) +

				naturalLocation.x);

				int shakenY = (int) ((Math.sin(angley) *

				SHAKE_DISTANCE) +

				naturalLocation.y);

				frame.setLocation(shakenX, shakenY);

				frame.repaint();

				// should we stop timer?

				if (elapsed >= SHAKE_DURATION)

					stopShake();

			}

		};
		shakeTimer = new Timer(SHAKE_UPDATE, listener);
		shakeTimer.start();

	}

	public void stopShake() {

		shakeTimer.stop();

		frame.setLocation(naturalLocation);

		frame.repaint();

	}

	// 去掉了原始代码中的main

}