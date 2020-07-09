package com.bit.module.system.ApplyLogs.bean;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class PageMongoInfo<T>  {
	//当前页
	private int pageNum;
	//每页的数量
	private int pageSize;
	//当前页的数量
	private int size;

	private int total;

	//由于startRow和endRow不常用，这里说个具体的用法
	//可以在页面中"显示startRow到endRow 共size条数据"

	//当前页面第一个元素在数据库中的行号
	private int startRow;
	//当前页面最后一个元素在数据库中的行号
	private int endRow;
	//总页数
	private int pages;

	//前一页
	private int prePage;
	//下一页
	private int nextPage;

	//是否为第一页
	private boolean isFirstPage = false;
	//是否为最后一页
	private boolean isLastPage = false;
	//是否有前一页
	private boolean hasPreviousPage = false;
	//是否有下一页
	private boolean hasNextPage = false;
	//导航页码数
	private int navigatePages;
	//所有导航页号
	private int[] navigatepageNums;
	//导航条上的第一页
	private int navigateFirstPage;
	//导航条上的最后一页
	private int navigateLastPage;
	//结果集
	protected List<T> list;

	public PageMongoInfo(List list, int navigatePages,int total,int pages,int pageNum,int pageSize){
		this.pages=pages;
		this.list=list;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
//		this.total=list.size();
		this.total=total;
		PageMongoInfo1(list,navigatePages);

	}
	public void  PageMongoInfo1(List list, int navigatePages) {

		if (list instanceof Page) {
			Page page = (Page) list;
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();

			this.pages = page.getPages();
			this.size = page.size();
			//由于结果是>startRow的，所以实际的需要+1
			if (this.size == 0) {
				this.startRow = 0;
				this.endRow = 0;
			} else {
				this.startRow = page.getStartRow() + 1;
				//计算实际的endRow（最后一页的时候特殊）
				this.endRow = this.startRow - 1 + this.size;
			}
		} else if (list instanceof Collection) {
			//this.pageNum = 1;
			//this.pageSize = list.size();

			//this.pages = this.pageSize > 0 ? 1 : 0;
			this.size = list.size();
//			this.size = total;
			this.startRow = 0;
//			this.endRow = total > 0 ? total - 1 : 0;
			this.startRow = pageNum - 1 <= 0 ? 0 : (pageNum-1) * pageSize;
			if (total<pageSize){
				this.endRow = startRow + total;
			}else {
				this.endRow = pageNum - 1 <= 0? pageSize - 1 : (pageSize * pageNum) - 1 ;
			}
		}
		if (list instanceof Collection) {
			this.navigatePages = navigatePages;
			//计算导航页
			calcNavigatepageNums();
			//计算前后页，第一页，最后一页
			calcPage();
			//判断页面边界
			judgePageBoudary();
		}
	}
	public PageMongoInfo() {
	}
	/**
	 * 包装Page对象
	 *
	 * @param list
	 */


	public PageMongoInfo(List<T> list) {
		this.list = list;
		if(list instanceof Page){
			this.total = (int) ((Page)list).getTotal();
		} else {
			this.total = list.size();
		}
		PageMongoInfo1(list,8);
	}



	/**
	 * 计算导航页
	 */
	private void calcNavigatepageNums() {
		//当总页数小于或等于导航页码数时
		if (pages <= navigatePages) {
			navigatepageNums = new int[pages];
			for (int i = 0; i < pages; i++) {
				navigatepageNums[i] = i + 1;
			}
		} else { //当总页数大于导航页码数时
			navigatepageNums = new int[navigatePages];
			int startNum = pageNum - navigatePages / 2;
			int endNum = pageNum + navigatePages / 2;

			if (startNum < 1) {
				startNum = 1;
				//(最前navigatePages页
				for (int i = 0; i < navigatePages; i++) {
					navigatepageNums[i] = startNum++;
				}
			} else if (endNum > pages) {
				endNum = pages;
				//最后navigatePages页
				for (int i = navigatePages - 1; i >= 0; i--) {
					navigatepageNums[i] = endNum--;
				}
			} else {
				//所有中间页
				for (int i = 0; i < navigatePages; i++) {
					navigatepageNums[i] = startNum++;
				}
			}
		}
	}

	/**
	 * 计算前后页，第一页，最后一页
	 */
	private void calcPage() {
		if (navigatepageNums != null && navigatepageNums.length > 0) {
			navigateFirstPage = navigatepageNums[0];
			navigateLastPage = navigatepageNums[navigatepageNums.length - 1];
			if (pageNum > 1) {
				prePage = pageNum - 1;
			}
			if (pageNum < pages) {
				nextPage = pageNum + 1;
			}
		}
	}

	/**
	 * 判定页面边界
	 */
	private void judgePageBoudary() {
		isFirstPage = pageNum == 1;
		isLastPage = pageNum == pages || pages == 0;;
		hasPreviousPage = pageNum > 1;
		hasNextPage = pageNum < pages;
	}
}
