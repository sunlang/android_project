package yunnex.com.testalipay;

/**
 * Created by sungongyan on 2017/5/27.
 * qq 379366152
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 付费买钻石
 */
public class Diamond implements Parcelable
{


	/**
	 * 钻石数量
	 */
	private int num;

	private String otherArr;

	public Diamond()
	{
	}

	public Diamond(int num)
	{
		this.num = num;
	}

	//	public static final Creator<Diamond> CREATOR = new Creator<Diamond>()
//	{
//		@Override
//		public Diamond createFromParcel(Parcel in)
//		{
//			return new Diamond(in);
//		}
//
//		@Override
//		public Diamond[] newArray(int size)
//		{
//			return new Diamond[size];
//		}
//	};

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public String getOtherArr()
	{
		return otherArr;
	}

	public void setOtherArr(String otherArr)
	{
		this.otherArr = otherArr;
	}

//	@Override
//	public int describeContents()
//	{
//		return 0;
//	}

//	@Override
//	public void writeToParcel(Parcel dest, int flags)
//	{
//		dest.writeInt(num);
//	}


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(this.num);
		dest.writeString(this.otherArr);
	}

	protected Diamond(Parcel in)
	{
		this.num = in.readInt();
		this.otherArr = in.readString();
	}

	public static final Parcelable.Creator<Diamond> CREATOR = new Parcelable.Creator<Diamond>()
	{
		@Override
		public Diamond createFromParcel(Parcel source)
		{
			return new Diamond(source);
		}

		@Override
		public Diamond[] newArray(int size)
		{
			return new Diamond[size];
		}
	};
}
