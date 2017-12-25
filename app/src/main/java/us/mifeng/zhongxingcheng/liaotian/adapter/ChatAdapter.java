package us.mifeng.zhongxingcheng.liaotian.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.Message;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends BaseAdapter {
    private final String TAG = "ChatAdapter";
    private View view;
    private ViewHolder viewHolder;
    private Context context;
    private String rightUrl;
    private List<TIMUserProfile> urlList;
    private List<Message> list;

    public ChatAdapter(Context context, String rightUrl, List<TIMUserProfile> urlList, List<Message> list) {
        this.context = context;
        this.rightUrl = rightUrl;
        this.urlList = urlList;
        this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            viewHolder.leftAvatar = (CircleImageView) view.findViewById(R.id.leftAvatar);
            viewHolder.rightAvatar = (CircleImageView) view.findViewById(R.id.rightAvatar);
            view.setTag(viewHolder);
        }
        String id = list.get(position).getSender();
        if (urlList.size() > 0) {
            for (int i = 0; i < urlList.size(); i++) {
                String identifier = urlList.get(i).getIdentifier();
                if (identifier.equals(id)) {
                    String faceUrl = urlList.get(i).getFaceUrl();
                    if (!TextUtils.isEmpty(faceUrl)) {
                        if (faceUrl.equals(rightUrl)) {
                            Glide.with(context).load(faceUrl).into(viewHolder.rightAvatar);
                        } else {
                            Glide.with(context).load(faceUrl).into(viewHolder.leftAvatar);
                        }
                    }
                }
            }
        }
        if (position < getCount()) {
            final Message data = list.get(position);
            data.showMessage(viewHolder, context);
        }
        return view;
    }

    public class ViewHolder {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public CircleImageView leftAvatar, rightAvatar;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
    }

}
