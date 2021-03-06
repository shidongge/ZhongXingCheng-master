package us.mifeng.zhongxingcheng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.bean.ShouCangBean;

/**
 * Created by shido on 2018/1/9.
 */

public class SC_SPAdapter extends BaseAdapter{
    private List<ShouCangBean.GoodsCollectionBean> list;
    private Context context;
    public SC_SPAdapter(List<ShouCangBean.GoodsCollectionBean> list, Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        if (list.size()!=0){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHorder vh;
        if (convertView==null){
            vh=new MyViewHorder();
            convertView = View.inflate(context, R.layout.item_sc_sp,null);
            vh.logo = (ImageView) convertView.findViewById(R.id.item_sc_sp_logo);
            vh.name = (TextView) convertView.findViewById(R.id.item_sc_sp_name);

            vh.jiage = (TextView) convertView.findViewById(R.id.item_sc_sp_jiage);
            convertView.setTag(vh);
        }else {
            vh = (MyViewHorder) convertView.getTag();
        }

        vh.jiage.setText(list.get(position).getGoodsMoney());
        vh.name.setText(list.get(position).getGoodsName());
        Glide.with(context).load(list.get(position).getImgUrl()).into(vh.logo);
        return convertView;
    }
    class MyViewHorder{
        TextView jiage,name;
        ImageView logo;
    }
}
