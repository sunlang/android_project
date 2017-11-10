package com.yunnex.canteen.takeout.bean;

import java.util.List;

public class OrderType {
	private int                orderType;
	private String             name;
	private List<OrderStatu> status;

	public int getOrderType()
	{
		return orderType;
	}

	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<OrderStatu> getStatus() {
		return status;
	}



	public void setStatus(List<OrderStatu> status) {
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "OrderType{" + "orderType=" + orderType + ", name='" + name + '\'' + ", status=" + status + '}';
	}
}
