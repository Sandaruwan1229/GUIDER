package com.SEproject.guiderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.SEproject.guiderapp.R;
import com.SEproject.guiderapp.data.AppUsage;


import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    Context context;
    String rTitle[];
    String rDescription[];
    int rImgs[];
    String rUsage[];
    String rVisits[];
    List<AppUsage> list;

    ListView listView;
   /* String mTitle[] = {"facebook","Whatsapp","Twitter", "Instargram", "Youtube"};
    String mUsage[] = {"Usage Time: 1 Hour 12 Minutes","Usage Time: 55 Minutes","Usage Time: 30 Minutes", "Usage Time: 20 Minutes", "Usage Time: 15 Minutes"};
    String mVisits[] = {"Visits: 19","Visits: 8","Visits: 20","Visits: 17","Visits: 14"};
    int images[] = {R.mipmap.facebook,R.mipmap.whatsapp,R.mipmap.twitter,R.mipmap.intargram,R.mipmap.youtube};
*/
    LayoutInflater layoutInflater = null;

    public ListViewAdapter(Context c, String title[], String usage[], String visits[],List<AppUsage> list){
        super(c, R.layout.row, R.id.textView1,title);
        this.context = c;
        this.rTitle = title;
        this.rUsage = usage;
        this.rVisits = visits;
        this.list = list;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, null, true);



       // ImageView images = row.findViewById(R.id.image);
        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myUsage = row.findViewById(R.id.textView2);
        //TextView myVisits = row.findViewById(R.id.textView3);

      //  images.setImageResource(rImgs[position]);
        myTitle.setText(rTitle[position]);
        myUsage.setText(rUsage[position]);
       // myVisits.setText(rVisits[position]);

        return row;
    }

}
