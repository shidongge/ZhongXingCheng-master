package us.mifeng.zhongxingcheng.liaotian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.Conversation;
import us.mifeng.zhongxingcheng.liaotian.utils.TimeUtil;
import us.mifeng.zhongxingcheng.utils.SharedUtils;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends BaseAdapter {
    private View view;
    private ViewHolder viewHolder;
    private Context context;
    private List<TIMGroupDetailInfo> groupInfoList;
    private List<Conversation> list;
    private List<TIMUserProfile> idList;
    private SharedUtils sharedUtils;

    public ConversationAdapter(Context context, List<Conversation> list, List<TIMGroupDetailInfo> groupInfoList, List<TIMUserProfile> idList) {
        this.context = context;
        this.list = list;
        this.idList = idList;
        this.groupInfoList = groupInfoList;
        sharedUtils = new SharedUtils();
    }

    @Override
    public int getCount() {
        return list.size();
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
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = (Conversation) getItem(position);
        String identify = data.getIdentify();
        String name = data.getName();
        String avatarUrl = data.getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(context).load(avatarUrl).into(viewHolder.avatar);
        }
        viewHolder.tvName.setText(name);
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        for (int i = 0; i < groupInfoList.size(); i++) {
            TIMGroupDetailInfo timGroupDetailInfo = groupInfoList.get(i);
            String groupId = timGroupDetailInfo.getGroupId();
            if (groupId.equals(identify)) {
                String faceUrl = timGroupDetailInfo.getFaceUrl();
                Glide.with(context).load(faceUrl).into(viewHolder.avatar);
            }
            if ("新朋友".equals(name)) {
                String friendFaceUrl = sharedUtils.getShared("friendFaceUrl", context);
                Glide.with(context).load(friendFaceUrl).into(viewHolder.avatar);
            }
        }
        for (int j = 0; j < idList.size(); j++) {
            TIMUserProfile userProfile = idList.get(j);
            String identifier = userProfile.getIdentifier();
            String remark = userProfile.getRemark();
            String nickName = userProfile.getNickName();
            String faceUrl = userProfile.getFaceUrl();
            if (identifier.equals(identify)) {
                if (!TextUtils.isEmpty(faceUrl)){
                    Glide.with(context).load(faceUrl).into(viewHolder.avatar);
                }
                if (!TextUtils.isEmpty(remark)) {
                    viewHolder.tvName.setText(remark);
                } else {
                    if (!TextUtils.isEmpty(nickName)) {
                        viewHolder.tvName.setText(nickName);
                    } else {
                        viewHolder.tvName.setText(identifier);
                    }
                }
            }
        }
        long unRead = data.getUnreadNum();
        if (unRead <= 0) {
            viewHolder.unread.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                viewHolder.unread.setBackground(context.getResources().getDrawable(R.drawable.point1));
            } else {
                viewHolder.unread.setBackground(context.getResources().getDrawable(R.drawable.point2));
                if (unRead > 99) {
                    unReadStr = context.getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder {
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;
    }
}
