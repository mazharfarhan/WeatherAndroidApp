package com.example.farhan.weather;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    public static final String JSO_NSTRING = "JSONstring";
    public static boolean flag =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.states,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

       EditText street = (EditText) findViewById(R.id.editText);
       String val = street.getText().toString();



    }

    public void submitClickHandler(View view) throws UnsupportedEncodingException {

        EditText street = (EditText) findViewById(R.id.editText);
        EditText city = (EditText) findViewById(R.id.editText2);
        Spinner state = (Spinner) findViewById(R.id.spinner);
        RadioGroup degree = (RadioGroup) findViewById(R.id.radioGroup);

        String streetval = street.getText().toString();
        String cityval = city.getText().toString();
        String stateval = state.getSelectedItem().toString();
        RadioButton deg = (RadioButton) findViewById(degree.getCheckedRadioButtonId());
        String degval = deg.getText().toString();

        Log.d("String", "Street" + streetval);

        if(streetval.length() == 0) {
            Toast.makeText(this,"Please enter the street",Toast.LENGTH_LONG).show();
            flag=false;


        }

        if(cityval.length() == 0) {
            Toast.makeText(this,"Please enter the city",Toast.LENGTH_LONG).show();
            flag=false;

        }

        if(stateval.matches("Select your state")) {
            Toast.makeText(this,"Please select the state",Toast.LENGTH_LONG).show();
            flag=false;

        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("forecastapplication.elasticbeanstalk.com")
                .appendPath("sample2.php")
                .appendQueryParameter("street", streetval)
                .appendQueryParameter("city", cityval)
                .appendQueryParameter("state", stateval)
                .appendQueryParameter("degree", degval);

        String url = builder.build().toString();

        if(isonline() && flag){

            Mytask mytask = new Mytask();
            mytask.execute(url);

        }
        else {
            Toast.makeText(this,"Network is not available", Toast.LENGTH_LONG).show();
        }
    }
    protected boolean isonline(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if(netinfo != null && netinfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    public void aboutClick(View view) {

        Intent aboutid = new Intent(MainActivity.this, About.class);
        startActivity(aboutid);

    }

    public void imageClick(View view) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://forecast.io"));
        startActivity(intent);
    }

    public void clearClickHandler(View view) {

        EditText street = (EditText) findViewById(R.id.editText);
        EditText city = (EditText) findViewById(R.id.editText2);
        Spinner state = (Spinner) findViewById(R.id.spinner);
        RadioGroup degree = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton deg = (RadioButton) findViewById(degree.getCheckedRadioButtonId());

        street.setText(" ");
        city.setText(" ");
        state.setSelection(0);




    }

    private class Mytask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

                RadioGroup degree = (RadioGroup) findViewById(R.id.radioGroup);
                RadioButton deg = (RadioButton) findViewById(degree.getCheckedRadioButtonId());
                String degval = deg.getText().toString();

                EditText city = (EditText) findViewById(R.id.editText2);
                String cityval = city.getText().toString();

                Spinner state = (Spinner) findViewById(R.id.spinner);
                String stateval = state.getSelectedItem().toString();

                Intent resultid = new Intent (MainActivity.this, ResultActivity.class);
                resultid.putExtra(JSO_NSTRING,result);
                resultid.putExtra("Degree",degval);
                resultid.putExtra("City",cityval);
                resultid.putExtra("State",stateval);
                startActivity(resultid);
        }

    }
}


