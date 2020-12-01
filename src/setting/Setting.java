package setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

import ui.MyButtonGroup;
import util.Debugger;
import util.IniProcesser;
import util.MyUtils;

/**
 * ����
 * <p>
 * ���ݽṹ��Map&ltObject, JMenuItem>��ֵ���˵��
 * */
public class Setting<T> {

	/** ���� */
	private String name;
	/** ��ǰֵ */
	protected T value;
	/** Ĭ��ֵ */
	private T defaultValue;
	/** ���ݣ�ֵ���˵�� */
	private Map<Object, JMenuItem> data;
	/** ����Ĭ��Ϊ���ƣ� */
	private String key;
	private List<T> staticValues;

	public Setting(String name) {
		this.name = name;
		key = name;
		staticValues = new ArrayList<T>();
	}

	/**
	 * ����Setting
	 * <p>
	 * 
	 * @param name
	 *            ����
	 * @param defaultValue
	 *            Ĭ��ֵ
	 * */
	public Setting(String name, T defaultValue) {
		this(name);
		setDefaultValue(defaultValue);
		setValue(defaultValue);
	}

	/** ������� */
	public void addData(T value, JMenuItem menuItem) {
		if (data == null) {
			data = new HashMap<Object, JMenuItem>();
		}
		data.put(value, menuItem);
	}

	public boolean addData(List<T> staticValues, MyButtonGroup btnGroup) {
		if (staticValues.size() != btnGroup.getButtonCount()) {
			Debugger.out("failed to addGroupData");
			return false;
		}
		for (int i = 0; i < staticValues.size(); i++) {
			addData(staticValues.get(i), (JMenuItem) btnGroup.get(i));
		}
		return true;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void addStaticValue(T value) {
		staticValues.add(value);
	}

	/** Ӧ�� */
	public void apply() {

	}

	/**
	 * ����UI
	 * <p>
	 * ����menuItem
	 * */
	public boolean updateUI() {
		if (data == null) {
			return false;
		}
		Iterator<T> keys = (Iterator<T>) data.keySet().iterator();
		JMenuItem menuItem = null;
		while (keys.hasNext()) {
			T tmpValue = keys.next();
			if (value.equals(tmpValue)) {
				menuItem = (JMenuItem) data.get(tmpValue);
				menuItem.setSelected(true);
			}
		}
		return true;
	}

	public void setDefaultValue(T value) {
		defaultValue = value;
	}

	/** ��ȡ�����ļ� */
	public T readConfigFile() {
		String strValue = IniProcesser.GetPrivateProfileString(
				SettingManager.getConfigFilePath(),
				SettingManager.getConfigFileSectionString(), key,
				String.valueOf(defaultValue));
		if (value instanceof Integer) {
			value = (T) Integer.valueOf(strValue);
		}
		if (value instanceof Long) {
			value = (T) Long.valueOf(strValue);
		}
		if (value instanceof Boolean) {
			value = (T) Boolean.valueOf(strValue);
		}
		if (value instanceof String) {
			value = (T) String.valueOf(strValue);
		}
		// Debugger.out("�������ļ��ж�ȡ��" + name + "���õ�ֵ��" + strValue + " ������ֵ��" +
		// value);
		return value;
	}

	/** д�������ļ� */
	public boolean writeConfigFile() {
		MyUtils.initFile(SettingManager.getConfigFilePath());
		try {
			IniProcesser.WritePrivateProfileString(
					SettingManager.getConfigFilePath(),
					SettingManager.getConfigFileSectionString(), name,
					String.valueOf(value));
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/** ��ȡֵ */
	public T getValue() {
		return value;
	}

	/** ����ֵ */
	public void setValue(T value) {
		this.value = value;
	}
}
