package us.mifeng.zhongxingcheng.liaotian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.qcloud.ui.CircleImageView;
import com.tencent.qcloud.ui.LineControllerView;
import com.tencent.qcloud.ui.ListPickerDialog;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.model.GroupInfo;
import us.mifeng.zhongxingcheng.liaotian.model.GroupMemberProfile;
import us.mifeng.zhongxingcheng.liaotian.model.UserInfo;

public class GroupMemberProfileActivity extends FragmentActivity {

    private String userIdentify, groupIdentify, userCard, groupType;
    private TIMGroupMemberRoleType currentUserRole;
    private GroupMemberProfile profile;
    private LineControllerView setManager;
    private String[] quietingOpt;
    private String[] quietOpt;
    private long[] quietTimeOpt = new long[]{600, 3600, 24 * 3600};
    private final int CARD_REQ = 100;
    private String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_profile);
        profile = (GroupMemberProfile) getIntent().getSerializableExtra("data");
        //userIdentify = profile.getIdentify();
        groupIdentify = getIntent().getStringExtra("groupId");
        userIdentify= getIntent().getStringExtra("userId");
        groupType = getIntent().getStringExtra("type");
        avatarUrl=getIntent().getStringExtra("faceUrl");
        Log.e("000", "onCreate: "+userIdentify);
        userCard = profile.getNameCard();
        currentUserRole = GroupInfo.getInstance().getRole(groupIdentify);
        quietingOpt = new String[]{getString(R.string.group_member_quiet_cancel)};
        quietOpt = new String[]{getString(R.string.group_member_quiet_ten_min),
                getString(R.string.group_member_quiet_one_hour),
                getString(R.string.group_member_quiet_one_day),
        };

        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(this.userIdentify);
        CircleImageView img = (CircleImageView) findViewById(R.id.avatar);
        if (avatarUrl != null) {
            Glide.with(this).load(avatarUrl).into(img);
        }
        TextView tvKick = (TextView) findViewById(R.id.kick);
        tvKick.setVisibility(canManage() && !groupType.equals(GroupInfo.privateGroup) ? View.VISIBLE : View.GONE);
        tvKick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMGroupManagerExt.DeleteMemberParam param = new TIMGroupManagerExt.DeleteMemberParam(groupIdentify, Collections.singletonList(GroupMemberProfileActivity.this.userIdentify));

                TIMGroupManagerExt.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_del_err), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                        Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_del_succ), Toast.LENGTH_SHORT).show();
                        setBackResult(true);
                        finish();
                    }
                });
            }
        });
        setManager = (LineControllerView) findViewById(R.id.manager);
        setManager.setVisibility(currentUserRole == TIMGroupMemberRoleType.Owner && currentUserRole != profile.getRole() && !groupType.equals(GroupInfo.privateGroup) ? View.VISIBLE : View.GONE);
        setManager.setSwitch(profile.getRole() == TIMGroupMemberRoleType.Admin);
        setManager.setCheckListener(checkListener);
        final LineControllerView setQuiet = (LineControllerView) findViewById(R.id.setQuiet);
        setQuiet.setVisibility(canManage() && !groupType.equals(GroupInfo.privateGroup) ? View.VISIBLE : View.GONE);
        if (canManage()) {
            if (isQuiet()) {
                setQuiet.setContent(getString(R.string.group_member_quiet_ing));
            }
            setQuiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ListPickerDialog().show(getQuietOption(), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupIdentify, GroupMemberProfileActivity.this.userIdentify);
                            if (!isQuiet()) {
                                param.setSilence(quietTimeOpt[which]);
                            } else {
                                param.setSilence(0);
                            }
                            TIMGroupManagerExt.getInstance().modifyMemberInfo(param,
                                    new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_quiet_err), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSuccess() {
                                            if (getQuietTime(which) == 0) {
                                                setQuiet.setContent("");
                                            } else {
                                                setQuiet.setContent(getString(R.string.group_member_quiet_ing));
                                            }
                                            profile.setQuietTime(getQuietTime(which) + Calendar.getInstance().getTimeInMillis() / 1000);
                                        }
                                    });
                        }
                    });
                }
            });
        }
        LineControllerView nameCard = (LineControllerView) findViewById(R.id.groupCard);
        nameCard.setContent(userCard);
        if (UserInfo.getInstance().getId().equals(this.userIdentify)) {
            nameCard.setCanNav(true);
            nameCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditActivity.navToEdit(GroupMemberProfileActivity.this, getResources().getString(R.string.group_member_change_card), userCard, CARD_REQ, new EditActivity.EditInterface() {
                        @Override
                        public void onEdit(String text, TIMCallBack callBack) {
                            TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupIdentify, GroupMemberProfileActivity.this.userIdentify);
                            param.setNameCard(text);
                            TIMGroupManagerExt.getInstance().modifyMemberInfo(param, callBack);
                        }
                    }, 20);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        setBackResult(false);
        super.onBackPressed();
    }


    private boolean canManage() {
        if ((currentUserRole == TIMGroupMemberRoleType.Owner && profile.getRole() != TIMGroupMemberRoleType.Owner) ||
                (currentUserRole == TIMGroupMemberRoleType.Admin && profile.getRole() == TIMGroupMemberRoleType.Normal))
            return true;
        return false;
    }

    private String[] getQuietOption() {
        if (!isQuiet()) {
            return quietOpt;
        } else {
            return quietingOpt;
        }
    }

    private long getQuietTime(int which) {
        if (!isQuiet()) {
            return quietTimeOpt[which];
        }
        return 0;
    }

    private void setBackResult(boolean isKick) {
        Intent mIntent = new Intent();
        mIntent.putExtra("data", profile);
        mIntent.putExtra("isKick", isKick);
        setResult(RESULT_OK, mIntent);
    }

    private final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            TIMGroupManagerExt.ModifyMemberInfoParam param = new TIMGroupManagerExt.ModifyMemberInfoParam(groupIdentify, userIdentify);
            param.setRoleType(isChecked ? TIMGroupMemberRoleType.Admin : TIMGroupMemberRoleType.Normal);
            TIMGroupManagerExt.getInstance().modifyMemberInfo(param,
                    new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            switch (i) {
                                case 10004:
                                    Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_type_err), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_err), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            //防止循环调用
                            setManager.setCheckListener(null);
                            setManager.setSwitch(!isChecked);
                            setManager.setCheckListener(checkListener);
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_succ), Toast.LENGTH_SHORT).show();
                            profile.setRoleType(isChecked ? TIMGroupMemberRoleType.Admin : TIMGroupMemberRoleType.Normal);
                        }
                    });
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARD_REQ) {
            if (resultCode == RESULT_OK) {
                LineControllerView nameCard = (LineControllerView) findViewById(R.id.groupCard);
                nameCard.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
                profile.setName(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }


    private boolean isQuiet() {
        if (profile == null) return false;
        return profile.getQuietTime() != 0 && profile.getQuietTime() > Calendar.getInstance().getTimeInMillis() / 1000;
    }
}
