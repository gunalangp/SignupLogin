package com.example.smartgladiatortask.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgladiatortask.R;
import com.example.smartgladiatortask.util.NetworkManager;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /***
     * This method to use Create or Show progress dialog
     * @param msg set message to progress dialog
     */
    public void showProgressDialog(String msg, boolean isDismissable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(msg);
        }
        progressDialog.setCancelable(isDismissable);
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    /***
     * This method terminate the progress dialog
     */
    public void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (final Exception e) {
            Log.e("Error : %s", e.getLocalizedMessage());
        } finally {
            this.progressDialog = null;
        }
    }

    /***
     *  This method use for displaying toast message
     * @param message set Message to show a toast
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * This method use for displaying toast message
     *
     * @param messageId set resource id to show a toast
     */
    public void showToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }

    public boolean checkNetworkState() {
        if (NetworkManager.isConnected(this)) {
            return true;
        } else {
            showToast(getString(R.string.msg_no_internet));
            return false;
        }
    }

    /**
     * This method is use for Slide Down Navigation Menu
     */
    public void slideDownNavigationMenu() {
        // do nothing now
    }

    /**
     * This method is use for Slide Up Navigation Menu
     */
    public void slideUpNavigationMenu() {
        // do nothing now
    }
}
