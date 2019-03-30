package com.ys7.ezsaas;

import android.support.multidex.MultiDexApplication;
import com.ys7.enterprise.core.application.EzSaaSSDK;

/**
 * @author xiezuoyuan
 * @time 2019/3/20 13:41
 */
public class EzApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        EzSaaSSDK.openDebug(true);
        EzSaaSSDK.init(this,"APP_KEY");
    }
}
