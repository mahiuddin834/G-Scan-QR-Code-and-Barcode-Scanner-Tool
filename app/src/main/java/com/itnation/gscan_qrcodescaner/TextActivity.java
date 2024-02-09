package com.itnation.gscan_qrcodescaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class TextActivity extends AppCompatActivity {


    ImageButton back, share, save;
    EditText phoneEdt;
    Button generateBtn;
    ImageView qrCodeIV;
    LinearLayout qrLay;
    RelativeLayout shareSaveLaye;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private InterstitialAd interstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        back = findViewById(R.id.back);
        phoneEdt = findViewById(R.id.phoneEdt);
        generateBtn = findViewById(R.id.generateBtn);
        qrCodeIV = findViewById(R.id.qrCodeIV);
        qrLay = findViewById(R.id.qrLay);
        shareSaveLaye = findViewById(R.id.shareSaveLaye);
        share= findViewById(R.id.share);
        save= findViewById(R.id.save);

        fullScreenAds();


        Window window = TextActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TextActivity.this,R.color.text));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });





        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                generateQR();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeIV.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                downloadImage(qrCodeIV);
                Toast.makeText(TextActivity.this, "QR Code Saved", Toast.LENGTH_SHORT).show();


            }
        });


    }// close oncreate------------------------


    private  void generateQR(){


        String phone= phoneEdt.getText().toString();


        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(TextActivity.this, "Enter Phone Number to generate QR Code", Toast.LENGTH_SHORT).show();
        } else {

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);


            Display display = manager.getDefaultDisplay();


            Point point = new Point();
            display.getSize(point);


            int width = point.x;
            int height = point.y;


            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;


            qrgEncoder = new QRGEncoder(phone, null, QRGContents.Type.TEXT, dimen);
            try {

                bitmap = qrgEncoder.encodeAsBitmap();

                qrCodeIV.setImageBitmap(bitmap);
                qrLay.setVisibility(View.VISIBLE);
                shareSaveLaye.setVisibility(View.VISIBLE);

                if (interstitialAd!=null && interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }

            } catch (WriterException e) {

                Log.e("Tag", e.toString());
            }
        }

    }// close generate





    private void downloadImage(View view){


        qrCodeIV.setImageBitmap(bitmap);
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0); // internal Storage
        File fileImage = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            fileImage = new File(storageVolume.getDirectory().getPath() + "/Download/"
                    + System.currentTimeMillis()
                    + ".jpeg");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesArray = byteArrayOutputStream.toByteArray();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
            fileOutputStream.write(bytesArray);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }


    //---------------------------------- share image --------------------------


    private void shareImageAndText(Bitmap bitmap){
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,"Sharing Images");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent,"Share via"));
    }

    private Uri getImageToShare (Bitmap bitmap){
        File imageFolder = new File(getCacheDir(),"images");
        Uri uri = null;

        try {
            imageFolder.mkdir();
            File file = new File(imageFolder,"shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(TextActivity.this,"com.itnation.imagesearch", file);
        } catch (Exception e){
            Toast.makeText(this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }


    //==================================

    private void fullScreenAds(){


        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback


                fullScreenAds();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed


            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback



            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback

            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());




    }


    @Override
    protected void onDestroy() {
        if (interstitialAd!=null) {
            interstitialAd.destroy();

        }
        super.onDestroy();
    }
}