package us.mifeng.zhongxingcheng.liaotian;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipManageView;
import com.tencent.qcloud.ui.CircleImageView;
import com.tencent.qcloud.ui.LineControllerView;
import com.tencent.qcloud.ui.ListPickerDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.FriendshipInfo;

//好友资料界面
public class ProfileActivity extends FragmentActivity implements FriendshipManageView, View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private final int CHANGE_CATEGORY_CODE = 100;
    private final int CHANGE_REMARK_CODE = 200;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private String identify, categoryStr;
    private List<String> list;
    private String remark1;
    private String nickName;
    private String faceUrl;

    public static void navToProfile(Context context, String identify) {
        /*Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("identify", identify);
        context.startActivity(intent);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        identify = getIntent().getStringExtra("identify");
        list = new ArrayList<>();
        list.add(identify);
        Log.e(TAG, "onCreate: " + identify);
        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        if (!TextUtils.isEmpty(identify)) {
            getFriendMsg();
        }
    }

    private void getFriendMsg(){
        TIMFriendshipManagerExt.getInstance().getFriendsProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                TIMUserProfile userProfile = timUserProfiles.get(0);
                faceUrl = userProfile.getFaceUrl();
                nickName = userProfile.getNickName();
                remark1 = userProfile.getRemark();
                showProfile(identify);
            }
        });
    }

    /**
     * 显示用户信息
     *
     * @param identify
     */
    public void showProfile(final String identify) {
        /*final FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
        if (profile == null) return;
        String avatarUrl = profile.getAvatarUrl();*/
        CircleImageView img = (CircleImageView) findViewById(R.id.avatar);
        Log.e(TAG, "showProfile: "+faceUrl );
        if (!faceUrl .equals("")) {
            Glide.with(this).load(faceUrl).into(img);
        }else {
            img.setImageResource(R.mipmap.head_other);
        }
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(nickName);
        LineControllerView id = (LineControllerView) findViewById(R.id.id);
        id.setContent(identify);
        final LineControllerView remark = (LineControllerView) findViewById(R.id.remark);
        remark.setContent(remark1);
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.navToEdit(ProfileActivity.this, getString(R.string.profile_remark_edit), remark.getContent(), CHANGE_REMARK_CODE, new EditActivity.EditInterface() {
                    @Override
                    public void onEdit(String text, TIMCallBack callBack) {
                        FriendshipManagerPresenter.setRemarkName(identify, text, callBack);
                    }
                }, 20);

            }
        });
//        LineControllerView category = (LineControllerView) findViewById(R.id.group);
        //一个用户可以在多个分组内，客户端逻辑保证一个人只存在于一个分组
        //category.setContent(categoryStr = profile.getGroupName());
        LineControllerView black = (LineControllerView) findViewById(R.id.blackList);
        black.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FriendshipManagerPresenter.addBlackList(Collections.singletonList(identify), new TIMValueCallBack<List<TIMFriendResult>>() {
                        @Override
                        public void onError(int i, String s) {
                            Log.e(TAG, "add black list error " + s);
                        }

                        @Override
                        public void onSuccess(List<TIMFriendResult> timFriendResults) {
                            if (timFriendResults.get(0).getStatus() == TIMFriendStatus.TIM_FRIEND_STATUS_SUCC) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.profile_black_succ), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChat:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", identify);
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDel:
                friendshipManagerPresenter.delFriend(identify);
                break;
            case R.id.group:
                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
                for (int i = 0; i < groups.length; ++i) {
                    if (groups[i].equals("")) {
                        groups[i] = getString(R.string.default_group_name);
                        break;
                    }
                }
                new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (groups[which].equals(categoryStr)) return;
                        friendshipManagerPresenter.changeFriendGroup(identify,
                                categoryStr.equals(getString(R.string.default_group_name)) ? null : categoryStr,
                                groups[which].equals(getString(R.string.default_group_name)) ? null : groups[which]);
                    }
                });
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_CATEGORY_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView category = (LineControllerView) findViewById(R.id.group);
                category.setContent(categoryStr = data.getStringExtra("category"));
            }
        } else if (requestCode == CHANGE_REMARK_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView remark = (LineControllerView) findViewById(R.id.remark);
                remark.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));

            }
        }

    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, getResources().getString(R.string.profile_del_succeed), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_FRIEND_STATUS_UNKNOWN:
                Toast.makeText(this, getResources().getString(R.string.profile_del_fail), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {
        LineControllerView category = (LineControllerView) findViewById(R.id.group);
        if (groupName == null) {
            groupName = getString(R.string.default_group_name);
        }
        switch (status) {
            case TIM_FRIEND_STATUS_UNKNOWN:
                Toast.makeText(this, getString(R.string.change_group_error), Toast.LENGTH_SHORT).show();
            case TIM_FRIEND_STATUS_SUCC:
                category.setContent(groupName);
                FriendshipEvent.getInstance().OnFriendGroupChange();
                break;
            default:
                Toast.makeText(this, getString(R.string.change_group_error), Toast.LENGTH_SHORT).show();
                category.setContent(getString(R.string.default_group_name));
                break;
        }
    }
}
