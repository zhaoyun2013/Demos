package com.zhaoyun.test.login;


import android.os.Handler;

/**
 * Created by zhaoyun on 17-4-7.
 */

public class LoginBusiness {

    public void login(final String username, final String password, final OnLoginListener onLoginListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Handler handler = new Handler();
                if("zhao".equals(username) && "123".equals(password)){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onLoginListener.onSuccess();
                        }
                    });

                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onLoginListener.onFailed();
                        }
                    });
                }
            }
        }).start();
    }


    public interface OnLoginListener{
        void onSuccess();
        void onFailed();
    }
}
