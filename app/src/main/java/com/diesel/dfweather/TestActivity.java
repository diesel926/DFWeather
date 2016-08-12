package com.diesel.dfweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

    public void myGroup(View v) {
        OkHttpUtils
                .get()
                .url("http://172.16.131.46:8080/appserver/my_group_list")
                .addParams("ts", "1470970554")
                .addParams("cs", "f8f0128613efd8ca47d0f87324f45155")
                .addParams("uid", "aad7bd22419e11e59ef7086266852939")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "myGroup#onError() id("+id+"), error="+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "myGroup#onResponse() id("+id+"), response:\n"+response);
            }
        });

        throw new RuntimeException("仅仅只是为了测试");
    }
}
