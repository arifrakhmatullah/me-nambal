package com.freelance.menambal.me_nambal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.freelance.menambal.me_nambal.helper.PermissionHelper;

public class splashscreen extends Activity {

    //Set waktu lama splashscreen by deadliner
    private static int splashInterval = 3000;
    private PermissionHelper permissionHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);
        permissionHelper = new PermissionHelper(this);

        checkAndRequestPermissions();

       new Handler().postDelayed(new Runnable() {

           @Override
           public void run() {
               // TODO Auto-generated method stub

               Intent i = new Intent(splashscreen.this, MainActivity.class);
               startActivity(i);

                //jeda selesai Splashscreen
               this.finish();
           }

           private void finish() {
                // TODO Auto-generated method stub

           }
        }, splashInterval);

    };

    private boolean checkAndRequestPermissions() {
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {

            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

}
