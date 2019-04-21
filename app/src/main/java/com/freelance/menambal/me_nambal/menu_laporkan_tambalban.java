package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
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

public class menu_laporkan_tambalban extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    CardView cardview;
    AlertDialog.Builder dialog;
    View dialogView;
    LayoutInflater inflater;
    NetworkImageView thumb_image;
    TextView nama_tmb_laporkan, jenis_usaha_tmb_laporkan, alamat_tmb_laporkan, id_tmb_laporan, id_gambar_tmb_laporan;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe_laporkan_tmb;
    String id;
    int success;
    String nama, jenis_usaha, alamat, gambar,isi_laporan,nama_pelapor_tmb;
    EditText txt_isi_laporan, txt_nama_pelapor_tmb;


    private static final String TAG = menu_laporkan_tambalban.class.getSimpleName();

    public static final String TAG_ID               = "id";
    public static final String TAG_NAMA             = "nama";
    public static final String TAG_JENIS_USAHA      = "jenis_usaha";
    public static final String TAG_ALAMAT           = "alamat";
    public static final String TAG_GAMBAR           = "gambar";
    private static final String TAG_SUCCESS         = "success";
    private static final String TAG_MESSAGE         = "message";

    //url menambahkan laporan tidak valid
    private static String url_insert     = Server.URL + "report_tambalban.php";


    private static final String url_detail  = Server.URL + "detail_tambalban.php";
    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporkan_tambalban);
        //Menampilkan Button Back di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Laporkan Lokasi Tambal Ban");

        thumb_image        = (NetworkImageView) findViewById(R.id.gambar_laporan_tmb);
        nama_tmb_laporkan  = (TextView) findViewById(R.id.nama_tmb_laporkan);
        jenis_usaha_tmb_laporkan = (TextView) findViewById(R.id.jenis_usaha_tmb_laporkan);
        alamat_tmb_laporkan      = (TextView) findViewById(R.id.alamat_tmb_laporkan);
        id_tmb_laporan      = (TextView) findViewById(R.id.id_tmb_laporan);
        id_gambar_tmb_laporan      = (TextView) findViewById(R.id.id_gambar_tmb_laporan);

        swipe_laporkan_tmb = (SwipeRefreshLayout) findViewById(R.id.swipe_laporkan_tmb);
        cardview     = (CardView) findViewById(R.id.laporkandisini);


        id = getIntent().getStringExtra(TAG_ID);

        swipe_laporkan_tmb.setOnRefreshListener(this);
        swipe_laporkan_tmb.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id.isEmpty()) {
                               callDetaillaporkantambalban(id);
                           }
                       }
                   }
        );

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("KIRIM");
            }
        });

    }

    // untuk menampilkan dialog from biodata
    private void DialogForm(String button_kirim_laporan_tmb) {
        dialog = new AlertDialog.Builder(menu_laporkan_tambalban.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_laporan_tambalban, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.logoab);
        dialog.setTitle("Laporkan Lokasi Tambal Ban");

        txt_nama_pelapor_tmb = (EditText) dialogView.findViewById(R.id.nama_pelapor_tmb);
        txt_isi_laporan  = (EditText) dialogView.findViewById(R.id.isi_laporkan_tmb);


        dialog.setPositiveButton(button_kirim_laporan_tmb, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id = id_tmb_laporan.getText().toString();
                nama = nama_tmb_laporkan.getText().toString();
                jenis_usaha = jenis_usaha_tmb_laporkan.getText().toString();
                alamat = alamat_tmb_laporkan.getText().toString();
                gambar = id_gambar_tmb_laporan.getText().toString();
                isi_laporan = txt_isi_laporan.getText().toString();
                nama_pelapor_tmb = txt_nama_pelapor_tmb.getText().toString();

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


                    Toast.makeText(menu_laporkan_tambalban.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    onBackPressed();

                } else {
                    Toast.makeText(menu_laporkan_tambalban.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
            Toast.makeText(menu_laporkan_tambalban.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting parameters ke post url
            Map<String, String> params = new HashMap<String, String>();
            // jika id kosong maka simpan, jika id ada nilainya maka update

                params.put("id", id);
                params.put("nama", nama);
                params.put("jenis_usaha", jenis_usaha);
                params.put("alamat", alamat);
                params.put("gambar", gambar);
                params.put("isi_laporan", isi_laporan);
                params.put("nama_pelapor_tmb", nama_pelapor_tmb);


            return params;
        }

    };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
}




    private void callDetaillaporkantambalban(final String id){
        swipe_laporkan_tmb.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe_laporkan_tmb.setRefreshing(false);


                try {
                    JSONObject obj = new JSONObject(response);

                 //   String Nama               = "Nama                    :"+             obj.getString(TAG_NAMA);
                 //   String Jenis_usaha        = "Jenis Usaha         :"+             obj.getString(TAG_JENIS_USAHA);
                 //   String Alamat             = "Alamat                  :"+             obj.getString(TAG_ALAMAT);

                    String Nama               = obj.getString(TAG_NAMA);
                    String Jenis_usaha        = obj.getString(TAG_JENIS_USAHA);
                    String Alamat             = obj.getString(TAG_ALAMAT);
                    String Id_tmb_laporan     = obj.getString(TAG_ID);
                    String Gambar             = obj.getString(TAG_GAMBAR);

                    nama_tmb_laporkan.setText(Nama);
                    jenis_usaha_tmb_laporkan.setText(Jenis_usaha);
                    alamat_tmb_laporkan.setText(Alamat);
                    id_tmb_laporan.setText(Id_tmb_laporan);
                    id_gambar_tmb_laporan.setText(Gambar);


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
                Toast.makeText(menu_laporkan_tambalban.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe_laporkan_tmb.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

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
        callDetaillaporkantambalban(id);
    }
}