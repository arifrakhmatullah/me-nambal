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


public class CustomListAdapterBensin extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Jarak> jarakItems;
    ImageLoader imageLoader;



    public CustomListAdapterBensin(Activity activity, List<Jarak> jarakItems) {
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
            convertView = inflater.inflate(R.layout.list_row_bensin, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.gambar_b);
        TextView nama_b = (TextView) convertView.findViewById(R.id.nama_bensin);
        TextView alamat_b = (TextView) convertView.findViewById(R.id.alamat_bensin);
        TextView jam_operasional_b = (TextView) convertView.findViewById(R.id.jam_operasional_bensin);
        TextView jarak_b = (TextView) convertView.findViewById(R.id.jarak_b);

        Jarak j = jarakItems.get(position);

        thumbNail.setImageUrl(j.getGambar_b(), imageLoader);
        nama_b.setText(j.getNama_b());
        alamat_b.setText(j.getAlamat_b());
        jam_operasional_b.setText(j.getJam_operasional_b());
        jarak_b.setText(j.getJarak_b()+" Km");

        return convertView;
    }

}