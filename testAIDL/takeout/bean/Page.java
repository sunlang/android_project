package com.yunnex.canteen.takeout.bean;

/**
 * Created by sungongyan on 2017/7/18.
 * qq 379366152
 */

public class Page
{
//				"currentPage":1,
//						"hasNextPage":true,
//						"hasPreviousPage":false,
//						"needCount":true,
//						"pageEndRow":2,
//						"pageSize":2,
//						"pageStartRow":0,
//						"pagination":true,
//						"totalPages":156,
//						"totalRows":312

	private int currentPage;
	private int totalRows;
	private boolean hasNextPage;

	public int getTotalRows()
	{
		return totalRows;
	}

	public void setTotalRows(int totalRows)
	{
		this.totalRows = totalRows;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public boolean isHasNextPage()
	{
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage)
	{
		this.hasNextPage = hasNextPage;
	}
}
