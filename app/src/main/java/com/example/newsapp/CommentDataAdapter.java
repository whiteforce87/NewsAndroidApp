package com.example.newsapp;

import android.content.Context;
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

public class CommentDataAdapter extends RecyclerView.Adapter<CommentDataAdapter.CommentDataHolder>  {

    Context ctx;
    List<CommentData> commentData;

    public CommentDataAdapter(Context ctx, List<CommentData> commentData){
        this.ctx = ctx;
        this.commentData = commentData;
    }



    @NonNull
    @Override
    public CommentDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(ctx).inflate(R.layout.commentdata_row_layout,parent,false);
        return new CommentDataHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentDataHolder holder, int position) {

        holder.txtName.setText(commentData.get(position).getName());
        holder.txtComment.setText(commentData.get(position).getComment());



    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    class CommentDataHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtComment;
        ConstraintLayout row2;


        public CommentDataHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComment);
            row2 = itemView.findViewById(R.id.row2);
        }
    }


}
