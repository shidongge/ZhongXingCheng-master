package us.mifeng.zhongxingcheng.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

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
import us.mifeng.zhongxingcheng.activity.HuoDong;
import us.mifeng.zhongxingcheng.activity.RXPH;
import us.mifeng.zhongxingcheng.activity.ShangXinPinPai;
import us.mifeng.zhongxingcheng.adapter.ZXSC_ShouYeAdapter;
import us.mifeng.zhongxingcheng.bean.ADBean;
import us.mifeng.zhongxingcheng.bean.Home_ShangPinBean;
import us.mifeng.zhongxingcheng.dianpu.DianPuActivity;
import us.mifeng.zhongxingcheng.utils.OkUtils;
import us.mifeng.zhongxingcheng.utils.ToSi;
import us.mifeng.zhongxingcheng.utils.WangZhi;
import us.mifeng.zhongxingcheng.view.MyListView;
import us.mifeng.zhongxingcheng.wpxq_ceshi.WPXQ_CeSi;
import us.mifeng.zhongxingcheng.wxapi.WXEntryActivity;


/**
 * Created by shido on 2017/11/15.
 */

/**
 * 中星商城首页fragment
 */
public class ZXSC_ShouYeFragment extends Fragment implements View.OnClickListener, ViewPagerEx.OnPageChangeListener {

    //聊天
    private int mTargetScene0 = SendMessageToWX.Req.WXSceneSession;
    //朋友圈
    private int mTargetScene1 = SendMessageToWX.Req.WXSceneTimeline;


    private static final String TAG = "ZXSC_ShouYeFragment";
    private MyListView listView;
    private View inflate;
    private List<Home_ShangPinBean> list;
    private LinearLayout sxpp, fxlq, rxph;
    private SliderLayout slider;
    private VerticalTextview paomadeng;
    private ArrayList<String> titleList = new ArrayList<String>();

    private List<ADBean.DataBean> data;
    private int position;
    private Intent intent;
    private List<Home_ShangPinBean.GoodsBean> goodsBeanList;
    private ZXSC_ShouYeAdapter zxsc_shouYeAdapter;
    private ImageView yihao, erhao, sanhao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = View.inflate(getActivity(), R.layout.fragment_zxsc_shouye, null);
        listView = (MyListView) inflate.findViewById(R.id.zxsc_home_list);
        paomadeng = (VerticalTextview) inflate.findViewById(R.id.zxsc_home_paomadeng);
        initView();
        list = new ArrayList<>();
        initAD();
        initPaoMaDeng();
        initLianWang();
        return inflate;

    }

    private void initAD() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("", "");
        OkUtils.UploadSJ(WangZhi.SYDB, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                String string = response.body().string();
                Message message = hand.obtainMessage();
                message.obj = string;
                message.what = 100;
                hand.sendMessage(message);

            }
        });
    }

    private void initPaoMaDeng() {
        titleList.add("欢迎登陆中星宝，中星宝正式上线");
        titleList.add("哎呀  ，，，，，，，，，，");
        paomadeng.setTextList(titleList);//加入显示内容,集合类型
        paomadeng.setText(18, 5, Color.RED);//设置属性,具体跟踪源码
        paomadeng.setTextStillTime(3000);//设置停留时长间隔
        paomadeng.setAnimTime(300);//设置进入和退出的时间间隔
        //对单条文字的点击监听
        paomadeng.setOnItemClickListener(new VerticalTextview.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TO DO
                ToSi.show(getActivity(), "点击了 : " + titleList.get(position));
            }
        });

    }

//    private void initSlider(HashMap<String, String> url_maps) {
//        for (String name : url_maps.keySet()) {
//            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
//            // initialize a SliderLayout
//            defaultSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information 点击图片时可用到
//            defaultSliderView.bundle(new Bundle());
//            defaultSliderView.getBundle()
//                    .putString("extra", name);
//
//            slider.addSlider(defaultSliderView);
//        }
//        // 设置默认过渡效果(可在xml中设置)
//        slider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
//        // 设置默认指示器位置(默认指示器白色,位置在sliderlayout底部)
//        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        // 设置TextView自定义动画
//        slider.setCustomAnimation(new DescriptionAnimation());
//        // 设置持续时间
//        slider.setDuration(2000);
//        slider.addOnPageChangeListener(this);
//    }


    protected void initList(HashMap<String, String> url_maps) {
        slider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            for (int i = 0; i < data.size(); i++) {
                                ADBean.DataBean dataBean = data.get(i);
                                String url = slider.getUrl();
                                if (url.equals(dataBean.getImg())) {
                                    String type = dataBean.getType();
                                    if ("1".equals(type)) {
                                        intent = new Intent(getActivity(), HuoDong.class);
                                        intent.putExtra("wangzhi", dataBean.getUrl());
                                    } else {
                                        intent = new Intent(getActivity(), WPXQ_CeSi.class);
                                        intent.putExtra("spid", dataBean.getUrl());
                                    }
                                }
                            }
                            startActivity(intent);
                        }
                    });
            slider.addOnPageChangeListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            slider.addSlider(textSliderView);
        }
    }


    private void initLianWang() {
        HashMap<String, String> map = new HashMap<>();
        map.put("", "");
        OkUtils.UploadSJ(WangZhi.ZXSC_SY, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message mess = hand.obtainMessage();
                mess.obj = string;
                mess.what = 200;
                hand.sendMessage(mess);
            }
        });

    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //顶部banner图
            if (msg.what == 100) {
                HashMap<String, String> url_maps = new HashMap();
                String string = (String) msg.obj;
                Gson gson = new Gson();
                ADBean adBean = gson.fromJson(string, ADBean.class);
                data = adBean.getData();
                for (int i = 0; i < data.size(); i++) {
                    url_maps.put(data.get(i).getTitle(), data.get(i).getImg());
                }
                initList(url_maps);
            }
            if (msg.what == 200) {
                String str = (String) msg.obj;
                try {
                    JSONArray jsonArray = new JSONArray(str);

                    for (int z = 0; z < jsonArray.length(); z++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(z);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String picture = jsonObject.getString("picture");
                        Home_ShangPinBean shangPinBean = new Home_ShangPinBean();
                        shangPinBean.setId(id);
                        shangPinBean.setName(name);
                        shangPinBean.setPicture(picture);
                        JSONArray goods = jsonObject.getJSONArray("goods");
                        goodsBeanList = new ArrayList<>();
                        for (int j = 0; j < goods.length(); j++) {
                            Home_ShangPinBean.GoodsBean goodsBean = new Home_ShangPinBean.GoodsBean();
                            JSONObject jsonObject1 = goods.getJSONObject(j);
                            String goodsId = jsonObject1.getString("goodsId");
                            String goodsName = jsonObject1.getString("goodsName");
                            String imgCart = jsonObject1.getString("imgCart");
                            goodsBean.setGoodsId(goodsId);
                            goodsBean.setGoodsName(goodsName);
                            goodsBean.setImgCart(imgCart);
                            goodsBeanList.add(goodsBean);
                            shangPinBean.setGoods(goodsBeanList);
                        }
                        list.add(shangPinBean);
                    }
                    if (zxsc_shouYeAdapter == null) {
                        zxsc_shouYeAdapter = new ZXSC_ShouYeAdapter(list, getActivity());
                        listView.setAdapter(zxsc_shouYeAdapter);
                    } else {
                        zxsc_shouYeAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void initView() {
        ImageView back = (ImageView) inflate.findViewById(R.id.zxsc_home_back);
        sxpp = (LinearLayout) inflate.findViewById(R.id.fragment_zxsc_shouye_sxpp);
        fxlq = (LinearLayout) inflate.findViewById(R.id.fragment_zxsc_shouye_fxlq);
        rxph = (LinearLayout) inflate.findViewById(R.id.fragment_zxsc_shouye_rxph);

        yihao = (ImageView) inflate.findViewById(R.id.zxsc_sy_yihao);
        erhao = (ImageView) inflate.findViewById(R.id.zxsc_sy_erhao);
        sanhao = (ImageView) inflate.findViewById(R.id.zxsc_sy_sanhao);


        slider = (SliderLayout) inflate.findViewById(R.id.slider);
        sxpp.setOnClickListener(this);
        fxlq.setOnClickListener(this);
        rxph.setOnClickListener(this);

        yihao.setOnClickListener(this);
        erhao.setOnClickListener(this);
        sanhao.setOnClickListener(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_zxsc_shouye_sxpp:
                startActivity(new Intent(getActivity(), ShangXinPinPai.class));
                break;
            case R.id.zxsc_home_back:
                getActivity().finish();
                break;
            case R.id.fragment_zxsc_shouye_fxlq:
                WXEntryActivity activity = new WXEntryActivity();
                activity.fenxiang(getActivity(), mTargetScene0);
                break;
            case R.id.fragment_zxsc_shouye_rxph:
                startActivity(new Intent(getActivity(), RXPH.class));
                break;
            case R.id.zxsc_sy_yihao:
                Intent intent1 = new Intent(getActivity(), DianPuActivity.class);
                intent1.putExtra("dianpu","");
                startActivity(intent1);
                break;
            case R.id.zxsc_sy_erhao:
                Intent intent2 = new Intent(getActivity(), DianPuActivity.class);
                intent2.putExtra("dianpu","");
                startActivity(intent2);
                break;
            case R.id.zxsc_sy_sanhao:
                Intent intent3 = new Intent(getActivity(), DianPuActivity.class);
                intent3.putExtra("dianpu","");
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
//        Log.e(TAG, "onPageSelected: "+position );

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //开始滚动
    @Override
    public void onResume() {
        super.onResume();
        paomadeng.startAutoScroll();
    }

    //停止滚动
    @Override
    public void onPause() {
        super.onPause();
        paomadeng.stopAutoScroll();
    }

    @Override
    public void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }


    public static boolean isInteger(String str) {
        boolean b = true;
        try {
            Integer.valueOf(str);
        } catch (Exception e) {
            b = false;
        }
        return b;
    }
}
