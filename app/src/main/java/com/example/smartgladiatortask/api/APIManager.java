package com.example.smartgladiatortask.api;

import android.text.TextUtils;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIManager {

    public static final String NO_DATA_FOUND_ERROR_MESSAGE = "No Data Found";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong, please try again after sometime.";
    public static final String UNAUTHORISED_ERROR_MESSAGE = "Session expired, Please try login again.";
    private static final String SERVER_ERROR_MESSAGE = "Could not connect to server, Please try again later.";

    private static APIManager sAPIManager;
    private DefaultApi mDefaultAPI;
    private Executor mExecutor;

    private APIManager() {
        mDefaultAPI = new ApiClient().createService(DefaultApi.class);
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public static APIManager getInstance() {
        if (sAPIManager == null) {
            sAPIManager = new APIManager();
        }
        return sAPIManager;
    }

    public DefaultApi getDefaultAPI() {
        return mDefaultAPI;
    }

    public <T> void callAPI(final Call<T> api, final APIResponseListener listener) {
        mExecutor.execute(() -> api.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                    handleAPIResponse(response.code(), response, listener);
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                listener.onFail(SERVER_ERROR_MESSAGE);
            }
        }));
    }

    private <T> void handleAPIResponse(int status, Response<T> response, APIResponseListener listener) {
        String baseAPIResponse = null;
        switch (status) {
            case 200:
                listener.onSuccess(response.body());
            case 201:
                listener.onSuccess(response.body());
                break;

            case 400: // Bad request
                if (response.body() != null) {
                    baseAPIResponse = response.body().toString();
                    setFailureMessage(listener, baseAPIResponse);
                } else {
                    setFailureMessage(listener, response.message());
                }
                break;

            case 401: //Unauthorized
                listener.onFail(UNAUTHORISED_ERROR_MESSAGE);
                break;

            case 403:
                setFailureMessage(listener, response.message());
                break;

            case 500:

                if (response.body() != null) {
                    baseAPIResponse = response.body().toString();
                    setFailureMessage(listener, baseAPIResponse);
                } else {
                    listener.onFail(INTERNAL_SERVER_ERROR_MESSAGE);
                }
                break;

            default:
                listener.onFail(SERVER_ERROR_MESSAGE);
                break;
        }
    }

    private void setFailureMessage(APIResponseListener listener, String message) {
        listener.onFail(TextUtils.isEmpty(message) ? INTERNAL_SERVER_ERROR_MESSAGE : message);
    }


    public <String> void callAPITest(final Call<String> api, final APIResponseListener listener) {
        try {
            mExecutor.execute(() -> api.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                    if (response.isSuccessful()) {
                    } else {
                        handleAPIResponse(response.code(), response, listener);
                    }
                    listener.onFail(SERVER_ERROR_MESSAGE);
                }

                @Override
                public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                    listener.onFail(SERVER_ERROR_MESSAGE);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail(SERVER_ERROR_MESSAGE);
            
        }
    }


}
