package com.example.newsapp;

public class CommentData {

        int id;
        String name;
        String comment;


        public CommentData() {
        }

        public CommentData(int id, String name, String comment) {
                this.id = id;
                this.name = name;
                this.comment = comment;

        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getComment() {
                return comment;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setComment(String comment) {
                this.comment = comment;
        }

        @Override
        public String toString() {
                return "CommentData{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", comment='" + comment + '\'' +
                        '}';
        }
}
