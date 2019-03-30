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
    }
}
