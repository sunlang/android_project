/**
 * @author reason
 * @date 2015-1-15
 */

package com.yunnex.canteen.takeout.bean;

public class DeliverStringConfig
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private String id;
	private String value;
	private int    flag;

	// =======================================
	// Constructors
	// =======================================
	public DeliverStringConfig()
	{

	}

	public DeliverStringConfig(String id, String value, int flag)
	{
		this.id = id;
		this.value = value;
		this.flag = flag;
	}

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

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public int getFlag()
	{
		return flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public int getFlagInt()
	{
		if (this.flag == -1)
		{
			return 1;
		}
		else
		{
			return this.flag;
		}
	}

	// =======================================
	// Methods from SuperClass/Interfaces
	// =======================================

	@Override
	public String toString()
	{
		return value;
	}

	// =======================================
	// Methods
	// =======================================

	// =======================================
	// Inner Classes/Interfaces
	// =======================================
}
