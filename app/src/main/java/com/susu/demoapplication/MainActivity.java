package com.susu.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.susu.baselibrary.location.StartTracingActivity;
import com.susu.baselibrary.image.RoundedCornerTransformer;
import com.susu.baselibrary.speech.SpeechTestActivity;
import com.susu.baselibrary.web.WebViewActivity;
import com.susu.demoapplication.basetest.BaseTestActivity;
import com.susu.demoapplication.test.anim.AnimActivity;
import com.susu.demoapplication.test.CustomViewTestActivity;
import com.susu.demoapplication.test.KotlinTestActivity;
import com.susu.demoapplication.network.OkHttpClientImpl;
import com.susu.demoapplication.test.recyclerview.RecyclerViewActivity;
import com.susu.baselibrary.utils.JumpUtils;
import com.susu.baselibrary.utils.base.ThreadUtils;
import com.susu.baselibrary.utils.base.PermissionUtils;
import com.susu.baselibrary.utils.ui.DisplayUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;
import com.susu.demoapplication.test.recyclerview.ScrollRecyclerViewActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements PermissionUtils.PermissionCallbacks {


    private String url = "https://www.baidu.com";
    private static final String TAG = "WyHttpMonitor";
    private TextView tv;
    private ImageView iv1;
    private ImageView iv2;
    private double a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, BaseTestActivity.class);
            }
        });
        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, KotlinTestActivity.class);
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, RecyclerViewActivity.class);
            }
        });
        findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, CustomViewTestActivity.class);
            }
        });
        findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, AnimActivity.class);
            }
        });
        webViewClick();
        speechTestClick();
        scrollRecyclerClick();
        tracingActivityClick();
    }


    private void tracingActivityClick() {
        findViewById(R.id.tv8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, StartTracingActivity.class);
            }
        });
    }

    private void scrollRecyclerClick() {
        findViewById(R.id.tv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, ScrollRecyclerViewActivity.class);
            }
        });
    }

    private void webViewClick() {
        findViewById(R.id.tv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.goWeb(MainActivity.this,
                        "file:///android_asset/demo2.html");
            }
        });
    }

    private void speechTestClick() {
        findViewById(R.id.tv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.jumpToActivity(MainActivity.this, SpeechTestActivity.class);
            }
        });
    }


    private void requestPermission() {
        if (PermissionUtils.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtils.showToast("有权限");
        } else {
            PermissionUtils.requestPermissions(this, "需要权限",
                    1, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onPermissionsGranted(List<String> list) {
        ToastUtils.showToast("权限允许");
    }

    @Override
    public void onPermissionsDenied(List<String> list) {
        ToastUtils.showToast("权限拒绝");
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18866668888"));
    }


    private void testPlugin() {
//        for (int i = 0; i < 2; i++) {
        ThreadUtils.getDefaultThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                request();
            }
        });
//        }
    }


    private void request() {
        Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        try {

            final JSONObject jsonObject = OkHttpClientImpl.getInstance().postReq(request);
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(jsonObject.toString());
                    Log.v(TAG, jsonObject.toString());
                }
            });

        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        addPermission(permissions, Manifest.permission.ACCESS_COARSE_LOCATION);
        addPermission(permissions, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 前台服务权限
            addPermission(permissions, Manifest.permission.FOREGROUND_SERVICE);
        }

        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }


    private void initIv() {

//        RequestOptions myOptions = new RequestOptions()
////                .downsample(DownsampleStrategy.FIT_CENTER)
//                .transform(new RoundedCornerTransformer(dip2px(this, 12),
//                        true, false, true, false, ScaleMode.FIT_CENTER));
        RequestOptions myOptions = new RequestOptions()
//                .downsample(DownsampleStrategy.FIT_CENTER)
                .transforms(new FitCenter(), new RoundedCornerTransformer(DisplayUtils.dip2px(this, 12), true, false, true, false));

        Glide.with(this)
                .load(R.drawable.abc)
                .apply(myOptions)
                .into(iv1);
        Glide.with(this)
                .load(R.drawable.xyz)
                .apply(myOptions)
                .into(iv2);


    }


}
