package top.spiiiiiiiiiitter.home_control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 15173 on 2017/12/20.
 * 此数据库操作函数是用来对用户精心个增删改查的操作
 */

public class UserDataHandle {
    private MyHelper myHelper;
    private SQLiteDatabase sqLiteDatabase;

    public UserDataHandle(Context context){
        myHelper=new MyHelper(context);
        sqLiteDatabase=myHelper.getWritableDatabase();
    }

    //数据库插入语句
    public void insert(String user,String pwd){
        ContentValues values=new ContentValues();
        //将数据插入到数据库中
        values.put("username",user);
        values.put("password",pwd);
        long id=sqLiteDatabase.insert("user_information",null,values);
    }
    /*
    数据库修改语句,修改user的pwd
    String user：表中的user
    String pwd：自己修改的pwd
    */
    public int update(String user,String pwd){
        ContentValues values=new ContentValues();
        values.put("password",pwd);
        int number=sqLiteDatabase.update("user_information",values,"username=?",new String[]{user});
        return number;
    }

    //删除一条记录
    public int delete(String user){
        int number=sqLiteDatabase.delete("user_information","username=?",new String[]{user});
        return number;
    }

    public boolean find(String user){
        Cursor cursor=sqLiteDatabase.query("user_information",null,"username=?",new String[]{user},null,null,null);
        boolean result=cursor.moveToNext();
        cursor.close();//关闭游标
        return result;
    }

    public void closeDatabase(){
        sqLiteDatabase.close();
    }
}
