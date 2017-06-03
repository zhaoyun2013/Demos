package com.zhaoyun.test.login;

import android.app.Activity;
import android.os.Bundle;

import com.zhaoyun.test.R;

public class LoginActivity extends Activity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


}
