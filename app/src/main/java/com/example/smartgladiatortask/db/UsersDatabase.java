package com.example.smartgladiatortask.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartgladiatortask.model.home.UserModel;

import java.util.ArrayList;
import java.util.HashMap;



public class UsersDatabase extends SQLiteOpenHelper {

    Context con;
    private static final String DATABASE_NAME = "Database";
    private static final String TABLE_NAME = "users_sg";

    public static final String COLUMN_ID = "id";
    private static final String NAME = "Name";
    private static final String PASSWORD = "Password";
    private static final String FILEPATH = "FilePath";
    private static final String DATE = "date";
    private HashMap hp;

    public UsersDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table users_sg " +
                        "(id integer primary key, Name text, Password text,FilePath text,date text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS CallRecords_naan_live");
        //onCreate(db);
    }

    public boolean insert(String name, String password, String FilePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PASSWORD, password);
        contentValues.put(FILEPATH, FilePath);
        contentValues.put(DATE, date);

        Cursor c = db.rawQuery("SELECT Name,Password FROM users_sg WHERE Name = '"+name+ "'",null);
        if (c.getCount()  > 0) {
            return false;
        }
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public UserModel getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT Name,Password,FilePath FROM users_sg WHERE Name = '"+name+ "'",null);
        UserModel localModel = new UserModel();
        try {
            res.moveToFirst();
            while (!res.isAfterLast()) {
                //localModel.setId(res.getLong(res.getColumnIndex(COLUMN_ID)));
                localModel.setName(res.getString(res.getColumnIndex(NAME)));
                localModel.setPassword(res.getString(res.getColumnIndex(PASSWORD)));
                localModel.setFilepath(res.getString(res.getColumnIndex(FILEPATH)));
                //localModel.setDate(res.getString(res.getColumnIndex(DATE)));
                res.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null && !res.isClosed()) {
                res.close();
                res = null;
            }
            if (db != null) {
                db.close();
            }
        }
        return localModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(String Number, String FilePath, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, Number);
        contentValues.put(FILEPATH, FilePath);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public Integer deleteSingle(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public ArrayList<UserModel> getAll() {
        ArrayList<UserModel> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from users_sg", null);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                UserModel localTemplateModel = new UserModel();
                localTemplateModel.setId(res.getLong(res.getColumnIndex(COLUMN_ID)));
                localTemplateModel.setName(res.getString(res.getColumnIndex(NAME)));
                localTemplateModel.setPassword(res.getString(res.getColumnIndex(PASSWORD)));
                localTemplateModel.setFilepath(res.getString(res.getColumnIndex(FILEPATH)));
                array_list.add(localTemplateModel);
                res.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null && !res.isClosed()) {
                res.close();
                res = null;
            }
            if (db != null) {
                db.close();
            }
        }
        return array_list;
    }

}
