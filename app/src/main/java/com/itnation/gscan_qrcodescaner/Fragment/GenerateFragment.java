package com.itnation.gscan_qrcodescaner.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itnation.gscan_qrcodescaner.WifiActivity;
import com.itnation.gscan_qrcodescaner.MailActivity;
import com.itnation.gscan_qrcodescaner.MassageActivity;
import com.itnation.gscan_qrcodescaner.PhoneActivity;
import com.itnation.gscan_qrcodescaner.R;
import com.itnation.gscan_qrcodescaner.TextActivity;
import com.itnation.gscan_qrcodescaner.UrlActivity;


public class GenerateFragment extends Fragment {

    CardView  mail, phone, massage, text, url, card;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_generate, container, false);

        mail= myView.findViewById(R.id.mail);
        phone= myView.findViewById(R.id.phone);
        massage= myView.findViewById(R.id.massage);
        text= myView.findViewById(R.id.text);
        url= myView.findViewById(R.id.url);
        card= myView.findViewById(R.id.card);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(getActivity(), MailActivity.class));

            }
        });



        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), PhoneActivity.class));

            }
        });



        massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MassageActivity.class));

            }
        });



        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), TextActivity.class));

            }
        });



        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), UrlActivity.class));

            }
        });


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), WifiActivity.class));

            }
        });








        return myView;
    }
}