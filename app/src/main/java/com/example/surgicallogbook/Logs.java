package com.example.surgicallogbook;

class Logs {
    private String date, name, age, procedure;

    public Logs() {
    }

    public Logs(String date, String name, String age, String procedure) {
        this.date = date;
        this.name = name;
        this.age = age;
        this.procedure = procedure;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }
}
