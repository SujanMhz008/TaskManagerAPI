package com.sujanmaharjan008.taskmanagerapi.api;

import com.sujanmaharjan008.taskmanagerapi.model.Users;
import com.sujanmaharjan008.taskmanagerapi.serverresponse.ImageResponse;
import com.sujanmaharjan008.taskmanagerapi.serverresponse.SignUpResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPI {
    @POST("users/signup")
    Call<SignUpResponse> registerUser(@Body Users users);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);
}
