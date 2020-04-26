package com.example.daggertutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.daggertutorial.adapter.RandomUserAdapter;
import com.example.daggertutorial.interfaces.RandomUsersApi;
import com.example.daggertutorial.model.RandomUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final int CACHE_MAX_SIZE = 10 * 1000* 1000;

    Retrofit retrofit;
    RecyclerView recyclerView;
    RandomUserAdapter randomUserAdapter;

    Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Timber.plant(new Timber.DebugTree());

        File cacheFile = new File(this.getCacheDir(), "HttpCache");
        cacheFile.mkdirs();

        Cache cache = new Cache(cacheFile, CACHE_MAX_SIZE);

        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttpClient);
        picasso = new Picasso.Builder(this).downloader(okHttp3Downloader).build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        populateUsers();

    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateUsers() {
        Call<RandomUser> randomUsersCall = getRandomUserService().getRandomUser(10);
        randomUsersCall.enqueue(new Callback<RandomUser>() {
            @Override
            public void onResponse(Call<RandomUser> call, Response<RandomUser> response) {
                if(response.isSuccessful()){
                    randomUserAdapter = new RandomUserAdapter();
                    randomUserAdapter.setItems(response.body().getResultList());
                    recyclerView.setAdapter(randomUserAdapter);
                }
            }

            @Override
            public void onFailure(Call<RandomUser> call, Throwable t) {
                Timber.i(t.getMessage());
            }
        });
    }

    public RandomUsersApi getRandomUserService(){
        return retrofit.create(RandomUsersApi.class);
    }
}