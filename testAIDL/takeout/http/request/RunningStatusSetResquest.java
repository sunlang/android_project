package com.yunnex.canteen.takeout.http.request;

/**
 * Created by sungongyan on 2015/12/28.
 * wechat sun379366152
 */
public class RunningStatusSetResquest {

    private int platform;
    private int runningStatus;

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(int runningStatus) {
        this.runningStatus = runningStatus;
    }
}
