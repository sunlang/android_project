/**
 * @author reason
 * @date 2014-12-24
 */

package com.yunnex.canteen.takeout.http.request;

public class DeliverActionRequest
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private String id;
	private String sdcId;
	private String deliverNo;

	// =======================================
	// Constructors
	// =======================================

	// =======================================
	// Setters/Getters
	// =======================================
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSdcId()
	{
		return sdcId;
	}

	public void setSdcId(String sdcId)
	{
		this.sdcId = sdcId;
	}

	public String getDeliverNo()
	{
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo)
	{
		this.deliverNo = deliverNo;
	}
	// =======================================
	// Methods from SuperClass/Interfaces
	// =======================================

	// =======================================
	// Methods
	// =======================================

	// =======================================
	// Inner Classes/Interfaces
	// =======================================
}
