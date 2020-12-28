package com.xwy.common.constant;

/**
 * 数据库排序
 * 
 * @author xiangwy
 * @date: 2020-12-03 09:25:12
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
public enum Order {

	ASC("asc"), DESC("desc");

	private String des;

	Order(String des) {
		this.des = des;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
