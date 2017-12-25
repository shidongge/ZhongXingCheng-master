package us.mifeng.zhongxingcheng.liaotian;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.adapter.ContactAdapter;
import us.mifeng.zhongxingcheng.liaotian.model.FriendProfile;
import us.mifeng.zhongxingcheng.liaotian.model.FriendshipInfo;

/**
 * 联系人界面
 */
public class ContactActivity extends Activity {
    private ListView lv;
    private LinearLayout mNewFriBtn, mPublicGroupBtn, mChatRoomBtn, mPrivateGroupBtn;
    Map<String, List<FriendProfile>> friends;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact);
        lv = (ListView) findViewById(R.id.groupList);
        mNewFriBtn = (LinearLayout) findViewById(R.id.btnNewFriend);
        mPublicGroupBtn = (LinearLayout) findViewById(R.id.btnPublicGroup);
        mChatRoomBtn = (LinearLayout) findViewById(R.id.btnChatroom);
        mPrivateGroupBtn = (LinearLayout) findViewById(R.id.btnPrivateGroup);
        friends = FriendshipInfo.getInstance().getFriends();
        adapter = new ContactAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, false);
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showGroups(String type) {
        Intent intent = new Intent(this, GroupListActivity.class);
        intent.putExtra("type", type);
        this.startActivity(intent);
    }
}
