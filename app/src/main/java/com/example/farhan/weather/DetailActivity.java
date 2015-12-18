package com.example.farhan.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DetailActivity extends AppCompatActivity {

    public static String timezone;
    ArrayList<String> next24Hours = new ArrayList<String>();
    ArrayList<Integer> summaryIcon = new ArrayList<Integer>();
    ArrayList<String> Temp = new ArrayList<String>();
    ArrayList<String> daydate  = new ArrayList<String>();
    ArrayList<Integer> icons = new ArrayList<Integer>();
    ArrayList<String> minMaxTemps = new ArrayList<String>();
    String[] daysDateArray;
    String[] minMaxTempArray;
    Integer[] iconArray;

    LinearLayout Next24Hours;
    LinearLayout Next7days;

    ListView list;


    String[] times;
    Integer[] summaries;
    String[] temps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String Degree = getIntent().getStringExtra(("Degree"));
        String City = getIntent().getStringExtra(("City"));
        String State = getIntent().getStringExtra(("State"));
        String Stateval = getIntent().getStringExtra("statval");
        String jsonstring = getIntent().getStringExtra("jsonstr");

        String headertext = "More details for " + City + ", " + Stateval;
        TextView tvMoreDetails = (TextView) findViewById(R.id.header1);
        tvMoreDetails.setText(headertext);


        Next24Hours = (LinearLayout) findViewById(R.id.L24hrs);
        Next7days = (LinearLayout) findViewById(R.id.L7days);
        Next7days.setVisibility(View.GONE);

        JSONObject jsonobj;

        try {

            JSONTokener token = new JSONTokener(jsonstring);
            jsonobj = new JSONObject(token);
            timezone = jsonobj.getString("timezone");
            JSONObject hourly = jsonobj.getJSONObject("hourly");
            JSONArray  data =  hourly.getJSONArray("data");


            for(int i= 0; i< 24; i++) {


                JSONObject row  = data.getJSONObject(i);
                Integer time  = Integer.parseInt(row.getString("time"));

                Date date1= new Date(time * 1000L);
                DateFormat sdfAmerica = new SimpleDateFormat("hh:mm a");
                sdfAmerica.setTimeZone(TimeZone.getTimeZone(timezone));
                String timeval = sdfAmerica.format(date1);
                next24Hours.add(i, timeval);


                String icon = row.getString("icon");
                Integer summaryicon = getImage(icon);
                summaryIcon.add(i, summaryicon);

                Double tmp = row.getDouble("temperature");
                String temp = tmp.toString();
                Temp.add(i, temp);


            }

            next24Hours.add(24, " ");
            summaryIcon.add(24,R.drawable.add);
            Temp.add(24," ");

            times = next24Hours.toArray(new String[next24Hours.size()]);
            summaries = summaryIcon.toArray(new Integer[summaryIcon.size()]);
            temps = Temp.toArray(new String[Temp.size()]);


            JSONObject daily = jsonobj.getJSONObject("daily");
            JSONArray data1 = daily.getJSONArray("data");

            for(int i= 48; i <55 ; i++ ) {

                int j = i - 48;
                JSONObject row1 = data1.getJSONObject(j+1);
                Integer time1  = Integer.parseInt(row1.getString("time"));
                Date date2= new Date(time1 * 1000L);
                DateFormat sdfAmerica = new SimpleDateFormat("EEEE, MMM d");
                sdfAmerica.setTimeZone(TimeZone.getTimeZone(timezone));
                String day = sdfAmerica.format(date2);
                daydate.add(j, day);

                String icondaily = row1.getString("icon");
                Integer iconval = getImage(icondaily);
                icons.add(j,iconval);


                Double minTemp = row1.getDouble("temperatureMin");
                Double maxTemp = row1.getDouble("temperatureMax");
                int mTemp  = (int) Math.round(minTemp);
                int mxTemp  = (int) Math.round(maxTemp);

                String tempval;
                if(Degree.matches("Celsius")){

                    tempval = "Min: "+mTemp+"(°C ) | Max: " + mxTemp + "(°C )" ;


                }
                else {
                     tempval = "Min: "+mTemp+"(°F ) | Max: " + mxTemp + "(°F )" ;
                }

                minMaxTemps.add(j, tempval);


                daysDateArray = daydate.toArray(new String[daydate.size()]);
                iconArray = icons.toArray(new Integer[icons.size()]);
                minMaxTempArray = minMaxTemps.toArray(new String[minMaxTemps.size()]);
                Log.d("time", "t" +tempval);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView Temptext = (TextView)findViewById(R.id.tvTemp);
        if(Degree.matches("Celsius")){

            String tmpval = "Temp (°C ) ";
            Temptext.setText(tmpval);

        }
        else {

            String tmpval = "Temp (°F ) ";
            Temptext.setText(tmpval);
        }






    }



    public static Integer getImage(String summary) {
        if (summary.equals("clear-day"))
            return R.drawable.clear;
        if (summary.equals("clear-night"))
            return R.drawable.clear_night;
        if (summary.equals("rain"))
            return R.drawable.rain;
        if (summary.equals("snow"))
            return R.drawable.snow;
        if (summary.equals("sleet"))
            return R.drawable.sleet;
        if (summary.equals("wind"))
            return R.drawable.wind;
        if (summary.equals("fog"))
            return R.drawable.fog;
        if (summary.equals("cloudy"))
            return R.drawable.cloudy;
        if (summary.equals("partly-cloudy-day"))
            return R.drawable.cloud_day;
        else
            return R.drawable.cloud_night;
    }

    public void next24Submit(View view) {

        Next24Hours.setVisibility(View.VISIBLE);
        Next7days.setVisibility(View.GONE);
    }

    public void next7submit(View view) {

        Next24Hours.setVisibility(View.GONE);
        Next7days.setVisibility(View.VISIBLE);
    }
}

