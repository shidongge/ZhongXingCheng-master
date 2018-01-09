package us.mifeng.zhongxingcheng.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.fragment.ShouCang_DianPu;
import us.mifeng.zhongxingcheng.fragment.ShouCang_ShangPin;
import us.mifeng.zhongxingcheng.utils.JiaMi;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.SharedUtils;

/**
 * Created by shido on 2017/11/24.
 */

public class ShouCang extends FragmentActivity implements View.OnClickListener {
    private String token, zxcid, substring;
    private FragmentManager fm;
    private ShouCang_DianPu shouCang_dianPu;
    private ShouCang_ShangPin shouCang_shangPin;
    private LinearLayout shangpin, pinpai;
    private TextView shangpin_text, shangpin_number, shangpin_kuohao, pinpai_text, pinpai_number, pinpai_kuohao;
    private ImageView back;
    private static final String TAG = "ShouCang";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxsc_shoucang);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        shouCang_shangPin = new ShouCang_ShangPin();
        ft.add(R.id.shoucang_da, shouCang_shangPin);
        ft.commit();
        initView();
        initList();
    }

    private void initList() {
        SharedUtils sharedUtils = new SharedUtils();
        zxcid = sharedUtils.getShared("zxcid", ShouCang.this);
        String id = sharedUtils.getShared("id",ShouCang.this);
        token = sharedUtils.getShared("token", ShouCang.this);
        String newid = id;
        substring = newid.substring(0, 11);
        HashMap<String, String> map1 = new HashMap<>();

        map1.put("user_id", zxcid);
        map1.put("user_token", token);
        map1.put("user_mobile", substring);

        JSONObject jsonObject = new JSONObject(map1);
        String string1 = jsonObject.toString();
        final String s = JiaMi.jdkBase64Encoder(string1);
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("secret", s);

        OkUtils.UploadSJ("http://192.168.1.123:1002/Api_My/appShopMyfavorite", map2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getLocalizedMessage() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                String string = response.body().string();
                Message mess = hand.obtainMessage();
                mess.obj=string;
                mess.what=100;
                hand.sendMessage(mess);
            }
        });
    }

    private void initView() {
        shangpin = (LinearLayout) findViewById(R.id.shoucang_shangpin);
        shangpin_text = (TextView) findViewById(R.id.shoucang_shangpin_text);
        shangpin_number = (TextView) findViewById(R.id.shoucang_shangpin_number);
        shangpin_kuohao = (TextView) findViewById(R.id.shoucang_shangpin_kuohao);
        pinpai = (LinearLayout) findViewById(R.id.shoucang_pinpai);
        pinpai_text = (TextView) findViewById(R.id.shoucang_pinpai_text);
        pinpai_number = (TextView) findViewById(R.id.shoucang_pinpai_number);
        pinpai_kuohao = (TextView) findViewById(R.id.shoucang_pinpai_kuohao);
        back = (ImageView) findViewById(R.id.yhq_back);
        pinpai.setOnClickListener(this);
        shangpin.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        setText();
        hintFragment();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.shoucang_shangpin:
                shangpin.setBackgroundColor(Color.parseColor("#777777"));
                shangpin_text.setTextColor(Color.parseColor("#ffffff"));
                shangpin_number.setTextColor(Color.parseColor("#ffffff"));
                shangpin_kuohao.setTextColor(Color.parseColor("#ffffff"));
                if (shouCang_shangPin == null) {
                    ft.add(R.id.shoucang_da, shouCang_shangPin);
                } else {
                    ft.show(shouCang_shangPin);
                }
                break;
            case R.id.shoucang_pinpai:
                pinpai.setBackgroundColor(Color.parseColor("#777777"));
                pinpai_text.setTextColor(Color.parseColor("#ffffff"));
                pinpai_number.setTextColor(Color.parseColor("#ffffff"));
                pinpai_kuohao.setTextColor(Color.parseColor("#ffffff"));
                if (shouCang_dianPu == null) {
                    shouCang_dianPu = new ShouCang_DianPu();
                    ft.add(R.id.shoucang_da, shouCang_dianPu);
                } else {
                    ft.show(shouCang_dianPu);
                }
                break;

        }
        ft.commit();
    }

    private void setText() {
        shangpin_text.setTextColor(Color.parseColor("#000000"));
        shangpin_number.setTextColor(Color.parseColor("#000000"));
        shangpin_kuohao.setTextColor(Color.parseColor("#000000"));
        pinpai_text.setTextColor(Color.parseColor("#000000"));
        pinpai_number.setTextColor(Color.parseColor("#000000"));
        pinpai_kuohao.setTextColor(Color.parseColor("#000000"));
        shangpin.setBackgroundColor(Color.parseColor("#ffffff"));
        pinpai.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    public void hintFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (shouCang_dianPu != null) {
            ft.hide(shouCang_dianPu);
        }
        if (shouCang_shangPin != null) {
            ft.hide(shouCang_shangPin);
        }
        ft.commit();
    }
    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==100){
                String str= (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String shopsCount = jsonObject.getString("shopsCount");
                    String goodsCount = jsonObject.getString("goodsCount");
                    shangpin_number.setText(goodsCount);
                    pinpai_number.setText(shopsCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}