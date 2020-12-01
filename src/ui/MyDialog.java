package ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class MyDialog extends JDialog {

	public MyDialog(FrameMain frameMain, String title, int width, int height) {
		super(frameMain, title,true);
		setTitle(title);
//		setModal(true);
		setType(Type.UTILITY);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(new Rectangle(width, height));
		setLocationRelativeTo(frameMain);// æ”÷–
	}
	protected JButton addBtnBack() {
		JButton btnBack=new JButton("∑µªÿ");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyDialog.this.dispose();
			}
		});
		return btnBack;
	}
}
