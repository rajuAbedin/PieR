package com.groupl.project.pier;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.Calendar;



public class Previous1Month extends Fragment {

    private ListView mListView;
    private ImageButton goToCurrentMonth;
    private ImageButton goToPrevious2Month;
    private String TAG = "Previous1Month";

    int month;
    String[] fullMonthArray = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView currentMonthChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Log.d("Month", String.valueOf(month));
        //************************* ACCESS TO THE DATABASE **************************
        SQLiteDatabase pierDatabase = getActivity().openOrCreateDatabase("Statement", android.content.Context.MODE_PRIVATE, null);


        Cursor cursor = null;
        int month = 1;
        int currentYear =0;
        String yearString = "";
        String currentMonthString = "";
        try {
            //find the most recent year
            cursor = pierDatabase.rawQuery("SELECT MAX(year) FROM statement;", null);
            if (cursor.getCount() > 0) {
                Log.i(TAG, "onCreateView: if statement ran");
                cursor.moveToFirst();
                yearString = cursor.getString(0);
                currentYear = Integer.parseInt(yearString);
                Log.i(TAG, "onCreateView: year = " + yearString);
            }
            //find the most recent month from that year
            cursor = pierDatabase.rawQuery("SELECT MAX(month) FROM statement WHERE year ='" + yearString + "';", null);
            if (cursor.getCount() > 0) {
                Log.i(TAG, "onCreateView: second query ran");
                cursor.moveToFirst();
                currentMonthString = cursor.getString(0);
                month = Integer.parseInt(currentMonthString);
                Log.i(TAG, "onCreateView: month = " + currentMonthString);
            }
        }
        catch(Exception e){
            Log.i(TAG, "onCreateView: "+ e);
        }

        //move back n months
        month -=1;
        if (month <= 0) {
            month = 12 + month;
            currentMonthString = String.valueOf(month);
            //move the year back as well
            currentYear -= 1;
            yearString = String.valueOf(currentYear);
        }
        else{
            currentMonthString = String.valueOf(month);
        }


        try{
            cursor = pierDatabase.rawQuery("SELECT * FROM statement WHERE month = '" + currentMonthString + "' AND year = '" + yearString + "';", null);
        }
        catch(Exception e){
            Log.i(TAG, "onCreateView: "+e);
        }

        View view = inflater.inflate(R.layout.previous1_month_fragment_layout, container, false);
        currentMonthChange = (TextView) view.findViewById(R.id.currentMonthTextView);

        mListView = (ListView) view.findViewById(R.id.ListView);
        goToCurrentMonth = (ImageButton) view.findViewById(R.id.btnGoToCurrentMonth);
        goToPrevious2Month = (ImageButton) view.findViewById(R.id.btnGoToPrevious2);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        ArrayList<DayOfTheMonthListItem> MontlyList = new ArrayList<>();
        String[] monthArray = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String monthString = monthArray[month - 1];


        try {
            if (cursor.getCount() > 0) {
                int description = cursor.getColumnIndex("description");
                int category = cursor.getColumnIndex("category");
                int value = cursor.getColumnIndex("value");
                int day = cursor.getColumnIndex("day");
                int year = cursor.getColumnIndex("year");

                cursor.moveToFirst();

                while (cursor != null) {
                    DayOfTheMonthListItem item = new DayOfTheMonthListItem("drawable://" + R.drawable.label, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    if (cursor.getString(category).toLowerCase().equals("general")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.general, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("groceries")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.groceries, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("eating out")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.eating_out, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("transport")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.transport, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("bills")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.bills, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("rent")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.rent, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    if (cursor.getString(category).toLowerCase().equals("untagged")) {
                        item = new DayOfTheMonthListItem("drawable://" + R.drawable.shopping, cursor.getString(description), "£" + cursor.getString(value), cursor.getString(day), monthString);
                    }
                    currentMonthChange.setText(fullMonthArray[month - 1] + "\n" + yearString);

                    MontlyList.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MonthListItemAdapter adapter = new MonthListItemAdapter(getActivity(), R.layout.adapter_view_layout, MontlyList);
        mListView.setAdapter(adapter);

        goToCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this will give us acces to every method inside of the main activity
                ((FullStatement) getActivity()).setViewPager(0);
            }
        });
        goToPrevious2Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this will give us acces to every method inside of the main activity
                ((FullStatement) getActivity()).setViewPager(2);
            }
        });

        return view;
    }
}
