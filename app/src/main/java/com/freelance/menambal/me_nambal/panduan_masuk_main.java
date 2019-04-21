package com.freelance.menambal.me_nambal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by UutBT on 1/14/2019.
 */

public class panduan_masuk_main extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panduan_masuk_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Panduan Masuk");

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
}
