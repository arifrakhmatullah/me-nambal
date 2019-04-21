package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.freelance.menambal.me_nambal.app.AppController;
import com.freelance.menambal.me_nambal.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class menu_laporkan_bensineceran extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    CardView cardview_bns;
    AlertDialog.Builder dialog;
    View dialogView;
    LayoutInflater inflater;
    NetworkImageView thumb_image;
    TextView nama_bns_laporkan, jenis_usaha_bns_laporkan, alamat_bns_laporkan, id_bns_laporan, id_gambar_bns;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe_laporkan_bns;
    String id;
    int success;
    String nama_b, jenis_usaha_b, alamat_b, gambar_b,isi_laporan_b,nama_pelapor_b;
    EditText txt_isi_laporan_b, txt_nama_pelapor_b;


    private static final String TAG = menu_laporkan_bensineceran.class.getSimpleName();



    public static final String TAG_ID_B              = "id_b";
    public static final String TAG_NAMA             = "nama_b";
    public static final String TAG_JENIS_USAHA      = "jenis_usaha_b";
    public static final String TAG_ALAMAT           = "alamat_b";
    public static final String TAG_GAMBAR           = "gambar_b";
    private static final String TAG_SUCCESS         = "success";
    private static final String TAG_MESSAGE         = "message";


    //url menambahkan laporan tidak valid
    private static String url_insert     = Server.URL + "report_bensineceran.php";

    private static final String url_detail  = Server.URL + "detail_bensineceran.php";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporkan_bensineceran);
        //Menampilkan Button Back di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Laporkan Lokasi Bensin Eceran");

        thumb_image        = (NetworkImageView) findViewById(R.id.gambar_laporan_bns);
        nama_bns_laporkan  = (TextView) findViewById(R.id.nama_bns_laporkan);
        jenis_usaha_bns_laporkan = (TextView) findViewById(R.id.jenis_usaha_bns_laporkan);
        alamat_bns_laporkan      = (TextView) findViewById(R.id.alamat_bns_laporkan);
        id_bns_laporan     = (TextView) findViewById(R.id.id_bns_laporan);
        id_gambar_bns      = (TextView) findViewById(R.id.id_gambar_bns_laporan);

        swipe_laporkan_bns = (SwipeRefreshLayout) findViewById(R.id.swipe_laporkan_bns);
        cardview_bns     = (CardView) findViewById(R.id.laporkandisini_bns);

        id = getIntent().getStringExtra(TAG_ID_B);

        swipe_laporkan_bns.setOnRefreshListener(this);
        swipe_laporkan_bns.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id.isEmpty()) {
                               callDetaillaporkanbensineceran(id);
                           }
                       }
                   }
        );

        cardview_bns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("KIRIM");
            }
        });


    }

    // untuk menampilkan dialog from biodata
    private void DialogForm(String button_kirim_laporan_bns) {
        dialog = new AlertDialog.Builder(menu_laporkan_bensineceran.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_laporan_bensineceran, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.logoab);
        dialog.setTitle("Laporkan Lokasi Yang Tidak Valid !");

        txt_nama_pelapor_b = (EditText) dialogView.findViewById(R.id.nama_pelapor_bns);
        txt_isi_laporan_b  = (EditText) dialogView.findViewById(R.id.isi_laporkan_bns);


        dialog.setPositiveButton(button_kirim_laporan_bns, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id = id_bns_laporan.getText().toString();
                nama_b = nama_bns_laporkan.getText().toString();
                jenis_usaha_b = jenis_usaha_bns_laporkan.getText().toString();
                alamat_b = alamat_bns_laporkan.getText().toString();
                gambar_b = id_gambar_bns.getText().toString();
                isi_laporan_b = txt_isi_laporan_b.getText().toString();
                nama_pelapor_b = txt_nama_pelapor_b.getText().toString();

                simpan_data();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void simpan_data() {
        String url;
        url = url_insert;


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());


                        Toast.makeText(menu_laporkan_bensineceran.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        onBackPressed();

                    } else {
                        Toast.makeText(menu_laporkan_bensineceran.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(menu_laporkan_bensineceran.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update

                params.put("id_b", id);
                params.put("nama_b", nama_b);
                params.put("jenis_usaha_b", jenis_usaha_b);
                params.put("alamat_b", alamat_b);
                params.put("gambar_b", gambar_b);
                params.put("isi_laporan_b", isi_laporan_b);
                params.put("nama_pelapor_bns", nama_pelapor_b);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void callDetaillaporkanbensineceran(final String id){
        swipe_laporkan_bns.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe_laporkan_bns.setRefreshing(false);


                try {
                    JSONObject obj = new JSONObject(response);

                    String Nama               =obj.getString(TAG_NAMA);
                    String Jenis_usaha        =obj.getString(TAG_JENIS_USAHA);
                    String Alamat             =obj.getString(TAG_ALAMAT);
                    String Gambar             = obj.getString(TAG_GAMBAR);
                    String Id_bns_laporan     = obj.getString(TAG_ID_B);


                    nama_bns_laporkan.setText(Nama);
                    jenis_usaha_bns_laporkan.setText(Jenis_usaha);
                    alamat_bns_laporkan.setText(Alamat);
                    id_bns_laporan.setText(Id_bns_laporan);
                    id_gambar_bns.setText(Gambar);



                    if (obj.getString(TAG_GAMBAR)!=""){
                        thumb_image.setImageUrl(Gambar, imageLoader);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(menu_laporkan_bensineceran.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe_laporkan_bns.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_b", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //aksi untuk menjalankan button back di toolbar
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
    public void onRefresh() {
        callDetaillaporkanbensineceran(id);
    }
}