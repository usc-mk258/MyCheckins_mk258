package com.ucs.task2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ucs.task2.model.CheckInModels;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "checkins.db";
    private static final int DATABASE_VERSION = 3;
    private static final String CHECKIN_TABLE_NAME = "my_checkins";
    private static final String CHECKIN_COLUMN_ID = "id";
    private static final String CHECKIN_COLUMN_TITLE ="title";
    private static final String CHECKIN_COLUMN_PLACE ="place";
    private static final String CHECKIN_COLUMN_DETAILS ="details";
    private static final String CHECKIN_COLUMN_DATE ="date";
    private static final String CHECKIN_COLUMN_TIME ="time";
    private static final String CHECKIN_COLUMN_LOCATION ="location";
    private static final String CHECKIN_COLUMN_IMAGE_PATH = "image_path";
    private static SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE " + CHECKIN_TABLE_NAME + "("
                        + CHECKIN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CHECKIN_COLUMN_TITLE + " TEXT,"
                        + CHECKIN_COLUMN_PLACE + " TEXT,"
                        + CHECKIN_COLUMN_DETAILS + " TEXT,"
                        + CHECKIN_COLUMN_DATE+ " DATETIME,"
                        + CHECKIN_COLUMN_TIME+ " DATETIME,"
                        + CHECKIN_COLUMN_LOCATION+ " TEXT,"
                        + CHECKIN_COLUMN_IMAGE_PATH+ " TEXT"
                        + ")"
        );



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CHECKIN_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS "+ SLABS_TABLE_NAME);
       // onCreate(db);
    }
public long insertCheckins(CheckInModels checkInModels){
    db = this.getWritableDatabase();
    long ID = -1;
    try {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKIN_COLUMN_TITLE,checkInModels.getTitle());
        contentValues.put(CHECKIN_COLUMN_PLACE,checkInModels.getPlace());
        contentValues.put(CHECKIN_COLUMN_DETAILS, checkInModels.getDescription());
        contentValues.put(CHECKIN_COLUMN_DATE, checkInModels.getDate());
        contentValues.put(CHECKIN_COLUMN_TIME, checkInModels.getTime());
        contentValues.put(CHECKIN_COLUMN_LOCATION, checkInModels.getLocation());
        contentValues.put(CHECKIN_COLUMN_IMAGE_PATH, checkInModels.getImage_path());
        ID = db.insert(CHECKIN_TABLE_NAME, null, contentValues);


    }catch (Exception e){
        Log.d("DB", "Error in Inserting User");
        e.printStackTrace();
    }finally {
        // close db connection
        db.close();
    }



    return ID;



}


public ArrayList<CheckInModels> getCheckInList(){



    ArrayList<CheckInModels> checkInModelsArrayList = new ArrayList<>();
    db = this.getWritableDatabase();
    Cursor cursor = null;
    try {
        cursor = db.rawQuery("SELECT * FROM " + CHECKIN_TABLE_NAME +" ORDER BY "+ CHECKIN_COLUMN_ID +" DESC",null);
        cursor.moveToFirst();


        if (cursor.getCount() != 0){
            do {


                CheckInModels checkInModels = new CheckInModels();
                checkInModels.setId(cursor.getInt(cursor.getColumnIndex(CHECKIN_COLUMN_ID)));
                checkInModels.setTitle(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_TITLE)));
                checkInModels.setPlace(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_PLACE)));
                checkInModels.setDescription(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_DETAILS)));
                checkInModels.setDate(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_DATE)));
                checkInModels.setTime(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_TIME)));
                checkInModels.setLocation(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_LOCATION)));
                checkInModels.setImage_path(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_IMAGE_PATH)));


                checkInModelsArrayList.add( checkInModels);


            } while (cursor.moveToNext());
        }

    }catch (Exception e){
        Log.d("DB", "Error in Getting  Info");
        e.printStackTrace();
    }finally {

        if (cursor!= null)
            cursor.close();
        db.close();
    }

    return checkInModelsArrayList;

}

public CheckInModels getCheckInByID(int id){

    db = this.getWritableDatabase();
    Cursor cursor = null;
    try {
        cursor = db.rawQuery("SELECT * FROM " + CHECKIN_TABLE_NAME+ " WHERE " + CHECKIN_COLUMN_ID+ " = ?", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        if (cursor.getCount() == 0){
            return null;
        }else{
            CheckInModels checkInModels = new CheckInModels();
            checkInModels.setId(cursor.getInt(cursor.getColumnIndex(CHECKIN_COLUMN_ID)));
            checkInModels.setTitle(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_TITLE)));
            checkInModels.setPlace(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_PLACE)));
            checkInModels.setDescription(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_DETAILS)));
            checkInModels.setDate(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_DATE)));
            checkInModels.setTime(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_TIME)));
            checkInModels.setLocation(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_LOCATION)));
            checkInModels.setImage_path(cursor.getString(cursor.getColumnIndex(CHECKIN_COLUMN_IMAGE_PATH)));

            return checkInModels;
        }

    }catch (Exception e){
        Log.d("DB", "Error in Getting User Info");
        e.printStackTrace();
    }finally {

        if (cursor!= null)
            cursor.close();
        db.close();
    }

    return null;
}


public boolean deleteCheckInByID(int id){

    db = this.getWritableDatabase();
    int a = 0;
    // Cursor cursor = null;
    try {
        a  = db.delete(CHECKIN_TABLE_NAME, CHECKIN_COLUMN_ID + "=?", new String[]{String.valueOf(id)});

    }catch (Exception e){
        Log.d("DB", "Error in Getting User Info");
        e.printStackTrace();
    }finally {

        db.close();
    }

    return a > 0;
}
}
