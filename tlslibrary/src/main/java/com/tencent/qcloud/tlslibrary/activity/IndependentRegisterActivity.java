package com.tencent.qcloud.tlslibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.qcloud.tlslibrary.helper.MResource;
import com.tencent.qcloud.tlslibrary.service.TLSService;

public class IndependentRegisterActivity extends Activity {

    public final static String TAG = "IndependentRegisterActivity";
    private TLSService tlsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(getApplication(), "layout", "tencent_tls_ui_activity_independent_register"));

        tlsService = TLSService.getInstance();
        tlsService.initAccountRegisterService(this,
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "register_phone")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "register_code")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "register_psw")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "register_psw2")),
                (Button) findViewById(MResource.getIdByName(getApplication(), "id", "send_code")),
                (Button) findViewById(MResource.getIdByName(getApplication(), "id", "btn_register")),
                (TextView) findViewById(MResource.getIdByName(getApplication(),"id","register_miao"))
        );

        // 设置返回按钮
        findViewById(MResource.getIdByName(getApplication(), "id", "returnIndependentLoginActivity"))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IndependentRegisterActivity.this.onBackPressed();
                    }
                });
    }
}
