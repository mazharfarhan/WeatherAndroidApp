package com.example.farhan.weather;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {

    public static String wtemperature;
    public static String wprecipitation;
    public static String wrain;
    public static String wwindspeed;
    public static String wdewpoint;
    public static String wHumidity;
    public static String wvisibility;
    public static String timezone;
    public static String wsunrise;
    public static String wsunset;
    public static String wmin;
    public static String maxtemp;


    public CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String jsonstring = getIntent().getStringExtra(MainActivity.JSO_NSTRING);
        String Degree = getIntent().getStringExtra(("Degree"));
        String City = getIntent().getStringExtra(("City"));
        String State = getIntent().getStringExtra(("State"));
        int statid = getResources().getIdentifier(State, "string", getPackageName());
        String stateval = getResources().getString(statid);


        Log.d("Degree","Deg"+Degree);
        JSONObject jsonobj;
        try {

            JSONTokener token = new JSONTokener(jsonstring);
            jsonobj = new JSONObject(token);
            JSONObject currently = jsonobj.getJSONObject("currently");



            String weathericon = currently.getString("icon");

            switch (weathericon) {

                case "clear-day":
                    weathericon = "clear";
                    break;

                case "clear-night":
                    weathericon = "clear_night";
                    break;

                case "rain":
                    weathericon = "rain";
                    break;

                case "snow":
                    weathericon = "snow";
                    break;

                case "sleet":
                    weathericon = "sleet";
                    break;

                case "wind":
                    weathericon = "wind";
                    break;

                case "fog":
                    weathericon = "fog";
                    break;

                case "cloudy":
                    weathericon = "cloudy";
                    break;

                case "partly-cloudy-day":
                    weathericon = "cloud_day";
                    break;

                case "partly-cloudy-night":
                    weathericon = "cloud_night";
                    break;
            }



            int id  = getResources().getIdentifier(weathericon,"drawable",getPackageName());

            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageResource(id);


            timezone = jsonobj.getString("timezone");

            // weather condition
            String condition = currently.getString("summary");
            String infotext = condition + " in " + City +", " +stateval;
            TextView conditionText  = (TextView) findViewById(R.id.textView24);
            conditionText.setText(infotext);

            // Temperature

            Double temp = currently.getDouble("temperature");
            int tmp  = (int) Math.round(temp);
            TextView TempText  = (TextView) findViewById(R.id.textView25);

            if(Degree.matches("Celsius")){

                wtemperature = tmp + " \u00b0";
                TempText.setText(Html.fromHtml(wtemperature+"<sup style='font-size:12px'>C</sup>"));

            }
            else {
                wtemperature = tmp + " \u00b0";
                TempText.setText(Html.fromHtml(wtemperature+"<sup style='font-size:12px'>F</sup>"));
            }

            Double precipitation  = currently.getDouble("precipIntensity");
            if(Degree.matches("Celsius")){
                precipitation = precipitation / 25.4;
            }

            if(0 <= precipitation && precipitation < 0.002){
                wprecipitation = "None";
            }

            if(precipitation >= 0.002 && precipitation < 0.017){
                wprecipitation = "Very Light";
            }

            if(precipitation >= 0.017 && precipitation < 0.1){
                wprecipitation = "Light";
            }

            if(precipitation >= 0.1 && precipitation < 0.4){
                wprecipitation = "Moderate";
            }

            if(precipitation >= 0.4){
                wprecipitation = "Heavy";
            }

            TextView precipText  = (TextView) findViewById(R.id.textView9);
            precipText.setText(wprecipitation);

            Double rain  = currently.getDouble("precipProbability");
            int rainval = (int) (rain * 100);
            wrain  = rainval + "%";

            TextView rainText  = (TextView) findViewById(R.id.textView11);
            rainText.setText(wrain);

            Double windspeed = currently.getDouble("windSpeed");
            String windval = String.format("%.2f",windspeed);

            if(Degree.matches("Celsius")){
                wwindspeed = windval + " m/s";
            }
            else {

                wwindspeed = windval + " mph";
            }

            TextView windText  = (TextView) findViewById(R.id.textView13);
            windText.setText(wwindspeed);


            // Dew Point
            Double dew  = currently.getDouble("dewPoint");
            int dewval  = (int) Math.round(dew);

            if(Degree.matches("Celsius")){
                wdewpoint = dewval + " \u00b0" +"C";
            }
            else {

                wdewpoint = dewval + " \u00b0" +"F";
            }

            TextView dewText  = (TextView) findViewById(R.id.textView15);
            dewText.setText(wdewpoint);


            //Humidity
            Double humidity  = currently.getDouble("humidity");
            int humval = (int) (humidity * 100);
            wHumidity  = humval + "%";

            TextView humText  = (TextView) findViewById(R.id.textView17);
            humText.setText(wHumidity);

            Double visibility  = currently.getDouble("visibility");
            String vis = String.format("%.2f", visibility);

            if(Degree.matches("Celsius")){
                wvisibility = vis + " Km";
            }
            else {

                wvisibility = vis  +" mi";
            }

            TextView visText  = (TextView) findViewById(R.id.textView19);
            visText.setText(wvisibility);

            JSONObject daily = jsonobj.getJSONObject("daily");
            JSONArray data = daily.getJSONArray("data");
            JSONObject first = data.getJSONObject(0);

            Double minTemp = first.getDouble("temperatureMin");
            int mtmp  = (int) Math.round(minTemp);
            wmin = "L:"+mtmp + "\u00b0";

            TextView minText  = (TextView) findViewById(R.id.textView26);
            minText.setText(wmin);

            //Max tmp
            Double maxTemp = first.getDouble("temperatureMax");
            int maxtmp  = (int) Math.round(maxTemp);
            maxtemp = "H:"+ maxtmp + "\u00b0";

            TextView maxText  = (TextView) findViewById(R.id.textView27);
            maxText.setText(maxtemp);



            int sunrise = Integer.parseInt(first.getString("sunriseTime"));
            int sunset = Integer.parseInt(first.getString("sunsetTime"));


            Date date1  = new Date(sunrise * 1000L);
            Date date2   = new Date(sunset *1000L);

            DateFormat sdfAmerica = new SimpleDateFormat("hh:mm a");
            sdfAmerica.setTimeZone(TimeZone.getTimeZone(timezone));
            String sunrisetime = sdfAmerica.format(date1);
            String sunsettime = sdfAmerica.format(date2);
            Log.d("String","Sunrise"+sunrisetime);

            TextView riseText  = (TextView) findViewById(R.id.textView21);
            riseText.setText(sunrisetime);

            TextView seText  = (TextView) findViewById(R.id.textView23);
            seText.setText(sunsettime);


        } catch (Exception e) {
            Log.d("Weather", "Exception", e);
        }


    }


    public void detailshandler(View view) {

        String jsonstring = getIntent().getStringExtra(MainActivity.JSO_NSTRING);
        String Degree = getIntent().getStringExtra(("Degree"));
        String City = getIntent().getStringExtra(("City"));
        String State = getIntent().getStringExtra(("State"));
        int statid = getResources().getIdentifier(State, "string", getPackageName());
        String stateval = getResources().getString(statid);


        Intent intent= new Intent(ResultActivity.this,DetailActivity.class);
        intent.putExtra("Degree",Degree);
        intent.putExtra("City", City);
        intent.putExtra("State", State);
        intent.putExtra("jsonstr",jsonstring);
        intent.putExtra("statval",stateval);

        startActivity(intent);

    }

    public void mapsubmit(View view) throws JSONException {

        Intent newintent = new Intent(ResultActivity.this,MapActivity.class);
        String jsonstring = getIntent().getStringExtra(MainActivity.JSO_NSTRING);
        JSONObject jsonobj;
        JSONTokener token = new JSONTokener(jsonstring);
        jsonobj = new JSONObject(token);

        String lat = jsonobj.getString("latitude");
        String lon = jsonobj.getString("longitude");
        newintent.putExtra("lat",lat);
        newintent.putExtra("lon", lon);
        startActivity(newintent);
    }

    public void facebooksubmit(View view) {

        Intent faceIntent = new Intent(ResultActivity.this,FacebookActivity.class);
        String jsonstring = getIntent().getStringExtra(MainActivity.JSO_NSTRING);
        String Degree = getIntent().getStringExtra(("Degree"));
        String City = getIntent().getStringExtra(("City"));
        String State = getIntent().getStringExtra(("State"));
        int statid = getResources().getIdentifier(State, "string", getPackageName());
        String stateval = getResources().getString(statid);

        faceIntent.putExtra("degreevalue",Degree);
        faceIntent.putExtra("cityvalue", City);
        faceIntent.putExtra("statevalue", stateval);
        faceIntent.putExtra("result",jsonstring);
        faceIntent.putExtra("statval",State);




        startActivity(faceIntent);


    }
}
