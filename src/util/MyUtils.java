package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cell.Cell;

public class MyUtils {

	/** �ϲ���ȥͬ���죩 */
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
			if (!file.exists()) {// ��������
				if (file.isDirectory()) {// ����·��
					if (file.mkdirs()) {
						Debugger.out(file.getAbsolutePath() + "��·�������ڣ������ɹ���");
					} else {
						Debugger.out(file.getAbsolutePath() + "��·�������ڣ�����ʧ�ܡ�");
					}
				} else {
					file.getParentFile().mkdirs();
					if (file.createNewFile()) {
						Debugger.out(file.getAbsolutePath() + "���ļ������ڣ������ɹ���");
					} else {
						Debugger.out(file.getAbsolutePath() + "���ļ������ڣ�����ʧ�ܡ�");
					}
				}
			} else {
				Debugger.out(file.getAbsolutePath() + "�Ѵ��ڡ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Debugger.out(file.getAbsolutePath() + "��ʼ��ʧ�ܣ���Ϣ��" + e.getMessage());
		}
		return file;
	}

	public static void showList(List list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
