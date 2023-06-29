package com.example.smartgladiatortask.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    private MutableLiveData<Integer> toastMessage = new MutableLiveData<>();
    private MutableLiveData<String> strToastMessage = new MutableLiveData<>();
    private MutableLiveData<String> notifyActivity = new MutableLiveData<>();
    private MutableLiveData<Boolean> isShowProgressDialog = new MutableLiveData<>();

    public BaseViewModel() {
        initDefaultValues();
    }

    public MutableLiveData<Integer> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<String> getStrToastMessage() {
        return strToastMessage;
    }

    public MutableLiveData<String> getNotifyActivity() {
        return notifyActivity;
    }

    public MutableLiveData<Boolean> getIsShowProgressDialog() {
        return isShowProgressDialog;
    }

    private void initDefaultValues() {
        isShowProgressDialog.setValue(false);
    }
}
