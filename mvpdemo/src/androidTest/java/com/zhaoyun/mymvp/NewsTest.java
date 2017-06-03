package com.zhaoyun.mymvp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;

import com.zhaoyun.mymvp.login.LoginActivity;
import com.zhaoyun.mymvp.login.LoginBusiness;
import com.zhaoyun.mymvp.model.News;
import com.zhaoyun.mymvp.model.User;
import com.zhaoyun.mymvp.news.NewsBusiness;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NewsTest {
    @Test
    public void getList() throws Exception {
        //CountDownLatch类似阻止线程的效果
        final CountDownLatch latch = new CountDownLatch(1);
        NewsBusiness newsBusiness = new NewsBusiness();
        newsBusiness.getList(new NewsBusiness.OnLoadNewsListener() {
            @Override
            public void onSuccess(SparseArray<News> list) {
                if(list!=null && list.size()>0){
                    Assert.assertEquals(1,1);
                }else{
                    Assert.fail("新闻数据返回为null");
                }
                latch.countDown();
            }

            @Override
            public void onFailure(String str) {
                Assert.fail("获取新闻失败:"+str);
                latch.countDown();
            }
        });
        latch.await();
    }


}
