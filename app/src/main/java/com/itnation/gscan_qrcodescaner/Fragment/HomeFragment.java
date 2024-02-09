package com.itnation.gscan_qrcodescaner.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.itnation.gscan_qrcodescaner.QuickScannerActivity;
import com.itnation.gscan_qrcodescaner.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    ImageSlider imageSlider;
    LottieAnimationView lottieAnimationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView =inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider=myView.findViewById(R.id.imageSlider);
        lottieAnimationView=myView.findViewById(R.id.animationView);




        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://i.pinimg.com/736x/8b/7b/84/8b7b84fd89eff081dae32fb123b071e0.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://www.switchingtomac.com/wp-content/uploads/2022/02/image-5.png", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://www.guidingtech.com/wp-content/uploads/How-to-scan-QR-code-on-Android-1.jpg", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);


        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), QuickScannerActivity.class));

            }
        });


        return myView;
    }
}