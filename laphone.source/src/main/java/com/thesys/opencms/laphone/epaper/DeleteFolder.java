package com.thesys.opencms.laphone.epaper;

import java.io.File;

public class DeleteFolder {

	// 刪除文件夾
	// param folderPath 文件夾完整絕對路徑

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 刪除完裏面所有內容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 刪除空文件夾
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 刪除指定文件夾下所有文件
	// param path 文件夾完整絕對路徑
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先刪除文件夾裏面的文件
				delFolder(path + "/" + tempList[i]);// 再刪除空文件夾
				flag = true;
			}
		}
		return flag;
	}
}
