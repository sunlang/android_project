

package com.yunnex.canteen.takeout.http.request;

public class OrderListReqst
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private int  page;
	private int  newOrder;
	private long date;
	private int  orderType;
	private long orderStatus;

	public int getNewOrder()
	{
		return newOrder;
	}

	public void setNewOrder(int newOrder)
	{
		this.newOrder = newOrder;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public int getOrderType()
	{
		return orderType;
	}

	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}

	public long getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(long orderStatus)
	{
		this.orderStatus = orderStatus;
	}
}
