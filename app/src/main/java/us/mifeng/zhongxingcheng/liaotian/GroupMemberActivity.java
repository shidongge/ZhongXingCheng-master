package us.mifeng.zhongxingcheng.liaotian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;

import java.util.ArrayList;
import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.adapter.GroupMemberAdapter;
import us.mifeng.zhongxingcheng.liaotian.model.GroupInfo;
import us.mifeng.zhongxingcheng.liaotian.model.GroupMemberProfile;
import us.mifeng.zhongxingcheng.liaotian.model.ProfileSummary;

public class GroupMemberActivity extends Activity implements TIMValueCallBack<List<TIMGroupMemberInfo>> {
    private static final String TAG = "GroupMemberActivity";
    GroupMemberAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;
    TextView title;
    String groupId, type;
    private final int MEM_REQ = 100;
    private final int CHOOSE_MEM_CODE = 200;
    private int memIndex;
    private ImageView img_add;
    private List<String> identifierList;
    private List<TIMUserProfile> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        title = (TextView) findViewById(R.id.group_mem_title);
        groupId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        listView = (ListView) findViewById(R.id.list);
        img_add = (ImageView) findViewById(R.id.group_number_add);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                memIndex = position;
                Intent intent = new Intent(GroupMemberActivity.this, GroupMemberProfileActivity.class);
                GroupMemberProfile profile = (GroupMemberProfile) list.get(position);
                TIMUserProfile userProfile = userList.get(position);
                intent.putExtra("faceUrl", userProfile.getFaceUrl());
                Log.e("000", "onItemClick: " + userProfile.getIdentifier());
                intent.putExtra("data", profile);
                intent.putExtra("userId",userProfile.getIdentifier());
                intent.putExtra("groupId", groupId);
                intent.putExtra("type", type);
                startActivityForResult(intent, MEM_REQ);
            }
        });

        if (type.equals(GroupInfo.privateGroup)) {
            img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupMemberActivity.this, ChooseFriendActivity.class);
                    ArrayList<String> selected = new ArrayList<>();
                    for (ProfileSummary profile : list) {
                        selected.add(profile.getIdentify());
                    }
                    intent.putStringArrayListExtra("selected", selected);
                    startActivityForResult(intent, CHOOSE_MEM_CODE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TIMGroupManagerExt.getInstance().getGroupMembers(this.groupId, this);
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        list.clear();
        if (timGroupMemberInfos == null) return;
        for (TIMGroupMemberInfo item : timGroupMemberInfos) {
            list.add(new GroupMemberProfile(item));
        }
        Log.e(TAG, "onSuccess: 1");
        getFaceUrl();
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MEM_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                boolean isKick = data.getBooleanExtra("isKick", false);
                if (isKick) {
                    list.remove(memIndex);
                    adapter.notifyDataSetChanged();
                } else {
                    GroupMemberProfile profile = (GroupMemberProfile) data.getSerializableExtra("data");
                    if (memIndex < list.size() && list.get(memIndex).getIdentify().equals(profile.getIdentify())) {
                        GroupMemberProfile mMemberProfile = (GroupMemberProfile) list.get(memIndex);
                        mMemberProfile.setRoleType(profile.getRole());
                        mMemberProfile.setQuietTime(profile.getQuietTime());
                        mMemberProfile.setName(profile.getNameCard());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        } else if (CHOOSE_MEM_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                GroupManagerPresenter.inviteGroup(groupId, data.getStringArrayListExtra("select"),
                        new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(GroupMemberActivity.this, getString(R.string.chat_setting_invite_error), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                                TIMGroupManagerExt.getInstance().getGroupMembers(groupId, GroupMemberActivity.this);
                            }
                        });

            }
        }
    }


    private void getFaceUrl() {
        identifierList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String identify = list.get(i).getIdentify();
            identifierList.add(identify);
        }
        TIMFriendshipManager.getInstance().getUsersProfile(identifierList, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(final List<TIMUserProfile> timUserProfiles) {
                userList = timUserProfiles;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new GroupMemberAdapter(GroupMemberActivity.this, R.layout.item_profile_summary, userList);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

}
