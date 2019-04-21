package com.freelance.menambal.me_nambal;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int COARSE_PERMISSION_CODE = 1;
    private int FINE_PERMSSION_CODE = 1;


    FragmentManager fragmentManager;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // tampilan default awal ketika aplikasii dijalankan
        if (savedInstanceState == null) {
            fragment = new Root();
            callFragment(fragment);
        }

        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0
                && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            Toast.makeText(this, "You Have Already Granted This Permission !", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }


    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")
                && ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_COARSE_LOCATON")) {
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("This Permission is Needed Access Your Location").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MainActivity.this
                            .FINE_PERMSSION_CODE);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, MainActivity.this.COARSE_PERMISSION_CODE);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, this.FINE_PERMSSION_CODE);
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, this.COARSE_PERMISSION_CODE);
    }

    public void cari_tambalban(View view) {
        Intent intent = new Intent(MainActivity.this, tambalban.class);
        startActivity(intent);

    }

    public void cari_bensineceran(View view) {
        Intent intent = new Intent(MainActivity.this, bensineceran.class);
        startActivity(intent);

    }

    public void temukanlokasi(View view) {
        Intent intent = new Intent(MainActivity.this, panduan_temukanlokasi_main.class);
        startActivity(intent);

    }

    public void simbollokasi(View view) {
        Intent intent = new Intent(MainActivity.this, panduan_simbollokasi_main.class);
        startActivity(intent);

    }

    public void panduanmasuk(View view) {
        Intent intent = new Intent(MainActivity.this, panduan_masuk_main.class);
        startActivity(intent);

    }

    public void tambahlokasi(View view) {
        Intent intent = new Intent(MainActivity.this, panduan_tambahlokasi_main.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            fragment = new Root();
            callFragment(fragment);
        } else if (id == R.id.mylocation) {
            // Handle the mylocation action
            Intent intent = new Intent(MainActivity.this, MyLocationMaps.class);
            startActivity(intent);
        } else if (id == R.id.login) {

            // Handle the mylocation action
            Intent intent = new Intent(MainActivity.this, Login_actionlogin.class);
            startActivity(intent);


           //fragment = new Login();
           //callFragment(fragment);

        } else if (id == R.id.panduan) {
            fragment= new Panduan();
            callFragment(fragment);

        } else if (id == R.id.about_us) {
            fragment = new About_Us();
            callFragment(fragment);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // untuk mengganti isi kontainer menu yang dipiih
    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(MainActivity.this, Login_MainActivity.class));
        }
        super.onStart();
    }

}
