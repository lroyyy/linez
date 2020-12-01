package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cell.Cell;

public class MyUtils {

	/** 合并（去同存异） */
	public static List<Cell> merge(List<Cell> list1, List<Cell> list2) {
		List<Cell> repeats = new ArrayList<Cell>(list1);
		repeats.retainAll(list2);
		list2.removeAll(repeats);
		list1.addAll(list2);
		return list1;
	}

	public static File initFile(String path) {
		File file = new File(path);
		try {
			if (!file.exists()) {// 若不存在
				if (file.isDirectory()) {// 若是路径
					if (file.mkdirs()) {
						Debugger.out(file.getAbsolutePath() + "该路径不存在，创建成功。");
					} else {
						Debugger.out(file.getAbsolutePath() + "该路径不存在，创建失败。");
					}
				} else {
					file.getParentFile().mkdirs();
					if (file.createNewFile()) {
						Debugger.out(file.getAbsolutePath() + "该文件不存在，创建成功。");
					} else {
						Debugger.out(file.getAbsolutePath() + "该文件不存在，创建失败。");
					}
				}
			} else {
				Debugger.out(file.getAbsolutePath() + "已存在。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Debugger.out(file.getAbsolutePath() + "初始化失败，信息：" + e.getMessage());
		}
		return file;
	}

	public static void showList(List list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
