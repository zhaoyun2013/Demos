package com.zhaoyun.mymvp.model;

import android.util.SparseArray;

import com.zhaoyun.mymvp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zhaoyun on 17-3-1.
 */

public class News {
    private String title;
    private String icon;
    private String detail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static SparseArray<News> readJsonDataBeans(String json) throws Exception{
        SparseArray<News> list = new SparseArray<News>();
        JSONArray jsonArray = new JSONArray(json);
        int lenggth = jsonArray.length();
        if(jsonArray!=null && lenggth >0){
            News news = null;
            for (int i = 0; i < lenggth; i++){
                news = JsonUtils.deserialize(jsonArray.optString(i),News.class);
                if(news != null)
                    list.put(i,news);
            }
        }
        return list;
    }
}
