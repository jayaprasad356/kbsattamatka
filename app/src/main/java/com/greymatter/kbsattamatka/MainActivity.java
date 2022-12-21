package com.greymatter.kbsattamatka;

import static com.greymatter.kbsattamatka.helper.Constant.SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.greymatter.kbsattamatka.helper.ApiConfig;
import com.greymatter.kbsattamatka.helper.Constant;
import com.greymatter.kbsattamatka.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    TextView Phonenumber, Whatsapp ;
    Activity activity;
    Session session;
    DownloadManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        session = new Session(activity);


        Phonenumber = findViewById(R.id.tvPhoneNumber);
        Whatsapp = findViewById(R.id.tvWhatsappNumber);



        findViewById(R.id.llWhatsappShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();





            }
        });
        findViewById(R.id.llDownloadApk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse(session.getData(Constant.APP));
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                long reference = manager.enqueue(request);
                Uri uri = Uri.parse(""+session.getData(Constant.APP)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        settings_info();
    }

    private void settings_info()
    {


        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {

            Log.d("res",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        session.setData(Constant.WHATSAPP,jsonArray.getJSONObject(0).getString(Constant.WHATSAPP));
                        session.setData(Constant.APP,jsonArray.getJSONObject(0).getString(Constant.APP));

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, activity, Constant.SETTING_URL, params,true);



    }





    private void openWhatsApp() {

        String url = "https://api.whatsapp.com/send?phone="+"91"+session.getData(Constant.WHATSAPP);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }



}