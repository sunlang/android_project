package com.yunnex.canteen.takeout.mng;

import com.yunnex.canteen.common.CanteenApplication;
import com.yunnex.vpay.lib.oem.OemUtils;
import com.yunnex.vpay.lib.utils.RegisterUtils;

/**
 * Created by Administrator on 2015/9/28.
 */
public class TakeOutHttpUtils
{

	//模块 eshop
	public static final String MODDE_PRODER = "porder";

	//模块
	public static final String WM_MODDE_PRODER = "wmporder";
	// 拉取数据
	public static final String PATTERN         = "takeOut";
	public static final String FUN_DAY_BILL    = "static_list";

	// 拉取数据
	public static final String FUN_ORDER_ITEM        = "item";
	public static final String FUN_ORDER_LIST        = "list";
	public static final String FUN_ORDER_SEARCH_LIST = "order_search";
	public static final String FUN_ORDER_SDC_LIST    = "order_sdc_list";
	public static final String FUN_CANCEL_REASON     = "cancel_reason";

	//外卖2.2.5 function
	public static final String FUN_SHOP                   = "shop";
	public static final String FUN_CATEGORY_LIST          = "categoryList";
	public static final String FUN_DISH_LIST              = "dishList";
	public static final String FUN_DISH_DETAIL            = "dishDetail";
	public static final String FUN_DISH_SEARCH            = "dish_search";
	public static final String FUN_TIME_SETTING           = "time_setting";
	public static final String FUN_TIME_GETTING           = "time_getting";
	public static final String FUN_RUNNING_STATUS_SETTING = "runningStatus_setting";
	public static final String FUN_DISH_STORENUM_SETTING  = "dish_storeNum_setting";
	public static final String FUN_NOTICE_SETTING         = "notice_setting";
	public static final String FUN_NOTICE_Getting         = "notice_getting";
	// 动作，由于外卖订单于version3.7分离，该处动作废弃，见以下分离后的动作定义
	//	public static final String FUN_ACTION_SURE            = "action_sure";
	//	public static final String FUN_ACTION_DELIVER         = "action_deliver";
	//	public static final String FUN_ACTION_CANCEL          = "action_cancel";
	//	public static final String FUN_ACTION_FINISH          = "deliver_finish";

	//	外卖订单分离后的动作
	public static final String FUN_ACTION_SURE    = "sureAction";
	public static final String FUN_ACTION_DELIVER = "deliverAction";
	public static final String FUN_ACTION_CANCEL  = "cancelAction";
	public static final String FUN_ACTION_FINISH  = "finishAction";

	public static final String FUN_ACTION_OrderDelay    = "confirmDelayedOrder";

	public static String getUrl(String function)
	{
		return OemUtils.getOemUrl(CanteenApplication.getmApplication(), PATTERN, function);
	}

	public static boolean setUrl(String function, String value)
	{
		return RegisterUtils.setInfo(CanteenApplication.getmApplication(), PATTERN + "_" + function + ".url", value);
	}

	public static String getWMProderUrl(String function)
	{
		return OemUtils.getOemUrl(CanteenApplication.getmApplication(), WM_MODDE_PRODER, function);
	}

	public static String getProderUrl(String function)
	{
		return OemUtils.getOemUrl(CanteenApplication.getmApplication(), MODDE_PRODER, function);
	}

}
