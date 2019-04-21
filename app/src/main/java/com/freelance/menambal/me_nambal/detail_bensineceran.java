package com.freelance.menambal.me_nambal;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class detail_bensineceran extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    NetworkImageView thumb_image;
    TextView nama_b, jenis_usaha_b, alamat_b, jam_operasional_b, harga_b, no_hp_b, deskripsi_b, latbns, lngbns, id_bns;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe_bensin;
    String idbns;
    String id_bensineceran;
    String latbensineceran;
    String lngbensineceran;
    String rutelatlngbensin;

    /*Deklarasi variable*/
    CardView button_rute_bensineceran;
    String goolgeMap = "com.google.android.apps.maps"; // identitas package aplikasi google masps android
    Uri gmmIntentUri;
    Intent mapIntent;

    /*Deklarasi variable*/

    private static final String TAG = detail_bensineceran.class.getSimpleName();

    public static final String TAG_ID_B               = "id_b";
    public static final String TAG_NAMA_B             = "nama_b";
    public static final String TAG_JENIS_USAHA_B      = "jenis_usaha_b";
    public static final String TAG_ALAMAT_B           = "alamat_b";
    public static final String TAG_JAM_OPERASIONAL_B  = "jam_operasional_b";
    public static final String TAG_HARGA_B            = "harga_b";
    public static final String TAG_NO_HP_B            = "no_hp_b";
    public static final String TAG_DESKRIPSI_B        = "deskripsi_b";
    public static final String TAG_GAMBAR_B           = "gambar_b";
    public static final String TAG_LAT_B           = "lat_b";
    public static final String TAG_LNG_B           = "lng_b";

    private static final String url_detail  = Server.URL + "detail_bensineceran.php";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_bensineceran);
        //Menampilkan Button Back di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Bensin Eceran");

        thumb_image        = (NetworkImageView) findViewById(R.id.gambar_bns);
        nama_b               = (TextView) findViewById(R.id.nama_bns);
        jenis_usaha_b        = (TextView) findViewById(R.id.jenis_usaha_bns);
        alamat_b             = (TextView) findViewById(R.id.alamat_bns);
        jam_operasional_b    = (TextView) findViewById(R.id.jam_operasional_bns);
        harga_b              = (TextView) findViewById(R.id.harga_bns);
        no_hp_b              = (TextView) findViewById(R.id.no_hp_bns);
        deskripsi_b          = (TextView) findViewById(R.id.deskripsi_bns);
        latbns               = (TextView) findViewById(R.id.latbns);
        lngbns               = (TextView) findViewById(R.id.lngbns);
        id_bns               = (TextView) findViewById(R.id.id_bns);
        swipe_bensin          = (SwipeRefreshLayout) findViewById(R.id.swipe_detail_bensineceran);

        id_bensineceran = getIntent().getStringExtra(TAG_ID_B);

        swipe_bensin.setOnRefreshListener(this);
        swipe_bensin.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_bensineceran.isEmpty()) {
                               callDetailBensinEceran(id_bensineceran);
                           }
                       }
                   }
        );

        //whatsapp
        CardView message_bns = (CardView)findViewById(R.id.message_bns);
        message_bns.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String contact = no_hp_b.getText().toString();
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
        button_rute_bensineceran    = (CardView) findViewById(R.id.button_rute_bensineceran);


        // tombol untuk menjalankan navigasi goolge maps intents
        button_rute_bensineceran.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                latbensineceran = latbns.getText().toString();
                lngbensineceran = lngbns.getText().toString();
                rutelatlngbensin = latbensineceran + "," + lngbensineceran;


                    // Buat Uri dari intent string. Gunakan hasilnya untuk membuat Intent.
                    gmmIntentUri = Uri.parse("google.navigation:q=" + rutelatlngbensin);

                    // Buat Uri dari intent gmmIntentUri. Set action => ACTION_VIEW
                    mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                    // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
                    mapIntent.setPackage(goolgeMap);

                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(detail_bensineceran.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                                Toast.LENGTH_LONG).show();
                    }
            }

        });


        //telfon
        CardView call_bns = (CardView)findViewById(R.id.call_bns);
        call_bns .setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String nomorhp = no_hp_b.getText().toString();
                if(!TextUtils.isEmpty(nomorhp)){
                    String dial = "tel:"+ nomorhp;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else{
                    Toast.makeText(detail_bensineceran.this,"Tidak ada nomor hp",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void button_laporkan_bensineceran(View view) {
        idbns = id_bns.getText().toString();
        Intent intent = new Intent(detail_bensineceran.this, menu_laporkan_bensineceran.class);
        intent.putExtra(TAG_ID_B, idbns);
        startActivity(intent);

    }

    private void callDetailBensinEceran(final String id){
        swipe_bensin.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe_bensin.setRefreshing(false);

                try {
                    JSONObject obj = new JSONObject(response);

                    String Nama_B               = "Nama                       : "+obj.getString(TAG_NAMA_B);
                    String Jenis_usaha_B        = "Jenis Usaha            : "+obj.getString(TAG_JENIS_USAHA_B);
                    String Alamat_B             = "Alamat                    : "+obj.getString(TAG_ALAMAT_B);
                    String Jam_operasional_B    = "Jam Operasional    : "+obj.getString(TAG_JAM_OPERASIONAL_B);
                    String Harga_B              = "Harga                       : "+obj.getString(TAG_HARGA_B);
                    String No_hp_B              = obj.getString(TAG_NO_HP_B);
                    String Deskripsi_B          = obj.getString(TAG_DESKRIPSI_B);
                    String Gambar_B             = obj.getString(TAG_GAMBAR_B);
                    String LatBNS               = obj.getString(TAG_LAT_B);
                    String LngBNS               = obj.getString(TAG_LNG_B);
                    String Idbns                = obj.getString(TAG_ID_B);


                    nama_b.setText(Nama_B);
                    jenis_usaha_b.setText(Jenis_usaha_B);
                    alamat_b.setText(Alamat_B);
                    jam_operasional_b.setText(Jam_operasional_B);
                    harga_b.setText(Harga_B);
                    no_hp_b.setText(No_hp_B);
                    deskripsi_b.setText(Deskripsi_B);
                    latbns.setText(LatBNS);
                    lngbns.setText(LngBNS);
                    id_bns.setText(Idbns);



                    if (obj.getString(TAG_GAMBAR_B)!=""){
                        thumb_image.setImageUrl(Gambar_B, imageLoader);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail News Error: " + error.getMessage());
                Toast.makeText(detail_bensineceran.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe_bensin.setRefreshing(false);
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
        callDetailBensinEceran(id_bensineceran);
    }
}