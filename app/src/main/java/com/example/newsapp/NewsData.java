package com.example.newsapp;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewsData {

        int id;
        String title;
        String text;
        String date;
        String imagePath;
        String categoryName;

        public NewsData() {
        }

        public NewsData(int id, String title, String text, String date, String imagePath, String categoryName) throws ParseException {



                this.id = id;
                this.title = title;
                this.text = text;
                this.date = date;
                this.imagePath = imagePath;
                this.categoryName = categoryName;
        }

        public int getId() {
                return id;
        }

        public String getTitle() {
                return title;
        }

        public String getText() {
                return text;
        }

        public String getDate() {
                return date;
        }

        public String getImagePath() {
                return imagePath;
        }

        public String getCategoryName() {
                return categoryName;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public void setText(String text) {
                this.text = text;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
        }

        public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
        }


}
