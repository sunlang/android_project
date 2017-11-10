package com.yunnex.canteen.takeout.http.response;

import com.yunnex.vpay.lib.http.ResponseBase;

/**
 * Created by sungongyan on 2016/1/8.
 * wechat sun379366152
 */
public class NoticeGettingRes extends ResponseBase{

    private String notice;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
