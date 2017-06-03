package com.zhaoyun.mymvp.login;

import android.os.Handler;

import com.zhaoyun.mymvp.model.User;

/**
 * Created by zhaoyun on 17-3-1.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginBusiness loginBusiness;
    private LoginContract.View view;

    private LoginPresenter(){}

    public LoginPresenter(LoginContract.View view){
        this.view = view;
        this.loginBusiness = new LoginBusiness();
    }

    @Override
    public void login(String username, String password, boolean isAutoLogin) {
        view.setProgressIndicator(true);
        loginBusiness.login(username,password, new LoginBusiness.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                view.setProgressIndicator(false);
                view.loginSuccess();
            }

            @Override
            public void onFailed() {
                view.setProgressIndicator(false);
                view.loginFailed();
            }
        });
    }

    @Override
    public void start() {

    }
}
