package com.SEproject.guiderapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.SEproject.guiderapp.R;
import com.SEproject.guiderapp.data.AppUsage;
import com.SEproject.guiderapp.data.DbHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Report extends AppCompatActivity {

    ListView listview;
    HashMap total = new TheHashMap().getHash();
    String date[] = new String[31];
    int image[] = {R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1,R.drawable.mar1};
    String viewusage[] ;
    TextView textDaily,textWeekly, textMonthly;
    ImageView mainMenu,settingsMenu,graphMenu,starMenu;
    ArrayList<String> datess;
    HashMap<String, Integer> total_hash;
    DbHandler dbhandle;
    Context context;
    List<AppUsage> applist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = this;
        dbhandle = new DbHandler(context);
        applist = dbhandle.getData();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        listview = findViewById(R.id.listView);
        datess = new getDays().returnValues();
        CustomAdapter adapter = new CustomAdapter(this,date,image,viewusage,datess,applist);
        listview.setAdapter(adapter);


        textDaily = findViewById(R.id.dailyView);
        textWeekly = findViewById(R.id.weeklyView);
        textMonthly = findViewById(R.id.monthlyView);
        mainMenu = findViewById(R.id.main_menu);
        settingsMenu = findViewById(R.id.menu_apps_action_settings);
        graphMenu = findViewById(R.id.graph);
        starMenu = findViewById(R.id.imageView5);

        textDaily.setTextColor(Color.parseColor("#fc0320"));
        textWeekly.setTextColor(Color.parseColor("#000000"));
        textMonthly.setTextColor(Color.parseColor("#000000"));
        textDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDaily.setTextColor(Color.parseColor("#fc0320"));
                textWeekly.setTextColor(Color.parseColor("#000000"));
                textMonthly.setTextColor(Color.parseColor("#000000"));
                Intent i = new Intent(getApplicationContext(),Report.class);
                startActivity(i);
            }
        });
        textWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDaily.setTextColor(Color.parseColor("#000000"));
                textWeekly.setTextColor(Color.parseColor("#fc0320"));
                textMonthly.setTextColor(Color.parseColor("#000000"));

                Intent i = new Intent(getApplicationContext(),WeeklyActivity.class);
                startActivity(i);
            }
        });
        textMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDaily.setTextColor(Color.parseColor("#000000"));
                textWeekly.setTextColor(Color.parseColor("#000000"));
                textMonthly.setTextColor(Color.parseColor("#fc0320"));

                Intent i = new Intent(getApplicationContext(),MonthlyActivity.class);
                startActivity(i);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        settingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
            }
        });
//        graphMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),Report.class);
//                startActivity(i);
//            }
//        });
        starMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ScrollingActivity.class);
                startActivity(i);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_apps_action_settings:
                startActivity(new Intent(Report.this, SettingsActivity.class));
                break;
            case R.id.main_menu:
                startActivity(new Intent(Report.this,MainActivity.class ));
                break;
            case R.id.graph:
                startActivity(new Intent(Report.this, ScrollingActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

}

class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    String[] date;
    String[] viewusage;
    int[] images;
    ArrayList<String> datess;
    List<AppUsage> applist;
    //DbHandler dbhandle;
    String months[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    String hrs;
    String min;
    CustomAdapter(Context context, String[] date,int[] images,String[] viewusage,ArrayList<String> datess,List<AppUsage> applist){
        super(context,R.layout.single_row,R.id.dateView,datess);
        this.context = context;
        this.images = images;
        this.date = date;
        this.viewusage = viewusage;
        this.datess = datess;
        this.applist = applist;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.single_row,parent,false);

        ImageView imageView = row.findViewById(R.id.imageView);
        TextView date = row.findViewById(R.id.dateView);
        TextView usage = row.findViewById(R.id.usageView);
        TextView viewusage = row.findViewById(R.id.viewUsage);

        imageView.setImageResource(images[position]);
        String day = datess.get(position).substring(8,10);
        String month = months[Integer.parseInt(datess.get(position).substring(5,7))-1].substring(0,3);
        String year = datess.get(position).substring(0,4);
        date.setText(month+" "+day+", "+year);
        usage.setText("usage :");
        HashMap<String, Integer> total_hash = new TheHashMap().getDbData(applist);

        if((total_hash.get(datess.get(position))) instanceof Integer){
            this.hrs = String.valueOf((total_hash.get(datess.get(position)))/3600000);
            this.min = String.valueOf(((total_hash.get(datess.get(position)))%3600000)/60000);
        }
        else {
            this.hrs = "0";
            this.min = "0";
        }
        viewusage.setText(hrs+"h "+min+"m");
        return row;
    }
}

class TheHashMap {
    HashMap<String, Integer> totalUsageData = new HashMap<String, Integer>();

    public TheHashMap() {

    }
    public HashMap getDbData(List<AppUsage> applist){
        for (int i=0; i< applist.size(); i++){
            totalUsageData.put(applist.get(i).getDate(),applist.get(i).getUsage());
        }
        return totalUsageData;
    }
    public HashMap getDbMonthData(List<AppUsage> applist){
        for (int i=0; i< applist.size(); i++){
            if(applist.get(i).getDate() != "2021.01.13") {
                int count = applist.get(i).getUsage();
                totalUsageData.put("January", count);
            }

        }
        return totalUsageData;
    }

    public HashMap getHash(){
        return this.totalUsageData;
    }
}

class getDays{
    Calendar today;
    int current =0;
    String month;
    String year;
    String day;
    public getDays(){
        this.today = Calendar.getInstance();
        this.current = today.get(Calendar.DAY_OF_MONTH);
        this.month = String.valueOf(today.get(Calendar.MONTH)+1);
        this.year = String.valueOf(today.get(Calendar.YEAR));

    }

    public ArrayList<String> returnValues(){
        ArrayList<String> thedays = new ArrayList<String>();
        if(month.length() != 2){
            month = "0"+month;
        }
        for(int i=current; i>= 1 ; i--){
            day = String.valueOf(i);
            if(day.length() != 2){
                day= "0"+day;
            }
            String full_date = year+"."+month+"."+day;
            thedays.add(full_date);
            if(thedays.size() == 7){
                return thedays;
            }
        }
        return thedays;
    }

    public ArrayList<String> returnAllValues(){
        ArrayList<String> thedays = new ArrayList<String>();
        if(month.length() != 2){
            month = "0"+month;
        }
        for(int i=current; i>= 1 ; i--){
            day = String.valueOf(i);
            if(day.length() != 2){
                day= "0"+day;
            }
            String full_date = year+"."+month+"."+day;
            thedays.add(full_date);
        }
        return thedays;
    }

}
