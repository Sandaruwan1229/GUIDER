package com.SEproject.guiderapp.ui;

import com.SEproject.guiderapp.data.AppUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BarChartData {
    int time;
    String day;
    ArrayList<BarChartData>  timeHourArrayList = new ArrayList<>();

    public BarChartData(int time, String day){
        this.time = time;
        this.day = day;
    }


    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public String getDay(){
        return day;
    }

    public void setDay(String hour){
        this.day = hour;
    }

    public ArrayList fillData(List<AppUsage> list){

        HashMap<String, Integer> total_hash = new TheHashMap().getDbData(list);

        for (int i=total_hash.size()-1; i>=0 ; i--){
            timeHourArrayList.add(new BarChartData(list.get(i).getUsage()/3600000, list.get(i).getDate()));
        }


       /* timeHourArrayList.add(new BarChartData(8,"Monday"));
        timeHourArrayList.add(new BarChartData(12,"Tuesday"));
        timeHourArrayList.add(new BarChartData(15,"Wednesday"));
        timeHourArrayList.add(new BarChartData(3,"Thursday"));
        timeHourArrayList.add(new BarChartData(6,"Friday"));
        timeHourArrayList.add(new BarChartData(4,"Saturday"));
        timeHourArrayList.add(new BarChartData(9,"Sunday"));
       /* Random rand = new Random();
        String hours;
        hours = String.format("%sAM", 12);
        int number = rand.nextInt(60);
        timeHourArrayList.add(new BarChartData(number,hours));

        for(int i=1; i<12; i++){
            if(i%4 ==0){
                hours = String.format("%sAM", i);
            }else{
                hours = "";
            }

            number = rand.nextInt(60);
            timeHourArrayList.add(new BarChartData(number,hours));
        }

        hours = String.format("%sPM", 12);
        number = rand.nextInt(60);
        timeHourArrayList.add(new BarChartData(number,hours));

        for(int i=1; i<12; i++){
            if(i%4 ==0){
                hours = String.format("%sPM", i);
            }else{
                hours = "";
            }
            // hours = String.format("%sPM", i);
            number = rand.nextInt(60);
            timeHourArrayList.add(new BarChartData(number,hours));
        }

        hours = String.format("%sAM", 12);
        number = rand.nextInt(60);
        timeHourArrayList.add(new BarChartData(number,hours));*/

        return timeHourArrayList;
    }
}
