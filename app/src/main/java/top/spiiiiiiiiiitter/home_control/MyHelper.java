package top.spiiiiiiiiiitter.home_control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 15173 on 2017/12/19.
 */

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"itcast.db",null,2);
    }

    public void onCreate(SQLiteDatabase db){
        //初始化数据库的表结构，执行一条建表的语句
        db.execSQL("CREATE TABLE user_information(_id integer primary key autoincrement," +
                   "username VARCHAR(20),password VARCHAR(20))");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
