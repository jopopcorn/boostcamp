package com.example.boostcamp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText searchMovie;
    public TextView result;
    MovieAdapter adapter;
    final String clientID = "";
    final String clientSecret = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchMovie = findViewById(R.id.searchMovie);
        Button searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.textView);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> sendRequest(searchMovie.getText().toString()));

        adapter.setOnItemClickListener((holder, view, position) -> {
            Movie item = adapter.getItem(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(item.getLink()));
            startActivity(intent);
        });

        if(AppHelper.requestQueue==null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public void sendRequest(final String movieName){
        result.setText("");
        Log.d("영화제목=>",""+movieName);
        String url = "https://openapi.naver.com/v1/search/movie.json?query="+movieName;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    Log.d("Response =>", response);
                    adapter.items.clear();
                    adapter.notifyDataSetChanged();
                    processResponse(response);
                },
                error -> {
                    println("에러"+ error.getMessage());
                    Log.d("ERROR_RESPONSE =>", error.toString());
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Naver-Client-Id",clientID);
                params.put("X-Naver-Client-Secret",clientSecret);
                Log.d("getHeader=> ",""+params);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response){
        Gson gson = new Gson();
        Movies movies = gson.fromJson(response,Movies.class);
        Log.d("검색 일치 갯수",""+movies.getItems().size());
        for(int i=0;i<movies.getItems().size();i++){
            adapter.addItem(new Movie(movies.getItems().get(i).getImage(),
                    movies.getItems().get(i).getTitle(),
                    movies.getItems().get(i).getUserRating(),
                    movies.getItems().get(i).getPubDate(),
                    movies.getItems().get(i).getDirector(),
                    movies.getItems().get(i).getActor(),
                    movies.getItems().get(i).getLink()));
        }
    }

    public void println(String data){
        result.append(data + "\n");
    }
}