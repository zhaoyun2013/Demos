package com.zhaoyun.mymvp.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LogUtil {

	public static boolean CONSOLE_OUTPUT = true;
	public static boolean WRITE_TO_FILE = false;

	/**
	 * 打印Info(信息)信息
	 * 
	 * @param msg
	 */
	public static void i(String msg) {
		if (CONSOLE_OUTPUT) {
			Log.i("LogUtil", msg);
		}

		if (WRITE_TO_FILE) {
			write2File(msg);
		}

	}

	/**
	 * 打印Error(错误)信息
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		if (CONSOLE_OUTPUT) {
			Log.e("LogUtil", msg);
		}

		if (WRITE_TO_FILE) {
			write2File("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" + msg);
			write2File("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}

	}

	private static void write2File(final String msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!FileUtil.isExternalStorageExit()) {
					return;
				}

				// File fir = FileUtil.getBaseExternalDir("外勤助手");

				File dir = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "jsict" + File.separator);
				if (!dir.exists()) {
					dir.mkdir();
				}
				if (dir != null) {
					BufferedWriter out = null;
					try {
						File file = new File(dir, "locTimingLog.txt");
						if (!file.exists()) {
							file.createNewFile();
						}
						out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
						out.write(DateUtils.getNowTimeStr(DateUtils.YYYYMMDD_HHMMSS) + "   " + msg + "\n");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (out != null) {
								out.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}).start();

	}

}
