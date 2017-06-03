package com.zhaoyun.mymvp.news;

import android.content.Context;
import android.util.SparseArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhaoyun.mymvp.base.BaseApplication;
import com.zhaoyun.mymvp.constants.HttpConstant;
import com.zhaoyun.mymvp.model.News;
import com.zhaoyun.mymvp.utils.JsonUtils;
import com.zhaoyun.mymvp.volley.VolleyCallBack;
import com.zhaoyun.mymvp.volley.VolleyUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoyun on 17-3-1.
 */

public class NewsBusiness {

    static SparseArray<News> cacheNews;


    public void getList(final OnLoadNewsListener onLoadNewsListener){
        VolleyUtil.getInstance(BaseApplication.getInstance()).post(HttpConstant.LIST_DEMO_URL,new HashMap<String, String>(),
             new VolleyCallBack(){
                    @Override
                    public void onSuccess(String result) {
                        try {
                            if(cacheNews !=null && cacheNews.size()>0){
                                cacheNews.get(0).setTitle("cache");
                                onLoadNewsListener.onSuccess(cacheNews);
                            }else {
                                Thread.sleep(500);
                                SparseArray<News> list = News.readJsonDataBeans(result);
                                if(list !=null && list.size()>0) {
                                    cacheNews = list;
                                }
                                onLoadNewsListener.onSuccess(list);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            onLoadNewsListener.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onFail(String errCode, String errMsg) {
                        onLoadNewsListener.onFailure(errCode+errMsg);
                    }
            });

    }

    public void uploadFile(File file,final OnUploadListener onUploadListener){
        VolleyUtil.getInstance(BaseApplication.getInstance()).uploadFile(HttpConstant.UPLOAD_FILE_URL, file, null, new VolleyCallBack() {
            @Override
            public void onSuccess(String result) {
                onUploadListener.onSuccess(result);
            }

            @Override
            public void onFail(String errCode, String errMsg) {
                onUploadListener.onFailure(errCode+errMsg);
            }
        });
    }

    public interface OnLoadNewsListener{
        void  onSuccess(SparseArray<News> list);
        void  onFailure(String str);
    }

    public interface OnUploadListener{
        void  onSuccess(String str);
        void  onFailure(String str);
    }
}
