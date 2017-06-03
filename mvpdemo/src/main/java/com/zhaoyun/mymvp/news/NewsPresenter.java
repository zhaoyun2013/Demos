package com.zhaoyun.mymvp.news;

import android.util.SparseArray;

import com.zhaoyun.mymvp.model.News;

import java.io.File;

/**
 * Created by zhaoyun on 17-3-1.
 */

public class NewsPresenter implements NewsContract.Presenter{
    private NewsBusiness newsBusiness;
    private NewsContract.View view;
    public NewsPresenter(NewsContract.View view){
        this.view = view;
        newsBusiness = new NewsBusiness();
    }

    public void loadData(){
        view.showLoading();
        newsBusiness.getList(new NewsBusiness.OnLoadNewsListener() {
            @Override
            public void onSuccess(SparseArray<News> list) {
                view.hideLoading();
                view.addData(list);
            }

            @Override
            public void onFailure(String str) {
                view.hideLoading();
            }
        });
    }

    public void  uploadFile(File file){
        newsBusiness.uploadFile(file, new NewsBusiness.OnUploadListener() {
            @Override
            public void onSuccess(String str) {
                view.uploadSuccess(str);
            }

            @Override
            public void onFailure(String str) {
                view.uploadFailed(str);
            }
        });
    }

    @Override
    public void start() {
        loadData();
    }
}
