package com.penglecode.xmodule.common.support;

/**
 * 通用返回结果类(针对分页)
 * 
 * @param <T>
 * @author  pengpeng
 * @date 	 2015年5月7日 上午9:53:27
 * @version 1.0
 */
public class PageResult<T> extends Result<T> {

	private static final long serialVersionUID = 1L;
	
	/** 当存在分页查询时此值为总记录数 */
	private int totalRowCount;

	PageResult() {}
	
	PageResult(Result<T> result, int totalRowCount) {
		super(result);
		this.totalRowCount = totalRowCount;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	protected void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	public static PageBuilder success() {
		return new PageBuilder(Boolean.TRUE);
	}
	
	public static PageBuilder failure() {
		return new PageBuilder(Boolean.FALSE);
	}
	
	public String toString() {
		return "PageResult [success=" + isSuccess() + ", code=" + getCode() + ", message="
				+ getMessage() + ", data=" + getData() + ", totalRowCount=" + getTotalRowCount() + "]";
	}

	public static class PageBuilder extends Builder {
		
		private int totalRowCount = 0;
		
		PageBuilder(boolean success) {
			super(success);
		}
		
		public PageBuilder totalRowCount(int totalRowCount) {
			this.totalRowCount = totalRowCount;
			return this;
		}
		
		public PageBuilder code(String code) {
			return (PageBuilder) super.code(code);
		}
		
		public PageBuilder message(String message) {
			return (PageBuilder) super.message(message);
		}
		
		public PageBuilder data(Object data) {
			return (PageBuilder) super.data(data);
		}
		
		public <T> PageResult<T> build() {
			return new PageResult<T>(super.build(), totalRowCount);
		}
		
	}
	
}
