package com.example.smartgladiatortask.repository;


import com.example.smartgladiatortask.api.APIManager;
import com.example.smartgladiatortask.api.APIResponseListener;
import com.example.smartgladiatortask.model.home.ContentModel;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;

public class DashboardRepository {

    private DashboardRepository() {
        throw new IllegalStateException("Dashboard Repository");
    }

    public static void serviceList(APIResponseListener listener) {
        Call<List<ContentModel>> call = APIManager.getInstance().getDefaultAPI().getUserInfo();
        APIManager.getInstance().callAPI(call, listener);
    }

}
