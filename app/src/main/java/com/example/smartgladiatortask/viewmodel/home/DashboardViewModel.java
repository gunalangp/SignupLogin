package com.example.smartgladiatortask.viewmodel.home;

import androidx.lifecycle.MutableLiveData;


import com.example.smartgladiatortask.api.APIResponseListener;
import com.example.smartgladiatortask.db.UsersDatabase;
import com.example.smartgladiatortask.model.home.ContentModel;
import com.example.smartgladiatortask.repository.DashboardRepository;
import com.example.smartgladiatortask.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends BaseViewModel {

    private MutableLiveData<String> name = new MutableLiveData<>();

    private MutableLiveData<List<ContentModel>> listMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> profilePic = new MutableLiveData<>();
    private MutableLiveData<Boolean> loader = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> noDataFound = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getNoDataFound() {
        return noDataFound;
    }

    public MutableLiveData<List<ContentModel>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<String> getProfilePic() {
        return profilePic;
    }

    public MutableLiveData<Boolean> getLoader() {
        return loader;
    }

    public MutableLiveData<String> getName() {
        return name;
    }



    public void callHomeAPI() {
        loader.setValue(true);
        DashboardRepository.serviceList(new APIResponseListener() {
            @Override
            public void onSuccess(Object response) {
                loader.setValue(false);
                if (response != null) {
                    List<ContentModel> list = (List<ContentModel>) response;
                    if (list != null && list.size() > 0) {
                        listMutableLiveData.setValue(list);
                        noDataFound.setValue(false);
                    }else{
                        noDataFound.setValue(true);
                    }
                } else {
                    noDataFound.setValue(true);
                }
            }

            @Override
            public void onFail(String message) {
                loader.setValue(false);
                noDataFound.setValue(true);
            }
        });
    }


}
