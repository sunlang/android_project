/**
 * @author reason
 * @date 2014-12-24
 */

package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.Order;
import com.yunnex.vpay.lib.http.ResponseBase;

public class OrderItemResponse extends ResponseBase
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private WmOrderDetaiResponse response;

	public WmOrderDetaiResponse getResponse()
	{
		return response;
	}

	public void setResponse(WmOrderDetaiResponse response)
	{
		this.response = response;
	}

	public class WmOrderDetaiResponse
	{
		private Order order;

		// =======================================
		// Constructors
		// =======================================

		// =======================================
		// Setters/Getters
		// =======================================
		public Order getOrder()
		{
			return order;
		}

		public void setOrder(Order order)
		{
			this.order = order;
		}
	}
}
