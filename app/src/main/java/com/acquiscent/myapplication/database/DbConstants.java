package com.acquiscent.myapplication.database;

/**
 * Created by acquiscent on 29/1/18.
 */

public class DbConstants {

    public static final String TAG = "FMS";
    static final String DataBase_Name = "FMSS.db";
    static final int DB_version = 14;

    /*
    Table Names...
     */
    static final String tableMyFmsData = "table_fms_data";
    static final String tableJournyData = "table_journy_data";
    static final String tablelatlongData = "table_slatlong_data";
    static final String tablePrevlatlongData = "table_Prevlatlong_data";
    static final String tableLoginData = "table_login_data";
    static final String tableLatLongOfflineData = "table_latlong_Offline_data";
    static final String tableJourneyDetailsOfflineData = "table_JourneyDetails_Offline_data";
    static final String tableJourneyDetailsOfflineDataForList = "table_JourneyDetails_Offline_data_for_list";
    static final String tablePauseJourneyOfflineData = "table_PauseJourney_Offline_data";
    static final String tableNewOrStartedJourney = "table_NewOrStarted_Journey";


    //tableLatLongOffline
    public static final String idJ = "idj";
    public static final String journyId = "journyId";
    public static final String offLineData = "offlineData";
    public static final String sendToserverLat = "sendtoserverlat";



    //tableJourneyDetailsOffline
    public static final String idJD = "idjd";
    public static final String journyIdJD = "journyIdJd";
    public static final String offLineJDData = "offlineJDData";
    public static final String sendToserverJD = "sendtoserverjd";

    //tableJourneyDetailsOffline
    public static final String idL = "idl";
    public static final String journyIdL = "journyIdl";
    public static final String offLineLData = "offlinelData";
    public static final String sendToserverL = "sendtoserverl";

    //tableLoginData
    public static final String idl = "idl";
    public static final String dataLogin = "dataLogin";


    //tableMyAutokattaContacts
    public static final String id = "id";
    public static final String locationID = "locationID";
    public static final String Latitude = "Latitude";
    public static final String longitude = "longitude";
    public static final String DriverID = "DriverID";
    public static final String JourneyID = "JourneyID";
    public static final String Speed = "Speed";
    public static final String IdleTime = "IdleTime";
    public static final String DeviceID = "DeviceID";
    public static final String EndTime = "end_timestr";
    public static final String EndDate = "end_datestr";


    //AfterJournyInfo
    public static final String Id = "Id";
    public static final String journeyID = "journeyID";
    public static final String Begin_km = "begin_kmstr";
    public static final String End_km = "end_kmstr";
    public static final String End_Tim = "end_timestr";
    public static final String End_date = "end_datestr";
    public static final String Cancel_time = "cancle_timestr";
    public static final String Cancel_date = "cancle_datestr";
    public static final String Comment = "commentstr";
    public static final String BookedBy = "BookedBy";
    public static final String isSynced = "false";


    //Starting lat long
    public static final String SId = "SId";
    public static final String Slatitude = "Slatitude";
    public static final String Slongitudes = "Slongitude";
    public static final String isSend = "isSend";


    public static final String PrevId = "PrevId";
    public static final String Prevlatitude = "Prevlatitude";
    public static final String Prevlongitudes = "Prevlongitude";


    //tablePauseOfflineData
    public static final String idP = "idp";
    public static final String journeyIdp = "journeyIdp";
    public static final String status = "status";

    public static final String ido = "ido";
    public static final String JourneyIdO = "JourneyIdO";
    public static final String started = "started";
    public static final String ActualBeginKm = "ActualBeginKm";


}
