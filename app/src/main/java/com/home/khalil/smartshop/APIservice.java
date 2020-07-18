package com.home.khalil.smartshop;



import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIservice {

   /* @GET("smartshop-db/{id}")
    Call<Product> getProduct(@Path("id") String id);*/

    @GET("smartshop-db")
    Call<Product> getProduct();


    @Multipart
    @POST("model/predict")
    Call<TextResult> uploadImage(@Part MultipartBody.Part image);

}
