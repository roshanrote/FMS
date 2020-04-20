package com.acquiscent.myapplication.Model;

public class PauseModel {

    private String journyIdp;
    private String idp;
    private String status;

    public PauseModel(String journyIdp, String idp, String status) {
        this.journyIdp = journyIdp;
        this.idp = idp;
        this.status = status;
    }

    public String getJournyIdp() {
        return journyIdp;
    }

    public void setJournyIdp(String journyIdp) {
        this.journyIdp = journyIdp;
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
