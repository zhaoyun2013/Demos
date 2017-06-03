/**   
 * 文件说明：
 * @version：1.0.0.1
 * @author：Wenbo 
 * @date：2015年3月18日 
 * @Copyright：版权所有  江苏鸿信  2013 
 */
package com.zhaoyun.mymvp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * 类描述：
 * 
 */
public class FileUtil {

	// ///////////////////////////////////////////// 文件路径相关常量
	// /////////////////////////////////////////////////////

	public final static String EXT_PATH_TEMP_FILE = "tempFile";

	public final static String EXT_PATH_DEBUG = "debug";// 日志文件存储目录

	public final static String EXT_PATH_RECORD_FILE = "recordFiles";

	public final static String EXT_PATH_NOTICE_ATTACH = "noticeAttaches";// 通知公告附件存储目录

	public final static String CACHE_PATH_RADIO = "radio";// 缓存目录下的音频文件目录

	public final static String CACHE_PATH_TEMP = "temp";// 缓存目录下的临时文件目录(可以存放音频\视频\图片等上传成功的时候会删除相应文件)，每次进入应用的时候清空

	public final static String EXT_PATH_IMAGE_FILE="image";//图片文件目录
	public final static String EXT_PATH_VIDEO_FILE="video";//视频文件目录

	public final static String EXT_PATH_IMAGE_PHOTO_TEMP="photoTemp";
	// /////////////////////////////////////////SD下的文件操作////////////////////////////////////////////
/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 方法说明：判断存储设备是否可用
	 * 
	 * @return boolean
	 * @author Wenbo
	 * @date 2015年3月18日
	 */
	public static boolean isExternalStorageExit() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取应用在SD卡中的根目录的路径
	 * 
	 * @return
	 */
	public static String getBaseExternalDirPath(Context context) {
		return Environment.getExternalStorageDirectory() + File.separator
				+ AppUtil.getApplicationName(context);
	}

	/**
	 * 获取应用在SD卡中的根目录
	 * 
	 * @return
	 */
	public static File getBaseExternalDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + AppUtil.getApplicationName(context));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	/**
	 * 获取应用在SD卡中的根目录
	 * 
	 * @return
	 */
	public static File getBaseExternalDir(String packageName) {
		if (isExternalStorageExit()) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + packageName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	// /**
	// *
	// * 方法说明：获取运行日志文件存储目录
	// *
	// * @param packageName
	// * @author Wenbo
	// * @date 2015年3月30日
	// */
	// public static File getLogDir(String packageName){
	// if(isExternalStorageExit()){
	// File dir = new File(Environment.getExternalStorageDirectory() +
	// File.separator + packageName);
	// if(!dir.exists()){
	// dir.mkdirs();
	// }
	//
	// File logDir = new File(dir, AppConstants.)
	//
	//
	// }else{
	//
	// }
	// }

	/**
	 * 获取应用在拍照\摄像\录音的时候的临时路径
	 */
	public static File getExtraTempFileDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(getBaseExternalDir(context), EXT_PATH_TEMP_FILE);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	/**
	 * 获取应用在拍照\摄像\录音的时候的临时路径
	 */
	public static File getExtraRecordFileDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(getBaseExternalDir(context),
					EXT_PATH_RECORD_FILE);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	/**
	 * 获取通知公告附件下载目录
	 */
	public static File getExtraNoticeAttachFileDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(getBaseExternalDir(context),
					EXT_PATH_NOTICE_ATTACH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	/**
	 * 获取通知公告附件下载目录
	 */
	public static File getExtraImageFileDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(getBaseExternalDir(context),
					EXT_PATH_IMAGE_FILE);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	/**
	 * 获取通知公告附件下载目录
	 */
	public static File getExtraVideoFileDir(Context context) {
		if (isExternalStorageExit()) {
			File dir = new File(getBaseExternalDir(context),
					EXT_PATH_VIDEO_FILE);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

	public static File getExtraTakePhotoFile(Context context){
		if(isExternalStorageExit()){
			String fileName=DateUtils.getTimeStrForFileName(new GregorianCalendar())+new Random().nextInt(1000)+".jpg";
			File file=new File(getExtraImageFileDir(context),fileName);
			return file;
		}
		return null;
	}

	public static File getExtraTakeVideoFile(Context context){
		if(isExternalStorageExit()){
			String fileName=DateUtils.getTimeStrForFileName(new GregorianCalendar())+new Random().nextInt(1000)+".mp4";
			File file=new File(getExtraVideoFileDir(context),fileName);
			return file;
		}
		return null;
	}

	// /////////////////////////////////////////缓存目录下的文件操作////////////////////////////////////////////

	// /**
	// *
	// * 方法说明：获取缓存目录下的录音文件目录
	// *
	// * @author Wenbo
	// * @date 2015年3月26日
	// */
	// public static File getCacheRadioDir(Context context){
	// File file = new File(context.getCacheDir(), CACHE_PATH_RADIO);
	// if(!file.exists()){
	// file.mkdirs();
	// }
	// return file;
	// }
	//
	// /**
	// *
	// * 方法说明：获取缓存目录下的临时文件目录
	// *
	// * @author Wenbo
	// * @date 2015年3月26日
	// */
	// public static File getCacheTempDir(Context context){
	// File file = new File(context.getCacheDir(), CACHE_PATH_TEMP);
	// if(!file.exists()){
	// file.mkdirs();
	// }
	// return file;
	// }

	// /////////////////////////////////////////其他文件控制////////////////////////////////////////////

	// 建立一个MIME类型与文件后缀名的匹配表
	private static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" },
			{ ".h", "text/plain" }, { ".htm", "text/html" },
			{ ".html", "text/html" }, { ".jar", "application/java-archive" },
			{ ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" },
			{ ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" }, { ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".prop", "text/plain" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" }, { ".zip", "application/zip" } };

	/**
	 * 
	 * 方法说明：打开文件
	 * 
	 * @author Wenbo
	 * @date 2015年4月15日
	 */
	public static void openFile(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		String type = null;
		type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath()));
		if (type == null) {
			String tmp = file.getAbsolutePath().toLowerCase();
			tmp = "." + tmp.split("\\.")[1];
			for (int i = 0; i < MIME_MapTable.length; i++) {
				if (tmp.equals(MIME_MapTable[i][0]))
					type = MIME_MapTable[i][1];
			}
		}
		if (type != null) {
			intent.setDataAndType(uri, type);
			List<ResolveInfo> resInfo = context.getPackageManager()
					.queryIntentActivities(intent, 0);
			int size = resInfo.size();
			try {
				if (size > 1) {
					// 如果有多个可打开文件的应用，跳转应用选择器
					// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(Intent.createChooser(intent,
							"选择打开文件方式").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					// context.startActivity(intent);
					// 如果只有一个可打开的应用，直接打开
				} else if (1 == size) {
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			} catch (ActivityNotFoundException e) {
				Toast.makeText(context, "设备没有安装打开该文件的应用", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "无法打开该类型的额文件", Toast.LENGTH_SHORT).show();
		}
	}

	public static String getRealPathFromURI(Uri contentUri, Context context) {
		System.out.println("uri=" + contentUri);
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor == null) {
			System.out.println("空的");
		} else {
			System.out.println("不空");
		}
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	public static void saveImageToLocal(final Context context, final File file, final SaveImageCallback callback) {

		new AsyncTask<Void,Void,Boolean>(){
			File sdFile=null;
			String fileName="";
			@Override
			protected Boolean doInBackground(Void... params) {
				if(file==null||!file.exists()){
					return false;
				}
				//保存在sd卡
				final File folder=getExtraImageFileDir(context.getApplicationContext());
				if(folder==null){
					return false;
				}
				String time=DateUtils.getTimeStrForFileName(new GregorianCalendar());
				int random=new Random().nextInt(1000);
				fileName=time+ String.format("%03d",random)+".jpg";
				sdFile = new File(folder, fileName);
				try {
					FileInputStream inputStream=new FileInputStream(file);
					FileOutputStream fos = new FileOutputStream(sdFile);
					byte[] buffer = new byte[4 * 1024];
					int length;
					while ((length = (inputStream.read(buffer))) > 0) {
						fos.write(buffer, 0, length);
					}
					fos.flush();
					fos.close(); // 关闭输出流
					return true;
				} catch (Exception e) {
					if(callback!=null){
						callback.onFailed();
					}
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					try {
						MediaStore.Images.Media.insertImage(context.getContentResolver(),sdFile.getAbsolutePath(),fileName,null);
						context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+sdFile.getPath())));
						if(callback!=null){
							callback.onSuccessed(sdFile);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						callback.onFailed();
					}
				}else {
					callback.onFailed();
				}


			}
		}.execute();

	}

	public interface SaveImageCallback{
		public void onSuccessed(File savedFile);
		public void onFailed();
	}

}
