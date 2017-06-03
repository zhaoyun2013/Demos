package com.zhaoyun.mymvp.news;


import android.util.SparseArray;

import com.zhaoyun.mymvp.base.BasePresenter;
import com.zhaoyun.mymvp.base.BaseView;
import com.zhaoyun.mymvp.model.News;

import java.io.File;

/**
 * Created by zhaoyun on 17-4-7.
 */

public class NewsContract {
    public interface View extends BaseView<Presenter> {
        void addData(SparseArray<News> datas);
        void hideLoading();
        void showLoading();
        void uploadSuccess(String msg);
        void uploadFailed(String msg);
    }
    interface Presenter extends BasePresenter {
        void loadData();
        void uploadFile(File file);
    }
}
