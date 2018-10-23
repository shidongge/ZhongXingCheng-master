package us.mifeng.zhongxingcheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.bean.Home_ShangPinBean;
import us.mifeng.zhongxingcheng.dianpu.DianPuActivity;
import us.mifeng.zhongxingcheng.utils.WangZhi;
import us.mifeng.zhongxingcheng.wpxq_ceshi.WPXQ_CeSi;


/**
 * Created by shido on 2017/11/16.
 */

/**
 * 中星商城首页的listview的适配器
 */
public class ZXSC_ShouYeAdapter extends BaseAdapter  {
    private static final String TAG = "ZXSC_ShouYeAdapter";
    private List<Home_ShangPinBean> list;
    private Context context;
    public ZXSC_ShouYeAdapter(List<Home_ShangPinBean> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        if (list.size() !=0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHorder vh ;
        if (convertView==null){
            vh = new MyViewHorder();
            convertView = View.inflate(context, R.layout.item_zxsc_shouye,null);
            vh.img = (ImageView) convertView.findViewById(R.id.item_shouye_shang_img);
            vh.oneimg = (ImageView) convertView.findViewById(R.id.item_shouye_xia_yi_ll_img);
            vh.twoimg = (ImageView) convertView.findViewById(R.id.item_shouye_xia_er_ll_img);
            vh.threeimg = (ImageView) convertView.findViewById(R.id.item_shouye_xia_san_ll_img);
            vh.onetext = (TextView) convertView.findViewById(R.id.item_shouye_xia_yi_ll_text);
            vh.twotext = (TextView) convertView.findViewById(R.id.item_shouye_xia_er_ll_text);
            vh.threetext = (TextView) convertView.findViewById(R.id.item_shouye_xia_san_ll_text);
            vh.one = (LinearLayout) convertView.findViewById(R.id.item_shouye_xia_yi_ll);
            vh.two = (LinearLayout) convertView.findViewById(R.id.item_shouye_xia_er_ll);
            vh.three = (LinearLayout) convertView.findViewById(R.id.item_shouye_xia_san_ll);
            convertView.setTag(vh);

        }else {
            vh = (MyViewHorder) convertView.getTag();
        }

        Glide.with(context).load(WangZhi.DIANPU+list.get(position).getPicture()).into(vh.img);
        List<Home_ShangPinBean.GoodsBean> goods = list.get(position).getGoods();
       if (goods!=null){
           for (int i = 0 ;i<goods.size();i++){
               final Home_ShangPinBean.GoodsBean goodsBean = goods.get(i);
               final String id = list.get(position).getId();
               if (i==0){
                   Glide.with(context).load(WangZhi.DIANPU+goodsBean.getImgCart()).into(vh.oneimg);
                   vh.onetext.setText(goodsBean.getGoodsName());
                   vh.one.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           String goodsId = goodsBean.getGoodsId();
                           Intent intent = new Intent(context, WPXQ_CeSi.class);
                           intent.putExtra("spid",goodsId);
                           intent.putExtra("dpid",id);
                           context.startActivity(intent);
                       }
                   });
               }
               if (i==1){
                   Glide.with(context).load(WangZhi.DIANPU+goodsBean.getImgCart()).into(vh.twoimg);
                   vh.twotext.setText(goodsBean.getGoodsName());
                   vh.two.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           String goodsId = goodsBean.getGoodsId();
                           Intent intent = new Intent(context, WPXQ_CeSi.class);
                           intent.putExtra("spid",goodsId);
                           intent.putExtra("dpid",id);
                           context.startActivity(intent);
                       }
                   });
               }
               if (i==2){
                   Glide.with(context).load(WangZhi.DIANPU+goodsBean.getImgCart()).into(vh.threeimg);
                   vh.threetext.setText(goodsBean.getGoodsName());
                   vh.three.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           String goodsId = goodsBean.getGoodsId();
                           Intent intent = new Intent(context, WPXQ_CeSi.class);
                           intent.putExtra("spid",goodsId);
                           intent.putExtra("dpid",id);
                           context.startActivity(intent);
                       }
                   });
               }
           }
       }
        vh.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DianPuActivity.class);
                String id = list.get(position).getId();
                intent.putExtra("dianpu",id);
                context.startActivity(intent);
//                ToSi.show(context,"点击是大的");
            }
        });
        return convertView;
    }



    class MyViewHorder{
        TextView onetext,twotext,threetext;
        ImageView img,oneimg,twoimg,threeimg;
        LinearLayout one,two,three;
    }
}
