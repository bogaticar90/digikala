package com.example.digikala.network;

import com.example.digikala.model.ImagesItem;
import com.example.digikala.model.WoocommerceBody;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WoocommerceService {

    @GET("products")
    Call<List<WoocommerceBody>> getWooCommerceBody(@QueryMap Map<String, String> queries);

}