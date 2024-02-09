package com.itnation.gscan_qrcodescaner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.itnation.gscan_qrcodescaner.Fragment.GenerateFragment;
import com.itnation.gscan_qrcodescaner.Fragment.HomeFragment;
import com.itnation.gscan_qrcodescaner.Fragment.ScanFragment;

public class MainActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    TextView headerMail, headerName;
    View headerView;


    AdView adView;
    int bannerAdsClick=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        materialToolbar= findViewById(R.id.toolbar_layout);
        frameLayout= findViewById(R.id.frame_layout);
        navigationView= findViewById(R.id. navigation_view);
        headerView= navigationView.getHeaderView(0);
        headerMail= headerView.findViewById(R.id.headerMail);
        headerName= headerView.findViewById(R.id.headerName);




        loadBannerAds();



        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle
                (MainActivity.this, drawerLayout, materialToolbar, R.string.opendrawer, R.string.closedrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.home){


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                    fragmentTransaction.commit();

                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (item.getItemId()==R.id.scan){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ScanFragment());
                    fragmentTransaction.commit();

                    drawerLayout.closeDrawer(GravityCompat.START);

                }else if (item.getItemId()==R.id.generate){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new GenerateFragment());
                    fragmentTransaction.commit();

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (item.getItemId()==R.id.rating) {


                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }


                    drawerLayout.closeDrawer(GravityCompat.START);

                }


                return true;
            }
        });


    }//------------------ close onCreate Method----------------------------------

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
        }
        super.onDestroy();
    }
}