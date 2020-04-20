package com.acquiscent.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by acquiscent on 29/1/18.
 */

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, DbConstants.DataBase_Name, null, DbConstants.DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         try {
            db.execSQL(Dbqueries.Create_Table_MyFms);
            db.execSQL(Dbqueries.Create_Table_MyJourny);
            db.execSQL(Dbqueries.Create_Table_Slatlong);
            db.execSQL(Dbqueries.Create_Table_Prevlatlong);
            db.execSQL(Dbqueries.Create_Table_Login_Data);
            db.execSQL(Dbqueries.Create_Table_LatLongOffline_Data);
            db.execSQL(Dbqueries.Create_Table_JDOffline_Data);
            db.execSQL(Dbqueries.Create_Table_LOffline_Data);
            db.execSQL(Dbqueries.Create_Table_PauseOffline_Journey);
            db.execSQL(Dbqueries.Create_Table_StartedOrNot);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbConstants.tablelatlongData);
        onCreate(db);
    }
}
