package com.example.hamza.bookapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Model {

    String author;
    String title;

    public Model(String author, String title) {
        this.author = author;
        this.title = title;
    }

    protected Model(Parcel in) {
        author = in.readString();
        title = in.readString();
    }

    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }


}












