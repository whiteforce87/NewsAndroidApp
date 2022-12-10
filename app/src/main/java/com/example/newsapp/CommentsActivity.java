package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recView2;
    ProgressBar prg2;
    int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ActionBar actionBar = getSupportActionBar();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getExtras().getInt("id");
        newsId = id;

        recView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        prg2 = findViewById(R.id.progressBar2);
        recView2.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    public void onStart() {
        super.onStart();

        GetAllCommentData dataThread = new GetAllCommentData();
        dataThread.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.post_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int selectedItem = item.getItemId();
        String message = "";
        switch (selectedItem){

            case R.id.plus:
                Intent i = new Intent(this,PostActivity.class);
                i.putExtra("id",newsId);

                startActivity(i);
                return true;

        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

        //return super.onOptionsItemSelected(item);
        //return true;
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    Handler allDataHandler = new Handler(new AllDataCallback());

    class AllDataCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<CommentData> allData = (List<CommentData>)msg.getData().getSerializable("allData");
            CommentDataAdapter adp = new CommentDataAdapter(CommentsActivity.this,allData);
            recView2.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
            //recView2.setLayoutManager(layman);
            recView2.setAdapter(adp);

            prg2.setVisibility(View.INVISIBLE);
            recView2.setVisibility(View.VISIBLE);

            return true;
        }
    }


    class GetAllCommentData extends Thread {

        @Override
        public void run() {

            try {
                URL url = new URL("http://185.48.181.6:8080/newsapp/getcommentsbynewsid/" + newsId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }


                JSONObject obj = new JSONObject(buffer.toString());

                JSONArray arr = obj.getJSONArray("items");

                ArrayList<CommentData> data = new ArrayList<>();


                for (int i = 0; i < arr.length(); i++) {

                    JSONObject current = arr.getJSONObject(i);

                    CommentData commentData = new CommentData();
                    commentData.setId(current.getInt("id"));
                    commentData.setName(current.getString("name"));
                    commentData.setComment(current.getString("text"));


                    data.add(commentData);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("allData", data);
                Message msg = new Message();
                msg.setData(bundle);

                allDataHandler.sendMessage(msg);


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