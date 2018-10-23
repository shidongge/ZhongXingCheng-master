package us.mifeng.zhongxingcheng.wpxq_ceshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.activity.TJDD;
import us.mifeng.zhongxingcheng.utils.SharedUtils;
import us.mifeng.zhongxingcheng.utils.ToSi;


/**
 * Created by shido on 2017/11/28.
 */

public class WPXQ_CeSi extends FragmentActivity implements View.OnClickListener {
    public PagerSlidingTabStrip psts_tabs;
    public NoScrollViewPager vp_content;
    public TextView tv_title;
    private LinearLayout shoucang;
    private List<Fragment> fragmentList = new ArrayList<>();
    private GoodsInfoFragment goodsInfoFragment;
    private GoodsDetailFragment goodsDetailFragment;
    private GoodsCommentFragment goodsCommentFragment;
    private Button goumai, jrgwc;
    private ImageView back;
    private String spid,zxcid,substring,token,dpid;
    private static final String TAG = "WPXQ_CeSi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wpxq_cs);
        SharedUtils sharedUtils = new SharedUtils();
        token = sharedUtils.getShared("token",WPXQ_CeSi.this);
        String id = sharedUtils.getShared("id",WPXQ_CeSi.this);
        zxcid = sharedUtils.getShared("zxcid",WPXQ_CeSi.this);
        String newid = id;
        substring = newid.substring(0, 11);
        spid = getIntent().getStringExtra("spid");
        dpid = getIntent().getStringExtra("dpid");
        initView();

    }

    public void initView() {
        back = (ImageView) findViewById(R.id.wuping_back);
        goumai = (Button) findViewById(R.id.wuping_goumai);
        jrgwc = (Button) findViewById((R.id.wuping_jrgwc));
        shoucang = (LinearLayout) findViewById(R.id.wpxq_sc);

        psts_tabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        tv_title = (TextView) findViewById(R.id.tv_title);

        fragmentList.add(goodsInfoFragment = new GoodsInfoFragment());
        fragmentList.add(goodsDetailFragment = new GoodsDetailFragment());
        fragmentList.add(goodsCommentFragment = new GoodsCommentFragment());
        vp_content.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"}));
        vp_content.setOffscreenPageLimit(3);
        psts_tabs.setViewPager(vp_content);
        goumai.setOnClickListener(this);
        jrgwc.setOnClickListener(this);
        back.setOnClickListener(this);
        shoucang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wuping_goumai:
                startActivity(new Intent(WPXQ_CeSi.this, TJDD.class));
                break;
            case R.id.wuping_jrgwc:
                ToSi.show(WPXQ_CeSi.this, "加入购物车成功");

                break;
            case R.id.wuping_back:
                finish();
                break;
            case R.id.wpxq_sc:
                /**
                 * 点击事件里面代码还没出接口，所以就先写着，出接口再改
                 * */
//                HashMap<String, String> map = new HashMap<>();
//                map.put("user_token",token);
//                map.put("user_id",zxcid);
//                map.put("user_mobile",substring);
//                map.put("spid",spid);
//                JSONObject jsonObject = new JSONObject(map);
//                String string1 = jsonObject.toString();
//                final String s = JiaMi.jdkBase64Encoder(string1);
//                HashMap<String, String> map2 = new HashMap<>();
//                map2.put("secret", s);
//                OkUtils.UploadSJ(WangZhi.ZXSC_SC, map2, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, "onFailure: "+e.getLocalizedMessage() );
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e(TAG, "onResponse: "+response.body().string() );
//                    }
//                });
                break;
            default:
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
}
