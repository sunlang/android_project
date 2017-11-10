package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.Time;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 *
 * @Description:    ShopTimeResponse
 *
 * @author Xing.Zheng
 * @version 1.0
 * @created at 2016/1/11 15:28
 * @Copyright (c) 2015云移科技-版权所有
 */

public class ShopTimeResponse extends ResponseBase {

    private List<Time> runningTimes;

    private ShopHoursResponse response;

    public ShopHoursResponse getResponse()
    {
        return response;
    }

    public void setResponse(ShopHoursResponse response)
    {
        this.response = response;
    }

    public List<Time> getRunningTimes() {
        return runningTimes;
    }

    public void setRunningTimes(List<Time> runningTimes) {
        this.runningTimes = runningTimes;
    }

    //==============================================

    /**
     * 以下是适配轮询异常单这类需要的营业时间
     */

    public class ShopHoursResponse
    {

        private List<Time> shopHours;

        public List<Time> getShopHours()
        {
            return shopHours;
        }

        public void setShopHours(List<Time> shopHours)
        {
            this.shopHours = shopHours;
        }
    }
}
