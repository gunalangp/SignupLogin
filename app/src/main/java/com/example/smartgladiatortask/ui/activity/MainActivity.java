package com.example.smartgladiatortask.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.databinding.ActivityMainBinding;
import com.example.smartgladiatortask.db.UsersDatabase;
import com.example.smartgladiatortask.model.home.UserModel;
import com.example.smartgladiatortask.ui.BaseActivity;
import com.example.smartgladiatortask.ui.adapter.HomeAdapter;
import com.example.smartgladiatortask.util.PreferenceUtils;
import com.example.smartgladiatortask.viewmodel.home.DashboardViewModel;

import java.io.File;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    DashboardViewModel viewModel;

    UsersDatabase usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding.setDashboardViewModel(viewModel);
        binding.setLifecycleOwner(this);

        usersDatabase = new UsersDatabase(getApplicationContext());
        initObserver();

        getUser();
        if (checkNetworkState()) {
            viewModel.getLoader().setValue(true);
            viewModel.callHomeAPI();
        } else {
            viewModel.getLoader().setValue(false);
        }
    }

    private void getUser() {
        if(PreferenceUtils.getPrefUserName() != null) {
            UserModel userModel = usersDatabase.getData(PreferenceUtils.getPrefUserName());
            if (userModel != null) {
                if (userModel.getName() != null) {
                    viewModel.getName().setValue(userModel.getName());
                }
                if (userModel.getFilepath() != null) {
                    if (new File(userModel.getFilepath()).exists()) {
                        Glide.with(getApplicationContext())
                                .load(userModel.getFilepath())
                                .into(binding.imgPic);
                    }
                }
            }
        }else{
            PreferenceUtils.setUserLoggedIn(false);
            PreferenceUtils.setPrefUserName("");
            Intent intent = new Intent(getBaseContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void initObserver() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);
        viewModel.getNotifyActivity().observe(this, s -> {
                showToast(s);
        });
        viewModel.getListMutableLiveData().observe(this, data -> {
            if (data != null && data.size() > 0) {
                HomeAdapter mGuestHomeAdapter = new HomeAdapter(this, data);
                binding.recyclerview.setAdapter(mGuestHomeAdapter);
                mGuestHomeAdapter.setItemClickListener((data1, position) -> {
                    showToast(data1.getTitle());
                });
            }
        });
    }

    public void logout() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure to Logout?")
                .setNeutralButton("Yes", (dialog, which) -> {
                    PreferenceUtils.setUserLoggedIn(false);
                    PreferenceUtils.setPrefUserName("");
                    Intent intent = new Intent(getBaseContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
    }


}