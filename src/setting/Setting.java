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
 * 设置
 * <p>
 * 数据结构：Map&ltObject, JMenuItem>（值，菜单项）
 * */
public class Setting<T> {

	/** 名称 */
	private String name;
	/** 当前值 */
	protected T value;
	/** 默认值 */
	private T defaultValue;
	/** 数据（值，菜单项） */
	private Map<Object, JMenuItem> data;
	/** 键（默认为名称） */
	private String key;
	private List<T> staticValues;

	public Setting(String name) {
		this.name = name;
		key = name;
		staticValues = new ArrayList<T>();
	}

	/**
	 * 构造Setting
	 * <p>
	 * 
	 * @param name
	 *            名称
	 * @param defaultValue
	 *            默认值
	 * */
	public Setting(String name, T defaultValue) {
		this(name);
		setDefaultValue(defaultValue);
		setValue(defaultValue);
	}

	/** 添加数据 */
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

	/** 应用 */
	public void apply() {

	}

	/**
	 * 更新UI
	 * <p>
	 * 更新menuItem
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

	/** 读取配置文件 */
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
		// Debugger.out("从配置文件中读取到" + name + "设置的值：" + strValue + " ；最终值：" +
		// value);
		return value;
	}

	/** 写入配置文件 */
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

	/** 获取值 */
	public T getValue() {
		return value;
	}

	/** 设置值 */
	public void setValue(T value) {
		this.value = value;
	}
}
