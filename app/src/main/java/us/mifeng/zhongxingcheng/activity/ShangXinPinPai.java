package us.mifeng.zhongxingcheng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import us.mifeng.zhongxingcheng.adapter.SXPPAdapter;
import us.mifeng.zhongxingcheng.bean.SXPPBean;
import us.mifeng.zhongxingcheng.utils.JiaMi;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.ToSi;
import us.mifeng.zhongxingcheng.utils.WangZhi;

/**
 * Created by shido on 2017/11/16.
 */

/**
 * 上新品牌界面
 */
public class ShangXinPinPai extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {
    public ListView mLv;
    private List<SXPPBean.DataBean> list;
    private List<SXPPBean.DataBean.GoodsInfoBean> shopslist;
    private View inflate;
    private ProgressBar mBar;
    private static final String TAG = "ShangXinPinPai";
    private int index = 0;
    private SXPPAdapter sxppAdapter;
    private int lastVisIdnex;
    private int page;
    private int page_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sxpp);
        inflate = View.inflate(ShangXinPinPai.this, R.layout.footerview, null);
        mBar = (ProgressBar) inflate.findViewById(R.id.mBar);
        initView();
        list = new ArrayList<>();

        mLv.addFooterView(inflate);
        initList();
        mLv.setOnScrollListener(this);
    }

    private void initList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", "" + index++);
        JSONObject jsonObject = new JSONObject(map);
        String string = jsonObject.toString();
        String s = JiaMi.jdkBase64Encoder(string);
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("secret", s);

        OkUtils.UploadSJ(WangZhi.SXPP, map1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message message = hand.obtainMessage();
                message.obj = string;
                message.what = 100;
                hand.sendMessage(message);
            }
        });
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.sxpp_mlv);
        ImageView back = (ImageView) findViewById(R.id.sxpp_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sxpp_back:
                finish();
                break;
            default:
                break;
        }
    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    page = jsonObject.getInt("page");
                    Log.e(TAG, "handleMessage: page" + page);
                    page_count = jsonObject.getInt("page_count");
                    Log.e(TAG, "handleMessage: page_count" + page_count);
                    if (page_count == page) {
                        mBar.setVisibility(View.GONE);
                        ToSi.show(ShangXinPinPai.this, "没有更多数据了");
                    } else {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String id1 = jsonObject1.getString("id");
                            SXPPBean.DataBean dataBean = new SXPPBean.DataBean();
                            dataBean.setAudeType(jsonObject1.getString("audeType"));
                            dataBean.setGoodsCount(jsonObject1.getString("goodsCount"));
                            dataBean.setImgIcon(jsonObject1.getString("imgIcon"));
                            dataBean.setSellCount(jsonObject1.getString("sellCount"));
                            dataBean.setShopName(jsonObject1.getString("shopName"));
                            dataBean.setId(id1);
                            JSONArray goodsInfo = jsonObject1.getJSONArray("goodsInfo");
                            shopslist = new ArrayList<>();
                            for (int j = 0; j < goodsInfo.length(); j++) {
                                SXPPBean.DataBean.GoodsInfoBean goodsInfoBean = new SXPPBean.DataBean.GoodsInfoBean();
                                JSONObject jsonObject2 = goodsInfo.getJSONObject(j);

                                String shopId = jsonObject2.getString("shopId");

                                String goodsMoney_old = jsonObject2.getString("goodsMoney_old");

                                goodsInfoBean.setImgCart(jsonObject2.getString("imgCart"));
                                goodsInfoBean.setId(jsonObject2.getString("id"));
                                goodsInfoBean.setGoodsMoney(jsonObject2.getString("goodsMoney"));
                                shopslist.add(goodsInfoBean);

                                dataBean.setGoodsInfo(shopslist);
                            }
                            list.add(dataBean);
                        }
                        if (sxppAdapter == null) {
                            sxppAdapter = new SXPPAdapter(list, ShangXinPinPai.this);
                            mLv.setAdapter(sxppAdapter);
                        } else {
                            sxppAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && lastVisIdnex == sxppAdapter.getCount()) {
            //确认滑倒底部 加载更多
            mBar.setVisibility(View.VISIBLE);
            initList();

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastVisIdnex = firstVisibleItem + visibleItemCount - 1;
    }
}