package com.yunnex.canteen.takeout.bean;

/**
 * Created by sungongyan on 2017/7/18.
 * qq 379366152
 * <p>
 * sources 订单来源1 pos 2 微信 3触屏端 4 百度外卖 5 美团外卖 6 饿了么外卖 7微信外卖
 * <p>
 * 智慧餐厅内部订单状态orderStatus:1待支付  2 已下单 3 完成 4 关闭 5待审核(待审核的单代表着新订单)
 */

/**
 * sources 订单来源1 pos 2 微信 3触屏端 4 百度外卖 5 美团外卖 6 饿了么外卖 7微信外卖
 */

/**
 智慧餐厅内部订单状态orderStatus:1待支付  2 已下单 3 完成 4 关闭 5待审核(待审核的单代表着新订单)
 */


/**
 * 外卖订单状态waimaiStatus: 2待确认  4 待配送   6 待收货
 * 8待配送(未付款)   9 待收货(未付款)")   90 关闭 100 完成
 */
public class OrderDtoSub
{

	public static final int TYPE_TS = 100;
	public static final int TYPE_WM = 200;

	public static final int SOURCE_POS  = 1;
	public static final int SOURCE_WX   = 2;
	public static final int SOURCE_CPD  = 3;
	public static final int SOURCE_BD   = 4;
	public static final int SOURCE_MT   = 5;
	public static final int SOURCE_ELE  = 6;
	public static final int SOURCE_WXWM = 7;


	public static final int STATU_TS_DFK = 1;
	public static final int STATU_TS_YXD = 2;
	public static final int STATU_TS_WC  = 3;
	public static final int STATU_TS_GB  = 4;
	public static final int STATU_TS_DSH = 5;

	public static final int STATU_WM_DQR     = 2;
	public static final int STATU_WM_DPS     = 4;
	public static final int STATU_WM_DSH     = 6;
	public static final int STATU_WM_DPS_WFK = 8;
	public static final int STATU_WM_DSH_WFK = 9;
	public static final int STATU_WM_GB      = 90;
	public static final int STATU_WM_WC      = 100;
	/**
	 * {
	 * "checkingAmount":0,
	 * "createTime":1500529418000,//订单创建时间
	 * "customerName":"Janus",
	 * "customerPhone":"0",
	 * "goodsAmount":2,//订单商品数量
	 * "goodsFee":0,
	 * "newOrder":0,//是否是新订单
	 * "orderId":"17887910851422588928",//订单id
	 * "orderStatus":1,//订单状态
	 * "orderType":100,//订单类型
	 * "payStatus":0,//支付状态
	 * "personAmount":3,
	 * "serialNo":10,//序列号
	 * "sources":2,//订单来源
	 * "tableId":0,
	 * "tableName":"",
	 * "totalFee":0,//订单总金额
	 * "waimaiOrderId":"",
	 * "zbOrderId":""
	 * "isAddDish":0//是否是加菜单
	 * "payFee":0//支付金额，订单列表使用的是这个
	 * }
	 */

	private String orderId;
	private String waimaiOrderId;

	private long createTime;

	private int goodsAmount;

	private long totalFee;
	private long payFee;

	private int newOrder;

	private int orderStatus;//堂食订单状态
	private int waimaiStatus;//外卖订单状态

	private int orderType;

	private int serialNo;

	private int sources;

	private int sendImmediately = -1;

	private int isAddDish;

	private int payStatus;
	private int pushStatus;

	private int apiVersion;

	public int getApiVersion()
	{
		return apiVersion;
	}

	public void setApiVersion(int apiVersion)
	{
		this.apiVersion = apiVersion;
	}

	public int getPushStatus()
	{
		return pushStatus;
	}

	public void setPushStatus(int pushStatus)
	{
		this.pushStatus = pushStatus;
	}

	public int getWaimaiStatus()
	{
		return waimaiStatus;
	}

	public void setWaimaiStatus(int waimaiStatus)
	{
		this.waimaiStatus = waimaiStatus;
	}

	public long getPayFee()
	{
		return payFee;
	}

	public void setPayFee(long payFee)
	{
		this.payFee = payFee;
	}

	public String getWaimaiOrderId()
	{
		return waimaiOrderId;
	}

	public void setWaimaiOrderId(String waimaiOrderId)
	{
		this.waimaiOrderId = waimaiOrderId;
	}

	public int getPayStatus()
	{
		return payStatus;
	}

	public void setPayStatus(int payStatus)
	{
		this.payStatus = payStatus;
	}

	public int getIsAddDish()
	{
		return isAddDish;
	}

	public void setIsAddDish(int isAddDish)
	{
		this.isAddDish = isAddDish;
	}

	public int getSendImmediately()
	{
		return sendImmediately;
	}

	public void setSendImmediately(int sendImmediately)
	{
		this.sendImmediately = sendImmediately;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public int getGoodsAmount()
	{
		return goodsAmount;
	}

	public void setGoodsAmount(int goodsAmount)
	{
		this.goodsAmount = goodsAmount;
	}

	public long getTotalFee()
	{
		return totalFee;
	}

	public void setTotalFee(long totalFee)
	{
		this.totalFee = totalFee;
	}

	public int getNewOrder()
	{
		return newOrder;
	}

	public void setNewOrder(int newOrder)
	{
		this.newOrder = newOrder;
	}

	public int getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public int getOrderType()
	{
		return orderType;
	}

	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}

	public int getSerialNo()
	{
		return serialNo;
	}

	public void setSerialNo(int serialNo)
	{
		this.serialNo = serialNo;
	}

	public int getSources()
	{
		return sources;
	}

	public void setSources(int sources)
	{
		this.sources = sources;
	}

	@Override
	public String toString()
	{
		return "OrderDtoSub{" + "orderId='" + orderId + '\'' + ", createTime=" + createTime + ", goodsAmount=" + goodsAmount + ", totalFee=" + totalFee + ", newOrder=" + newOrder + ", orderStatus=" + orderStatus + ", orderType=" + orderType + ", serialNo=" + serialNo + ", sources=" + sources + ", sendImmediately=" + sendImmediately + '}';
	}
}
