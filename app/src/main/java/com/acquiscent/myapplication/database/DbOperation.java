package com.acquiscent.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acquiscent.myapplication.Activity.Constants;
import com.acquiscent.myapplication.Activity.JourneyDetails;
import com.acquiscent.myapplication.Model.JourneyModel;
import com.acquiscent.myapplication.Model.JourneyStatus;
import com.acquiscent.myapplication.Model.LatDataModel;
import com.acquiscent.myapplication.Model.PauseModel;

import java.util.ArrayList;

/**
 * Created by acquiscent on 29/1/18.
 */

public class DbOperation {
    private Context context;

    private SQLiteDatabase sqLiteDatabase = null;

    public DbOperation(Context context1) {
        this.context = context1;
    }

    public SQLiteDatabase OPEN() {
        try {
            sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqLiteDatabase;
    }

    public SQLiteDatabase open(){
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        return sqLiteDatabase;
    }

    public void CLOSE() {
        try {
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long addMyFmsData(String locationID, String Latitude, String longitude, String DriverID,
                             String JourneyID, String Speed, Double IdleTime, String DeviceID,String end_timestr,String end_datestr) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.locationID, locationID);
            values.put(DbConstants.Latitude, Latitude);
            values.put(DbConstants.longitude, longitude);
            values.put(DbConstants.DriverID, DriverID);
            values.put(DbConstants.JourneyID, JourneyID);
            values.put(DbConstants.Speed, Speed);
            values.put(DbConstants.IdleTime, IdleTime);
            values.put(DbConstants.DeviceID, DeviceID);
            values.put(DbConstants.EndTime, end_timestr);
            values.put(DbConstants.EndDate, end_datestr);


            result = sqLiteDatabase.insert(DbConstants.tableMyFmsData, null, values);

            //result = sqLiteDatabase.insert(DbConstants.tableMyFmsData,null,values);
            // }
            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public long addMyAfterJournyData(String journeyid1, String begin_kmstr, String end_kmstr, String end_timestr,
                             String end_datestr, String cancle_timestr, String cancle_datestr, String commentstr,String BookedBy,Boolean isSynced) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.journeyID, journeyid1);
            values.put(DbConstants.Begin_km, begin_kmstr);
            values.put(DbConstants.End_km, end_kmstr);
            values.put(DbConstants.End_Tim, end_timestr);
            values.put(DbConstants.End_date, end_datestr);
            values.put(DbConstants.Cancel_time, cancle_timestr);
            values.put(DbConstants.Cancel_date, cancle_datestr);
            values.put(DbConstants.Comment, commentstr);
            values.put(DbConstants.BookedBy, BookedBy);
            values.put(DbConstants.isSynced, isSynced);
            result = sqLiteDatabase.insert(DbConstants.tableJournyData, null, values);

            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /*
    Get My Autokatta Contact...
     */
    public Cursor getMyJournyData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getMyJournyData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public long addSlatlongData(String Latitude, String longitude,String isSend) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.Slatitude, Latitude);
            values.put(DbConstants.Slongitudes, longitude);
            values.put(DbConstants.isSend, isSend);


            result = sqLiteDatabase.insert(DbConstants.tablelatlongData, null, values);

            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public long addPrevlatlongData(String Latitude, String longitude) {
        long result = -1;

        try {

            DbOperation operation = new DbOperation(context);
            operation.OPEN();

            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.Prevlatitude, Latitude);
            values.put(DbConstants.Prevlongitudes, longitude);
            result = sqLiteDatabase.insert(DbConstants.tablePrevlatlongData, null, values);

            operation.CLOSE();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public long addLoginData(String Data) {
        long result = -1;

        try {

            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/

            ContentValues values = new ContentValues();
            values.put(DbConstants.dataLogin, Data);
          //  values.put(DbConstants.Prevlongitudes, longitude);
            result = sqLiteDatabase.insert(DbConstants.tableLoginData, null, values);
            operation.CLOSE();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public long addLatLongOfflineData(String journyId,String data,String sendToServer) {

        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            ContentValues values = new ContentValues();
            values.put(DbConstants.journyId, journyId);
            values.put(DbConstants.offLineData, data);
            values.put(DbConstants.sendToserverLat, sendToServer);
            result = sqLiteDatabase.insert(DbConstants.tableLatLongOfflineData, null, values);
            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public long addJourneyDetailsOfflineData(String journyId,String data,String sendToServer) {
        long result = -1;

        try {
            DbOperation operation = new DbOperation(context);

            operation.OPEN();
            ContentValues values = new ContentValues();
            values.put(DbConstants.journyIdJD, journyId);
            values.put(DbConstants.offLineJDData, data);
            values.put(DbConstants.sendToserverJD, sendToServer);

            result = sqLiteDatabase.insert(DbConstants.tableJourneyDetailsOfflineData, null, values);
            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public long addJourneyDetailsOfflineDataforList(String journyId,String data,String sendToServer) {

        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();

            ContentValues values = new ContentValues();

            values.put(DbConstants.journyIdL, journyId);
            values.put(DbConstants.offLineLData, data);
            values.put(DbConstants.sendToserverL, sendToServer);
            result = sqLiteDatabase.insert(DbConstants.tableJourneyDetailsOfflineDataForList, null, values);

            operation.CLOSE();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Cursor getLoginData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getLoginData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public long updateSlatlongData(String Latitude, String longitude) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.Slongitudes, Latitude);
            values.put(DbConstants.Slongitudes, longitude);


            result = sqLiteDatabase.update(DbConstants.tablelatlongData, values,DbConstants.SId + "= 1", null);

            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public long updatePrevlatlongData(String Latitude, String longitude) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
            values.put(DbConstants.Prevlatitude, Latitude);
            values.put(DbConstants.Prevlongitudes, longitude);


            result = sqLiteDatabase.update(DbConstants.tablePrevlatlongData, values,DbConstants.PrevId + "= 1", null);

            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public long updatelatlongData(String id) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            /*cursor = db.rawQuery(DbQuery.checkPresentNumber + contact + "'", null);
            Log.i("Query", "->" + DbQuery.checkPresentNumber + contact + "'");
            if (cursor.getCount() > 0) {
                Log.e("Present", "->");
            } else {*/
            ContentValues values = new ContentValues();
           // values.put(DbConstants.SId, id);
            values.put(DbConstants.isSend, "true");


            result = sqLiteDatabase.update(DbConstants.tablelatlongData, values,DbConstants.SId + "= " +id, null);

            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Cursor getFmsData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getMyFmsData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getSlatlongData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getSLatLongData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getnewlatlongData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getnewLatLongData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getPrevlatlongData() {
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(Dbqueries.getPrevLatLongData, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public ArrayList<JourneyModel> getNotUploadedJourneyDetails() {
        ArrayList<JourneyModel> arrLogDetailModels = new ArrayList<>();
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tableJourneyDetailsOfflineData + " WHERE " + DbConstants.sendToserverJD + " = " + "0", null);
        if (objCursor.getCount() > 0) {
            objCursor.moveToFirst();
            do {
                arrLogDetailModels.add(new JourneyModel(
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.journyIdJD)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.offLineJDData)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.sendToserverJD))
                ));

            } while (objCursor.moveToNext());
        }
        objCursor.close();
        sqLiteDatabase.close();
        //Utils.out("DB : OFFLINE
        //LOGS : " + arrLogDetailModels.size());
        return arrLogDetailModels;
    }


    public ArrayList<JourneyModel> getNotUploadedJourneyDetailsforList() {
        ArrayList<JourneyModel> arrLogDetailModels = new ArrayList<>();
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tableJourneyDetailsOfflineDataForList + " WHERE " + DbConstants.sendToserverL + " = " + "0", null);
        if (objCursor.getCount() > 0) {
            objCursor.moveToFirst();
            do {
                arrLogDetailModels.add(new JourneyModel(
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.journyIdL)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.offLineLData)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.sendToserverL))
                ));

            } while (objCursor.moveToNext());
        }
        objCursor.close();
        sqLiteDatabase.close();
        //Utils.out("DB : OFFLINE
        //LOGS : " + arrLogDetailModels.size());
        return arrLogDetailModels;
    }

    public ArrayList<LatDataModel> getNotUploadedLatLongDetails() {
        ArrayList<LatDataModel> arrLogDetailModels = new ArrayList<>();
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tableLatLongOfflineData + " WHERE " + DbConstants.sendToserverLat + " = " + "0", null);
        if (objCursor.getCount() > 0) {
            objCursor.moveToFirst();
            do {
                arrLogDetailModels.add(new LatDataModel(
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.journyId)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.idJ)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.offLineData)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.sendToserverLat))
                ));

            } while (objCursor.moveToNext());
        }
        objCursor.close();
        sqLiteDatabase.close();
        return arrLogDetailModels;
    }

    public long updateLog(JourneyModel object) {
        long result = 0;
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(DbConstants.offLineJDData, object.getOffLineJDData());
        objContentValues.put(DbConstants.sendToserverJD, object.getSendToserverJD());
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        String whereClause = DbConstants.journyIdJD + "=?";
        String whereArgs[] = {String.valueOf(object.getJournyIdJD())};
        result = sqLiteDatabase.update(DbConstants.tableJourneyDetailsOfflineData, objContentValues, whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }

    public long updateLogforList(JourneyModel object) {

        long result = 0;
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(DbConstants.offLineLData, object.getOffLineJDData());
        objContentValues.put(DbConstants.sendToserverL, object.getSendToserverJD());
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        String whereClause = DbConstants.journyIdL + "=?";
        String whereArgs[] = {String.valueOf(object.getJournyIdJD())};

        result = sqLiteDatabase.update(DbConstants.tableJourneyDetailsOfflineDataForList, objContentValues, whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }

    public long updateLatLong(LatDataModel object, String id) {
        long result = 0;
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(DbConstants.offLineData, object.getLatLongData());
        objContentValues.put(DbConstants.sendToserverLat, object.getSendToserverLatLong());
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        String whereClause = DbConstants.idJ + "=?";
        String whereArgs[] = {id};
        result = sqLiteDatabase.update(DbConstants.tableLatLongOfflineData, objContentValues, whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }


    public long addPauseOfflineData(String journyId,String status) {
        long result = -1;
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            ContentValues values = new ContentValues();
            values.put(DbConstants.journeyIdp, journyId);
            values.put(DbConstants.status, status);
            result = sqLiteDatabase.insert(DbConstants.tablePauseJourneyOfflineData, null, values);
            operation.CLOSE();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<PauseModel> getNotUploadedPauseJourneyDetails() {

        ArrayList<PauseModel> arrLogDetailModels = new ArrayList<>();
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tablePauseJourneyOfflineData + " WHERE " + DbConstants.status + " = " + "true", null);

        if (objCursor.getCount() > 0) {
            objCursor.moveToFirst();
            do {
                arrLogDetailModels.add(new PauseModel(
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.journeyIdp)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.idP)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.status)))
                );

            } while (objCursor.moveToNext());
        }

        objCursor.close();
        sqLiteDatabase.close();
        return arrLogDetailModels;
    }

    public long addJourneyStartedorNot(String journyId,String started,String ActualBeginKm) {
        long result = -1;
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        try {
            DbOperation operation = new DbOperation(context);
            operation.OPEN();
            ContentValues values = new ContentValues();
            values.put(DbConstants.JourneyIdO, journyId);
            values.put(DbConstants.started, started);
            values.put(DbConstants.ActualBeginKm, ActualBeginKm);
            result = sqLiteDatabase.insert(DbConstants.tableNewOrStartedJourney, null, values);

            operation.CLOSE();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public JourneyStatus getJourneyStartedOrNew(String id) {
        JourneyStatus logDetails = null;
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tableNewOrStartedJourney + " WHERE " + DbConstants.JourneyIdO + " = '" + id + "'", null);
        objCursor.moveToFirst();
        logDetails = new JourneyStatus(
                objCursor.getString(objCursor.getColumnIndex(DbConstants.JourneyIdO)),
                objCursor.getString(objCursor.getColumnIndex(DbConstants.started)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.ActualBeginKm)
                ) );
        objCursor.close();
        sqLiteDatabase.close();
        return logDetails;
    }

    public long updateNewOrStartesJourney(String status, String id) {
        long result = 0;
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(DbConstants.started, status);
        sqLiteDatabase = new DbHelper(context).getWritableDatabase();
        String whereClause = DbConstants.JourneyIdO + "=?";
        String whereArgs[] = {id};
        result = sqLiteDatabase.update(DbConstants.tableNewOrStartedJourney, objContentValues, whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }

    public boolean checkForTableExists() {

        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+DbConstants.tableNewOrStartedJourney+"'";
        Cursor mCursor = sqLiteDatabase.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }
    public boolean CheckIsDataAlreadyInDBorNot( String fieldValue) {

        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        String Query = "Select * from " + DbConstants.tableNewOrStartedJourney + " where " + DbConstants.JourneyIdO + " = " + fieldValue;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /*public ArrayList<LatDataModel> getNotUploadedLatLongDetails() {
        ArrayList<LatDataModel> arrLogDetailModels = new ArrayList<>();
        sqLiteDatabase = new DbHelper(context).getReadableDatabase();
        Cursor objCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbConstants.tableNewOrOldJourney + " WHERE " + DbConstants.started + " = " + "0", null);
        if (objCursor.getCount() > 0) {
            objCursor.moveToFirst();
            do {
                arrLogDetailModels.add(new LatDataModel(
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.journyId)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.idJ)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.offLineData)),
                        objCursor.getString(objCursor.getColumnIndex(DbConstants.sendToserverLat))
                ));

            } while (objCursor.moveToNext());
        }
        objCursor.close();
        sqLiteDatabase.close();
        return arrLogDetailModels;
    }*/

    public void createTable(){
        //String updateQuery = "Update tbl_ro set updateStatus = '"+ status +"' where userId="+"'"+ no +"'";
        sqLiteDatabase.execSQL(Dbqueries.Create_Table_Slatlong);

    }

    public void deleteSyncStatus(){
        //String updateQuery = "Update tbl_ro set updateStatus = '"+ status +"' where userId="+"'"+ no +"'";
        String updateQuery = "delete from " + DbConstants.tableJournyData ;
        sqLiteDatabase.execSQL(updateQuery);
    }

    public void clearDatabase() {
        String clearDBQuery = "DROP TABLE IF EXISTS '"+ DbConstants.tablelatlongData +"'";
        sqLiteDatabase.execSQL(clearDBQuery);
    }
}
