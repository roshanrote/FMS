package com.acquiscent.myapplication.Model;

public class JourneyStatus {

    private String journyId;
    private String status;
    private String ActualBeginKm;

    public JourneyStatus(String journyId, String status, String actualBeginKm) {
        this.journyId = journyId;
        this.status = status;
        ActualBeginKm = actualBeginKm;
    }


    public String getActualBeginKm() {
        return ActualBeginKm;
    }

    public void setActualBeginKm(String actualBeginKm) {
        ActualBeginKm = actualBeginKm;
    }

    public String getJournyId() {
        return journyId;
    }

    public void setJournyId(String journyId) {
        this.journyId = journyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
