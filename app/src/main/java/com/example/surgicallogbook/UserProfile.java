package com.example.surgicallogbook;

public class UserProfile {
    public String  name, degree, country, state, city,  regNo, hospital, designation, email, mobile;

    public UserProfile() {

    }

    public UserProfile(String name, String degree, String country, String state, String city, String regNo, String hospital, String designation, String email, String mobile) {
        this.name = name;
        this.degree = degree;
        this.country = country;
        this.state = state;
        this.city = city;
        this.regNo = regNo;
        this.hospital = hospital;
        this.designation = designation;
        this.email = email;
        this.mobile = mobile;

    }

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getHospital() {
        return hospital;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}

