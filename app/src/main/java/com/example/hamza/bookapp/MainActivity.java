package com.example.hamza.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

        public static final String LOG_TAG = MainActivity.class.getSimpleName();
        EditText editText;
        RecAdapter adapter;
        ImageButton imageButton;
        ListView recyclerView;
        static final String SEARCH_RESULTS = "booksSearchResults";
        ArrayList<Model> books;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            editText =  findViewById(R.id.editText);
            imageButton=findViewById(R.id.imageButton);
            recyclerView=findViewById(R.id.rec);
            adapter=new RecAdapter(getApplicationContext(),1);
            recyclerView.setAdapter(adapter);

            imageButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute();
                }
            });


            if (savedInstanceState != null) {
                Model[] books = (Model[]) savedInstanceState.getParcelableArray(SEARCH_RESULTS);
                adapter.addAll(books);
            }

        }

    private void updateUi(ArrayList<Model> books){
        if (books.isEmpty()){
            // if no books found, show a message
            Toast.makeText(this, "No books", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

        }
        adapter.clear();
        adapter.addAll(books);
    }
    private String getUserInput() {
        return editText.getText().toString();
    }
    private String getUrlForHttpRequest() {
        final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=search+";
        String formatUserInput = getUserInput().trim().replaceAll("\\s+","+");
        String url = baseUrl + formatUserInput;
        return url;
    }

    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Model>> {

        @Override
        protected ArrayList<Model> doInBackground(URL... urls) {
            URL url = createURL(getUrlForHttpRequest());
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

             books = parseJson(jsonResponse);

            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<Model> books) {
            if (books == null) {
                return;
            }
            updateUi(books);
        }}

        private URL createURL(String stringUrl) {
            try {
                return new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null){
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<Model> parseJson(String json) {

            if (json == null) {
                return null;
            }

            ArrayList<Model> books =  QueryUtils.extractBooks(json);
            return books;
        }
    }


