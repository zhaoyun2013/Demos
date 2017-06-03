package com.zhaoyun.liteormdemo;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLiteHelper;

import java.util.List;
/**
 * Created by zhaoyun on 17-3-28.
 */

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static LiteOrm liteOrm;
    private static DatabaseManager ourInstance = new DatabaseManager();

    private DatabaseManager() {
        if (liteOrm == null) {
//            liteOrm = LiteOrm.newSingleInstance(BaseApplication.getInstance().getApplicationContext(), "liteorm.db");

            DataBaseConfig config = new DataBaseConfig(BaseApplication.getInstance().getApplicationContext(), "liteorm.db");
            config.debugged = true; // open the log
            config.dbVersion = 3; // set database version
            config.onUpdateListener = new SQLiteHelper.OnUpdateListener() {
                @Override
                public void onUpdate(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                    Log.i(TAG, "onUpdate: oldVersion:"+oldVersion+" newVersion:"+newVersion);
                    if(oldVersion < 2){//如此判断可以使高版本按顺序升级每一个版本需要执行的sql
                        sqLiteDatabase.execSQL("alter table test_model add v2 varchar(255)");
                    }
                    if(oldVersion < 3){
                        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS test_model2 (id INTEGER PRIMARY KEY AUTOINCREMENT)");
                    }
                }
            }; // set database update listener
            liteOrm = LiteOrm.newSingleInstance(config);
        }
    }

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    /**
     * 插入一条记录
     * @param t
     */
    public <T> long insert(T t) {
        return liteOrm.save(t);
    }

    /**
     * 插入所有记录
     * @param list
     */
    public <T> void insertAll(List<T> list) {
        liteOrm.save(list);
    }

    /**
     * 查询所有
     * @param cla
     * @return
     */
    public <T> List<T> getQueryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }

    /**
     * 查询  某字段 等于 Value的值
     * @param cla
     * @param field
     * @param value
     * @return
     */
    public <T> List<T> getQueryByWhere(Class<T> cla, String field, String[] value) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @return
     */
    public <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String[] value, int start, int length) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除一个数据
     * @param t
     * @param <T>
     */
    public <T> void delete( T t){
        liteOrm.delete( t ) ;
    }

    /**
     * 删除一个表
     * @param cla
     * @param <T>
     */
    public <T> void delete( Class<T> cla ){
        liteOrm.delete( cla ) ;
    }

    /**
     * 删除集合中的数据
     * @param list
     * @param <T>
     */
    public <T> void deleteList( List<T> list ){
        liteOrm.delete( list ) ;
    }

    /**
     * 删除数据库
     */
    public void deleteDatabase(){
        liteOrm.deleteDatabase() ;
    }

}