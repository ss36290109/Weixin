package com.wx.test;

import java.io.File;

public class Test {
	public static void main(String[] args) {
		File file = new File("C://Users/sl/Desktop/280595944");
		File[] files = file.listFiles();
		File file2 = new File("C://Users/sl/Desktop/DevilComic");
		if (!file2.exists())
			file2.mkdirs();

		file = null;
		for (int i = 1; i <= files.length; i++) {
			file = files[i-1];
			file.renameTo(new File(file2.getAbsolutePath() + "/" + i + ".jpg"));
		}

	}
}
