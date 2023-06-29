package com.example.smartgladiatortask.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.databinding.ActivityLoginBinding;
import com.example.smartgladiatortask.db.UsersDatabase;
import com.example.smartgladiatortask.model.home.UserModel;
import com.example.smartgladiatortask.ui.BaseActivity;
import com.example.smartgladiatortask.util.PreferenceUtils;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    private UsersDatabase usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginActivity(this);
        binding.setLifecycleOwner(this);
        usersDatabase = new UsersDatabase(getApplicationContext());

    }


    public void signIn() {
        if (binding.etName.getText().toString().isEmpty()) {
            showToast("Enter Username");
        } else if (binding.etPassword.getText().toString().isEmpty()) {
            showToast("Enter Password");
        } else {
            UserModel userModel = usersDatabase.getData(binding.etName.getText().toString());
            if (userModel != null && userModel.getName() != null && userModel.getPassword() != null) {
                if (userModel.getName().equals(binding.etName.getText().toString())
                        && userModel.getPassword().equals(binding.etPassword.getText().toString())) {
                    PreferenceUtils.setUserLoggedIn(true);
                    PreferenceUtils.setPrefUserName(binding.etName.getText().toString());
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast("Password wrong");
                }
            } else {
                showToast("User not found");
            }
        }
    }

    public void signUp() {
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
    }


}