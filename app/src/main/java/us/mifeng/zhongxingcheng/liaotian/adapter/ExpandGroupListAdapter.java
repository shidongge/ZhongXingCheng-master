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
 * 分组列表Adapters
 */
public class ExpandGroupListAdapter extends BaseAdapter {
    private Context mContext;
    private boolean selectable;
    private List<String> groups;
    private Map<String, List<FriendProfile>> mMembers;

    public ExpandGroupListAdapter(Context context, List<String> groups, Map<String, List<FriendProfile>> members, boolean selectable) {
        mContext = context;
        this.groups = groups;
        mMembers = members;
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildrenHolder itemHolder;
        if (convertView == null) {
            itemHolder = new ChildrenHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_childmember, null);
            itemHolder.tag = (ImageView) convertView.findViewById(R.id.chooseTag);
            itemHolder.name = (TextView) convertView.findViewById(R.id.name);
            itemHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ChildrenHolder) convertView.getTag();
        }
        FriendProfile data = (FriendProfile) getItem(position);
        Glide.with(mContext).load(data.getAvatarUrl()).into(itemHolder.avatar);
        itemHolder.name.setText(data.getName());
        itemHolder.tag.setVisibility(selectable ? View.VISIBLE : View.GONE);
        itemHolder.tag.setImageResource(data.isSelected() ? R.drawable.selected : R.drawable.unselected);
        return convertView;
    }

    class ChildrenHolder {
        public TextView name;
        public ImageView avatar;
        public ImageView tag;
    }


}
