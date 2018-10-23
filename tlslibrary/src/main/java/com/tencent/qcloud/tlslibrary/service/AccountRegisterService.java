package com.tencent.qcloud.tlslibrary.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.qcloud.tlslibrary.R;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.utils.OkUtils;
import com.tencent.qcloud.tlslibrary.utils.SharedUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by dgy on 15/8/13.
 */
public class AccountRegisterService implements View.OnClickListener {

    private final static String TAG = "AccountRegisterService";
    private final String LOGIN_PSW = "123456789";

    private Context context;
    private EditText mobile;
    private EditText code;
    private EditText psw;
    private EditText yqm;
    private Button send_code;
    private Button btn_register;
    private TextView miao;
    private TLSService tlsService;
    private StrAccRegListener strAccRegListener;
    private String username;
    private String password;
    private int a = 60;
    private String duanxin;
    private String yaoqingma;
    private String s;

    public AccountRegisterService(Context context,
                                  EditText mobile,
                                  EditText code,
                                  EditText psw,
                                  EditText yqm,
                                  Button send_code,
                                  Button btn_register,
                                  TextView miao
    ) {
        this.miao = miao;
        this.context = context;
        this.mobile = mobile;
        this.code = code;
        this.psw = psw;
        this.yqm = yqm;
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
        duanxin = AccountRegisterService.this.code.getText().toString();
        yaoqingma = AccountRegisterService.this.yqm.getText().toString();
        int i = view.getId();
        if (i == R.id.send_code) {
            boolean mobileNO = isMobileNO(username);
            if (username.equals("")) {
                Util.showToast(AccountRegisterService.this.context, "请输入手机号");
            } else if (!mobileNO){
                Util.showToast(AccountRegisterService.this.context,"手机号格式输入有误");
            } else {
                Util.showToast(AccountRegisterService.this.context, "验证码已发送");
                send_code.setVisibility(View.GONE);
                miao.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int j=0;j<60;j++){
                            try {
                                Thread.sleep(1000);
                                hand.sendEmptyMessage(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
        if (i == R.id.btn_register) {

            if (username.length() == 0 || password.length() == 0) {
                Util.showToast(AccountRegisterService.this.context, "用户名密码不能为空");
                return;
            }

            s = username + "a";
            int result = tlsService.TLSStrAccReg(s, LOGIN_PSW, strAccRegListener);
            if (result == TLSErrInfo.INPUT_INVALID) {
                Util.showToast(AccountRegisterService.this.context, "帐号不合法");
            }
        }
    }

    class StrAccRegListener implements TLSStrAccRegListener {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo userInfo) {
            Util.showToast(context, "成功注册 " + userInfo.identifier);

            HashMap<String,String> map = new HashMap<>();
            map.put("mobile",username);
            map.put("pwd",password);
            //陈哥接口 192.168.1.130:8081
            //小东接口 192.168.1.111:1003
            OkUtils.UploadSJ("http://www.taogt.cn/app/check_login", map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "onFailure: "+e.getLocalizedMessage() );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                            Log.e(TAG, "onResponse: "+response.body().string() );
                    String string = response.body().string();
                    Message mess = hand.obtainMessage();
                    mess.obj=string;
                    mess.what=200;
                    hand.sendMessage(mess);
                }
            });


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

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                a--;
                miao.setText(a + "秒");
                if (a == 0) {
                    miao.setVisibility(View.GONE);
                    send_code.setVisibility(View.VISIBLE);
                    a=60;
                }
            }
            if (msg.what == 200) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    Log.e(TAG, "jsonObject: " + jsonObject);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String msg1 = data.getString("msg");
                    if ("0".equals(msg1)){
                        String sessionId = data.getString("sessionId");
                        String token = data.getString("token");
                        String mobile = data.getString("mobile");
                        String id = data.getString("id");
                        SharedUtils sharedUtils = new SharedUtils();
                        Log.e(TAG, "handleMessage: "+sessionId );
                        sharedUtils.saveShared("sessionId","sessionId="+sessionId,context);
                        sharedUtils.saveShared("token",token,context);
                        sharedUtils.saveShared("mobile",mobile,context);
                        sharedUtils.saveShared("zxcid",id,context);
                        username = username + "a";
                        sharedUtils.saveShared("id",username,context);
                        TLSService.getInstance().setLastErrno(0);
                        StrAccountLogin login = new StrAccountLogin(context);
                        login.doStrAccountLogin(s, LOGIN_PSW);
                    }else if ("1".equals(msg1)){
                        Util.showToast(context, "用户不存在");
                    }else if ("2".equals(msg1)){
                        Util.showToast(context,"账号或密码不正确");
                    }else if ("3".equals(msg1)){
                        Util.showToast(context,"用户已被禁止登录");
                    }else {
                        Util.showToast(context,"参数不齐");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }
        else return mobiles.matches(telRegex);
    }

}
