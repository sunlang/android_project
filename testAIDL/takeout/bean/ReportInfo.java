package com.yunnex.canteen.takeout.bean;

/**
 * Created by sungongyan on 2017/6/7.
 * qq 379366152
 */

/**
 * bugly上传数据对象(针对未处理订单)
 */
public class ReportInfo
{
	private String id;

	public ReportInfo()
	{
	}

	public ReportInfo(String id)
	{
		this.id = id;
	}


	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}
