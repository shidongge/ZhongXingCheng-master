package us.mifeng.zhongxingcheng.liaotian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import us.mifeng.zhongxingcheng.R;
import us.mifeng.zhongxingcheng.liaotian.adapter.ExpandGroupListAdapter;
import us.mifeng.zhongxingcheng.liaotian.model.FriendProfile;
import us.mifeng.zhongxingcheng.liaotian.model.FriendshipInfo;


public class ChooseFriendActivity extends Activity {

    private ExpandGroupListAdapter mGroupListAdapter;
    private ListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<>();
    private List<String> mAlreadySelect = new ArrayList<>();
    private List<FriendProfile> alreadySelectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        ImageView back = (ImageView) findViewById(R.id.xzlxr_back);
        TextView title = (TextView) findViewById(R.id.chooseTitle);
        List<String> selected = getIntent().getStringArrayListExtra("selected");
        if (selected != null) {
            mAlreadySelect.addAll(selected);
        }
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.size() == 0) {
                    Toast.makeText(ChooseFriendActivity.this, getString(R.string.choose_need_one), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("select", getSelectIds());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        for (String id : mAlreadySelect) {
            for (String key : friends.keySet()) {
                for (FriendProfile profile : friends.get(key)) {
                    if (id.equals(profile.getIdentify())) {
                        profile.setIsSelected(true);
                        alreadySelectList.add(profile);
                    }
                }
            }
        }
        mGroupListView = (ListView) findViewById(R.id.groupList);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendProfile profile = friends.get(FriendshipInfo.getInstance().getGroups().get(0)).get(position);
                if (!alreadySelectList.contains(profile)) {
                    onSelect(profile);
                    mGroupListAdapter.notifyDataSetChanged();
                }
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (FriendProfile item : selectList) {
            item.setIsSelected(false);
        }
        for (FriendProfile item : alreadySelectList) {
            item.setIsSelected(false);
        }
    }

    private void onSelect(FriendProfile profile) {
        if (!profile.isSelected()) {
            selectList.add(profile);
        } else {
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds() {
        ArrayList<String> result = new ArrayList<>();
        for (FriendProfile item : selectList) {
            result.add(item.getIdentify());
        }
        return result;
    }
}
