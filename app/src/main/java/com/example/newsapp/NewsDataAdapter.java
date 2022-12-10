package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.NewsDataHolder>  {

    Context ctx;
    List<NewsData> newsData;

    public NewsDataAdapter(Context ctx, List<NewsData> newsData){
        this.ctx = ctx;
        this.newsData = newsData;
    }

    @NonNull
    @Override
    public NewsDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(ctx).inflate(R.layout.newsdata_row_layout,parent,false);
        return new NewsDataHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDataHolder holder, int position) {

        holder.txtTitle.setText(newsData.get(position).getTitle());
        holder.txtDate.setText(newsData.get(position).getDate());


        if(holder.imageDownloaded == false){
            ImageDownloadThread imgThread = new ImageDownloadThread(newsData.get(position).getImagePath(),holder);
            imgThread.start();
        }

        holder.row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                NavController navCont = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putInt("id", newsData.get(holder.getAdapterPosition()).getId());
                bundle.putString("title", newsData.get(holder.getAdapterPosition()).getTitle());
                bundle.putString("text", newsData.get(holder.getAdapterPosition()).getText());
                bundle.putString("date", newsData.get(holder.getAdapterPosition()).getDate().substring(0,10));
                bundle.putString("image", newsData.get(holder.getAdapterPosition()).getImagePath());
                //bundle.putSerializable("image",newsData.get(holder.getAdapterPosition()).getImagePath());

                navCont.navigate(R.id.action_homeFragment_to_detailFragment, bundle);



            }
        });

    }

    @Override
    public int getItemCount() {
        return newsData.size();
    }

    class NewsDataHolder extends RecyclerView.ViewHolder{
        boolean imageDownloaded = false;
        TextView txtTitle;
        TextView txtDate;
        ConstraintLayout row;
        ImageView imgNewsData;


        public NewsDataHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.textDate);

            row = itemView.findViewById(R.id.row);
            imgNewsData = itemView.findViewById(R.id.imageNews);
        }
    }

    ImageHandler imgHandler = new ImageHandler();


    class ImageHandler extends Handler {

        ImageView imageViewToSet;
        Bitmap bmp;

        @Override
        public void handleMessage(@NonNull Message msg){

            imageViewToSet.setImageBitmap(bmp);
        }

        public void setImageViewToSet(ImageView imageViewToSet) {
            this.imageViewToSet = imageViewToSet;
        }

        public void setBmp(Bitmap bmp) {

            this.bmp = bmp;
        }
    }

    class ImageDownloadThread extends Thread{

            String urlStr;
            private NewsDataHolder holder;

            public ImageDownloadThread(String urlStr, NewsDataHolder holder){
                this.urlStr = urlStr;
                this.holder = holder;
            }

        @Override
        public void run(){

            Bitmap bmp;
            try {
                URL url = new URL(urlStr);
                InputStream in = new BufferedInputStream(url.openStream());
                bmp = BitmapFactory.decodeStream(in);
                holder.imageDownloaded = true;

                imgHandler.setBmp(bmp);
                imgHandler.setImageViewToSet(holder.imgNewsData);
                imgHandler.sendEmptyMessage(0);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
