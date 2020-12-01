package bomb;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import resource.Resource;
import element.GeneralManager;

/** Õ¨µ¯°´Å¥ */
public class ButtonBomb extends JButton {
	private static final long serialVersionUID = 1L;
	private ImageIcon bombIcon;
	private ImageIcon aimedBombIcon;

	public ButtonBomb() {
		setMargin(new Insets(0, 0, 0, 0));
		bombIcon = Resource.bomb.getImageIcon("32px");
		aimedBombIcon = Resource.aimedBomb.getImageIcon("32px");
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftClickAction();
			}
		});
	}

	private void leftClickAction() {
		if (GeneralManager.isLocked) {
			return;
		}
		if (BombManager.getBombCount() <= 0) {
			return;
		}
		if (!BombManager.bombActive) {
			setIcon(aimedBombIcon);
		} else {
			setIcon(bombIcon);
		}
		BombManager.bombActive = !BombManager.bombActive;
	}

	public void setAimed(boolean aimed) {
		if (aimed) {
			setIcon(aimedBombIcon);
		} else {
			setIcon(bombIcon);
		}
	}
}
