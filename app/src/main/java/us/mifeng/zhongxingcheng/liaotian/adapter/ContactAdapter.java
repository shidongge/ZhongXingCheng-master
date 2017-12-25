package us.mifeng.zhongxingcheng.liaotian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.FriendProfile;

/**
 * Created by user on 2017/12/4.
 */

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private boolean selectable;
    private List<String> groups;
    private Map<String, List<FriendProfile>> mMembers;

    public ContactAdapter(Context context, List<String> groups, Map<String, List<FriendProfile>> members, boolean selectable) {
        this.context = context;
        this.groups = groups;
        this.mMembers = members;
        this.selectable = selectable;
    }

    @Override
    public int getCount() {
        return mMembers.get(groups.get(0)).size();
    }

    @Override
    public Object getItem(int position) {
        return mMembers.get(groups.get(0)).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = new VH();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_childmember, null);
            vh.tag = (ImageView) convertView.findViewById(R.id.chooseTag);
            vh.name = (TextView) convertView.findViewById(R.id.name);
            vh.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        FriendProfile data = (FriendProfile) getItem(position);
        vh.name.setText(data.getName());
        vh.tag.setVisibility(selectable ? View.VISIBLE : View.GONE);
        String avatarUrl = data.getAvatarUrl();
        if ("".equals(avatarUrl)){
            vh.avatar.setImageResource(R.drawable.head_other);
        }else {

            Glide.with(context).load(data.getAvatarUrl()).into(vh.avatar);
        }
        return convertView;
    }

    private class VH {
        public TextView name;
        public ImageView avatar;
        public ImageView tag;
    }
}
