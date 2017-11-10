package com.yunnex.canteen.takeout.bean;

import java.util.List;

/**
 * Created by songdan on 2015/12/23.
 */
public class Order
{

	public static final int ORDER_STATUS_DPS = 4;//待配送
	public static final int ORDER_STATUS_DSH = 6;//待收货
	public static final int ORDER_STATUS_YWC = 100;//已完成
	public static final int ORDER_STATUS_YQX = 90;//已取消
	public static final int ORDER_STATUS_DQR = 2;//待确认

	//    public static final int ORDER_DELIVERY_BDPS = 1;//百度配送
	//    public static final int ORDER_DELIVERY_MTPS = 2;//美团配送
	//    public static final int ORDER_DELIVERY_ZPS = 100;//自配送

	public static final int ORDER_SDC_SHOP_DELIVER = 0;// 店铺发货

	private String              id;            //订单id
	private StringConfig        orderForm;     //订单来源
	// FIXME: 2016/2/1 2.2.5遗留问题：暂时以后台为准序列号字段为int,下一版按协议修改为string类型
	private int                 orderBd;       //百度序列号或者ele序列号
	private StringConfig        orderStatus;   //订单状态
	private String              person;        //联系人
	//add by sun,微信外卖字段取代person使用
	private String              deliveryName;  //联系人
	private String              phone;         //联系电话
	private String              address;       //配送地址
	private StringConfig        delivery;      //配送方式
	private String              arriveTime;    //送达时间
	private StringConfig        payType;       //支付方式
	private String              remark;        //备注信息
	private String              receipt;       //发票信息
	private List<ProductDetail> productDetails; //订单商品信息
	private long                mealFee;       //餐盒费
	private long                distributionCharge;//配送费
	private long                amount;        //订单金额
	private long                actPayment;   //用户实付金额
	private long                actAmount;    //实收金额金额
	private String              platformOrderId;    //第三方平台订单号 add by sungongyan
	private String              deliveryPhone;    //骑手电话 add by sungongyan 2016.06.27
	private String              cancelReason;    //取消原因 add by sungongyan  2016.06.27
	private int                 dealCount;//订单处理次数，0 or 1 ,add by sungongyan 2017.6.5
	private String              dealTime;//订单处理时间 empty or sometime,add by sun 2017.6.5

	private long payTime;//支付时间

	public String getDeliveryName()
	{
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName)
	{
		this.deliveryName = deliveryName;
	}

	public long getPayTime()
	{
		return payTime;
	}

	public void setPayTime(long payTime)
	{
		this.payTime = payTime;
	}

	public List<StringConfig> getTakeoutDiscount()
	{
		return takeoutDiscount;
	}

	public Order setTakeoutDiscount(List<StringConfig> takeoutDiscount)
	{
		this.takeoutDiscount = takeoutDiscount;
		return this;
	}

	private List<StringConfig> takeoutDiscount;//优惠信息
	private long               discountPrice;//优惠总金额
	private long               discount;//优惠金额
	private int                orderNum;      //订单具有的商品数量
	private String             serialNum;    //订单号
	private String             orderTime;    //下单时间
	private List<StringConfig> action;        //订单动作

	private List<String> zengpinList;//赠品
	private String       dinnersNum;//用餐人数
	//	"apiVersion": 0, // 0 表示是旧的，要走老的外卖的订单处理流程   1表示是新的，要走新的订单处理流程

	private int apiVersion;

	public int getApiVersion()
	{
		return apiVersion;
	}

	public void setApiVersion(int apiVersion)
	{
		this.apiVersion = apiVersion;
	}

	public List<String> getZengpinList()
	{
		return zengpinList;
	}

	public void setZengpinList(List<String> zengpinList)
	{
		this.zengpinList = zengpinList;
	}

	public String getDinnersNum()
	{
		return dinnersNum;
	}

	public void setDinnersNum(String dinnersNum)
	{
		this.dinnersNum = dinnersNum;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getOrderBd()
	{
		return orderBd;
	}

	public StringConfig getOrderForm()
	{
		return orderForm;
	}

	public void setOrderForm(StringConfig orderForm)
	{
		this.orderForm = orderForm;
	}

	public void setOrderBd(int orderBd)
	{
		this.orderBd = orderBd;
	}

	public StringConfig getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(StringConfig orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public String getPerson()
	{
		return person;
	}

	public void setPerson(String person)
	{
		this.person = person;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public StringConfig getDelivery()
	{
		return delivery;
	}

	public void setDelivery(StringConfig delivery)
	{
		this.delivery = delivery;
	}

	public String getArriveTime()
	{
		return arriveTime;
	}

	public void setArriveTime(String arriveTime)
	{
		this.arriveTime = arriveTime;
	}

	public StringConfig getPayType()
	{
		return payType;
	}

	public void setPayType(StringConfig payType)
	{
		this.payType = payType;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getReceipt()
	{
		return receipt;
	}

	public void setReceipt(String receipt)
	{
		this.receipt = receipt;
	}

	public List<ProductDetail> getProductDetails()
	{
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails)
	{
		this.productDetails = productDetails;
	}

	public long getMealFee()
	{
		return mealFee;
	}

	public void setMealFee(long mealFee)
	{
		this.mealFee = mealFee;
	}

	public long getDistributionCharge()
	{
		return distributionCharge;
	}

	public void setDistributionCharge(long distributionCharge)
	{
		this.distributionCharge = distributionCharge;
	}

	public long getAmount()
	{
		return amount;
	}

	public void setAmount(long amount)
	{
		this.amount = amount;
	}

	public long getActAmount()
	{
		return actAmount;
	}

	public void setActAmount(long actAmount)
	{
		this.actAmount = actAmount;
	}

	public long getDiscount()
	{
		return discount;
	}

	public void setDiscount(long discount)
	{
		this.discount = discount;
	}

	public int getOrderNum()
	{
		return orderNum;
	}

	public void setOrderNum(int orderNum)
	{
		this.orderNum = orderNum;
	}

	public String getSerialNum()
	{
		return serialNum;
	}

	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}

	public String getOrderTime()
	{
		return orderTime;
	}

	public void setOrderTime(String orderTime)
	{
		this.orderTime = orderTime;
	}


	public List<StringConfig> getAction()
	{
		return action;
	}

	public void setAction(List<StringConfig> action)
	{
		this.action = action;
	}

	public String getPlatformOrderId()
	{
		return platformOrderId;
	}

	public void setPlatformOrderId(String platformOrderId)
	{
		this.platformOrderId = platformOrderId;
	}

	public long getActPayment()
	{
		return actPayment;
	}

	public void setActPayment(long actPayment)
	{
		this.actPayment = actPayment;
	}

	public long getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(long discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public String getDeliveryPhone()
	{
		return deliveryPhone;
	}

	public void setDeliveryPhone(String deliveryPhone)
	{
		this.deliveryPhone = deliveryPhone;
	}

	public String getCancelReason()
	{
		return cancelReason;
	}

	public void setCancelReason(String cancelReason)
	{
		this.cancelReason = cancelReason;
	}

	public int getDealCount()
	{
		return dealCount;
	}

	public void setDealCount(int dealCount)
	{
		this.dealCount = dealCount;
	}

	public String getDealTime()
	{
		return dealTime;
	}

	public void setDealTime(String dealTime)
	{
		this.dealTime = dealTime;
	}
}
