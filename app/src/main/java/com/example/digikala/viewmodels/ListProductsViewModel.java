package com.example.digikala.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.digikala.model.WoocommerceBody;
import com.example.digikala.network.WooCommerce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Woo.Repository.Repository;

public class ListProductsViewModel extends AndroidViewModel {
    private MutableLiveData<List<WoocommerceBody>> mNewestProducts;
    private MutableLiveData<List<WoocommerceBody>> mPopularProducts;
    private MutableLiveData<List<WoocommerceBody>> mRatedProducts;
    private MutableLiveData<List<WoocommerceBody>> mSortedProducts;
    private MutableLiveData<List<WoocommerceBody>> mSearchedProducts;
    private Repository mRepository;
    private WooCommerce mWooCommerce;


    public ListProductsViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance();
        mNewestProducts = mRepository.getNewestProducts();
        mPopularProducts = mRepository.getPopularProducts();
        mRatedProducts = mRepository.getRatedProducts();
        mSortedProducts = mRepository.getSortedProducts();
        mSearchedProducts=mRepository.getSearchedProducts();
        mWooCommerce = new WooCommerce();
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
        try {
            mWooCommerce.getSortedBaseProducts(queries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<WoocommerceBody>> getSortedProducts() {
        return mSortedProducts;
    }

    public MutableLiveData<List<WoocommerceBody>> getSearchedProducts() {
        return mSearchedProducts;
    }
}