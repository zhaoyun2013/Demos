package com.zhaoyun.mymvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhaoyun.mymvp.login.LoginActivity;
import com.zhaoyun.mymvp.login.LoginBusiness;
import com.zhaoyun.mymvp.model.User;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {


    @Test
    public void loginSucces() throws Exception {
        //CountDownLatch类似阻止线程的效果
        final CountDownLatch latch = new CountDownLatch(1);
        LoginBusiness loginBusiness = new LoginBusiness();
        loginBusiness.login("zhao", "123", new LoginBusiness.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                Assert.assertEquals("zhao", user.getUsername());
                latch.countDown();
            }

            @Override
            public void onFailed() {
                Assert.assertEquals(1,2);
                latch.countDown();
            }
        });
//        latch.await(8000, TimeUnit.MILLISECONDS);
        latch.await();
//
//        LoginBusiness loginBusiness = new LoginBusiness();
//        LoginBusiness.OnLoginListener callBack = Mockito.mock(LoginBusiness.OnLoginListener.class);
//        loginBusiness.login("zhao", "123", callBack);
//        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        Mockito.verify(callBack).onSuccess(captor.capture());
//        Assert.assertEquals(captor.getValue().getUsername(), "zhao");
//        signal.await();
    }

    @Test
    public void loginFailed() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        LoginBusiness loginBusiness = new LoginBusiness();
        loginBusiness.login("zhao", "123", new LoginBusiness.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                Assert.assertEquals("zhao", user.getUsername());
                latch.countDown();
            }

            @Override
            public void onFailed() {
                Assert.assertEquals(1,1);
                latch.countDown();
            }
        });
        latch.await();
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void loginBtnClick() throws Exception{
        onView(withId(R.id.btnLogin)).perform(click());
        //点击之后如何判断是否跳转到另一个activity？？
    }
}
