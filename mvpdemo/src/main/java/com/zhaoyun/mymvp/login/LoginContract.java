package com.zhaoyun.mymvp.login;


import com.zhaoyun.mymvp.base.BasePresenter;
import com.zhaoyun.mymvp.base.BaseView;

/**
 * Created by zhaoyun on 17-4-7.
 */

public class LoginContract {
    interface View extends BaseView<Presenter> {
        void setProgressIndicator(boolean active);
        void loginSuccess();
        void loginFailed();
    }
    interface Presenter extends BasePresenter {
        void login(String username, String password, boolean isAutoLogin);
    }
}
