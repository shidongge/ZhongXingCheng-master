package us.mifeng.zhongxingcheng.liaotian.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

import us.mifeng.zhongxingcheng.R;

/**
 * Created by user on 2017/12/8.
 */

public class GroupMemberAdapter extends ArrayAdapter<TIMUserProfile> {
    private Context context;
    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    public GroupMemberAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TIMUserProfile> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.des = (TextView) view.findViewById(R.id.description);
            view.setTag(viewHolder);
        }
        TIMUserProfile data = getItem(position);
        String avatarUrl = data.getFaceUrl();
        if (!avatarUrl .equals("")) {
            Glide.with(context).load(avatarUrl).into(viewHolder.avatar);
        }else {
            viewHolder.avatar.setImageResource(R.drawable.head_other);
        }
        String userName = data.getNickName();
        String identifier = data.getIdentifier();
        if (!TextUtils.isEmpty(userName)) {
            viewHolder.name.setText(userName);
        } else {
            viewHolder.name.setText(identifier);
        }
        return view;
    }

    public class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView des;
    }
}
