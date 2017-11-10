package com.yunnex.canteen.takeout.http.request;

import com.yunnex.canteen.takeout.bean.Time;

import java.util.List;

/**
 *
 * @Description:    ShopTimeRequest
 *
 * @author Xing.Zheng
 * @version 1.0
 * @created at 2016/1/11 15:28
 * @Copyright (c) 2015云移科技-版权所有
 */

public class ShopTimeRequest {

    List<Time> time;

    public List<Time> getTime() {
        return time;
    }

    public void setTime(List<Time> time) {
        this.time = time;
    }

}
