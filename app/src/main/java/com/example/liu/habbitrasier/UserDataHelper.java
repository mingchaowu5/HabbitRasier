package com.example.liu.habbitrasier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by liu on 3/26/19.
 */

public class UserDataHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserDataHelper";
    private static final String TABLE_NAME = "user_table";

    //Table Details
    private static final String ColUserID = "ID";
    private static final String ColUserName = "userName";
    private static final String ColUserMemo = "userMemo";

    public UserDataHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColUserName +  " TEXT, " + ColUserMemo + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
    }

    public boolean addData(String item1, String item2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColUserName, item1);
        contentValues.put(ColUserMemo, item2);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //If data is inserted incorrectly, it will return
        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    //Cursor to return database data
    public Cursor getData(){
        Log.d(TAG, "getData: got into function.");
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "getData: got database.");
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.d(TAG, "getData: made cursor and reutrn.");
        return data;
    }

    //Cursor to get item id
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + ColUserID + " FROM " + TABLE_NAME +
                " WHERE " + ColUserName + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //Update name of user
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE '" + TABLE_NAME + "' WHERE " + ColUserID +
                " = '" + id + "' " + " AND " + ColUserName + " = '" +
                oldName + "'";
        db.execSQL(query);
    }

    //Update memo of user
    public void updateMemo(String newMemo, int id, String oldMemo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE '" + TABLE_NAME + "' WHERE " + ColUserID +
                " = '" + id + "' " + " AND " + ColUserMemo + " = '" +
                oldMemo + "'";
        db.execSQL(query);
    }
}
