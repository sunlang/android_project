/**
 * @author reason
 * @date 2014-12-24
 */

package com.yunnex.canteen.takeout.http.response;

import com.yunnex.canteen.takeout.bean.DeliverStringConfig;
import com.yunnex.vpay.lib.http.ResponseBase;

import java.util.List;

public class SdcListResponse extends ResponseBase
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================
	private List<DeliverStringConfig> expressList;

	// =======================================
	// Constructors
	// =======================================

	// =======================================
	// Setters/Getters
	// =======================================

	public List<DeliverStringConfig> getExpressList()
	{
		return expressList;
	}

	public void setExpressList(List<DeliverStringConfig> expressList)
	{
		this.expressList = expressList;
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
