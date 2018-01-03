package us.mifeng.zhongxingcheng.dianpu;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import us.mifeng.zhongxingcheng.bean.ZXSC_DianPuBean;
import us.mifeng.zhongxingcheng.utils.JiaMi;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.SharedUtils;
import us.mifeng.zhongxingcheng.utils.ToSi;
import us.mifeng.zhongxingcheng.utils.WangZhi;

import static us.mifeng.zhongxingcheng.R.id.dp_yihao;

public class DianPuActivity extends FragmentActivity implements View.OnClickListener {
    private String[] mTitles = new String[]{"全部", "销量", "价格", "新品"};
    private RecyclerView youhuiquan;
    private LinearLayout mIndicator, guanzhu,jiage;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];
    private RecyclerView mRecyclerView;
    private List<ZXSC_DianPuBean.GoodsInfoBean> mDatas = new ArrayList<>();
    private List<String> xiaolianglist = new ArrayList<>();
    private TextView yihao, erhao, sanhao, sihao, biaoti, shaixuan;
    private DianPuAdapter dianPuAdapter;
    private ImageView back;
    private static final String TAG = "DianPuActivity";
    private String dianpu;
    private ImageView banner, logo;
    private TextView dianming, shangxin, quanbu, quanbuxiao, xiaoliang, gz_text;
    private String token, zxcid, substring;
    private ImageView img,img_xia,img_shang;
    private ProgressDialog progressDialog;
    private boolean isTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_pu);
        dianpu = getIntent().getStringExtra("dianpu");
        SharedUtils sharedUtils = new SharedUtils();
        zxcid = sharedUtils.getShared("zxcid", DianPuActivity.this);
        String id = sharedUtils.getShared("id", DianPuActivity.this);
        token = sharedUtils.getShared("token", DianPuActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载数据。。。。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String newid = id;
        substring = newid.substring(0, 11);
        initView();
        initLianWang();
        //；粘性布局
        initViews();
        initGuanZhu();
    }

    private void initGuanZhu() {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("type", "2");
        map1.put("shopId", dianpu);
        map1.put("user_id", zxcid);
        map1.put("user_token", token);
        map1.put("user_mobile", substring);

        JSONObject jsonObject = new JSONObject(map1);
        String string1 = jsonObject.toString();
        final String s = JiaMi.jdkBase64Encoder(string1);
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("secret", s);

        OkUtils.UploadSJ(WangZhi.SFGZ, map2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                String string = response.body().string();
                Message message = hand.obtainMessage();
                message.obj = string;
                message.what = 200;
                hand.sendMessage(message);
            }
        });
    }

    private void initView() {
        banner = (ImageView) findViewById(R.id.dianpu_banner);
        logo = (ImageView) findViewById(R.id.dianpu_logo);
        dianming = (TextView) findViewById(R.id.dianpu_dianming);
        shangxin = (TextView) findViewById(R.id.dianpu_shangxin);
        quanbu = (TextView) findViewById(R.id.dianpu_quanbu);
        quanbuxiao = (TextView) findViewById(R.id.shangpu_quanbuxiao);
    }

    private void initLianWang() {
        HashMap<String, String> map = new HashMap<>();
        map.put("shopId", dianpu);
        OkUtils.UploadSJ(WangZhi.ZXSC_DP, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                String string = response.body().string();
                Message message = hand.obtainMessage();
                message.what = 100;
                message.obj = string;
                hand.sendMessage(message);
            }
        });
    }

    private void initViews() {
        yihao = (TextView) findViewById(R.id.dp_yihao);
        erhao = (TextView) findViewById(R.id.dp_erhao);
        sanhao = (TextView) findViewById(R.id.dp_sanhao);
        sihao = (TextView) findViewById(R.id.dp_sihao);
//        shaixuan = (TextView) findViewById(R.id.dp_shaixuan);
        back = (ImageView) findViewById(R.id.title_back);
        biaoti = (TextView) findViewById(R.id.title_text);
        xiaoliang = (TextView) findViewById(R.id.dianpu_xiaoliang);
        guanzhu = (LinearLayout) findViewById(R.id.dianpu_guanzhu);
        jiage = (LinearLayout) findViewById(R.id.dp_jiage);

        img = (ImageView) findViewById(R.id.dianpu_gz_img);
        gz_text = (TextView) findViewById(R.id.dianpu_gz_text);

        img_shang = (ImageView) findViewById(R.id.jiage_img_shang);
        img_xia = (ImageView) findViewById(R.id.jiage_img_xia);

        youhuiquan = (RecyclerView) findViewById(R.id.dianpu_youhuiquan);
        mIndicator = (LinearLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_stickynavlayout_vp);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        biaoti.setText("商家店铺");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        yihao.setOnClickListener(this);
        erhao.setOnClickListener(this);
        jiage.setOnClickListener(this);
        sihao.setOnClickListener(this);
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("type", "2");
                map1.put("shopId", dianpu);
                map1.put("user_id", zxcid);
                map1.put("user_token", token);
                map1.put("user_mobile", substring);

                JSONObject jsonObject = new JSONObject(map1);
                String string1 = jsonObject.toString();
                final String s = JiaMi.jdkBase64Encoder(string1);
                HashMap<String, String> map2 = new HashMap<>();
                map2.put("secret", s);

                if (img.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.mipmap.yigz).getConstantState()) {
                    img.setImageResource(R.mipmap.weigz);
                    gz_text.setText("未关注");
                } else {
                    img.setImageResource(R.mipmap.yigz);
                    gz_text.setText("已关注");
                }

                OkUtils.UploadSJ(WangZhi.ZXSC_GZ, map2, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "onResponse: "+response.body().string());
                        String string = response.body().string();
                        Message mess = hand.obtainMessage();
                        mess.what = 300;
                        mess.obj = string;
                        hand.sendMessage(mess);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        setTextColor();
        switch (v.getId()) {
            case dp_yihao:
                progressDialog.show();
                yihao.setTextColor(Color.parseColor("#ff0000"));
                mDatas.clear();
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("shopId", dianpu);
                OkUtils.UploadSJ(WangZhi.ZXSC_DPLB, map1, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "onResponse: " + response.body().string());
                        String string = response.body().string();
                        Message message = hand.obtainMessage();
                        message.obj = string;
                        message.what = 400;
                        hand.sendMessage(message);
                    }
                });

                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 000" + "点击了哈哈哈");
                    }
                });
                break;

            case R.id.dp_erhao://设置点击事件请求不同的接口,先把上一个list的数据清空，然后在加载这次的数据
                erhao.setTextColor(Color.parseColor("#ff0000"));
                mDatas.clear();
                progressDialog.show();
                final HashMap<String, String> map = new HashMap<>();
                map.put("shopId", dianpu);
                map.put("filter", "ssc");
                OkUtils.UploadSJ(WangZhi.ZXSC_DPLB, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "onResponse: " + response.body().string());
                        String string = response.body().string();
                        Message mess = hand.obtainMessage();
                        mess.obj = string;
                        mess.what = 500;
                        hand.sendMessage(mess);
                    }
                });
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 111111" + "点击了哈哈哈");
                    }
                });
                break;
            case R.id.dp_jiage:
                sanhao.setTextColor(Color.parseColor("#ff0000"));
                mDatas.clear();
                progressDialog.show();
                HashMap<String, String> map2 = new HashMap<>();
                map2.put("shopId", dianpu);
                if (isTag) {

                    img_shang.setImageResource(R.mipmap.jiantou_up);
                    map2.put("filter", "gsc");
                    map2.put("weight", "1");
                    isTag = false;
                } else {

                    img_xia.setImageResource(R.mipmap.jiantou_down);
                    map2.put("filter", "gde");
                    map2.put("weight", "2");
                    isTag = true;
                }
                OkUtils.UploadSJ(WangZhi.ZXSC_DPLB, map2, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "onResponse: "+response.body().string());
                        String string = response.body().string();
                        Message message = hand.obtainMessage();
                        message.obj = string;
                        message.what = 600;
                        hand.sendMessage(message);
                    }
                });
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 222222" + "点击了哈哈哈");
                    }
                });
                break;
            case R.id.dp_sihao:
                sihao.setTextColor(Color.parseColor("#ff0000"));
                mDatas.clear();
                progressDialog.show();
                HashMap<String, String> map4 = new HashMap<>();
                map4.put("shopId", dianpu);
                map4.put("filter", "nsc");
                OkUtils.UploadSJ(WangZhi.ZXSC_DPLB, map4, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Message mess7 = hand.obtainMessage();
                        mess7.what=700;
                        mess7.obj=string;
                        hand.sendMessage(mess7);
                    }
                });
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 333333" + "点击了哈哈哈");
                    }
                });
                break;


        }
    }


    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //进入app请求网络
            if (msg.what == 100) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject info = jsonObject.getJSONObject("info");
                    String shopName = info.getString("shopName");
                    String id = info.getString("id");
                    String imgIcon = info.getString("imgIcon");
                    String imgTop = info.getString("imgTop");
                    String sellCount = info.getString("sellCount");
                    String goodsCount = info.getString("goodsCount");
                    dianming.setText(shopName);
                    Glide.with(DianPuActivity.this).load(WangZhi.DIANPU + imgIcon).into(logo);
                    Glide.with(DianPuActivity.this).load(WangZhi.DIANPU + imgTop).into(banner);
                    quanbu.setText(goodsCount);
                    shangxin.setText(goodsCount);
                    quanbuxiao.setText("共" + goodsCount + "件宝贝");
                    xiaoliang.setText("销量" + sellCount);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    progressDialog.dismiss();
                    for (int i = 0; i < goodsInfo.length(); i++) {
                        JSONObject jsonObject1 = goodsInfo.getJSONObject(i);
                        String id1 = jsonObject1.getString("id");
                        String shopId = jsonObject1.getString("shopId");
                        String goodsName = jsonObject1.getString("goodsName");
                        String shortDesc = jsonObject1.getString("shortDesc");
                        String imgCart = jsonObject1.getString("imgCart");
                        String goodsMoney = jsonObject1.getString("goodsMoney");
                        String goodsMoney1 = jsonObject1.getString("goodsMoney1");
                        String goodsMoneyConsult = jsonObject1.getString("goodsMoneyConsult");
                        String goodsMoney_old = jsonObject1.getString("goodsMoney_old");
                        ZXSC_DianPuBean.GoodsInfoBean goodsInfoBean = new ZXSC_DianPuBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsName(goodsName);
                        goodsInfoBean.setId(id1);
                        goodsInfoBean.setShopId(shopId);
                        goodsInfoBean.setShortDesc(shortDesc);
                        goodsInfoBean.setImgCart(imgCart);
                        goodsInfoBean.setGoodsMoney(goodsMoney);
                        goodsInfoBean.setGoodsMoney1(goodsMoney1);
                        goodsInfoBean.setGoodsMoney_old(goodsMoney_old);
                        goodsInfoBean.setGoodsMoneyConsult(goodsMoneyConsult);
                        mDatas.add(goodsInfoBean);
                    }


                    dianPuAdapter = new DianPuAdapter(DianPuActivity.this, mDatas);
                    mRecyclerView.setAdapter(dianPuAdapter);

                    dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.e(TAG, "onItemClick: 000000" + "点击了哈哈哈");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //查询是否关注
            if (msg.what == 200) {
                String string = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        img.setImageResource(R.mipmap.weigz);
                        gz_text.setText("未关注");
                    } else {
                        img.setImageResource(R.mipmap.yigz);
                        gz_text.setText("已关注");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //点击关注
            if (msg.what == 300) {
                String string = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String success = jsonObject.getString("success");
                    if (success.equals("true")) {

                        ToSi.show(DianPuActivity.this, "关注成功");
                    } else {
                        ToSi.show(DianPuActivity.this, "未关注");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //点击全部
            if (msg.what == 400) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    progressDialog.dismiss();
                    for (int i = 0; i < goodsInfo.length(); i++) {
                        JSONObject jsonObject1 = goodsInfo.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String shopId = jsonObject1.getString("shopId");
                        String goodsName = jsonObject1.getString("goodsName");
                        String shortDesc = jsonObject1.getString("shortDesc");
                        String imgCart = jsonObject1.getString("imgCart");
                        String goodsMoney = jsonObject1.getString("goodsMoney");
                        String goodsMoney1 = jsonObject1.getString("goodsMoney1");
                        String goodsMoneyConsult = jsonObject1.getString("goodsMoneyConsult");
                        String goodsMoney_old = jsonObject1.getString("goodsMoney_old");
                        ZXSC_DianPuBean.GoodsInfoBean goodsInfoBean = new ZXSC_DianPuBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsName(goodsName);
                        goodsInfoBean.setId(id);
                        goodsInfoBean.setShopId(shopId);
                        goodsInfoBean.setShortDesc(shortDesc);
                        goodsInfoBean.setImgCart(imgCart);
                        goodsInfoBean.setGoodsMoney(goodsMoney);
                        goodsInfoBean.setGoodsMoney1(goodsMoney1);
                        goodsInfoBean.setGoodsMoney_old(goodsMoney_old);
                        goodsInfoBean.setGoodsMoneyConsult(goodsMoneyConsult);
                        mDatas.add(goodsInfoBean);
                    }
                    dianPuAdapter = new DianPuAdapter(DianPuActivity.this, mDatas);
                    mRecyclerView.setAdapter(dianPuAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //点击销量
            if (msg.what == 500) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    progressDialog.dismiss();
                    for (int i = 0; i < goodsInfo.length(); i++) {
                        JSONObject jsonObject1 = goodsInfo.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String shopId = jsonObject1.getString("shopId");
                        String goodsName = jsonObject1.getString("goodsName");
                        String shortDesc = jsonObject1.getString("shortDesc");
                        String imgCart = jsonObject1.getString("imgCart");
                        String goodsMoney = jsonObject1.getString("goodsMoney");
                        String goodsMoney1 = jsonObject1.getString("goodsMoney1");
                        String goodsMoneyConsult = jsonObject1.getString("goodsMoneyConsult");
                        String goodsMoney_old = jsonObject1.getString("goodsMoney_old");
                        ZXSC_DianPuBean.GoodsInfoBean goodsInfoBean = new ZXSC_DianPuBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsName(goodsName);
                        goodsInfoBean.setId(id);
                        goodsInfoBean.setShopId(shopId);
                        goodsInfoBean.setShortDesc(shortDesc);
                        goodsInfoBean.setImgCart(imgCart);
                        goodsInfoBean.setGoodsMoney(goodsMoney);
                        goodsInfoBean.setGoodsMoney1(goodsMoney1);
                        goodsInfoBean.setGoodsMoney_old(goodsMoney_old);
                        goodsInfoBean.setGoodsMoneyConsult(goodsMoneyConsult);
                        mDatas.add(goodsInfoBean);
                    }
                    dianPuAdapter = new DianPuAdapter(DianPuActivity.this, mDatas);
                    mRecyclerView.setAdapter(dianPuAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //价格列表
            if (msg.what == 600) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    String field = jsonObject.getString("field");
                    progressDialog.dismiss();
                    for (int i = 0; i < goodsInfo.length(); i++) {
                        JSONObject jsonObject1 = goodsInfo.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e(TAG, "handleMessage: " + id);
                        String shopId = jsonObject1.getString("shopId");
                        String goodsName = jsonObject1.getString("goodsName");
                        String shortDesc = jsonObject1.getString("shortDesc");
                        String imgCart = jsonObject1.getString("imgCart");
                        String goodsMoney = jsonObject1.getString("goodsMoney");
                        String goodsMoney1 = jsonObject1.getString("goodsMoney1");
                        String goodsMoneyConsult = jsonObject1.getString("goodsMoneyConsult");
                        String goodsMoney_old = jsonObject1.getString("goodsMoney_old");
                        ZXSC_DianPuBean.GoodsInfoBean goodsInfoBean = new ZXSC_DianPuBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsName(goodsName);
                        goodsInfoBean.setId(id);
                        goodsInfoBean.setShopId(shopId);
                        goodsInfoBean.setShortDesc(shortDesc);
                        goodsInfoBean.setImgCart(imgCart);
                        goodsInfoBean.setGoodsMoney(goodsMoney);
                        goodsInfoBean.setGoodsMoney1(goodsMoney1);
                        goodsInfoBean.setGoodsMoney_old(goodsMoney_old);
                        goodsInfoBean.setGoodsMoneyConsult(goodsMoneyConsult);
                        mDatas.add(goodsInfoBean);
                    }
                    dianPuAdapter = new DianPuAdapter(DianPuActivity.this, mDatas);
                    mRecyclerView.setAdapter(dianPuAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //价格列表
            if (msg.what == 700) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    String field = jsonObject.getString("field");
                    Log.e(TAG, "handleMessage: " + field);
                    progressDialog.dismiss();
                    for (int i = 0; i < goodsInfo.length(); i++) {
                        JSONObject jsonObject1 = goodsInfo.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e(TAG, "handleMessage: " + id);
                        String shopId = jsonObject1.getString("shopId");
                        String goodsName = jsonObject1.getString("goodsName");
                        String shortDesc = jsonObject1.getString("shortDesc");
                        String imgCart = jsonObject1.getString("imgCart");
                        String goodsMoney = jsonObject1.getString("goodsMoney");
                        String goodsMoney1 = jsonObject1.getString("goodsMoney1");
                        String goodsMoneyConsult = jsonObject1.getString("goodsMoneyConsult");
                        String goodsMoney_old = jsonObject1.getString("goodsMoney_old");
                        ZXSC_DianPuBean.GoodsInfoBean goodsInfoBean = new ZXSC_DianPuBean.GoodsInfoBean();
                        goodsInfoBean.setGoodsName(goodsName);
                        goodsInfoBean.setId(id);
                        goodsInfoBean.setShopId(shopId);
                        goodsInfoBean.setShortDesc(shortDesc);
                        goodsInfoBean.setImgCart(imgCart);
                        goodsInfoBean.setGoodsMoney(goodsMoney);
                        goodsInfoBean.setGoodsMoney1(goodsMoney1);
                        goodsInfoBean.setGoodsMoney_old(goodsMoney_old);
                        goodsInfoBean.setGoodsMoneyConsult(goodsMoneyConsult);
                        mDatas.add(goodsInfoBean);
                    }
                    dianPuAdapter = new DianPuAdapter(DianPuActivity.this, mDatas);
                    mRecyclerView.setAdapter(dianPuAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void setTextColor() {
        yihao.setTextColor(Color.parseColor("#666666"));
        erhao.setTextColor(Color.parseColor("#666666"));
        sanhao.setTextColor(Color.parseColor("#666666"));
        sihao.setTextColor(Color.parseColor("#666666"));
        img_xia.setImageResource(R.mipmap.jtxh);
        img_shang.setImageResource(R.mipmap.jtsh);
//        shaixuan.setTextColor(Color.parseColor("#666666"));
    }
}
