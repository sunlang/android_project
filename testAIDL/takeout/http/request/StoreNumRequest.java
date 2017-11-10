package com.yunnex.canteen.takeout.http.request;

/**
 * Created by Administrator on 2016/1/6.
 */
public class StoreNumRequest
{
//	private String dishId;
//	private String storeNum;
//	private int actionType;

	private  int id;
	private int soldStatus;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSoldStatus()
	{
		return soldStatus;
	}

	public void setSoldStatus(int soldStatus)
	{
		this.soldStatus = soldStatus;
	}
}
