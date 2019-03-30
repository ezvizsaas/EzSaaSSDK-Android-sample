package com.ys7.ezsaas;

import android.support.multidex.MultiDexApplication;

import com.ys7.enterprise.core.application.EzSaaSSDK;
import com.ys7.enterprise.core.util.LG;

/**
 * @author xiezuoyuan
 * @time 2019/3/20 13:41
 */
public class EzApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        EzSaaSSDK.openDebug(true);
        EzSaaSSDK.init(this,"431cf2e3495b11e9acb91aca0191c622");
        //EzSaaSSDK.getInstance().setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTMwNDkwMDUsInNhYXNVc2VySWQiOjkwODU2LCJ1c2VyX25hbWUiOiI5MDg1NiIsImp0aSI6IjhjZGFmZGIyLTUxNzctNDg5NC1hZmVmLWVjYzVhYzg2ZmE4MiIsImNsaWVudF9pZCI6InlzN0FwcCIsInNjb3BlIjpbImFsbCJdfQ.QmpbK2oeYxjRAhtREj5s7CTzv31s7Ari9RC6e-fGebg");
        //EzSaaSSDK.getInstance().setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiI5MDg1NiIsInNjb3BlIjpbImFsbCJdLCJ1c2VyVHlwZSI6ImVudGVycHJpc2UiLCJleHAiOjE1NTMzMDcyMzgsInNhYXNVc2VySWQiOjkwODU2LCJqdGkiOiIwNTYzYjYzMC00ZGEzLTRlZDAtYTc2NC1jYjA4MzJlMjUxNGEiLCJjbGllbnRfaWQiOiJ5czdBcHAifQ.kXgVzGaYSlCvxBm4eCOsUwVXgFbZLtTHi5U6wibSDEM");
        LG.e("EzSaaSSDK.getInstance().setToken======================");
        //PermissionManager.getInstance().setPermissionStrategy(null);
    }
}
