package us.mifeng.zhongxingcheng.liaotian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupInfoPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendInfoView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupInfoView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.adapter.ConversationAdapter;
import us.mifeng.zhongxingcheng.liaotian.model.Conversation;
import us.mifeng.zhongxingcheng.liaotian.model.CustomMessage;
import us.mifeng.zhongxingcheng.liaotian.model.FriendshipConversation;
import us.mifeng.zhongxingcheng.liaotian.model.GroupInfo;
import us.mifeng.zhongxingcheng.liaotian.model.GroupManageConversation;
import us.mifeng.zhongxingcheng.liaotian.model.MessageFactory;
import us.mifeng.zhongxingcheng.liaotian.model.NomalConversation;
import us.mifeng.zhongxingcheng.liaotian.utils.PushUtil;
import us.mifeng.zhongxingcheng.utils.SharedUtils;

/**
 * 会话列表界面
 */
public class ConversationFragment extends Fragment implements ConversationView, FriendshipMessageView, GroupManageMessageView, View.OnClickListener, FriendInfoView, GroupInfoView {
    private static final String TAG = "ConversationFragment";
    private View view;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter adapter;
    private ListView listView;
    private ConversationPresenter presenter;
    private GroupInfoPresenter groupInfoPresenter;
    private FriendshipManagerPresenter friendshipManagerPresenter, friendPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;
    private ImageView iv_search, iv_lianxiren, iv_add;
    private RelativeLayout rela_sao;
    private RelativeLayout rela_add;
    private RelativeLayout rela_chats;
    private AlertDialog showDialog2;
    private String faceUrl;
    private SharedUtils sharedUtils;
    private List<TIMGroupDetailInfo> groupInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            listView = (ListView) view.findViewById(R.id.list);
            iv_add = (ImageView) view.findViewById(R.id.conversion_add);
            iv_lianxiren = (ImageView) view.findViewById(R.id.conversion_lianxiren);
            iv_search = (ImageView) view.findViewById(R.id.conversion_search);
            iv_add.setOnClickListener(this);
            iv_search.setOnClickListener(this);
            iv_lianxiren.setOnClickListener(this);
            showDialog2 = showDialog2();
            sharedUtils = new SharedUtils();
            adapter = new ConversationAdapter(getActivity(), conversationList, groupInfoList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    conversationList.get(position).navToDetailAndUrl(getActivity(), faceUrl);
                    if (conversationList.get(position) instanceof GroupManageConversation) {
                        groupManagerPresenter.getGroupManageLastMessage();
                    }
                }
            });
            friendshipManagerPresenter = new FriendshipManagerPresenter((FriendshipMessageView) this);
            friendPresenter = new FriendshipManagerPresenter((FriendInfoView) this);
            groupManagerPresenter = new GroupManagerPresenter((GroupManageMessageView) this);
            presenter = new ConversationPresenter(this);
            presenter.getConversation();
            registerForContextMenu(listView);
        }
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        friendPresenter.getMyProfile();
        PushUtil.getInstance().reset();
        String groupId = sharedUtils.getShared("REFRESH_TAG", getActivity());
        Log.e(TAG, "onResume: " + groupId);
        if (!TextUtils.isEmpty(groupId)) {
            groupList.add(groupId);
            groupInfoPresenter = new GroupInfoPresenter(this, groupList, true);
            groupInfoPresenter.getGroupDetailInfo();
            sharedUtils.clearKey("REFRESH_TAG", getActivity());
        }
    }

    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();
        groupInfoPresenter = new GroupInfoPresenter(this, groupList, true);
        groupInfoPresenter.getGroupDetailInfo();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify)) {
                iterator.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList) {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        Collections.sort(conversationList);
        adapter.notifyDataSetChanged();
    }


    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        if (friendshipConversation == null) {
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        } else {
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (groupManageConversation == null) {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        } else {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Conversation conversation = conversationList.get(info.position);
        if (conversation instanceof NomalConversation) {
            menu.add(0, 1, Menu.NONE, getString(R.string.conversation_del));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NomalConversation conversation = (NomalConversation) conversationList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                if (conversation != null) {
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())) {
                        conversationList.remove(conversation);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum() {
        long num = 0;
        for (Conversation conversation : conversationList) {
            num += conversation.getUnreadNum();
        }
        return num;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conversion_add:
                if (showDialog2 != null) {
                    if (showDialog2.isShowing()) {
                        showDialog2.dismiss();
                    } else {
                        showDialog2.show();
                    }
                }
                break;
            case R.id.conversion_lianxiren:
                startActivity(new Intent(getActivity(), FriendsActivity.class));
                break;
            case R.id.conversion_search:

                break;
            case R.id.rela_add:
                startActivity(new Intent(getActivity(), SearchFriendActivity.class));
                break;
            case R.id.rela_chat:
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                intent.putExtra("faceUrl", faceUrl);
                intent.putExtra("type", GroupInfo.publicGroup);
                startActivity(intent);
                break;
            case R.id.rela_erweima:
                startActivity(new Intent(getActivity(), SearchGroupActivity.class));
                break;
        }
    }

    private AlertDialog showDialog2() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.conversation_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflate);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.dismiss();
        Window window = alertDialog.getWindow();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        attributes.width = (int) (display.getWidth() * 0.5);
        attributes.y = 80;
        window.setAttributes(attributes);
        rela_chats = (RelativeLayout) inflate.findViewById(R.id.rela_chat);
        rela_add = (RelativeLayout) inflate.findViewById(R.id.rela_add);
        rela_sao = (RelativeLayout) inflate.findViewById(R.id.rela_erweima);
        rela_add.setOnClickListener(this);
        rela_chats.setOnClickListener(this);
        rela_sao.setOnClickListener(this);
        return alertDialog;
    }

    /**
     * 获取自己头像
     */
    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        faceUrl = users.get(0).getFaceUrl();
    }

    /**
     * 获取群头像
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        groupInfoList = groupInfos;
        adapter = new ConversationAdapter(getActivity(), conversationList, groupInfoList);
        listView.setAdapter(adapter);
    }
}