package com.example.smartgladiatortask.ui.activity;

import static android.os.Build.VERSION.SDK_INT;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.databinding.ActivityMainBinding;
import com.example.smartgladiatortask.databinding.ActivitySignUpBinding;
import com.example.smartgladiatortask.db.UsersDatabase;
import com.example.smartgladiatortask.ui.BaseActivity;
import com.example.smartgladiatortask.util.PreferenceUtils;
import com.example.smartgladiatortask.util.imagepicker.activities.AlbumSelectActivity;
import com.example.smartgladiatortask.util.imagepicker.helpers.ConstantsCustomGallery;
import com.example.smartgladiatortask.util.imagepicker.models.Image;
import com.example.smartgladiatortask.viewmodel.home.DashboardViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends BaseActivity {

    private UsersDatabase usersDatabase;
    private ActivitySignUpBinding binding;

    private String filePath;

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_STORAGE = 1;

    private static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    private static final int REQUEST_CODE_Gallery = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setSignupActivity(this);
        binding.setLifecycleOwner(this);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Signup");
        }

        usersDatabase = new UsersDatabase(getApplicationContext());

    }


    public void signup() {
        if (filePath == null) {
            showToast("Select Profile Pic");
        } else if (binding.etName.getText().toString().isEmpty()) {
            showToast("Enter Username");
        } else if (binding.etPassword.getText().toString().isEmpty()) {
            showToast("Enter Password");
        } else {
            boolean saved = usersDatabase.insert(binding.etName.getText().toString(),
                    binding.etPassword.getText().toString(), filePath,
                    String.valueOf(Calendar.getInstance().getTimeInMillis()));
            if (saved) {
                PreferenceUtils.setUserLoggedIn(true);
                PreferenceUtils.setPrefUserName(binding.etName.getText().toString());
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                showToast("Username Already exist");
            }
        }
    }

    public void pickImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE);
        } else {
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        TextView title = new TextView(SignUpActivity.this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                    } else {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, REQUEST_CODE_TAKE_PICTURE);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(getBaseContext(), AlbumSelectActivity.class);
                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(SignUpActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantsCustomGallery.REQUEST_CODE && data != null) {
                //The array list has the image paths of the selected images
                ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
                if (images != null && images.size() > 0) {
                    Glide.with(getApplicationContext())
                            .load(images.get(0).path).into(binding.ivProfilePic);
                    filePath = images.get(0).path;
                }
            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = null;
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "profile_pic" + System.currentTimeMillis() + ".jpg");
                } else {
                    destination = new File(Environment.getExternalStorageDirectory(), "profile_pic_" + System.currentTimeMillis() + ".jpg");

                }
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File mProfileFile = new File(destination.getAbsolutePath());
                // Set the Image in ImageView after decoding the String
                filePath = mProfileFile.getAbsolutePath();
                Glide.with(getApplicationContext())
                        .load(filePath)
                        .into(binding.ivProfilePic);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}