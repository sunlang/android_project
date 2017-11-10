package com.yunnex.canteen.takeout.http.request;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2015/12/8.
 */
public class DayBillRequest
{
	// =======================================
	// Constants
	// =======================================

	//@format:off

	//@format:on

	// =======================================
	// Fields
	// =======================================

	//@format:off
	private String checkingDate;//查询起始时间, 区分日周月
	//@format:on

	// =======================================
	// Constructors
	// =======================================

	// =======================================
	// Setters/Getters
	// =======================================


	public String getCheckingDate()
	{
		return checkingDate;
	}

	public void setCheckingDate(String checkingDate)
	{
		this.checkingDate = checkingDate;
	}
}
