package com.proyecto.pruebaapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BacheFinderClient {
    private static BacheFinderApi api;

    public static BacheFinderApi getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(BacheFinderApi.class);
        }
        return api;
    }
}