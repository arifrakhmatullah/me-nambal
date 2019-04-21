package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 1/14/2019.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;


public class Login_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView mNavigationView;
    View mHeaderView;

    private int COARSE_PERMISSION_CODE = 1;
    private int FINE_PERMSSION_CODE = 1;

    FragmentManager fragmentManager;
    Fragment fragment = null;

    Button sign_out;
    GoogleSignInClient mGoogleSignInClient;
    TextView nameTV;
    TextView emailTV;
    TextView idTV;
    CircleImageView photoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_login);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view_login);
        mHeaderView = mNavigationView.getHeaderView(0);

        nameTV = (TextView)mHeaderView.findViewById(R.id.name);
        emailTV =(TextView)mHeaderView.findViewById(R.id.email);
        idTV = (TextView)mHeaderView.findViewById(R.id.id);
        photoIV = (CircleImageView)mHeaderView.findViewById(R.id.photo);
        sign_out = findViewById(R.id.log_out);

        mNavigationView.setNavigationItemSelectedListener(this);


        // tampilan default awal ketika aplikasii dijalankan
        if (savedInstanceState == null) {
            fragment = new Login_Root();
            callFragment(fragment);
        }


        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0
                && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            Toast.makeText(this, "You Have Already Granted This Permission !", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login_MainActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nameTV.setText(personName);
            emailTV.setText(personEmail);
            idTV.setText(personId);
            Glide.with(this).load(personPhoto).into(photoIV);
        }
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")
                && ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_COARSE_LOCATON")) {
            new AlertDialog.Builder(this).setTitle("Permission Needed").setMessage("This Permission is Needed Access Your Location").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(Login_MainActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, Login_MainActivity.this
                            .FINE_PERMSSION_CODE);
                    ActivityCompat.requestPermissions(Login_MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, Login_MainActivity.this.COARSE_PERMISSION_CODE);
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
        Intent intent = new Intent(Login_MainActivity.this, tambalban.class);
        startActivity(intent);

    }

    public void cari_bensineceran(View view) {
        Intent intent = new Intent(Login_MainActivity.this, bensineceran.class);
        startActivity(intent);

    }

    public void temukanlokasi(View view) {
        Intent intent = new Intent(Login_MainActivity.this, panduan_temukanlokasi_main.class);
        startActivity(intent);

    }

    public void simbollokasi(View view) {
        Intent intent = new Intent(Login_MainActivity.this, panduan_simbollokasi_main.class);
        startActivity(intent);

    }

    public void panduanmasuk(View view) {
        Intent intent = new Intent(Login_MainActivity.this, panduan_masuk_main.class);
        startActivity(intent);

    }

    public void tambahlokasi(View view) {
        Intent intent = new Intent(Login_MainActivity.this, panduan_tambahlokasi_main.class);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_login);
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
            fragment = new Login_Root();
            callFragment(fragment);

        } else if (id == R.id.addlocation) {
            // Handle the mylocation action
            Intent intent = new Intent(Login_MainActivity.this, Login_MyLocationMaps.class);
            startActivity(intent);

        } else if (id == R.id.panduan) {
            fragment = new Panduan();
            callFragment(fragment);

        } else if (id == R.id.about_us) {
            fragment = new About_Us();
            callFragment(fragment);

        } else if (id == R.id.logout) {
            Intent intent = new Intent(Login_MainActivity.this, Login_signout.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_login);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // untuk mengganti isi kontainer menu yang dipiih

    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_login, fragment)
                .commit();
    }



}
