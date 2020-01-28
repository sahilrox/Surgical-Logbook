package com.example.surgicallogbook;

public class OperationProfile {
    String surgeryDate, dateIn, timeIn, dateOut, timeOut, position;

    public String getSurgeryDate() {
        return surgeryDate;
    }

    public String getDateIn() {
        return dateIn;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getDateOut() {
        return dateOut;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getPosition() {
        return position;
    }

    public OperationProfile(String surgeryDate, String dateIn, String timeIn, String dateOut, String timeOut, String position) {
        this.surgeryDate = surgeryDate;
        this.dateIn = dateIn;
        this.timeIn = timeIn;
        this.dateOut = dateOut;
        this.timeOut = timeOut;
        this.position = position;
    }

    public OperationProfile() {
    }
}
