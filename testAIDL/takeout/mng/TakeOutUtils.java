package com.yunnex.canteen.takeout.mng;

/**
 * Created by lion on 16/1/7.
 */
public class TakeOutUtils
{
	//	商品状态

	// 操作状态
	public static final int ACTION_SET_STORE_NUM = 3;    // 单纯的修改库存
	public static final int ACTION_SOLD_OUT      = 1;    // 触发售完操作
	public static final int ACTION_RESTORE       = 2;    // 触发恢复售卖操作

	//订单来源序列号
	public static final int SERIALNUM_NULL = 0;//序列号为为null（int值则默认为0）


}
