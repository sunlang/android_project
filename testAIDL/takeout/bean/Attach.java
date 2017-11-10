package com.yunnex.canteen.takeout.bean;

import java.util.List;

/**
 * Created by songdan on 2016/1/20.
 */
public class Attach
{
	private String name;
	private long   singleAmount;
	private int    num;
	private List<CheckAttr> checkAttrs;

	public List<CheckAttr> getCheckAttrs()
	{
		return checkAttrs;
	}

	public void setCheckAttrs(List<CheckAttr> checkAttrs)
	{
		this.checkAttrs = checkAttrs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getSingleAmount()
	{
		return singleAmount;
	}

	public void setSingleAmount(long singleAmount)
	{
		this.singleAmount = singleAmount;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}


	@Override
	public String toString()
	{
		return "Attach{" +
				"name='" + name + '\'' +
				", price=" + singleAmount +
				", num=" + num +
				'}';
	}


	/**
	 * "id":25,
	 * "name":"选项1",
	 * "pid":9,
	 * "pname":"属性名称1"
	 */
	public class CheckAttr
	{
		private int    id;
		private int    pid;
		private String name;
		private String pname;

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public int getPid()
		{
			return pid;
		}

		public void setPid(int pid)
		{
			this.pid = pid;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getPname()
		{
			return pname;
		}

		public void setPname(String pname)
		{
			this.pname = pname;
		}
	}
}
