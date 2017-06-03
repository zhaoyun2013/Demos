package com.zhaoyun.liteormdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        Log.d(TAG, "onCreate:"+MainActivity.class.getSimpleName());
        ProguardTest.test();
    }

    private void initData() {

        findViewById(R.id.insert_object2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestModel2 testModel2 = new TestModel2() ;
                DatabaseManager.getInstance().insert(testModel2);
            }
        });

        //查询所有
        List<TestModel> queryList = DatabaseManager.getInstance().getQueryAll( TestModel.class ) ;

        if(queryList.size()>0)
            return;

        final TestModel testModel = new TestModel() ;
        testModel.setName( "jack" ) ;
        testModel.setPassword( "123456" ) ;
        testModel.setLogin( true );

        TestModel testMode2 = new TestModel() ;
        testMode2.setName( "jack2" ) ;
        testMode2.setPassword( "123456" ) ;
        testMode2.setLogin( false );

        final List<TestModel> list = new ArrayList<>() ;
        list.add( testModel ) ;
        list.add( testMode2 ) ;


        //插入一条数据
        DatabaseManager.getInstance().insert( testModel ) ;

        //插入一个集合
        DatabaseManager.getInstance().insertAll( list ) ;



        //删除一个数据
        findViewById( R.id.delete_object ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.getInstance().delete( testModel );
            }
        });

        //删除一个集合
        findViewById( R.id.delete_list ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.getInstance().deleteList( list );
            }
        });

        //删除一个表
        findViewById( R.id.delete_table ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.getInstance().delete( TestModel.class );
            }
        });

        //删除整个数据库
        findViewById( R.id.delete_database ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager.getInstance().deleteDatabase();
            }
        });
    }

}
