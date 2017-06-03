package com.zhaoyun.test.login;

/**
 * Created by zhaoyun on 17-4-7.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginBusiness mLoginBusiness;
    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view){
        mView = view;
        mLoginBusiness = new LoginBusiness();
    }

    @Override
    public void login(String username, String password, boolean isAutoLogin) {
        mLoginBusiness.login(username, password, new LoginBusiness.OnLoginListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void logout() {

    }

    @Override
    public void start() {

    }
}
