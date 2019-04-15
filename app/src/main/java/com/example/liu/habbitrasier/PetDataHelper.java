package com.example.liu.habbitrasier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PetDataHelper extends SQLiteOpenHelper {

    private static final String TAG = "PetDataHelper";
    private static final String TABLE_NAME = "petTable";

    //Table Details
    private static final String ColPetID = "ID";
    private static final String ColPetName = "petName";
    private static final String ColHealthy = "healthy";
    private static final String ColFood = "food";
    private String initName = "pet";
    private String initHealthy = "4";
    private String initFood = "2";

    public PetDataHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColPetName + " TEXT, " + ColHealthy + " TEXT, " + ColFood + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
    }

    public boolean addData(String item1, String item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColPetName, initName);
        contentValues.put(ColHealthy, item1);
        contentValues.put(ColFood, item2);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //If data is inserted incorrectly, it will return
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void changeFood(boolean add, int amount){
        Pet pet = getPet("pet");
        if (add) {
            updateFood(String.valueOf(Integer.valueOf(pet.getFood()) + amount), "pet");
        } else {
            updateFood(String.valueOf(Integer.valueOf(pet.getFood()) - amount), "pet");
        }
    }

    //Cursor to return database data
//    public Cursor getData() {
//        Log.d(TAG, "getData: got into function.");
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Log.d(TAG, "getData: got database.");
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        Log.d(TAG, "getData: made cursor and reutrn.");
//        return data;
//    }

    public Pet getPet(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.query(TABLE_NAME, new String[]{ColPetID, ColPetName, ColHealthy, ColFood}, ColPetName + "=?",
                new String[]{name}, null, null, null, null);
        if (data != null && data.moveToFirst()) {
            Pet temp = new Pet(data.getString(2), data.getString(3));
            return temp;
        } else {
            System.out.println("------------didn't find , initial pet-------");
            addData(initHealthy, initFood);
            Pet temp = new Pet(initHealthy, initFood);
            return temp;
        }

    }
    //Cursor to get item id
//    public Cursor getItemID(String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + ColPetID + " FROM " + TABLE_NAME +
//                " WHERE " + ColPetName + " = '" + name + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }

    //Update healthy of pet
    public void updateHealthy(String newHealthy, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE '" + TABLE_NAME + "' SET " + ColHealthy + " = '" + newHealthy + "' WHERE " + ColPetName +
                " = '" + name + "' ";
        db.execSQL(query);
    }

    //Update food of pet
    public void updateFood(String newFood, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE '" + TABLE_NAME + "' SET " + ColFood + " = '" + newFood + "' WHERE " + ColPetName +
                " = '" + name + "' ";
        db.execSQL(query);
    }

//    public void deletePet( String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ColPetName +
//                " = '" + name + "'";
//        db.execSQL(query);
//    }
}
