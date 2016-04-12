package com.vann.csdn.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vann.csdn.util.ActivityCollector;

/**
 * author： bwl on 2016-03-24.
 * email: bxl049@163.com
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
