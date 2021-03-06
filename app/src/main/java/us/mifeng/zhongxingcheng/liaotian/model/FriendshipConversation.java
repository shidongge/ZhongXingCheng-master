package us.mifeng.zhongxingcheng.liaotian.model;

import android.content.Context;

import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.app.MyApplicaiton;


/**
 * 新朋友会话
 */
public class FriendshipConversation extends Conversation {
    private TIMFriendFutureItem lastMessage;
    private long unreadCount;
    private final TIMUserProfile profile;

    public FriendshipConversation(TIMFriendFutureItem message) {
        lastMessage = message;
        profile = message.getProfile();
    }

    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        if (lastMessage == null) return 0;
        return lastMessage.getAddTime();
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        return unreadCount;
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {

    }

    /**
     * 获取头像
     */
    @Override
    public int getAvatar() {
        return R.drawable.ic_news;
    }

    @Override
    public String getAvatarUrl() {
        String faceUrl = profile.getFaceUrl();
        if (faceUrl != null) {
            return faceUrl;
        }
        return null;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        /*Intent intent = new Intent(context, FriendshipManageMessageActivity.class);
        context.startActivity(intent);*/
    }

    @Override
    public void navToDetailAndUrl(Context context, String faceUrl) {

    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        if (lastMessage == null) return "";
        String name = lastMessage.getProfile().getNickName();
        if (name.equals("")) name = lastMessage.getIdentifier();
        switch (lastMessage.getType()) {
            case TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE://我收到的好友申请的未决消息
                return name + MyApplicaiton.getContext().getString(R.string.summary_friend_add);
            case TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE://我发出的好友申请的未决消息
                return MyApplicaiton.getContext().getString(R.string.summary_me) + MyApplicaiton.getContext().getString(R.string.summary_friend_add_me) + name;
            case TIM_FUTURE_FRIEND_DECIDE_TYPE://已决消息
                return MyApplicaiton.getContext().getString(R.string.summary_friend_added) + name;
            case TIM_FUTURE_FRIEND_RECOMMEND_TYPE://好友推荐
                return MyApplicaiton.getContext().getString(R.string.summary_friend_recommend) + name;
            default:
                return "";
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        return MyApplicaiton.getContext().getString(R.string.conversation_system_friend);
    }


    /**
     * 设置最后一条消息
     */
    public void setLastMessage(TIMFriendFutureItem message) {
        lastMessage = message;
    }


    /**
     * 设置未读数量
     *
     * @param count 未读数量
     */
    public void setUnreadCount(long count) {
        unreadCount = count;
    }


}
