package org.base.result;

import java.util.List;
import java.util.Map;

/**
 * 结果集
 * 
 * JacobKing 2013-10-17 上午10:32:58
 */
public class ResultMap {
	/**
	 * 返回码（100:正确；101：系统异常；102：数据有误；103：数据库异常；104:空数据;105:数据不存在...）
	 */
	private int code;
	/**
	 * 消息内容
	 */
	private String msg;
	/**
	 * 数据集
	 */
	private Map<String, Object> data;
	@SuppressWarnings("rawtypes")
	/**
	 * 数据集
	 */
	private List list;
	/**
	 * 对象
	 */
	private Object obj;

	/**
	 * 构造函数
	 * 
	 * @param code
	 *            <p>
	 *            返回码
	 *            <li>
	 *            100：正确</li>
	 *            <li>
	 *            101：系统异常；</li>
	 *            <li>
	 *            102：数据有误；</li>
	 *            <li>
	 *            103：数据库异常；</li>
	 *            <li>
	 *            104：空数据;</li>
	 *            <li>
	 *            105：数据不存在</li>
	 *            </p>
	 */
	public ResultMap(int code) {
		this.code = code;
	}

	public ResultMap(int code, Object obj) {
		this.code = code;
		this.obj = obj;
	}

	public ResultMap(int code, List<Object> list) {
		this.code = code;
		this.list = list;
	}

	public ResultMap(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public void add(int code) {
		this.code = code;
	}

	public void add(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isError() {
		return code > 100;
	}

	public boolean isEmpty() {
		return code == 104;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
