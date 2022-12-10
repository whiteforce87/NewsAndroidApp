package com.example.newsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class DetailFragment extends Fragment {



    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.plus);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.plus) {

            Intent i = new Intent(getActivity(),CommentsActivity.class);
            int newsId = getArguments().getInt("id");
            i.putExtra("id",newsId);

            startActivity(i);

            return true;
        }
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }



    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_detail, container, false);

        int id = getArguments().getInt("id");
        String title = getArguments().getString("title");
        String text = getArguments().getString("text");
        String date = getArguments().getString("date");
        String imagePath = getArguments().getString("image");
        TextView titleDetail = root.findViewById(R.id.titleDetail);
        titleDetail.setText(title);
        TextView textDetail = root.findViewById(R.id.textDetail);
        textDetail.setText(text);
        TextView dateDetail = root.findViewById(R.id.dateDetail);
        dateDetail.setText(date);
        ImageView imageView = (ImageView) root.findViewById(R.id.imageDetail);
        Glide.with(this)
                .load(imagePath)
                .into(imageView);

        setHasOptionsMenu(true);

        getActivity().setTitle("News Details");



        return root;
    }


}