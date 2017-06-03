package com.zhaoyun.mymvp;

import com.zhaoyun.mymvp.login.LoginBusiness;
import com.zhaoyun.mymvp.model.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setUp() throws Exception {
        // 如果有使用到rxjava，可以在这里处理rxjava变成同步执行
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void login1() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        LoginBusiness loginBusiness = new LoginBusiness();
        loginBusiness.login("zhao", "123", new LoginBusiness.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                Assert.assertEquals("zhao1", user.getUsername());
                latch.countDown();
            }

            @Override
            public void onFailed() {
                Assert.assertEquals(1,2);
                latch.countDown();
            }
        });
        latch.await(5000, TimeUnit.MILLISECONDS);
//        latch.await();
//
//        LoginBusiness loginBusiness = new LoginBusiness();
//        LoginBusiness.OnLoginListener callBack = Mockito.mock(LoginBusiness.OnLoginListener.class);
//        loginBusiness.login("zhao", "123", callBack);
//        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        Mockito.verify(callBack).onSuccess(captor.capture());
//        Assert.assertEquals(captor.getValue().getUsername(), "zhao");
//        signal.await();
    }
}