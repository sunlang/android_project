package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.DayBill;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/8.
 */
public class DayBillResponse extends ResponseBase
{
	private DayBillResponseDeail response;

	public DayBillResponseDeail getResponse()
	{
		return response;
	}

	public void setResponse(DayBillResponseDeail response)
	{
		this.response = response;
	}

	public class DayBillResponseDeail{
		private String             checkedTime;	//被选中的时间
		private     int                count;	//实收订单
		private     long               totalFee;	//订单金额
		private     long               totalDiscountFee;	//优惠金额
		private     long               realFee;	//实收金额
		private     String             startTime;	//统计开始时间
		private     String             endTime;	//统计结束时间
		private     ArrayList<DayBill> sourceStatResult;//	订单来源列表

		public int getCount()
		{
			return count;
		}

		public void setCount(int count)
		{
			this.count = count;
		}

		public long getTotalFee()
		{
			return totalFee;
		}

		public void setTotalFee(long totalFee)
		{
			this.totalFee = totalFee;
		}

		public long getTotalDiscountFee()
		{
			return totalDiscountFee;
		}

		public void setTotalDiscountFee(long totalDiscountFee)
		{
			this.totalDiscountFee = totalDiscountFee;
		}

		public long getRealFee()
		{
			return realFee;
		}

		public void setRealFee(long realFee)
		{
			this.realFee = realFee;
		}

		public ArrayList<DayBill> getSourceStatResult()
		{
			return sourceStatResult;
		}

		public void setSourceStatResult(ArrayList<DayBill> sourceStatResult)
		{
			this.sourceStatResult = sourceStatResult;
		}

		public String getCheckedTime()
		{
			return checkedTime;
		}

		public void setCheckedTime(String checkedTime)
		{
			this.checkedTime = checkedTime;
		}

		public String getStartTime()
		{
			return startTime;
		}

		public void setStartTime(String startTime)
		{
			this.startTime = startTime;
		}

		public String getEndTime()
		{
			return endTime;
		}

		public void setEndTime(String endTime)
		{
			this.endTime = endTime;
		}
	}
}
