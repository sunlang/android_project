package com.yunnex.canteen.takeout.http.request;

/**
 * Created by Administrator on 2015/12/24.
 */
public class OrderSearchRequest
{
	private int page;
	private String searchKey;
	private String searchValue;

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public String getSearchKey()
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey)
	{
		this.searchKey = searchKey;
	}

	public String getSearchValue()
	{
		return searchValue;
	}

	public void setSearchValue(String searchValue)
	{
		this.searchValue = searchValue;
	}
}
