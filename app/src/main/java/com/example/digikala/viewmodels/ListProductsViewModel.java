package com.example.digikala.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.digikala.RecyclersViews.utils.SharedPreferencesData;
import com.example.digikala.model.WoocommerceBody;
import com.example.digikala.network.RetrofitInstance;
import com.example.digikala.network.WoocommerceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Woo.Repository.Repository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Woo.Repository.Repository.FLICKR_REST_PATH;

public class ListProductsViewModel extends AndroidViewModel {
    final String CONSUMER_KEY = "ck_7c028a04c9faf616410b09e2ab90b1884c875d01";
    final String CONSUMER_SECRET = "cs_8c39f626780f01d135719f64214fd092b848f4aa";
    Map<String, String> mQueries = new HashMap<String, String>() {
        {
            put("consumer_key", CONSUMER_KEY);
            put("consumer_secret", CONSUMER_SECRET);

        }
    };
    WoocommerceService mWoocommerceApi = RetrofitInstance.getInstance(FLICKR_REST_PATH)
            .getRetrofit()
            .create(WoocommerceService.class);
    private MutableLiveData<List<WoocommerceBody>> mSubCategoriesProducts;
    private MutableLiveData<List<WoocommerceBody>> mNewestProducts;
    private MutableLiveData<List<WoocommerceBody>> mPopularProducts;
    private MutableLiveData<List<WoocommerceBody>> mRatedProducts;
    private MutableLiveData<List<WoocommerceBody>> mSortedProducts;
    private MutableLiveData<List<WoocommerceBody>> mSearchedProducts;
    private Repository mRepository;
    private Context mContext;


    public ListProductsViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        mRepository = Repository.getInstance();
        mNewestProducts = mRepository.getNewestProducts();
        mPopularProducts = mRepository.getPopularProducts();
        mRatedProducts = mRepository.getRatedProducts();
        mSortedProducts = mRepository.getSortedProducts();
        mSearchedProducts = mRepository.getSearchedProducts();


    }


    public MutableLiveData<List<WoocommerceBody>> getNewestProducts() {
        return mNewestProducts;
    }


    public MutableLiveData<List<WoocommerceBody>> getPopularProducts() {
        return mPopularProducts;
    }


    public MutableLiveData<List<WoocommerceBody>> getRatedProducts() {
        return mRatedProducts;
    }

    public void loadSortedProducts(Map<String, String> queries) {
        mSortedProducts = new MutableLiveData<>();
        try {
            getSortedBaseProducts(queries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<WoocommerceBody>> getSortedProducts() {
        return mSortedProducts;
    }

    public void loadSearchedProducts() {
        mSearchedProducts = new MutableLiveData<>();
        try {
            searchInProducts(SharedPreferencesData.getQuery(mContext));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<WoocommerceBody>> getSearchedProducts() {
        return mSearchedProducts;
    }

    public MutableLiveData<List<WoocommerceBody>> getSortedBaseProducts(Map<String, String> queries) throws IOException {
        Call<List<WoocommerceBody>> call = Repository.getInstance().getWoocommerceApi().getSortedBaseProducts(queries);

        call.enqueue(new Callback<List<WoocommerceBody>>() {
            @Override
            public void onResponse(Call<List<WoocommerceBody>> call, Response<List<WoocommerceBody>> response) {
                if (response.isSuccessful()) {
                    response.body().remove(0);
                    mSortedProducts.setValue(response.body());

                } else {
                    mSortedProducts = null;
                }
            }

            @Override
            public void onFailure(Call<List<WoocommerceBody>> call, Throwable t) {
                mSortedProducts = null;
            }
        });


        return mSortedProducts;
    }

    public MutableLiveData<List<WoocommerceBody>> searchInProducts(String searchQuery) throws IOException {
        Call<List<WoocommerceBody>> call = Repository.getInstance().getWoocommerceApi().searchProducts(searchQuery, Repository.getInstance().getQueries());
        call.enqueue(new Callback<List<WoocommerceBody>>() {
            @Override
            public void onResponse(Call<List<WoocommerceBody>> call, Response<List<WoocommerceBody>> response) {
                if (response.isSuccessful()) {
                    mSearchedProducts.setValue(response.body());
                } else {
                    mSearchedProducts.setValue(new ArrayList<WoocommerceBody>());
                }

            }

            @Override
            public void onFailure(Call<List<WoocommerceBody>> call, Throwable t) {
                mSearchedProducts.setValue(new ArrayList<WoocommerceBody>());

            }
        });
        return mSearchedProducts;
    }
    public void loadSubCategoriesProducts(String categoryId)
    {
        mSubCategoriesProducts=new MutableLiveData<>();
        try {
            getSubCategoriesProducts(categoryId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MutableLiveData<List<WoocommerceBody>> getSubCategoriesProducts() {
        return mSubCategoriesProducts;
    }

    public MutableLiveData<List<WoocommerceBody>>  getSubCategoriesProducts(String categoryId)throws IOException
    {
        mQueries.put("category",categoryId);
        Call<List<WoocommerceBody>> call =mWoocommerceApi.getWooCommerceBody(mQueries);

        call.enqueue(new Callback<List<WoocommerceBody>>() {
            @Override
            public void onResponse(Call<List<WoocommerceBody>> call, Response<List<WoocommerceBody>> response) {
                if(response.isSuccessful())
                {
                    mSubCategoriesProducts.setValue(response.body());

                }else
                {
                    Log.d("categoryId", String.valueOf(response.body().size()));
                    mSubCategoriesProducts.setValue(new ArrayList<WoocommerceBody>());
                }
                Log.e("TAG4" , "On response " + response.message() + "  "  + response.code());
            }

            @Override
            public void onFailure(Call<List<WoocommerceBody>> call, Throwable t) {
                Log.e("TAG4" , "On fail " + t.getMessage());
                mSubCategoriesProducts.setValue(new ArrayList<WoocommerceBody>());
            }
        });

        return mSubCategoriesProducts;
    }
}
