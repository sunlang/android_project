/**
 * @author reason
 * @date 2014-12-24
 */

package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.StringConfig;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

public class ReasonListResponse extends ResponseBase
{

	private String message;
	private InnerResponse response;

	public InnerResponse getResponse()
	{
		return response;
	}

	public void setResponse(InnerResponse response)
	{
		this.response = response;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================
//	private List<StringConfig> reasonList;

	// =======================================
	// Constructors
	// =======================================

	// =======================================
	// Setters/Getters
	// =======================================

//	public List<StringConfig> getReasonList()
//	{
//		return reasonList;
//	}

//	public void setReasonList(List<StringConfig> reasonList)
//	{
//		this.reasonList = reasonList;
//	}

	// =======================================
	// Methods from SuperClass/Interfaces
	// =======================================

	// =======================================
	// Methods
	// =======================================

	// =======================================
	// Inner Classes/Interfaces
	// =======================================

	class InnerResponse
	{
		private int code;
		private List<StringConfig> reasonList;

		// =======================================
		// Constructors
		// =======================================

		// =======================================
		// Setters/Getters
		// =======================================

		public List<StringConfig> getReasonList()
		{
			return reasonList;
		}

		public void setReasonList(List<StringConfig> reasonList)
		{
			this.reasonList = reasonList;
		}
	}
}
