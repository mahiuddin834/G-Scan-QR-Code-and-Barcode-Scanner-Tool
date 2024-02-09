package com.itnation.gscan_qrcodescaner.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.itnation.gscan_qrcodescaner.BarcodeActivity;
import com.itnation.gscan_qrcodescaner.QuickScannerActivity;
import com.itnation.gscan_qrcodescaner.R;


public class ScanFragment extends Fragment {


    CardView qr_card, barcode_card;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myView = inflater.inflate(R.layout.fragment_scan, container, false);

        barcode_card = myView.findViewById(R.id.barcode_card);
        qr_card = myView.findViewById(R.id.qr_card);

        qr_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), QuickScannerActivity.class));

            }
        });



        barcode_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), BarcodeActivity.class));

            }
        });




        return myView;
    }
}