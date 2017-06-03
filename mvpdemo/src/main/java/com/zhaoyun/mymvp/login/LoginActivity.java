package com.zhaoyun.mymvp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhaoyun.mymvp.news.NewsActivity;
import com.zhaoyun.mymvp.R;

public class LoginActivity extends Activity implements LoginContract.View{

    private EditText etUsername,etPassword;
    private Button btnLogin,btnClear;
    private ProgressBar progressBar;
    private LoginPresenter loginPresenter = new LoginPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login(etUsername.getText().toString(),etPassword.getText().toString(),true);
            }
        });
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginPresenter.clear();
            }
        });
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }


    @Override
    public void loginSuccess() {
        Toast.makeText(this,"login success.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,NewsActivity.class));
    }

    @Override
    public void loginFailed() {
        Toast.makeText(this,"login failed.",Toast.LENGTH_SHORT).show();
    }
}
