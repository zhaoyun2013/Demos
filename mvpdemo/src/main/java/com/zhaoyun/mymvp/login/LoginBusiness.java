package com.zhaoyun.mymvp.login;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import com.zhaoyun.mymvp.model.User;


/**
 * Created by zhaoyun on 17-3-1.
 */

public class LoginBusiness {
    Handler handler = new Handler(Looper.getMainLooper());
    public void login(final String username, final String password, final OnLoginListener onLoginListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if("zhao".equals(username) && "123".equals(password)){
                    final User user = new User();
                    user.setPassword(password);
                    user.setUsername(username);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onLoginListener.onSuccess(user);
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
        void onSuccess(User user);
        void onFailed();
    }
}
