package com.example.lncts;

public class student {
    String sname;
    String sid;
    String classes;
    String spass;


    public student(String sname, String sid, String classes, String spass){
        this.sname=sname;
        this.sid=sid;
        this.classes=classes;
        this.spass=spass;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getSid() {
        return sid;
    }
    public String getClasses() {
        return classes;
    }
    public String getSpass() {
        return spass;
    }
    }

