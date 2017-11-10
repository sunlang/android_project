package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class OrderSearCondidtionResp extends ResponseBase
{

	private List<StringConfig> response;

	public List<StringConfig> getResponse()
	{
		return response;
	}

	public void setResponse(List<StringConfig> response)
	{
		this.response = response;
	}
}
