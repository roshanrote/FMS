package com.acquiscent.myapplication.Model;

public class JourneyModel {

    private String journyIdJD;
    private String offLineJDData;
    private String sendToserverJD;

    public String getJournyIdJD() {
        return journyIdJD;
    }

    public void setJournyIdJD(String journyIdJD) {
        this.journyIdJD = journyIdJD;
    }

    public String getOffLineJDData() {
        return offLineJDData;
    }

    public void setOffLineJDData(String offLineJDData) {
        this.offLineJDData = offLineJDData;
    }

    public String getSendToserverJD() {
        return sendToserverJD;
    }

    public void setSendToserverJD(String sendToserverJD) {
        this.sendToserverJD = sendToserverJD;
    }

    public JourneyModel(String journyIdJD, String offLineJDData, String sendToserverJD) {
        this.journyIdJD = journyIdJD;
        this.offLineJDData = offLineJDData;
        this.sendToserverJD = sendToserverJD;
    }
}
