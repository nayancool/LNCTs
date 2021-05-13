package com.example.lncts;

public class teacher {
    String tname,tid,subject,classes,tpass;



    public teacher(String tname, String tid, String subject, String classname, String tpass) {
        this.tname=tname;
        this.tid=tid;
        this.subject=subject;
        this.classes=classes;
        this.tpass=tpass;
    }
    public String getTname(){ return tname;}

    public String getTid(){ return tid;}

    public String getSub(){return subject;}

    public  String getClasses(){return classes;}

    public String getTpass(){return tpass;}
}
