package com.acquiscent.myapplication.database;

/**
 * Created by acquiscent on 29/1/18.
 */

public class Dbqueries {

    public static final String Create_Table_MyFms = "create table " + DbConstants.tableMyFmsData + "(" +
            DbConstants.id + " integer primary key" +
            " autoincrement," + DbConstants.locationID + " text," + DbConstants.Latitude + " text," +
            DbConstants.longitude + " text," + DbConstants.DriverID
            + " text," + DbConstants.JourneyID + " text," + DbConstants.Speed + " text," +
            DbConstants.IdleTime + " double," + DbConstants.DeviceID + " text," + DbConstants.EndTime + " text," + DbConstants.EndDate + " text)";

    public static final String Create_Table_MyJourny = "create table " + DbConstants.tableJournyData + "(" +
            DbConstants.Id + " integer primary key" +
            " autoincrement," + DbConstants.journeyID + " text," + DbConstants.Begin_km + " text," +
            DbConstants.End_km + " text," + DbConstants.End_Tim
            + " text," + DbConstants.End_date + " text," + DbConstants.Cancel_time + " text," +
            DbConstants.Cancel_date + " text," + DbConstants.Comment + " text," + DbConstants.BookedBy + " text," + DbConstants.isSynced + " text)";


    public static final String Create_Table_Slatlong = "create table " + DbConstants.tablelatlongData + "(" +
            DbConstants.SId + " integer primary key" +
            " autoincrement," + DbConstants.Slatitude + " text," + DbConstants.Slongitudes +  " text," + DbConstants.isSend +  " text)";


    public static final String Create_Table_Prevlatlong = "create table " + DbConstants.tablePrevlatlongData + "(" +
            DbConstants.PrevId + " integer primary key" +
            " autoincrement," + DbConstants.Prevlatitude + " text," + DbConstants.Prevlongitudes +  " text)";

    public static final String Create_Table_Login_Data = "create table " + DbConstants.tableLoginData + "(" +
            DbConstants.idl + " integer primary key" +
            " autoincrement," + DbConstants.dataLogin  +  " text)";


    public static final String Create_Table_LatLongOffline_Data = "create table " + DbConstants.tableLatLongOfflineData + "(" +
            DbConstants.idJ + " integer primary key" +
            " autoincrement," + DbConstants.journyId + " text," + DbConstants.offLineData + " text," + DbConstants.sendToserverLat + " text)";

    public static final String Create_Table_JDOffline_Data = "create table " + DbConstants.tableJourneyDetailsOfflineData + "(" +
            DbConstants.idJD + " integer primary key" +
            " autoincrement," + DbConstants.journyIdJD + " text," + DbConstants.offLineJDData + " text," + DbConstants.sendToserverJD + " text)";


    public static final String Create_Table_LOffline_Data = "create table " + DbConstants.tableJourneyDetailsOfflineDataForList + "(" +
            DbConstants.idL + " integer primary key" +
            " autoincrement," + DbConstants.journyIdL + " text," + DbConstants.offLineLData + " text," + DbConstants.sendToserverL + " text)";

    public static final String Create_Table_PauseOffline_Journey = "create table " + DbConstants.tablePauseJourneyOfflineData + "(" +
            DbConstants.idP + " integer primary key" +
            " autoincrement," + DbConstants.journeyIdp + " text," + DbConstants.status + " text)";

    public static final String Create_Table_StartedOrNot = "create table " + DbConstants.tableNewOrStartedJourney + "(" +
            DbConstants.ido + " integer primary key" +
            " autoincrement," + DbConstants.JourneyIdO + " text," + DbConstants.started + " text,"  + DbConstants.ActualBeginKm + " text)";

    static final String getMyFmsData = "select * from " + DbConstants.tableMyFmsData;
    static final String getLoginData = "select * from " + DbConstants.tableLoginData;
    static final String getMyJournyData = "select * from " + DbConstants.tableJournyData;

    static final String getSLatLongData = "select * from " + DbConstants.tablelatlongData + " WHERE " + DbConstants.SId + " = " + " 1 ";
    static final String getnewLatLongData = "select * from " + DbConstants.tablelatlongData + " order by " + DbConstants.SId + " desc " ;

    //static final String getnewLatLongData = "select * from " + DbConstants.tablelatlongData  ;
   // static final String getnewLatLongData = "select * from " + DbConstants.tablelatlongData + " order by " + DbConstants.SId + " desc limit [1] ";
    //select column_name from table_name order by column_name desc limit size.

    static final String getPrevLatLongData = "select * from " + DbConstants.tablePrevlatlongData + " WHERE " + DbConstants.PrevId + " = " + " 1 ";
    static final String addPrevLatLongData = "select * from " + DbConstants.tablePrevlatlongData + " WHERE " + DbConstants.PrevId + " = " + " 1 ";

    static final String DataBase_Name = "fms_db";
    static final int DB_version = 1;

}
