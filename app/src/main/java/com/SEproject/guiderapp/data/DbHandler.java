package com.SEproject.guiderapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//import com.SEproject.guider.model.App;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "AppUsage";
    private static final String TABLE_NAME = "AppUsage";

    //Column names
    private static final String ID = "id";
    private static final String APP_NAME = "app_name";
    private static final String DATE = "date";
    private static final String USAGE = "usage";


    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " " +
                "("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +APP_NAME+ " TEXT,"
                +DATE+ " TEXT,"
                +USAGE+ " INTEGER"
                +")"
                ;

        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(DROP_TABLE_QUERY);
        onCreate(db);
    }

    public void addUsage(AppUsage appUsage){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_NAME,appUsage.getApp_name());
        contentValues.put(DATE,appUsage.getDate());
        contentValues.put(USAGE,appUsage.getUsage());

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
    }


    public List<AppUsage> getAllData(){
        List<AppUsage> tot = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +APP_NAME+ ", "+DATE+ ", SUM(usage) AS total_usage" +
                "   FROM " + TABLE_NAME+
                " WHERE date = ?"+
                " GROUP BY app_name";

        Cursor cursor = db.rawQuery(query,new String[]{currentDate});

        if(cursor.moveToFirst()){
            do{
                AppUsage appUsage = new AppUsage();

                appUsage.setApp_name(cursor.getString(0));
                appUsage.setDate(cursor.getString(1));
                appUsage.setUsage(cursor.getInt(2));

                tot.add(appUsage);
            }while(cursor.moveToNext());
        }


        return  tot;
    }

    public List<AppUsage> getData(){
        List<AppUsage> tot = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +DATE+ ", SUM(usage) AS total_usage" +
                "   FROM " + TABLE_NAME+
                " GROUP BY date";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                AppUsage appUsage = new AppUsage();

//                appUsage.setApp_name(cursor.getString(0));
                appUsage.setDate(cursor.getString(0));
                appUsage.setUsage(cursor.getInt(1));

                tot.add(appUsage);
            }while(cursor.moveToNext());
        }



        return  tot;
    }

    public List<AppUsage> getMonthData(){
        String month;
        List<AppUsage> tot = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = getReadableDatabase();
        String date[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for(int i =0;i<date.length;i++) {
            month = String.valueOf(i+1);
            if(month.length() != 2){
                month = "0"+month;
            }
            String query = "SELECT SUM(usage) AS total_usage, INSTR(date, '."+month+".') val" +
                    " FROM " + TABLE_NAME +
                    " WHERE val > 0";
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    AppUsage appUsage = new AppUsage();

//                appUsage.setApp_name(cursor.getString(0));
                    appUsage.setMonth(date[i]);
                    appUsage.setUsage(cursor.getInt(0));

                    tot.add(appUsage);
                }while(cursor.moveToNext());
            }
        }
        return  tot;
    }

    public List<AppUsage> getTop(){
        List<AppUsage> top = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +APP_NAME+ ", SUM(usage) AS total_usage" +
                "   FROM " + TABLE_NAME+
                " WHERE date = ?"+
                " GROUP BY "+APP_NAME+
                " ORDER BY total_usage DESC"+
                " LIMIT 5";


        Cursor cursor = db.rawQuery(query, new String[]{currentDate});

        if(cursor.moveToFirst()){
            do{
                AppUsage appUsage = new AppUsage();

               appUsage.setApp_name(cursor.getString(0));
            //    appUsage.setDate(cursor.getString(0));
                appUsage.setUsage(cursor.getInt(1));

                top.add(appUsage);
            }while(cursor.moveToNext());
        }



        return  top;
    }


    public List<AppUsage> getWeek(){
        List<AppUsage> top = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +DATE+ ", SUM(usage) AS total_usage" +
                "   FROM " + TABLE_NAME+
                " GROUP BY "+DATE+
                " ORDER BY "+DATE+" DESC"+
                " LIMIT 7";


        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                AppUsage appUsage = new AppUsage();

                appUsage.setDate(cursor.getString(0));
                //    appUsage.setDate(cursor.getString(0));
                appUsage.setUsage(cursor.getInt(1));

                top.add(appUsage);
            }while(cursor.moveToNext());
        }



        return  top;
    }

}

