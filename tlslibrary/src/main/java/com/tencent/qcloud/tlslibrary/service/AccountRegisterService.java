package com.tencent.qcloud.tlslibrary.service;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.qcloud.tlslibrary.R;
import com.tencent.qcloud.tlslibrary.helper.Util;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by dgy on 15/8/13.
 */
public class AccountRegisterService implements View.OnClickListener {

    private final static String TAG = "AccountRegisterService";

    private Context context;
    private EditText mobile;
    private EditText code;
    private EditText psw;
    private EditText psw2;
    private Button send_code;
    private Button btn_register;
    private TLSService tlsService;
    private StrAccRegListener strAccRegListener;
    private String username;
    private String password;

    public AccountRegisterService(Context context,
                                  EditText mobile,
                                  EditText code,
                                  EditText psw,
                                  EditText psw2,
                                  Button send_code,
                                  Button btn_register) {
        this.context = context;
        this.mobile = mobile;
        this.code = code;
        this.psw = psw;
        this.psw2 = psw2;
        this.send_code = send_code;
        this.btn_register = btn_register;

        tlsService = TLSService.getInstance();
        strAccRegListener = new StrAccRegListener();
        send_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        username = AccountRegisterService.this.mobile.getText().toString();
        password = AccountRegisterService.this.psw.getText().toString();
        String tmp = AccountRegisterService.this.psw2.getText().toString();
        int i = view.getId();
        if (i == R.id.send_code) {

        }
        if (i == R.id.btn_register) {
            if (username.length() == 0 || password.length() == 0 || tmp.length() == 0) {
                Util.showToast(AccountRegisterService.this.context, "用户名密码不能为空");
                return;
            }

            if (!password.equals(tmp)) {
                Util.showToast(AccountRegisterService.this.context, "两次输入的密码不一致");
                return;
            }

            if (password.length() < 8) {
                Util.showToast(AccountRegisterService.this.context, "密码的长度不能小于8个字符");
            }

            int result = tlsService.TLSStrAccReg(username, password, strAccRegListener);
            if (result == TLSErrInfo.INPUT_INVALID) {
                Util.showToast(AccountRegisterService.this.context, "帐号不合法");
            }
        }
    }

    class StrAccRegListener implements TLSStrAccRegListener {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo userInfo) {
            Util.showToast(context, "成功注册 " + userInfo.identifier);
            TLSService.getInstance().setLastErrno(0);
            StrAccountLogin login = new StrAccountLogin(context);
            login.doStrAccountLogin(username, password);
        }

        @Override
        public void OnStrAccRegFail(TLSErrInfo errInfo) {
            Util.notOK(context, errInfo);
        }

        @Override
        public void OnStrAccRegTimeout(TLSErrInfo errInfo) {
            Util.notOK(context, errInfo);
        }
    }
}
