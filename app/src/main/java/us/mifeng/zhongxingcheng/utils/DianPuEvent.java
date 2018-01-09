package us.mifeng.zhongxingcheng.utils;

import us.mifeng.zhongxingcheng.bean.ShouCangBean;

/**
 * Created by shido on 2018/1/9.
 */

public class DianPuEvent {
    private ShouCangBean mMsg;
    public DianPuEvent(ShouCangBean msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }
    public ShouCangBean getMsg(){
        return mMsg;
    }
}
