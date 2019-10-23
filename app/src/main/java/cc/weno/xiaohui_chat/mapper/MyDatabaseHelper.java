package cc.weno.xiaohui_chat.mapper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 数据库
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库版本号
     */
    private static Integer Version = 1;

    /**
     * 创建表的sql句
     */
    private String CreateTableSql = "create table user(id integer primary key autoincrement," +
            "name varchar(16),pwd varchar(64), UNIQUE (name,pwd))";

    /**
     * 数据库的构造函数
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 调用时间：当数据库第一次创建的时候调用
     * 作用：创建数据库＆初始化数据
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CreateTableSql);
        // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开
    }

    /**
     * 当数据库的版本发生改变的时候则自动调用此函数
     * @param db
     * @param oldVersion 旧的版本数据库
     * @param newVersion 新的版本数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
