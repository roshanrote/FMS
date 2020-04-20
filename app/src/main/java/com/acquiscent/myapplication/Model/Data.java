package com.acquiscent.myapplication.Model;

/**
 * Created by acquiscent on 19/11/16.
 */

public class Data {


    public String getBookDate() {
        return BookDate;
    }

    public void setBookDate(String bookDate) {
        this.BookDate = bookDate;
    }

    public String getBookedBy() {
        return BookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.BookedBy = bookedBy;
    }

    public String getPassenger() {
        return Passenger;
    }

    public void setPassenger(String passenger) {
        this.Passenger = passenger;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.VehicleType = vehicleType;
    }

    public String getJourney_id() {
        return journey_id;
    }

    public void setJourney_id(String journey_id) {
        this.journey_id = journey_id;
    }

    public String journey_id;
    public String VehicleType;
    public String BookDate;
    public String BookedBy;
    public String Passenger;
    public String BeginKm;

    public String getIsPaused() {
        return IsPaused;
    }

    public void setIsPaused(String isPaused) {
        IsPaused = isPaused;
    }

    public String IsPaused;

    public String getBeginKm() {
        return BeginKm;
    }

    public void setBeginKm(String beginKm) {
        BeginKm = beginKm;
    }

    public String getBookingRef() {
        return BookingRef;
    }

    public void setBookingRef(String bookingRef) {
        BookingRef = bookingRef;
    }

    public String DeviceID;
    public String BookingRef;
    public String DriverID;

    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String lati;
    public String longi;

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getJourneyStartDate() {
        return JourneyStartDate;
    }

    public void setJourneyStartDate(String journeyStartDate) {

        this.JourneyStartDate = journeyStartDate;
    }

    public String getJourneyEndDate() {
        return JourneyEndDate;
    }

    public void setJourneyEndDate(String journeyEndDate) {
        this.JourneyEndDate = journeyEndDate;
    }

    public String getDestinationCode() {
        return DestinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.DestinationCode = destinationCode;
    }

    public String getProposedEndTime() {
        return ProposedEndTime;
    }

    public void setProposedEndTime(String proposedEndTime) {
        this.ProposedEndTime = proposedEndTime;
    }

    public String JourneyStartDate;
    public String JourneyEndDate;
    public String DestinationCode;
    public String ProposedEndTime;


}
