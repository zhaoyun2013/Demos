package com.zhaoyun.mymvp.volley;

public abstract class VolleyCallBack {

//	public abstract void onStart();

	/**
	 * 网络请求数据结果回调
	 * 
	 * @param result
	 *            保存网络请求返回数据的data部分，有些接口的返回数据中没有data部分，则该参数为null
	 */
	public abstract void onSuccess(String result);

	public abstract void onFail(String errCode, String errMsg);

	/**
	 * 下载或者上传文件结果回调
	 * 
	 * @param flag
	 *            文件标志位
	 *            结果的文字表述
	 */
	public void onFileTransmitSuccess(String flag, String result) {

	}

	/**
	 * 下载或者上传文件结果回调
	 * 
	 * @param flag
	 *            文件标志位
	 *            结果的文字表述
	 */
	public void onFileTransmitFail(String flag, String errCode, String errMsg) {

	}

	/**
	 * @param progressInfo
	 *            文件上传或者下载的进度 格式为"current&total&percent"
	 * 
	 * 
	 */
	public void onProgress(String flag, String progressInfo) {

	}

}
