package ui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * 缓冲界面-进度条
 * 
 * @version 0.0.0.120516
 * 
 */
public class DialogBuffer extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JProgressBar progressBar;// 不public不显示
	private String msg;
	private boolean canCanel = false;
	private int width = 391;
	private int height = 87;

	public DialogBuffer() {
		this("载入中...");
	}

	public DialogBuffer(String msg) {
		this(msg, null, false);
	}

	public DialogBuffer(JFrame parentFrame) {
		this("载入中...", parentFrame, false);
	}

	public DialogBuffer(String msg, JFrame parentFrame, boolean canCancel) {
		super(parentFrame);
		this.msg = msg;
		// setCanel(canCancel);
		setTitle(msg);
		// if (canCanel()) {
		// height += 40;
		// }
		setBounds(new Rectangle(width, height));
		setLocationRelativeTo(parentFrame);
		initLayout();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initLayout() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		{
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			progressBar.setString(msg);
			// progressBar.setIndeterminate(true);// 进度不确定
			contentPane.add(progressBar, BorderLayout.CENTER);
		}
		{
			if (!canCanel()) {
				return;
			}
			JPanel panelBtns = new JPanel();
			contentPane.add(panelBtns, BorderLayout.SOUTH);
			{
				JButton btnCanel = new JButton("取消");
				btnCanel.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						DialogBuffer.this.dispose();
					}
				});
				panelBtns.add(btnCanel);
			}
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean canCanel() {
		return canCanel;
	}

	public void setCanel(boolean canCanel) {
		this.canCanel = canCanel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

}
