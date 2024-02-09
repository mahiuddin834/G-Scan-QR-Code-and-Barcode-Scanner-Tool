package com.itnation.gscan_qrcodescaner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.ArrayList;
import java.util.List;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class QuickScannerActivity extends AppCompatActivity {


    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permission ->{
                boolean allGranted = true;

                for (Boolean isGranted : permission.values()){
                    if (!isGranted){
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted){
                    // All is granted
                } else {
                    // All is not granted
                }

            });



    ScannerLiveView camera;
    TextView scannedTV;
    ImageButton backButton, copyButton;

    AdView adView;
    int bannerAdsClick=0;
    private final String TAG = QuickScannerActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_scanner);


        camera= findViewById(R.id.camview);
        scannedTV = findViewById(R.id.scanResult);
        backButton = findViewById(R.id.backButton);
        copyButton = findViewById(R.id.copyButton);

        myPermissions();

        loadBannerAds();
        fullScreenAds();





        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager cm = (ClipboardManager)QuickScannerActivity.this.getSystemService(QuickScannerActivity.this.CLIPBOARD_SERVICE);
                cm.setText(scannedTV.getText());
                Toast.makeText(QuickScannerActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });



        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(QuickScannerActivity.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.
                Toast.makeText(QuickScannerActivity.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(QuickScannerActivity.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.
                scannedTV.setText(data);

                if (interstitialAd!=null && interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }
            }
        });

    }//----------- Close On create Method




    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set decoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    protected void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }




    private void myPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            String[] permissions = new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
            };


            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions){
                if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()){
                // All permissions are already granted
                Toast.makeText(this, "All permissions are already granted", Toast.LENGTH_SHORT).show();


            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray){
                    if (shouldShowRequestPermissionRationale(permission)){
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale){
                    new AlertDialog.Builder(this)
                            .setMessage("Please allow all permissions")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissionLauncher.launch(permissionsArray);
                                }
                            })

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();

                } else {
                    requestPermissionLauncher.launch(permissionsArray);
                }


            }




        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{


                    android.Manifest.permission.READ_EXTERNAL_STORAGE,


            };


            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions){
                if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()){
                // All permissions are already granted
                Toast.makeText(this, "All permissions are already granted", Toast.LENGTH_SHORT).show();


            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray){
                    if (shouldShowRequestPermissionRationale(permission)){
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale){
                    new AlertDialog.Builder(this)
                            .setMessage("Please allow all permissions")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissionLauncher.launch(permissionsArray);
                                }
                            })

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();

                } else {
                    requestPermissionLauncher.launch(permissionsArray);
                }


            }


        }


    } // myPermissions end here ================

    // Banner ads


    public void loadBannerAds(){

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);




        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {


            }

            @Override
            public void onAdLoaded(Ad ad) {
// Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {

                bannerAdsClick++;

                if (bannerAdsClick>=2){

                    if (adView!=null){
                        adView.destroy();
                        adContainer.setVisibility(View.GONE);
                    }
                }
// Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
// Ad impression logged callback
            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }else if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }


    //Full Screen ADs---------------------------

    private void fullScreenAds(){


        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");

                fullScreenAds();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");


            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());




    }

}