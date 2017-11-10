package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.OrderDto;
import com.yunnex.canteen.takeout.bean.Page;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class OrderListResp extends ResponseBase
{

	private MyResponse response;

	public MyResponse getResponse()
	{
		return response;
	}

	public void setResponse(MyResponse response)
	{
		this.response = response;
	}

	public class MyResponse
	{
		private List<OrderDto> list;

		private Page page;

		public List<OrderDto> getList()
		{
			return list;
		}

		public void setList(List<OrderDto> list)
		{
			this.list = list;
		}

		public Page getPage()
		{
			return page;
		}

		public void setPage(Page page)
		{
			this.page = page;
		}
	}
}
