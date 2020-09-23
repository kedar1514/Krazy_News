package com.example.krazynews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//  <<,
interface ExtraNewsInterface {
    @GET("fetchsinglepost.php?")
    Call<String> STRING_CALL(
            @Query("id") String id,
            @Query("email") String email
    );
}
