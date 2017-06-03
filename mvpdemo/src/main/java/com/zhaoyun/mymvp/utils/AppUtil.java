/**   
 * 文件说明：
 * @version：1.0.0.1
 * @author：Wenbo 
 * @date：2015年3月18日 
 * @Copyright：版权所有  江苏鸿信  2013 
 */
package com.zhaoyun.mymvp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**   
 * 类描述：  
 */
public class AppUtil {
	
	/**
	 * 获取应用程序名称
	 * @return
	 */
	public static String getApplicationName(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
			return pm.getApplicationLabel(applicationInfo).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	/**
	 * 获取客户端版本号
	 * @return
	 */
	public static String getAppVersion(Context context){
		PackageManager packageManager = context.getPackageManager();
		String versionName = null;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//			versionName = packageInfo.versionName.substring(0, packageInfo.versionName.lastIndexOf("."));
			versionName=packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return versionName;		
	}

	/**
	 * 获取客户端版本号
	 * @return
	 */
	public static int getAppVersionCode(Context context){
		PackageManager packageManager = context.getPackageManager();
		int versionName = -1;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//			versionName = packageInfo.versionName.substring(0, packageInfo.versionName.lastIndexOf("."));
			versionName=packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
		return versionName;
	}
	
	/**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();
            
        }else{
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

	/**
	 * 是否连接WIFI
	 * */
	public static boolean isWifiConnected(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiNetworkInfo.isConnected()){
			return true ;
		}
		return false ;
	}

	/**
	 * 是否有外部存储权限
	 * */
	public static boolean hasExternalStoragePermission(Context context){
		int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

}
