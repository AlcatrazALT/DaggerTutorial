package com.example.daggertutorial.interfaces;


import com.example.daggertutorial.model.RandomUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomUsersApi {

    @GET("api")
    Call<RandomUser> getRandomUser(@Query("results") int size);
}
