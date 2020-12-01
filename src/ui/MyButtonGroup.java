package ui;

import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;

public class MyButtonGroup extends ButtonGroup {

	private static final long serialVersionUID = 1L;

	private String name;

	public MyButtonGroup() {

	}

	public MyButtonGroup(String name, AbstractButton btn1, AbstractButton btn2) {
		this.name = name;
		add(btn1);
		add(btn2);
	}

	public MyButtonGroup(String name, AbstractButton btn1, AbstractButton btn2,
			AbstractButton btn3) {
		this.name = name;
		add(btn1);
		add(btn2);
		add(btn3);
	}

	public void add(AbstractButton[] btns) {
		for (AbstractButton btn : btns) {
			add(btn);
		}
	}

	public void addActionListener(ActionListener listener) {
		for (int i = 0; i < getButtonCount(); i++) {
			get(i).addActionListener(listener);
			// Debugger.out("groupName=" + name + ",menuitem=" +
			// get(i).getText());
		}
	}

	public AbstractButton get(int index) {
		Enumeration<AbstractButton> elements = getElements();
		int i = 0;
		while (elements.hasMoreElements()) {
			JMenuItem menuItem = (JMenuItem) elements.nextElement();
			if (i == index) {
				return menuItem;
			} else {
				i++;
			}
		}
		// for (int i = 0; i < getButtonCount(); i++) {
		// JMenuItem menuItem = (JMenuItem) getElements().nextElement();
		// if (i == index) {
		// return menuItem;
		// }
		// }
		return null;
	}

	public String getSelectedString() {
		return getSelection().getActionCommand();
	}
}
