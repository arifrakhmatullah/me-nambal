package com.freelance.menambal.me_nambal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by UutBT on 11/2/2018.
 */

public class Login_About_Us extends Fragment {

    public Login_About_Us(){}
    RelativeLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.about_us, container, false);


        getActivity().setTitle("About Us");

        return view;
    }
}
