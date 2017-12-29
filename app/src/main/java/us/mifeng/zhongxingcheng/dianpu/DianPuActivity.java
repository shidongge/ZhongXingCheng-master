package us.mifeng.zhongxingcheng.dianpu;

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
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.WangZhi;

public class DianPuActivity extends FragmentActivity implements View.OnClickListener {
    private String[] mTitles = new String[]{"全部", "销量", "价格", "新品", "筛选"};
    private RecyclerView youhuiquan;
    private LinearLayout mIndicator;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];
    private RecyclerView mRecyclerView;
    private List<ZXSC_DianPuBean.GoodsInfoBean> mDatas = new ArrayList<>();
    private String mTitle = "hahah";
    private TextView yihao, erhao, sanhao, sihao, biaoti;
    private DianPuAdapter dianPuAdapter;
    private ImageView back;
    private static final String TAG = "DianPuActivity";
    private String dianpu;
    private ImageView banner,logo;
    private TextView dianming,shangxin,quanbu,quanbuxiao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_pu);
        dianpu = getIntent().getStringExtra("dianpu");
        initView();
        initLianWang();
        //；粘性布局
        initViews();
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
        map.put("shopId",dianpu);
        OkUtils.UploadSJ(WangZhi.ZXSC_DP, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message message = hand.obtainMessage();
                message.what=100;
                message.obj=string;
                hand.sendMessage(message);
            }
        });
    }

    private void initViews() {
        yihao = (TextView) findViewById(R.id.dp_yihao);
        erhao = (TextView) findViewById(R.id.dp_erhao);
        sanhao = (TextView) findViewById(R.id.dp_sanhao);
        sihao = (TextView) findViewById(R.id.dp_sihao);
        back = (ImageView) findViewById(R.id.title_back);
        biaoti = (TextView) findViewById(R.id.title_text);

        youhuiquan = (RecyclerView) findViewById(R.id.dianpu_youhuiquan);
        mIndicator = (LinearLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_stickynavlayout_vp);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        biaoti.setText("商家店铺");
        back.setOnClickListener(this);
        yihao.setOnClickListener(this);
        erhao.setOnClickListener(this);
        sanhao.setOnClickListener(this);
        sihao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dp_yihao:
                dianPuAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(dianPuAdapter);
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 000"+"点击了哈哈哈" );
                    }
                });
                break;
            case R.id.dp_erhao:
                dianPuAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(dianPuAdapter);
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 111111"+"点击了哈哈哈" );
                    }
                });
                break;
            case R.id.dp_sanhao:
                dianPuAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(dianPuAdapter);
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 222222"+"点击了哈哈哈" );
                    }
                });
                break;
            case R.id.dp_sihao:
                dianPuAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(dianPuAdapter);
                dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onItemClick: 333333"+"点击了哈哈哈" );
                    }
                });
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }


    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==100){
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
                    Glide.with(DianPuActivity.this).load(WangZhi.DIANPU+imgIcon).into(logo);
                    Glide.with(DianPuActivity.this).load(WangZhi.DIANPU+imgTop).into(banner);
                    quanbu.setText(goodsCount);
                    shangxin.setText(sellCount);
                    quanbuxiao.setText(goodsCount);
                    JSONArray goodsInfo = jsonObject.getJSONArray("goodsInfo");
                    for (int i =0;i<goodsInfo.length();i++){
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
                    mRecyclerView.setAdapter(dianPuAdapter); dianPuAdapter.setOnItemClickListener(new DianPuAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Log.e(TAG, "onItemClick: 000000"+"点击了哈哈哈" );
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
