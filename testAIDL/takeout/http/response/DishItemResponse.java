package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.vpay.lib.http.ResponseBase;

/**
 * Created by Administrator on 2016/1/5.
 */
public class DishItemResponse extends ResponseBase
{
	private Dish response;

	public Dish getResponse()
	{
		return response;
	}

	public void setResponse(Dish response)
	{
		this.response = response;
	}
}
