/**
 * @author reason
 * @date 2014-12-24
 */

package com.yunnex.canteen.takeout.http.request;

public class CancelActionRequest
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private String id;
	private String reasonId;

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

	public String getReasonId()
	{
		return reasonId;
	}

	public void setReasonId(String reasonId)
	{
		this.reasonId = reasonId;
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
