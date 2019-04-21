package com.freelance.menambal.me_nambal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.freelance.menambal.me_nambal.R;
import com.freelance.menambal.me_nambal.app.AppController;
import com.freelance.menambal.me_nambal.module.Jarak;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Jarak> jarakItems;
    ImageLoader imageLoader;



    public CustomListAdapter(Activity activity, List<Jarak> jarakItems) {
        this.activity = activity;
        this.jarakItems = jarakItems;

    }

    @Override
    public int getCount() {
        return jarakItems.size();
    }

    @Override
    public Object getItem(int location) {
        return jarakItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.gambar);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView jam_operasional = (TextView) convertView.findViewById(R.id.jam_operasional);
        TextView jarak = (TextView) convertView.findViewById(R.id.jarak);

        Jarak j = jarakItems.get(position);

        thumbNail.setImageUrl(j.getGambar(), imageLoader);
        nama.setText(j.getNama());
        alamat.setText(j.getAlamat());
        jam_operasional.setText(j.getJam_operasional());
        jarak.setText(j.getJarak()+" Km");

        return convertView;
    }

}