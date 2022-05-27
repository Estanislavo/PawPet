package com.example.petsplace.service;

import com.example.petsplace.json.WeatherInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherService {
    @GET("v1/current.json")
    Call<WeatherInformation> getPosts(@Query("key") String key, @Query("q") String q, @Query("aqi") String aqi);
}
