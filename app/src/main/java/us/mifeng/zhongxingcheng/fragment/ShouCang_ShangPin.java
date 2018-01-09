package us.mifeng.zhongxingcheng.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.activity.ZXSC_Android;
import us.mifeng.zhongxingcheng.adapter.SC_SPAdapter;
import us.mifeng.zhongxingcheng.bean.ShouCangBean;
import us.mifeng.zhongxingcheng.utils.JiaMi;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.SharedUtils;

/**
 * Created by shido on 2017/11/24.
 */

public class ShouCang_ShangPin extends Fragment implements View.OnClickListener {

    private LinearLayout zero;
    private ListView lv;
    private List<ShouCangBean.GoodsCollectionBean> goodsCollectionlist;
    private String token, zxcid, substring;
    private static final String TAG = "ShouCang_ShangPin";
    private SC_SPAdapter sc_spAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.fragment_shoucang_shangpin, null);
        zero = (LinearLayout) inflate.findViewById(R.id.sc_sp_zero);
        lv = (ListView) inflate.findViewById(R.id.sc_sp_lv);
        goodsCollectionlist = new ArrayList<>();
        initList();
        
        
        TextView guangguang = (TextView) inflate.findViewById(R.id.shangpin_gg);
        guangguang.setTextColor(Color.parseColor("#00ff00"));
        guangguang.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        guangguang.setOnClickListener(this);
        return inflate;
    }

    public void initList() {
        SharedUtils sharedUtils = new SharedUtils();
        zxcid = sharedUtils.getShared("zxcid", getActivity());
        String id = sharedUtils.getShared("id", getActivity());
        token = sharedUtils.getShared("token", getActivity());
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
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                String string = response.body().string();
                Message mess = hand.obtainMessage();
                mess.obj = string;
                mess.what = 100;
                hand.sendMessage(mess);
            }
        });
    }
    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray goodsCollection = jsonObject.getJSONArray("goodsCollection");
                    int length = goodsCollection.length();
                    if (length==0){
                        zero.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }else {
                        zero.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                        for (int i = 0; i < goodsCollection.length(); i++) {
                            JSONObject jsonObject1 = goodsCollection.getJSONObject(i);
                            ShouCangBean.GoodsCollectionBean goodsCollectionBean = new ShouCangBean.GoodsCollectionBean();
                            goodsCollectionBean.setId(jsonObject1.getString("id"));
                            goodsCollectionBean.setType(jsonObject1.getString("type"));
                            goodsCollectionBean.setShopId(jsonObject1.getString("shopId"));
                            goodsCollectionBean.setGoodsId(jsonObject1.getString("goodsId"));
                            goodsCollectionBean.setUserId(jsonObject1.getString("userId"));
                            goodsCollectionBean.setAddTime(jsonObject1.getString("addTime"));
                            goodsCollectionBean.setUpdTime(jsonObject1.getString("updTime"));
                            goodsCollectionBean.setStatus(jsonObject1.getString("status"));
                            goodsCollectionBean.setImgUrl(jsonObject1.getString("imgUrl"));
                            goodsCollectionBean.setGoodsName(jsonObject1.getString("goodsName"));
                            goodsCollectionBean.setShortDesc(jsonObject1.getString("shortDesc"));
                            goodsCollectionBean.setGoodsMoney(jsonObject1.getString("goodsMoney"));
                            goodsCollectionlist.add(goodsCollectionBean);
                        }
                        if (sc_spAdapter == null) {
                            sc_spAdapter = new SC_SPAdapter(goodsCollectionlist, getActivity());
                            lv.setAdapter(sc_spAdapter);
                        } else {
                            sc_spAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shangpin_gg:
                Intent intent = new Intent(getActivity(),ZXSC_Android.class);
                intent.putExtra("page",1);
                startActivity(intent);
                break;
        }
    }
}
