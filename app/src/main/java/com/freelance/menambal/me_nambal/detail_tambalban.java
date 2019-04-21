package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_VIEW;

public class detail_tambalban extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    NetworkImageView thumb_image;
    TextView nama, jenis_usaha, alamat, jam_operasional, harga, no_hp, deskripsi, lattmb, lngtmb, id_tmb;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe;
    String idtmbl;
    String id_tambalban;
    String lattambalban;
    String lngtambalban;
    String rutelatlng;


    /*Deklarasi variable*/
    CardView button_rute_tambalban;
    String goolgeMap = "com.google.android.apps.maps"; // identitas package aplikasi google masps android
    Uri gmmIntentUri;
    Intent mapIntent;

    /*Deklarasi variable*/

    private static final String TAG = detail_tambalban.class.getSimpleName();

    public static final String TAG_ID               = "id";
    public static final String TAG_NAMA             = "nama";
    public static final String TAG_JENIS_USAHA      = "jenis_usaha";
    public static final String TAG_ALAMAT           = "alamat";
    public static final String TAG_JAM_OPERASIONAL  = "jam_operasional";
    public static final String TAG_HARGA            = "harga";
    public static final String TAG_NO_HP            = "no_hp";
    public static final String TAG_DESKRIPSI        = "deskripsi";
    public static final String TAG_GAMBAR           = "gambar";
    public static final String TAG_lAT              = "lat";
    public static final String TAG_LNG              = "lng";

    private static final String url_detail  = Server.URL + "detail_tambalban.php";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_tambalban);
        //Menampilkan Button Back di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Tambal Ban");

        thumb_image        = (NetworkImageView) findViewById(R.id.gambar_tmb);
        nama               = (TextView) findViewById(R.id.nama_tmb);
        jenis_usaha        = (TextView) findViewById(R.id.jenis_usaha_tmb);
        alamat             = (TextView) findViewById(R.id.alamat_tmb);
        jam_operasional    = (TextView) findViewById(R.id.jam_operasional_tmb);
        harga              = (TextView) findViewById(R.id.harga_tmb);
        no_hp              = (TextView) findViewById(R.id.no_hp_tmb);
        lattmb             = (TextView) findViewById(R.id.lattmb);
        lngtmb             = (TextView) findViewById(R.id.lngtmb);
        deskripsi          = (TextView) findViewById(R.id.deskripsi_tmb);
        id_tmb           = (TextView) findViewById(R.id.id_tmb);
        swipe              = (SwipeRefreshLayout) findViewById(R.id.swipe_detail);


        id_tambalban = getIntent().getStringExtra(TAG_ID);

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_tambalban.isEmpty()) {
                               callDetailtambalban(id_tambalban);
                           }
                       }
                   }
        );

        //whatsapp
        CardView message = (CardView)findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String contact = no_hp.getText().toString();
                String url = "https://api.whatsapp.com/send?phone="+"+62"+contact+"&text=" + "Masih Buka Bang ??";;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setPackage("com.whatsapp");
                if(i.resolveActivity(getApplicationContext().getPackageManager()) != null){
                    startActivity(i);
                }

            }
        });


        // menyamakan variable pada layout detail_tambalban.xml
        button_rute_tambalban    = (CardView) findViewById(R.id.button_rute_tambalban);


        // tombol untuk menjalankan navigasi goolge maps intents
        button_rute_tambalban.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            lattambalban = lattmb.getText().toString();
            lngtambalban = lngtmb.getText().toString();
            rutelatlng = lattambalban + "," + lngtambalban;

            // Buat Uri dari intent string. Gunakan hasilnya untuk membuat Intent.
            gmmIntentUri = Uri.parse("google.navigation:q=" + rutelatlng);

            // Buat Uri dari intent gmmIntentUri. Set action => ACTION_VIEW
            mapIntent = new Intent(ACTION_VIEW, gmmIntentUri);

            // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
            mapIntent.setPackage(goolgeMap);

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(detail_tambalban.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                        Toast.LENGTH_LONG).show();
            }
        }

    });

        //telfon
        CardView call = (CardView)findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String nomorhp = no_hp.getText().toString();
                if(!TextUtils.isEmpty(nomorhp)){
                    String dial = "tel:"+ nomorhp;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else{
                    Toast.makeText(detail_tambalban.this,"Tidak ada nomor hp",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void button_laporkan_tambalban(View view) {
        idtmbl = id_tmb.getText().toString();
        Intent intent = new Intent(detail_tambalban.this, menu_laporkan_tambalban.class);
        intent.putExtra(TAG_ID, idtmbl);
        startActivity(intent);

    }

    private void callDetailtambalban(final String id){
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe.setRefreshing(false);


                try {
                    JSONObject obj = new JSONObject(response);

                    String Nama               = "Nama                       : "+obj.getString(TAG_NAMA);
                    String Jenis_usaha        = "Jenis Usaha            : "+obj.getString(TAG_JENIS_USAHA);
                    String Alamat             = "Alamat                    : " +obj.getString(TAG_ALAMAT);
                    String Jam_operasional    = "Jam Operasional    : "+obj.getString(TAG_JAM_OPERASIONAL);
                    String Harga              = "Harga                       : "+obj.getString(TAG_HARGA);
                    String No_hp              = obj.getString(TAG_NO_HP);
                    String Deskripsi          = obj.getString(TAG_DESKRIPSI);
                    String Gambar             = obj.getString(TAG_GAMBAR);
                    String LatTMB             = obj.getString(TAG_lAT);
                    String LngTMB             = obj.getString(TAG_LNG);
                    String Idtmb              = obj.getString(TAG_ID);

                    nama.setText(Nama);
                    jenis_usaha.setText(Jenis_usaha);
                    alamat.setText(Alamat);
                    jam_operasional.setText(Jam_operasional);
                    harga.setText(Harga);
                    no_hp.setText(No_hp);
                    deskripsi.setText(Deskripsi);
                    lattmb.setText(LatTMB);
                    lngtmb.setText(LngTMB);
                    id_tmb.setText(Idtmb);

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
                Toast.makeText(detail_tambalban.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
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
        callDetailtambalban(id_tambalban);
    }
}