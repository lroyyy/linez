package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TxtProcesser {

	public static String read(String path) {
		File file = new File(path);
		if (!file.exists() || !file.canRead()) {
			return null;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tmpLine;
			String lines = "";
			while ((tmpLine = reader.readLine()) != null) {
				lines += tmpLine;
			}
			reader.close();
			return lines;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean write(String path, String content) {
		File file = new File(path);
		if (!file.exists() || !file.canWrite()) {
			return false;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeLine(String path, String line) {
		File file = new File(path);
		if (!file.exists() || !file.canWrite()) {
			return false;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));
			writer.write(line);
			writer.newLine();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean updateLine(String path, String oldLine, String newLine) {
		String oldContent = read(path);
		String[] oldLines = oldContent.split("\n");
		String head = "";
		String tail = "";
		String tmpLines = "";
		boolean matchFlag = false;
		for (String tmpLine : oldLines) {
			if (tmpLine.equals(oldLine)) {// match
				head = tmpLines;
				matchFlag = true;
			} else {
				tmpLines += tmpLine;
			}
		}
		if (!matchFlag) {
			return false;
		}
		tail = tmpLines;
		String newContent = head + "\n" + newLine + "\n" + tail;
		return write(path, newContent);
	}
}
