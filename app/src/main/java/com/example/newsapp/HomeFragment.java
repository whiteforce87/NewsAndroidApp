package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

public class HomeFragment extends Fragment {

    RecyclerView recView;
    ProgressBar prg;
    Spinner spn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        recView = root.findViewById(R.id.recNewsData);
        prg = root.findViewById(R.id.progressBar);
        spn = root.findViewById(R.id.newsSpinner);

        ArrayList<String> category = new ArrayList<>();
        category.add("All");
        category.add("Economics");
        category.add("Politics");
        category.add("Sports");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_dropdown_item_1line, category);

        spn.setAdapter(adp);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onStart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });



        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        GetAllNewsData dataThread = new GetAllNewsData();
        dataThread.start();
    }




    Handler allDataHandler = new Handler(new AllDataCallback());

    class AllDataCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            List<NewsData> allData = (List<NewsData>)msg.getData().getSerializable("allData");
            NewsDataAdapter adp = new NewsDataAdapter(requireActivity(),allData);
            LinearLayoutManager layman = new LinearLayoutManager(requireActivity());
            recView.setLayoutManager(layman);
            recView.setAdapter(adp);

            prg.setVisibility(View.INVISIBLE);
            recView.setVisibility(View.VISIBLE);

            return true;
        }
    }


    class GetAllNewsData extends Thread{

        @Override
        public void run(){

            try {
                URL url = null;
                if(spn.getSelectedItem().equals("All")) {
                    url = new URL("http://185.48.181.6:8080/newsapp/getall");
                }else if(spn.getSelectedItem().equals("Economics")){
                    url = new URL("http://185.48.181.6:8080/newsapp/getbycategoryid/1");
                }else if(spn.getSelectedItem().equals("Sports")){
                url = new URL("http://185.48.181.6:8080/newsapp/getbycategoryid/2");
                }else if(spn.getSelectedItem().equals("Politics")){
                url = new URL("http://185.48.181.6:8080/newsapp/getbycategoryid/3");
            }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }


                JSONObject obj = new JSONObject(buffer.toString());

                JSONArray arr = obj.getJSONArray("items");

                ArrayList<NewsData> data = new ArrayList<>();


                for (int i = 0; i <arr.length() ; i++) {

                    JSONObject current = arr.getJSONObject(i);

                    NewsData newsData = new NewsData();
                    newsData.setId(current.getInt("id"));
                    newsData.setTitle(current.getString("title"));
                    newsData.setDate(current.getString("date").substring(0,10));
                    newsData.setText(current.getString("text"));
                    newsData.setCategoryName(current.getString("categoryName"));
                    newsData.setImagePath(current.getString("image"));

                    data.add(newsData);
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