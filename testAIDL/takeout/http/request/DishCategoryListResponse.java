package com.yunnex.canteen.takeout.http.request;

import com.yunnex.canteen.takeout.bean.DishCategory;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 * Created by lion on 16/1/7.
 */
public class DishCategoryListResponse extends ResponseBase
{
	private List<DishCategory> response;

	public List<DishCategory> getResponse()
	{
		return response;
	}

	public void setResponse(List<DishCategory> response)
	{
		this.response = response;
	}
}
