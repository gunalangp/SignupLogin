package com.example.smartgladiatortask.api;


import com.example.smartgladiatortask.model.home.ContentModel;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DefaultApi {


    @GET("photos")
    Call<List<ContentModel>> getUserInfo();


}