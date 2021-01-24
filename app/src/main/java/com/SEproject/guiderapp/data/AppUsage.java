package com.SEproject.guiderapp.data;

public class AppUsage {
    private  String id ;
    private  String app_name ;
    private  String date;
    private  int usage ;
    private  String month;
    public AppUsage(){

    }

    public AppUsage(String id, String app_name, String date, int usage) {
        this.id = id;
        this.app_name = app_name;
        this.date = date;
        this.usage = usage;
    }

    public AppUsage(String app_name, String date, int usage) {
        this.app_name = app_name;
        this.date = date;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }
    public String getMonth(){
        return month;
    }
    public void setMonth(String month){
        this.month = month;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }
}
