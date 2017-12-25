package us.mifeng.zhongxingcheng.liaotian.model;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.ext.group.TIMGroupBasicSelfInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.ChatActivity;
import us.mifeng.zhongxingcheng.liaotian.GroupListActivity;


/**
 * 群资料
 */
public class GroupProfile implements ProfileSummary {


    private TIMGroupDetailInfo profile;
    private TIMGroupBasicSelfInfo selfInfo;
    private String faceUrl;

    public GroupProfile(TIMGroupCacheInfo profile) {
        this.profile = profile.getGroupInfo();
        selfInfo = profile.getSelfInfo();
    }

    public GroupProfile(TIMGroupDetailInfo profile) {
        this.profile = profile;
    }

    /**
     * 获取群ID
     */
    @Override
    public String getIdentify() {
        return profile.getGroupId();
    }


    public void setProfile(TIMGroupCacheInfo profile) {
        this.profile = profile.getGroupInfo();
        selfInfo = profile.getSelfInfo();
    }

    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.drawable.head_group;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        if (!TextUtils.isEmpty(profile.getFaceUrl())) {
            return profile.getFaceUrl();
        }
        return null;
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        return profile.getGroupName();
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }


    /**
     * 获取自己身份
     */
    public TIMGroupMemberRoleType getRole() {
        return selfInfo.getRole();
    }

    /**
     * 获取消息接收状态
     */
    public TIMGroupReceiveMessageOpt getMessagOpt() {
        return selfInfo.getRecvMsgOption();
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        if (context instanceof GroupListActivity) {
            faceUrl = ((GroupListActivity) context).faceUrl;
        }
        ChatActivity.navToChat(context, profile.getGroupId(), TIMConversationType.Group, faceUrl);
    }
}
