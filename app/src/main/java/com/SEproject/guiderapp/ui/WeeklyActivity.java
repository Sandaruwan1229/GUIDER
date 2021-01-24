
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
import java.util.HashMap;
import java.util.List;

public class WeeklyActivity extends AppCompatActivity {

    ListView listview;
    String date[] = {"01 Week","02 Week","03 Week","04 Week"};
    int image[] = {R.drawable.weekone,R.drawable.weektwo,R.drawable.weekthree,R.drawable.weekfour};
    String viewusage[] = {"1h 50m", "2h 1m", "8h 5m","54m", "3h 43m"};
    TextView textDaily,textWeekly, textMonthly;
    ImageView mainMenu,settingsMenu,graphMenu,starMenu;
    ArrayList<String> datess;
    List<AppUsage> applist;
    DbHandler dbhandle;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        listview = findViewById(R.id.listView);
        datess = new getDays().returnValues();
        context = this;
        dbhandle = new DbHandler(context);
        applist = dbhandle.getData();
        CustomAdapter2 adapter = new CustomAdapter2(this,date,image,viewusage,datess,applist);
        listview.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        textDaily = findViewById(R.id.dailyView);
        textWeekly = findViewById(R.id.weeklyView);
        textMonthly = findViewById(R.id.monthlyView);
        mainMenu = findViewById(R.id.main_menu);
        settingsMenu = findViewById(R.id.menu_apps_action_settings);
        graphMenu = findViewById(R.id.graph);
        starMenu = findViewById(R.id.imageView5);

        textWeekly.setTextColor(Color.parseColor("#fc0320"));
        textDaily.setTextColor(Color.parseColor("#000000"));
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
//                Intent i = new Intent(getApplicationContext(),WeeklyActivity.class);
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
                startActivity(new Intent(WeeklyActivity.this, SettingsActivity.class));
                break;
            case R.id.main_menu:
                startActivity(new Intent(WeeklyActivity.this,MainActivity.class ));
                break;
            default:
                break;
        }
        return true;
    }
}

class CustomAdapter2 extends ArrayAdapter<String> {

    Context context;
    String[] date;
    String[] viewusage;
    int[] images;
    ArrayList<String> datess;
    List<AppUsage> applist;
    Integer weeks[];
    CustomAdapter2(Context context, String[] date, int[] images, String[] viewusage, ArrayList<String> datess, List<AppUsage> applist){
        super(context,R.layout.single_row,R.id.dateView,date);
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
        date.setText(this.date[position]);
        usage.setText("usage :");
        datess = new getDays().returnAllValues();
        weeks = new getWeeks(applist).weekData(datess);
        //HashMap<String, Integer> total_hash = new TheHashMap().getDbData(applist);
        String hours = String.valueOf(weeks[position]/3600000);
        String minutes = String.valueOf((weeks[position]%3600000)/(60*1000));
        viewusage.setText(hours+"h "+minutes+"m");


        return row;
    }
}

class getWeeks {
    Integer[] weeks = new Integer[]{0,0,0,0};
    List<AppUsage> applist;
    HashMap<String, Integer> total_hash;
    public getWeeks(List<AppUsage> applist){
        this.applist =applist;
        this.total_hash = new TheHashMap().getDbData(applist);
    }
    public Integer[] weekData(ArrayList<String> datess){
        int value = datess.size() / 7;
        int j = 0;
        for(int i = value;i>0;i--){
            int boundary = j+7;
            int totals = 0;
            while (j < boundary){
                if(total_hash.get(datess.get(j)) instanceof Integer){
                    totals += total_hash.get(datess.get(j));
                }
                j+=1;
            }
            //Timber.i("Total Usages: %s %d", datess.size(),i);
            weeks[i] = totals;
            j = boundary;

        }
        //weeks[0] = applist.size();
        return weeks;
    }
}
