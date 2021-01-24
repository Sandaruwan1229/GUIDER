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
import java.util.List;

public class MonthlyActivity extends AppCompatActivity {

    ListView listview;
    String date[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    int image[] = {R.drawable.jan,R.drawable.feb,R.drawable.mar,R.drawable.apr,R.drawable.may,R.drawable.jun,R.drawable.jul,R.drawable.aug,R.drawable.sep,R.drawable.oct,R.drawable.nov,R.drawable.dec};
    String viewusage[];
    TextView textDaily,textWeekly, textMonthly;
    ImageView mainMenu,settingsMenu,graphMenu,starMenu;
    ArrayList<String> datess;
    DbHandler dbhandle;
    Context context;
    List<AppUsage> applist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        context = this;
        dbhandle = new DbHandler(context);
        applist =  dbhandle.getMonthData();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        listview = findViewById(R.id.listView);
        datess = new getDays().returnValues();
        CustomAdapter3 adapter = new CustomAdapter3(this,date,image,viewusage,datess,applist);
        listview.setAdapter(adapter);

        textDaily = findViewById(R.id.dailyView);
        textWeekly = findViewById(R.id.weeklyView);
        textMonthly = findViewById(R.id.monthlyView);
        mainMenu = findViewById(R.id.main_menu);
        settingsMenu = findViewById(R.id.menu_apps_action_settings);
        graphMenu = findViewById(R.id.graph);
        starMenu = findViewById(R.id.imageView5);

        textWeekly.setTextColor(Color.parseColor("#000000"));
        textDaily.setTextColor(Color.parseColor("#000000"));
        textMonthly.setTextColor(Color.parseColor("#fc0320"));
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
//                Intent i = new Intent(getApplicationContext(),MonthlyActivity.class);
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
                startActivity(new Intent(MonthlyActivity.this, SettingsActivity.class));
                break;
            case R.id.main_menu:
                startActivity(new Intent(MonthlyActivity.this,MainActivity.class ));
                break;
            default:
                break;
        }
        return true;
    }
}

class CustomAdapter3 extends ArrayAdapter<String> {

    Context context;
    String[] date;
    String[] viewusage;
    int[] images;
    ArrayList<String> datess;
    List<AppUsage> applist;
    CustomAdapter3(Context context, String[] date, int[] images, String[] viewusage, ArrayList<String> datess,List<AppUsage> applist){
        super(context,R.layout.single_row,R.id.dateView,date);
        this.context = context;
        this.images = images;
        this.date = date;
        this.viewusage = viewusage;
        this.datess = datess;
        this.applist =applist;
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
        date.setText(String.valueOf(applist.get(position).getMonth()));
        usage.setText("usage :");
        String hrs = String.valueOf(applist.get(position).getUsage()/3600000);
        String min = String.valueOf((applist.get(position).getUsage()%3600000)/(60000));
        viewusage.setText(hrs+"h "+min+"m");


        return row;
    }
}