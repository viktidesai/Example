package com.mobileappclass.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Tracker.db";
    public static final String TABLE_NAME = "Location";
    public static final String COL1 = "TimeStamp";
    public static final String COL2 = "Longitude";
    public static final String COL3 = "Latitude";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
        System.out.println("\n 299 Database was created");


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +

                COL1 + " TEXT, " +
                COL2 + " REAL, " +
                COL3 + " REAL " +
                ");";
        db.execSQL(query);
        System.out.println("\n  Database was created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addRow(String datetime, double x, double y) {
        ContentValues values = new ContentValues();

        values.put(COL1, datetime);
        values.put(COL2, x);
        values.put(COL3, y);
        SQLiteDatabase db = getWritableDatabase();

        db.close();
    }

    public ArrayList<String> getRows() {
        ArrayList<String> myValues = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (cursor != null && icount > 0) {
            String query = "SELECT * FROM " + TABLE_NAME;

            cursor = db.rawQuery(query, null);

            cursor.moveToFirst();


            do {

                if (cursor.getDouble(cursor.getColumnIndex(COL2)) != 0) {

                    String datetime = cursor.getString(cursor.getColumnIndex(COL1));
                    String xCord = "\t" + cursor.getDouble(cursor.getColumnIndex(COL2));
                    String yCord = "\t" + cursor.getDouble(cursor.getColumnIndex(COL3));
                    myValues.add(datetime + xCord + yCord);
                    //myValues.add(cursor.getInt(cursor.getColumnIndex(COLUMN_X)));
                }
            } while (cursor.moveToNext());

            db.close();
            int i = 0;
            while (myValues.size() > i) {
                System.out.println("499 from Main--" + myValues.get(i).toString() + "\n");
                i++;
            }
        }
        else{
            System.out.println(" ------Empty db-----");
        }
        return myValues;
    }

}