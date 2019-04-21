package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.freelance.menambal.me_nambal.adapter.CustomListAdapter;
import com.freelance.menambal.me_nambal.adapter.CustomListAdapterBensin;
import com.freelance.menambal.me_nambal.app.AppController;
import com.freelance.menambal.me_nambal.module.Jarak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class bensineceran extends AppCompatActivity implements LocationListener,
        SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipe_bensin;
    ListView list_bensin;
    List<Jarak> itemList = new ArrayList<>();
    CustomListAdapterBensin adapter;
    Double latitude, longitude;
    Criteria criteria;
    Location location;
    LocationManager locationManager;
    String provider;

    public static final String TAG_ID_B = "id_b";
    private static  final String TAG = bensineceran.class.getSimpleName();

    /* 10.0.2.2 adalah IP Address localhost EMULATOR ANDROID STUDIO,
    Ganti IP Address tersebut dengan IP Laptop Apabila di RUN di HP. HP dan Laptop harus 1 jaringan */
    //url hosting contoh : http://menambal.000webhostapp.com/android/me-nambal/bensineceran.php?lat_b=
    private static final String url = "https://official-menambal.000webhostapp.com/android/me-nambal/bensineceran.php?lat_b=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bensineceran_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Daftar Lokasi Bensin Eceran");

        // menyamakan variabel pada layout dan java
       list_bensin = (ListView) findViewById(R.id.list_bensin);
       swipe_bensin = (SwipeRefreshLayout) findViewById(R.id.swipe_bensin);

        // mengisi data dari adapter ke listview
        adapter = new CustomListAdapterBensin(this, itemList);
        list_bensin.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        swipe_bensin.setOnRefreshListener(this);

        swipe_bensin.post(new Runnable() {
                       @Override
                       public void run() {
                           lokasi_bensin();
                       }
                   }
        );

        // untuk bisa diklik listviewnya pindah activity
        list_bensin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(bensineceran.this, detail_bensineceran.class);
                intent.putExtra(TAG_ID_B, itemList.get(position).getId_b());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        lokasi_bensin();
    }

    // fungsi ngecek lokasi GPS device pengguna
    private void lokasi_bensin() {
        location = locationManager.getLastKnownLocation(provider);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // permintaan update lokasi device dalam waktu per detik
        locationManager.requestLocationUpdates(provider, 1000, 1, this);

        if (location != null) {
            onLocationChanged(location);
        } else {
           /* latitude longitude Pekanbaru sebagai default jika tidak ditemukan lokasi dari device pengguna */
            callListVolleyBensin(0.504865, 101.445979);
        }
    }

    // untuk menampilkan lokasi tambalban terdekat dari device pengguna
    private void callListVolleyBensin(double lat_b, double lng_b) {
        itemList.clear();
        adapter.notifyDataSetChanged();

        swipe_bensin.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url + lat_b + "&lng_b=" + lng_b,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Jarak j = new Jarak();
                                j.setId_b(obj.getString("id_b"));
                                j.setAlamat_b(obj.getString("alamat_b"));
                                j.setJam_operasional_b(obj.getString("jam_operasional_b"));
                                j.setNama_b(obj.getString("nama_b"));
                                j.setGambar_b(obj.getString("gambar_b"));

                                double jarak_b = Double.parseDouble(obj.getString("jarak_b"));

                                j.setJarak_b("" + round(jarak_b, 2));

                                itemList.add(j);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // memberitahu adapter jika ada perubahan data
                        adapter.notifyDataSetChanged();

                        swipe_bensin.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                swipe_bensin.setRefreshing(false);
            }
        });

        // menambah permintaan ke queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    // untuk menyederhanakan angka dibelakan koma jarak
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    // untuk menentukan lokasi gps dari device pengguna
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // untuk melihat latitude longitude posisi device pengguna pada logcat ditemukan atau tidak
        Log.e(TAG, "User location latitude:" + latitude + ", longitude:" + longitude);

        callListVolleyBensin(latitude, longitude);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}