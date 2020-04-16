package com.etkilit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Yusuf on 19.10.2018.
 */

public class Database extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "kkvt";//database adı
    private static final String TABLE_NAME = "kullanici";
    private static String IMEI = "imei";
    private static String KAYIT_TARIHI= "kayit_tarihi";
    private static String BITIS_TARIHI = "bitis_tarihi";

    public Database(Context context)  {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // this.context2=context;
         Log.e("DATA",DATABASE_NAME);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + IMEI + " TEXT,"
                + KAYIT_TARIHI + " TEXT,"
                + BITIS_TARIHI + " TEXT,"
                + ")";
        db.execSQL(CREATE_TABLE);


       Log.e("DATA",TABLE_NAME);
    }
    public HashMap<String, String> kullaniciKontrol(String id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String>  tel = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE imei="+"'"+id+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            tel.put(IMEI, cursor.getString(1));
            tel.put(KAYIT_TARIHI, cursor.getString(2));
            tel.put(BITIS_TARIHI, cursor.getString(3));
         }
        cursor.close();
        db.close();
        // return kitap
        return tel;
    }
    public int getRowCount(String id) {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME+" WHERE imei="+"'"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
