package com.example.filemanager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.filemanager.entity.UserContract;
import com.example.filemanager.greendao.DaoMaster;
import com.example.filemanager.greendao.DaoSession;

import java.util.List;

/**
 * 初始化 DaoSession
 */
public class MyApplication extends Application {
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    //静态单例
    public static MyApplication instances;
    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
    }
    public static MyApplication getInstances(){
        return instances;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，得到一个便利的 SQLiteOpenHelper 对象。
        // yt-db表示数据库名称
        mHelper = new DaoMaster.DevOpenHelper(this, "yt-db", null);
        db = mHelper.getWritableDatabase();
        // 该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
}