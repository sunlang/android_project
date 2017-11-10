package com.yunnex.canteen.takeout.base;

/**
 * Created by sungongyan on 2016/1/18.
 * wechat sun379366152
 */
public class Constant
{

	public static String selectedSourcePosition = "selectedSourcePosition";

	public static String requestTime = "requestTime";

	public static String TAG = "canteen";


	/**
	 * 1轮询
	 */
	public static String urlPollQuery = "http://saofu.client.zb25.com" + //
			".cn/saofu-client/canyin/waimai/order/query";

	/**
	 * 10获取查询条件，订单类型和状态
	 */
	public static String urlGetTypeStatus = "order_type_status";


	/**
	 * 3新的订单列表(包括外卖，堂食)
	 */
	public static String urlOrderPagelist = "order_page_list";

	/**
	 * 4订单详情(有3个，外卖，加菜，堂食)
	 */
	public static String urlWmOrderDetail = "waimai_order_item";//外卖订单详情

	/**
	 * 新的迁移后的外卖订单详情
	 */
	public static String urlWmOrderDetail2 = "cywaimai_order_item";


	/**
	 * 5外卖订单确认
	 */
	public static String urlWmOrderSure  = "waimai_sure_action";//确认订单
	public static String urlWmOrderSure2 = "cywaimai_sure_action";//确认订单

	/**
	 * 6外卖订单取消
	 */
	public static String urlWmOrderCancel = "waimai_cancel_action";//取消订单
	public static String urlWmOrderCancel2 = "cywaimai_cancel_action";//取消订单

	/**
	 * 7外卖订单开始配送
	 */
	public static String urlWmOrderDeliverAction  = "waimai_deliver_action";//配送订单
	public static String urlWmOrderDeliverAction2 = "cywaimai_waimai_deliver_action";//配送订单

	/**
	 * 8外卖订单已送达
	 */
	public static String urlWmOrderFinishAction = "waimai_finish_action";//已送达订单
	public static String urlWmOrderFinishAction2 = "cywaimai_waimai_finish_action";//已送达订单
	/**
	 * 9订单搜索条件查询
	 */
	public static String urlSearchCondition     = "order_condition";


	/**
	 * 11获取门店各渠道营业状态
	 */
	public static String urlRunningStasus = "http://saofu.client.zb25.com" + //
			".cn/saofu-client/chuping/waimai/wmconfig/update_remind_status";

	/**
	 * 12获取门店订单提醒的营业时间
	 */
	public static String urlRunningTime = "http://saofu.client.zb25.com" + //
			".cn/saofu-client/chuping/waimai/wmconfig/remind";


	/**
	 * 13日结账单
	 */
	public static String urlDayBill = "daily_for_app";

	/**
	 * 14打印
	 */

	/**
	 * 15菜品管理(沽清动作)
	 */


	/**
	 * 16门店信息
	 */
	public static String urlShop = "waimai_shop";

	/**
	 * 商品分类信息
	 */
	public static String urlCategoryList = "category_list";

	/**
	 * 查询商品列表
	 */
	public static String urlGoodsList = "goods_list";

	/**
	 * 商品详情
	 */
	public static String urlGoodsDetail = "goods_detail";

	/**
	 * 修改商品销售状态
	 */
	public static String urlUpdateSoldStatus = "update_sold_status";


}
