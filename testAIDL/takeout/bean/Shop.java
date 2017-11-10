package com.yunnex.canteen.takeout.bean;

import java.util.List;

/**
 * Created by sungongyan on 2016/1/5.
 * wechat sun379366152
 */
public class Shop
{

	private String     id;
	private String     icon;
	private String     name;
	private String     address;
	private List<Time> runningTimes;
	private int        bDRunning;
	private int        mTRunning;
	private int        eleRunning;
	private int        wxRunning;
	private int        wmOpen;
	// TODO: 2017/7/13
	private String        data_dish_list="";//门店下的菜品

	public int getWxRunning()
	{
		return wxRunning;
	}

	public void setWxRunning(int wxRunning)
	{
		this.wxRunning = wxRunning;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public List<Time> getRunningTimes()
	{
		return runningTimes;
	}

	public void setRunningTimes(List<Time> runningTimes)
	{
		this.runningTimes = runningTimes;
	}

	public int getbDRunning()
	{
		return bDRunning;
	}

	public void setbDRunning(int bDRunning)
	{
		this.bDRunning = bDRunning;
	}

	public int getmTRunning()
	{
		return mTRunning;
	}

	public void setmTRunning(int mTRunning)
	{
		this.mTRunning = mTRunning;
	}

	public int getEleRunning()
	{
		return eleRunning;
	}

	public void setEleRunning(int eleRunning)
	{
		this.eleRunning = eleRunning;
	}

	public int getTRunning()
	{
		return mTRunning;
	}

	public void setTRunning(int TRunning)
	{
		mTRunning = TRunning;
	}

	public int getWmOpen()
	{
		return wmOpen;
	}

	public void setWmOpen(int wmOpen)
	{
		this.wmOpen = wmOpen;
	}


	public String getData_dish_list()
	{
		return data_dish_list;
	}

	public void setData_dish_list(String data_dish_list)
	{
		this.data_dish_list = data_dish_list;
	}

	@Override
	public String toString()
	{
		return "Shop{" +
				"id='" + id + '\'' +
				", icon='" + icon + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", runningTimes=" + runningTimes +
				", bDRunning=" + bDRunning +
				", mTRunning=" + mTRunning +
				", eleRunning=" + eleRunning +
				", wmOpen=" + wmOpen +
				", data_dish_list=" + data_dish_list +
				'}';
	}
}
