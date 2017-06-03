package com.zhaoyun.test.login;

import com.zhaoyun.test.base.BasePresenter;
import com.zhaoyun.test.base.BaseView;

/**
 * Created by zhaoyun on 17-4-7.
 */

public class LoginContract {
    interface View extends BaseView<Presenter>{
        void showLoading();
        void hideLoading();
    }
    interface Presenter extends BasePresenter{
        void login(String username,String password,boolean isAutoLogin);
        void logout();
    }
}
