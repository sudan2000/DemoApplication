package com.susu.baselibrary.location;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.susu.baselibrary.R;
import com.susu.baselibrary.location.util.PermissionUtil;
import com.susu.baselibrary.utils.base.LogUtils;
import com.susu.baselibrary.utils.ui.ToastUtils;

/**
 * Author : sudan
 * Time : 2021/11/29
 * Description:
 */
public class StartTracingActivity extends Activity implements View.OnClickListener {

    private static final String ENTITY_NAME = "MyTrace";

    private LocationManager mLocationManager = LocationManager.getInstance(ENTITY_NAME);
    private TextView mTvTrace;
    private TextView mTvTraceStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_activity_baidu_start_tracing);
        mTvTrace = findViewById(R.id.btn_trace);
        mTvTrace.setOnClickListener(this);
        mTvTraceStop = findViewById(R.id.btn_trace_stop);
        mTvTraceStop.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_trace) {

            LogUtils.d("test---", "click button Trace-----");
            mLocationManager.startTrace(this, new StartTraceListener() {
                @Override
                public void onTraceStartSuccess() {
                    ToastUtils.showToast("开启成功");
                    mTvTrace.setText("开启成功");

                }

                @Override
                public void onTraceStartFailed() {
                    ToastUtils.showToast("开启失败");
                    mTvTrace.setText("开启失败");
                }
            });


        } else if (view.getId() == R.id.btn_trace_stop) {
            LogUtils.d("test---", "click button Stop Trace-----");
            mLocationManager.stopTrace(new StopTraceListener() {
                @Override
                public void onStopTraceSuccess() {

                }

                @Override
                public void onStopTraceFailed() {

                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        PermissionUtil.requestPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionUtil.requestBackgroundLocationPermission(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.onDestroy();
    }


}
