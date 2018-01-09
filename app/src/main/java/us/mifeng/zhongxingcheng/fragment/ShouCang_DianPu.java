package us.mifeng.zhongxingcheng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import us.mifeng.zhongxingcheng.adapter.SC_DPAdapter;
import us.mifeng.zhongxingcheng.bean.ShouCangBean;
import us.mifeng.zhongxingcheng.utils.JiaMi;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.SharedUtils;

/**
 * Created by shido on 2017/11/24.
 */

public class ShouCang_DianPu extends Fragment {
    private static final String TAG = "ShouCang_DianPu";
    private ListView lv;
    private String token, zxcid, substring;
    private SC_DPAdapter sc_dpAdapter;
    private List<ShouCangBean.ShopsCollectionBean> shopsCollectionlist;
    private ShouCangBean.ShopsCollectionBean shopsCollectionBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = View.inflate(getActivity(), R.layout.fragment_shoucang_dianpu, null);
        lv = (ListView) inflate.findViewById(R.id.sc_dianpu_lv);
        shopsCollectionlist = new ArrayList<>();
        initList();
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
                    JSONArray shopsCollection = jsonObject.getJSONArray("shopsCollection");
                    for (int i = 0; i < shopsCollection.length(); i++) {
                        JSONObject jsonObject1 = shopsCollection.getJSONObject(i);
                        shopsCollectionBean = new ShouCangBean.ShopsCollectionBean();
                        shopsCollectionBean.setId(jsonObject1.getString("id"));
                        shopsCollectionBean.setType(jsonObject1.getString("type"));
                        shopsCollectionBean.setShopId(jsonObject1.getString("shopId"));
                        shopsCollectionBean.setGoodsId(jsonObject1.getString("goodsId"));
                        shopsCollectionBean.setUserId(jsonObject1.getString("userId"));
                        shopsCollectionBean.setAddTime(jsonObject1.getString("addTime"));
                        shopsCollectionBean.setUpdTime(jsonObject1.getString("updTime"));
                        shopsCollectionBean.setStatus(jsonObject1.getString("status"));
                        shopsCollectionBean.setImgIcon(jsonObject1.getString("imgIcon"));
                        shopsCollectionBean.setShopName(jsonObject1.getString("shopName"));
                        shopsCollectionBean.setSellCount(jsonObject1.getString("sellCount"));
                        shopsCollectionBean.setGoodsCount(jsonObject1.getString("goodsCount"));
                        shopsCollectionlist.add(shopsCollectionBean);
                    }
                    if (sc_dpAdapter == null) {
                        sc_dpAdapter = new SC_DPAdapter(shopsCollectionlist, getActivity());
                        lv.setAdapter(sc_dpAdapter);
                    } else {
                        sc_dpAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
