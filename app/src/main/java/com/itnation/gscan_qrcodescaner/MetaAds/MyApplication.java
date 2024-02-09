package com.itnation.gscan_qrcodescaner.MetaAds;

import android.app.Application;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("a78eb007-2391-47fc-a4a4-3a9ece135964");
    }

}
