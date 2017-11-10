package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.OrderType;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class OrderTypeResp extends ResponseBase
{

	private List<OrderType> response;

	public List<OrderType> getResponse()
	{
		return response;
	}

	public void setResponse(List<OrderType> response)
	{
		this.response = response;
	}

	//	private MyResponse response;
//
//	public MyResponse getResponse()
//	{
//		return response;
//	}
//
//	public void setResponse(MyResponse response)
//	{
//		this.response = response;
//	}
//
//	public class MyResponse
//	{
//		private List<OrderType> response;
//	}
}
