package com.example.surgicallogbook;

public class PatientProfile {
    String name, age, sex, hospital_reg_no;

    public PatientProfile() {
    }

    public PatientProfile(String name, String age, String sex, String hospital_reg_no) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.hospital_reg_no = hospital_reg_no;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getHospital_reg_no() {
        return hospital_reg_no;
    }
}
