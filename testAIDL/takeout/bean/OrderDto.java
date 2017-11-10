package com.yunnex.canteen.takeout.bean;

import java.util.List;

/**
 * Created by sungongyan on 2017/7/18.
 * qq 379366152
 */

public class OrderDto
{
	/**
	 * {
	 * "contactInfoVo":[
	 * {
	 * "id":"取餐编号:",
	 * "value":"10"
	 * },
	 * {
	 * "id":"就餐人数:",
	 * "value":"3"
	 * },
	 * {
	 * "id":"定单来源",
	 * "value":"微信"
	 * }
	 * ],
	 "cyWmOrderDto":
	 {
	 "createTime":"1505966014000",
	 "goodsAmount":"3",
	 "isAddDish":"0",
	 "newOrder":"0",
	 "orderId":"17910713588765822976",
	 "orderStatus":"2",
	 "orderType":"100",
	 "payFee":"2202",
	 "payStatus":"0",
	 "sendImmediately":"-1",
	 "serialNo":"5",
	 "sources":"1",
	 "totalFee":"2202",
	 "waimaiOrderId":"",
	 "waimaiStatus":"0"
	 }
	 * }
	 */

	private OrderDtoSub cyWmOrderDto;

	List<StringConfig> contactInfoVo;

	public OrderDtoSub getCyWmOrderDto()
	{
		return cyWmOrderDto;
	}

	public void setCyWmOrderDto(OrderDtoSub cyWmOrderDto)
	{
		this.cyWmOrderDto = cyWmOrderDto;
	}

	public List<StringConfig> getContactInfoVo()
	{
		return contactInfoVo;
	}

	public void setContactInfoVo(List<StringConfig> contactInfoVo)
	{
		this.contactInfoVo = contactInfoVo;
	}

	@Override
	public String toString()
	{
		return "OrderDto{" + "cyWmOrderDto=" + cyWmOrderDto + ", contactInfoVo=" + contactInfoVo + '}';
	}
}
