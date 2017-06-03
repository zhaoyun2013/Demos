/**   
 * 文件说明：
 * @version：1.0.0.1
 * @author：Wenbo 
 * @date：2015年6月8日 
 * @Copyright：版权所有  江苏鸿信  2013 
 */
package com.zhaoyun.mymvp.volley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhaoyun.mymvp.constants.HttpConstant;
import com.zhaoyun.mymvp.utils.LogUtil;

public class VolleyUtil {

	private final String TAG=VolleyUtil.class.getSimpleName();

	private static VolleyUtil instance;
	private RequestQueue mRequestQueue;
	private Context mContext;
	private final int TIME_OUT = 120000;

	private VolleyUtil(Context context) {
		this.mContext = context;
		mRequestQueue = getRequestQueue();
	}

	public static synchronized VolleyUtil getInstance(Context context) {
		if (instance == null) {
			instance = new VolleyUtil(context);
		}
		return instance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(mContext
					.getApplicationContext());
		}
		return mRequestQueue;
	}

	/**
	 * 将Request对象添加进RequestQueue，由于Request有*StringRequest,JsonObjectResquest...
	 * 等多种类型，所以需要用到*泛型。同时可将*tag作为可选参数以便标示出每一个不同请求
	 */

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// 如果tag为空的话，就是用默认TAG
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	// 通过各Request对象的Tag属性取消请求
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * 
	 * 方法说明：
	 * 
	 */
	private void doStringPost(final String url, final int method,
							 final HashMap<String, String> parameter,
							 final VolleyCallBack callBack) {

		Response.Listener<String> successListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				LogUtil.i("response:\n" + url + "\n" + response);
				VolleyUtil.this.onResponse(response,callBack);
			}
		};

		Response.ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyUtil.this.onErrorResponse(error,callBack);
			}
		};

		// Formulate the request and handle the response.
		StringRequest stringRequest = new StringRequest(method,
				url, successListener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return VolleyUtil.this.getParams(parameter);
			}
		};
		stringRequest.setShouldCache(false);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		callBack.onStart();
		addToRequestQueue(stringRequest);

	}

	public void post(String url, HashMap<String, String> parameter, VolleyCallBack callBack) {
		doStringPost(url,Request.Method.POST,parameter, callBack);
	}
	public void get(String url, HashMap<String, String> parameter, VolleyCallBack callBack) {
		doStringPost(url,Request.Method.GET,parameter, callBack);
	}

	public void uploadFile(final String url, File file,	final HashMap<String, String> parameter,final VolleyCallBack callBack) {

		Response.Listener<String> successListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				LogUtil.i("response:\n" + url + "\n" + response);
				VolleyUtil.this.onResponse(response,callBack);
			}
		};

		Response.ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyUtil.this.onErrorResponse(error,callBack);
			}

		};
		Map<String, String> params = null;
		params = VolleyUtil.this.getParams(parameter);

		MultipartRequest request = new MultipartRequest(url, errorListener,
				successListener, "file", file, params);
		request.setShouldCache(false);
		request.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		addToRequestQueue(request);
	}

	private void onResponse(String response,VolleyCallBack callBack) {
		if (!TextUtils.isEmpty(response)) {
			try {
				// String result = Des3.decode(response);
				String result = response;
				result =new String(response.getBytes("iso-8859-1"), "utf-8");
				LogUtil.i("response解密后:"+ result);

				JSONObject main = new JSONObject(result);
				String code = main.getString("code");
				String msg = main.getString("desc");
				String data = main.getString("data");
				if (TextUtils.isEmpty(code)) {
					LogUtil.e("response:数据有误!");
					callBack.onFail("XXX", "数据有误");
				}else if (HttpConstant.HTTP_RESPONSE_STATUS_SUCCESS.equals(code)) {
					callBack.onSuccess(data);
				}else if(code.equals(HttpConstant.HTTP_RESPONSE_STATUS_TOKEN_ERR)){
					callBack.onFail(code, TextUtils.isEmpty(msg) ? "": msg);
				}else {
					LogUtil.e("response:code=" + code);
					callBack.onFail(code, TextUtils.isEmpty(msg) ? "": msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				callBack.onFail("XXX", "Exception Occor");
			}
		} else {
			// 数据请求失败
			LogUtil.e("response:接口请求返回数据为空!");
			callBack.onFail("XXX", "服务器访问失败,接口请求返回数据为空");
		}
	}


	private void onErrorResponse(VolleyError error,VolleyCallBack callBack) {
		if (error instanceof TimeoutError) {
			LogUtil.e("response:网络连接超时!");
			callBack.onFail("XXX", "网络连接超时");
		} else if (error instanceof NoConnectionError) {
			LogUtil.e("response:网络未连接!");
			callBack.onFail("XXX", "网络未连接!");
		} else if (error instanceof AuthFailureError) {
			LogUtil.e("response:网络连接异常，请检查网络连接!");
			callBack.onFail("XXX", "网络连接异常，请检查网络连接");
		} else if (error instanceof ServerError) {
			LogUtil.e("response:服务器异常!");
			callBack.onFail("XXX", "服务器异常");
		} else if (error instanceof NetworkError) {
			LogUtil.e("response:网络连接异常，请检查网络连接!");
			callBack.onFail("XXX", "网络连接异常，请检查网络连接");
		} else if (error instanceof ParseError) {
			LogUtil.e("response:数据异常!");
			callBack.onFail("XXX", "数据异常");
		}
	}

	private Map<String, String> getParams(Map<String, String> parameter){
		Map<String, String> params = parameter;
		//TODO 字段加密处理
		return params;
	}

}
