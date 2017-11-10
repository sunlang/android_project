package com.yunnex.canteen.takeout.bean;

/**
 * Created by sungongyan on 2016/1/5.
 * wechat sun379366152
 */
public class Time {

    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "{\"startTime\":\"" + startTime + "\", \"endTime\":\""+ endTime + "\"}";
    }
}
