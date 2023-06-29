package com.example.smartgladiatortask.api;

public interface APIResponseListener {
    void onSuccess(Object response);

    void onFail(String message);
}
