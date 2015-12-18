package com.example.farhan.weather;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.List;

public class FacebookActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ShareDialog fb_shareDialog;
    private LoginManager manager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");
        manager = LoginManager.getInstance();


        manager.logInWithPublishPermissions(this, permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbShare();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                finish();
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                finish();
            }
        });;
    }

    public void fbShare() {
        try {
            Intent intent = getIntent();
            String res = intent.getExtras().getString("result");
            JSONObject response;
            JSONTokener token = new JSONTokener(res);
            response = new JSONObject(token);

            String city = intent.getExtras().getString("cityvalue");
            String state = intent.getExtras().getString("statevalue");
            String degree = intent.getExtras().getString("degreevalue");



            ShareDialog shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(FacebookActivity.this, "Successfully Shared", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FacebookActivity.this, "Not Shared", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FacebookActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        exception.printStackTrace();
                        finish();
                    }
                });

                JSONObject currently = response.getJSONObject("currently");
                String summary = currently.getString("summary");
                String icon = currently.getString("icon");
                String image_value = iconSummary(icon);
                String temp_curr = currently.getString("temperature");
                float currtemp = Float.parseFloat(temp_curr);
                Integer tempcurr = Math.round(currtemp);
                String temp_units_deg ="";
                String temp_units ="";
                if(degree.contentEquals("Celsius")){
                    temp_units="si";
                    temp_units_deg = "\u2103";
                }
                else{
                    temp_units="us";
                    temp_units_deg = "\u2109";
                }

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Current weather in "+city+", "+state)
                        .setContentDescription(summary+", "+tempcurr+temp_units_deg)
                        .setContentUrl(Uri.parse("forecast.io"))
                        .setImageUrl(Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/" + image_value + ".png"))
                        .build();
                shareDialog.show(linkContent);


            } else {
                Log.i("ERROR", "cannot show share");
            }
        }
        catch (Exception e){
            Log.d("FBEXCEPTION", e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,    Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }
    public String iconSummary(String icon){
        String image_value;
        if(icon.contentEquals("clear-day"))
            image_value = "clear";
        else if(icon.contentEquals("clear-night"))
            image_value = "clear_night";
        else if(icon.contentEquals("rain"))
            image_value = "rain";
        else if(icon.contentEquals("snow"))
            image_value = "snow";
        else if(icon.contentEquals("sleet"))
            image_value = "sleet";
        else if(icon.contentEquals("wind"))
            image_value = "wind";
        else if(icon.contentEquals("fog"))
            image_value = "fog";
        else if(icon.contentEquals("cloudy"))
            image_value = "cloudy";
        else if(icon.contentEquals("partly-cloudy-day"))
            image_value = "cloud_day";
        else
            image_value = "cloud_night";
        return image_value;
    }

}

