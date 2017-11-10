package com.yunnex.canteen.takeout.bean;

/**
 * Created by Administrator on 2015/12/8.
 * 订单来源详细
 */
public class DayBill
{
	private int count;	//订单数
	private long realFee;//订单金额
	private String	sourceName;//来源
	private int source;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public long getRealFee()
	{
		return realFee;
	}

	public void setRealFee(long realFee)
	{
		this.realFee = realFee;
	}

	public String getSourceName()
	{
		return sourceName;
	}

	public void setSourceName(String sourceName)
	{
		this.sourceName = sourceName;
	}

	public int getSource()
	{
		return source;
	}

	public void setSource(int source)
	{
		this.source = source;
	}
}
