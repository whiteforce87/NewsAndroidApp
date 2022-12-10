package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    EditText txtPostName;
    EditText txtPostComment;
    Button btnComment;
    int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        txtPostName = findViewById(R.id.txtPostName);
        txtPostComment = findViewById(R.id.txtPostComment);
        btnComment = findViewById(R.id.btnComment);
        int id = getIntent().getExtras().getInt("id");
        newsId = id;
    }

    public void getDataClicked(View v){

        JSONObject obj = new JSONObject();

        try {
            obj.put("name",txtPostName.getText().toString());
            obj.put("text",txtPostComment.getText().toString());
            obj.put("news_id",String.valueOf(newsId));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create and start thread

        JsonThread th = new JsonThread(obj.toString());
        th.start();
        finish();

    }


    class JsonCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            String name = msg.getData().getString("name");
            String text = msg.getData().getString("text");

            return true;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), CommentsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    Handler jsonHandler = new Handler(new JsonCallback());


    class JsonThread extends Thread{
        String json;
        public JsonThread(String json){

            this.json = json;
        }

        @Override
        public void run() {

            try {
                URL url = new URL("http://185.48.181.6:8080/newsapp/savecomment");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(json);

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){

                    BufferedReader reader
                            = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line = "";
                    StringBuilder buffer = new StringBuilder();
                    while((line=reader.readLine())!=null){

                        buffer.append(line);

                    }

                    JSONObject retObj = new JSONObject(buffer.toString());

                    Bundle bundle = new Bundle();
                    bundle.putString("name",retObj.getString(String.valueOf(txtPostName)));
                    bundle.putString("text",retObj.getString(String.valueOf(txtPostComment)));
                    bundle.putString("news_id",retObj.getString(String.valueOf(newsId)));
                    Message msg = new Message();
                    msg.setData(bundle);
                    jsonHandler.sendMessage(msg);
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}