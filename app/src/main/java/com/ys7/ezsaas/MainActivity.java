package com.ys7.ezsaas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.ys7.enterprise.core.application.EzSaaSSDK;
import com.ys7.enterprise.core.broadcast.EzAction;
import com.ys7.enterprise.core.http.constant.HttpCache;
import com.ys7.enterprise.core.http.response.app.DeviceBean;
import com.ys7.enterprise.core.http.response.app.MessageBean;
import com.ys7.enterprise.core.http.response.app.MessageDateBean;
import com.ys7.enterprise.core.router.LinkingNavigator;
import com.ys7.enterprise.core.ui.widget.EZDialog;
import com.ys7.enterprise.core.util.LG;
import com.ys7.enterprise.core.util.SPUtil;
import com.ys7.enterprise.core.util.UIUtil;
import com.ys7.enterprise.video.ui.cloudvideo.CloudVideoBean;

import java.util.Calendar;

import butterknife.BindView;


public class MainActivity extends AppCompatActivity {

    private static final int TARGET_PLAY = 0;
    private static final int TARGET_SETS = 1;
    private static final int TARGET_LOCAL = 2;
    private static final int TARGET_CLOUD = 3;
    private BroadcastReceiver receiver;
    private TextView tvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action)) {
                    switch (action) {
                        case EzAction.ACTION_DEVICE_ADDED:
                            //设备添加成功
                            String deviceSerial = intent.getStringExtra(LinkingNavigator.Extras.DEVICE_SERIAL);
                            LG.e("deviceSerial=========="+deviceSerial);
                            UIUtil.toast("设备添加成功"+deviceSerial);
                            break;
                        case EzAction.ACTION_TOKEN_EXPIRED:
                            //token过期,需设置新的token
                            //EzSaaSSDK.getInstance().setToken(TOKEN);
                            SPUtil.setStringValue("tvToken",null);
                            UIUtil.toast("token过期,需设置新的token");
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EzAction.ACTION_DEVICE_ADDED);
        intentFilter.addAction(EzAction.ACTION_TOKEN_EXPIRED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        tvToken = findViewById(R.id.tvToken);

        if (!TextUtils.isEmpty(SPUtil.getStringValue("tvToken",null))){
            EzSaaSSDK.getInstance().setToken(SPUtil.getStringValue("tvToken",null));
            tvToken.setText(SPUtil.getStringValue("tvToken",null));
        }

        findViewById(R.id.btnCapture1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EzSaaSSDK.getInstance().openAddDevicePage(true);
            }
        });
        findViewById(R.id.btnCapture2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EzSaaSSDK.getInstance().openAddDevicePage(false);
            }
        });
        findViewById(R.id.btnDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(TARGET_PLAY);
            }
        });
        findViewById(R.id.btnPlaybackLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(TARGET_LOCAL);
            }
        });
        findViewById(R.id.btnPlaybackCloud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(TARGET_CLOUD);
            }
        });
        findViewById(R.id.btnDeviceSets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(TARGET_SETS);
            }
        });
        findViewById(R.id.btnAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EzSaaSSDK.getInstance().openAlbumPage();
            }
        });

        findViewById(R.id.btnToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTokenInputDialog();
            }
        });



    }

    private void showTokenInputDialog(){
        final View inputView = View.inflate(this,R.layout.token_input_layout,null);
        final EditText etToken = inputView.findViewById(R.id.etToken);
        EZDialog.Builder builder = new EZDialog.Builder(this);
        builder.setView(inputView);
        builder.setTitle("设置token");
        builder.setPositiveButton(R.string.ys_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String token = etToken.getText().toString();
                if (!TextUtils.isEmpty(token)){
                    EzSaaSSDK.getInstance().setToken(token);
                    tvToken.setText(token);
                    SPUtil.setStringValue("tvToken",token);
                }
            }
        });

        builder.setNegativeButton(R.string.ys_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void showInputDialog(final int target){
            final View inputView = View.inflate(this,R.layout.item_input_layout,null);
            final EditText etSeriesNumber = inputView.findViewById(R.id.etSeriesNumber);
            EZDialog.Builder builder = new EZDialog.Builder(this);
            builder.setView(inputView);
            builder.setPositiveButton(R.string.ys_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String deviceSerial = etSeriesNumber.getText().toString();
                    String channelNo = ((EditText)inputView.findViewById(R.id.etChannelNumber)).getText().toString();
                    if (TextUtils.isEmpty(deviceSerial) || TextUtils.isEmpty(channelNo)){
                        return;
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSeriesNumber.getWindowToken(), 0);
                    DeviceBean deviceBean = new DeviceBean();
                    deviceBean.deviceSerial = deviceSerial;
                    deviceBean.channelNo = Integer.parseInt(channelNo);
                    switch (target){
                        case TARGET_PLAY:
                            EzSaaSSDK.getInstance().openLivePlayPage(deviceBean,true);
                            break;
                        case TARGET_SETS:
                            EzSaaSSDK.getInstance().openDeviceSetsPage(deviceSerial,Integer.parseInt(channelNo));
                            break;
                        case TARGET_LOCAL:
                            EzSaaSSDK.getInstance().openPlayBackLocalPage(deviceSerial,Integer.parseInt(channelNo),null, Calendar.getInstance());
                            break;
                        case TARGET_CLOUD:
                            EzSaaSSDK.getInstance().openPlayBackCloudPage(deviceSerial,Integer.parseInt(channelNo),null,Calendar.getInstance());
                            break;
                    }
                }
            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSeriesNumber.getWindowToken(), 0);
                }
            });

            builder.setNegativeButton(R.string.ys_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSeriesNumber.getWindowToken(), 0);
                }
            });
            builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
