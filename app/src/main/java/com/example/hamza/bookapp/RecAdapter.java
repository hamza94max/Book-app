package com.example.hamza.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter  extends ArrayAdapter<Model> {

public RecAdapter(Context context, int resource) {
        super(context, resource);
        }

@Override
public View getView(int position, View view, ViewGroup parent) {

        Model book = getItem(position);

        if (view == null){
        view = LayoutInflater.from(getContext()).inflate(
        R.layout.rec_items, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView author = (TextView) view.findViewById(R.id.author);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());

        return view;
        }
        }