package us.mifeng.zhongxingcheng.liaotian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.ProfileSummary;


/**
 * 好友或群等资料摘要列表的adapter
 */
public class ProfileSummaryAdapter extends ArrayAdapter<ProfileSummary> {
    private Context context;
    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ProfileSummaryAdapter(Context context, int resource, List<ProfileSummary> objects) {
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
        ProfileSummary data = getItem(position);
        String userName = data.getName();
        String avatarUrl = data.getAvatarUrl();
        if (avatarUrl != null) {
            Glide.with(context).load(avatarUrl).into(viewHolder.avatar);
        }
        if (userName != null) {
            viewHolder.name.setText(userName);
        }
        return view;
    }

    public class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView des;
    }
}
