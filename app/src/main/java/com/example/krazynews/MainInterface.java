package com.example.krazynews;

import java.util.Stack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//  <<,
interface MainInterface {
    @GET("fetchpost.php?")
    Call<String> STRING_CALL(
            @Query("page") int page,
            @Query("count") int count,
            @Query("email") String email,
            @Query("lang") String lang,
            @Query("category") String category
    );
}
