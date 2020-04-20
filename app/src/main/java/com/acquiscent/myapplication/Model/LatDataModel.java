package com.acquiscent.myapplication.Model;

public class LatDataModel {

    private String journyId;
    private String id;
    private String LatLongData;
    private String sendToserverLatLong;

    public LatDataModel(String journyId, String id, String latLongData, String sendToserverLatLong) {
        this.journyId = journyId;
        this.id = id;
        LatLongData = latLongData;
        this.sendToserverLatLong = sendToserverLatLong;
    }

    public String getJournyId() {
        return journyId;
    }

    public void setJournyId(String journyId) {
        this.journyId = journyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatLongData() {
        return LatLongData;
    }

    public void setLatLongData(String latLongData) {
        LatLongData = latLongData;
    }

    public String getSendToserverLatLong() {
        return sendToserverLatLong;
    }

    public void setSendToserverLatLong(String sendToserverLatLong) {
        this.sendToserverLatLong = sendToserverLatLong;
    }


}
