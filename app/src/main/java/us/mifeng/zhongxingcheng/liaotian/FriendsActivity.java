package us.mifeng.zhongxingcheng.liaotian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipManageView;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.StrAccountLogin;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.utils.OkUtils;
import com.tencent.qcloud.tlslibrary.utils.SharedUtils;
import com.tencent.qcloud.ui.NotifyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;
import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.adapter.LXRAdapter;
import us.mifeng.zhongxingcheng.bean.LXRBean;
import us.mifeng.zhongxingcheng.liaotian.model.GroupInfo;
import us.mifeng.zhongxingcheng.utils.ToSi;
import us.mifeng.zhongxingcheng.utils.WangZhi;


public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FriendshipManageView, StrAccountLogin.LoginListener, View.OnClickListener, AbsListView.OnScrollListener {
    private static final String TAG = "Fragment_LianXiRen";
    private List<LXRBean> list;
    private List<LXRBean> showList;
    private ListView lv;
    private LXRAdapter adapter;
    private String password = "123456789";
    private TLSService tlsService;
    private String mobile;
    private StrAccRegListener strAccRegListener;
    private FriendshipManagerPresenter presenter;
    private StrAccountLogin login;
    private String token;
    private AlertDialog showDialog2;
    private RelativeLayout rela_sao;
    private RelativeLayout rela_add;
    private RelativeLayout rela_chats;
    private LinearLayout qunliao, tianjia;
    private int startItem = 0;
    private int endItem = 20;
    private boolean loadFlag;
    private View inflate;
    private ProgressBar mBar;
    private int lastVisIdnex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initView();
        SharedUtils sharedUtils = new SharedUtils();
        token = sharedUtils.getShared("token", FriendsActivity.this);
        Log.e(TAG, "onCreate: "+token );
        tlsService = TLSService.getInstance();
        strAccRegListener = new StrAccRegListener();
        login = new StrAccountLogin(this);
        presenter = new FriendshipManagerPresenter(this);
        showList = new ArrayList<>();
        initLianWang();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.swipe_target);
        ImageView add = (ImageView) findViewById(R.id.txl_add);
        ImageView back = (ImageView) findViewById(R.id.txl_back);
        qunliao = (LinearLayout) findViewById(R.id.txl_qunliao);
        tianjia = (LinearLayout) findViewById(R.id.txl_tianjia);
        inflate = View.inflate(FriendsActivity.this, R.layout.footerview, null);
        mBar = (ProgressBar) inflate.findViewById(R.id.mBar);
        list = new ArrayList<>();
        qunliao.setOnClickListener(this);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        showDialog2 = showDialog2();
        lv.setOnItemClickListener(this);
        lv.setOnScrollListener(this);
    }


    private void initLianWang() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        OkUtils.UploadSJ(WangZhi.HAOYOU, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string());
                String string = response.body().string();
                Message mess = hand.obtainMessage();
                mess.obj = string;
                mess.what = 200;
                hand.sendMessage(mess);
            }
        });
    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                String str = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String msg2 = jsonObject.getString("msg");
                    if ("2".equals(msg2)){
                        ToSi.show(FriendsActivity.this,"暂无好友");
                    }
                    else if ("1".equals(msg2)){
                        ToSi.show(FriendsActivity.this,"参数不对");
                    }else if ("0".equals(msg2)) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray msg1 = data.getJSONArray("list");

                        for (int i = 0; i < msg1.length(); i++) {
                            JSONObject jsonObject1 = msg1.getJSONObject(i);
                            String mobile = jsonObject1.getString("mobile");
                            String vipLevel = jsonObject1.getString("vipLevel");
                            LXRBean lxrBean = new LXRBean();
                            lxrBean.setMobile(mobile);
                            lxrBean.setViplevel(vipLevel);
                            list.add(lxrBean);
                        }
                        if (adapter==null){
                            adapter = new LXRAdapter(FriendsActivity.this,list);
                            lv.setAdapter(adapter);
                        }else {
                            lv.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

//    private void load() {
//        for (int i = startItem; i < endItem; i++) {
//            showList.add(list.get(i));
//        }
//        //有数据
//        if (list.size() - endItem > 0) {
//            //如果数据不够20条
//            if (list.size() - endItem < 20) {
//                startItem = endItem;
//                endItem = list.size();
//            } else {
//                startItem = endItem;
//                endItem += 20;
//            }
//        } else {
//            loadFlag = true;
//        }
//        if (adapter == null) {
//            adapter = new LXRAdapter(this, showList);
//            lv.setAdapter(adapter);
//        } else {
//            adapter.notifyDataSetChanged();
//        }
//    }

    private AlertDialog showDialog2() {
        View inflate = LayoutInflater.from(FriendsActivity.this).inflate(R.layout.conversation_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
        builder.setView(inflate);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.dismiss();
        Window window = alertDialog.getWindow();
        WindowManager windowManager = FriendsActivity.this.getWindowManager();
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
        tianjia.setOnClickListener(this);
        return alertDialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LXRBean bean = (LXRBean) parent.getAdapter().getItem(position);
        mobile = bean.getMobile() + "a";
        Log.e(TAG, "onItemClick: " + mobile);
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("identify",mobile);
//        intent.putExtra("type", TIMConversationType.C2C);
//        startActivity(intent);
        int result = tlsService.TLSStrAccReg(mobile, password, strAccRegListener);
        if (result == TLSErrInfo.INPUT_INVALID) {
            Util.showToast(this, "帐号不合法");
        }
    }

    @Override
    public void login() {
        presenter.addFriend(mobile, "小明", "", "你好我是小明");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txl_add:
                if (showDialog2 != null) {
                    if (showDialog2.isShowing()) {
                        showDialog2.dismiss();
                    } else {
                        showDialog2.show();
                    }
                }
                break;
            case R.id.txl_back:
                finish();
                break;
            case R.id.rela_add:
                startActivity(new Intent(FriendsActivity.this, SearchFriendActivity.class));
                break;
            case R.id.rela_chat:
                Intent intent = new Intent(FriendsActivity.this, GroupListActivity.class);
                intent.putExtra("type", GroupInfo.publicGroup);
                startActivity(intent);
                break;
            case R.id.rela_erweima:
                startActivity(new Intent(FriendsActivity.this, SearchGroupActivity.class));
                break;
            case R.id.txl_qunliao:
                Intent intent1 = new Intent(FriendsActivity.this, GroupListActivity.class);
                intent1.putExtra("type", GroupInfo.publicGroup);
                startActivity(intent1);
                break;
            case R.id.txl_tianjia:
                startActivity(new Intent(FriendsActivity.this, SearchFriendActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE&&lastVisIdnex==adapter.getCount()){
            //确认滑倒底部 加载更多
            mBar.setVisibility(View.VISIBLE);
            initLianWang();

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastVisIdnex=firstVisibleItem+visibleItemCount-1;
    }

    class StrAccRegListener implements TLSStrAccRegListener {

        @Override
        public void OnStrAccRegSuccess(TLSUserInfo userInfo) {
            TLSService.getInstance().setLastErrno(0);
            login.doStrAccountLogin1(mobile, password);
        }

        @Override
        public void OnStrAccRegFail(TLSErrInfo errInfo) {
            presenter.addFriend(mobile, "小明", "", "你好我是小明");
            Util.notOK(FriendsActivity.this, errInfo);
        }

        @Override
        public void OnStrAccRegTimeout(TLSErrInfo errInfo) {
            Util.notOK(FriendsActivity.this, errInfo);
        }

    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
                Toast.makeText(this, "请求已发送", Toast.LENGTH_SHORT).show();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, "已添加好友", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, ProfileActivity.class);
//                intent.putExtra("yonghu", mobile);
//                startActivity(intent);
                break;
            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                Toast.makeText(this, "对方拒绝添加任何人为好友", Toast.LENGTH_SHORT).show();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                Toast.makeText(this, "我在对方的黑名单中", Toast.LENGTH_SHORT).show();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.add_friend_del_black_list), this.getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FriendshipManagerPresenter.delBlackList(Collections.singletonList(mobile + ""), new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(FriendsActivity.this, getResources().getString(R.string.add_friend_del_black_err), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                Toast.makeText(FriendsActivity.this, getResources().getString(R.string.add_friend_del_black_succ), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            default:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                intent1.putExtra("identify", mobile);
                startActivity(intent1);
                Toast.makeText(this, getResources().getString(R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {

    }

}