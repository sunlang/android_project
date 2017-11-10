package com.yunnex.canteen.takeout.http.request;

import com.yunnex.canteen.takeout.bean.Dish;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 * Created by lion on 16/1/7.
 */
public class DishIntroListResponse extends ResponseBase
{
	private List<Dish> response;

	public List<Dish> getResponse()
	{
		return response;
	}

	public void setResponse(List<Dish> response)
	{
		this.response = response;
	}
}
