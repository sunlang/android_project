/**
 * @author reason
 * @date 2015-1-15
 */

package com.yunnex.canteen.takeout.bean;

public class StringConfig
{
	// =======================================
	// Constants
	// =======================================

	// =======================================
	// Fields
	// =======================================

	private String id;
	private String value;

	// =======================================
	// Constructors
	// =======================================
	public StringConfig()
	{

	}

	public StringConfig(String id, String value)
	{
		this.id = id;
		this.value = value;
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

	public int getIdInteger()
	{
		int id = -1;
		try
		{
			id = Integer.parseInt(this.id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
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
