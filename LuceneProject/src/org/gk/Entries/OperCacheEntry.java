package org.gk.Entries;

public class OperCacheEntry {
	
	public static final String DBTABLE_STRING ="opercache";
	public static final String NAME_STRING= "name";
	public static final String OPER_STRING= "oper";
	
	public static final int DELETE = 0;
	public static final int ADD = 1;
	
	private String name;
	
	/**
	 * 2016年5月15日23:21:51
	 * 想把操作作为枚举类型
	 * @author pc_home
	 *
	 */
//	public enum OPER{
//		DELETE,ADD
//	};

	/**
	 * 0 删除
	 * 1 添加
	 */
	private int oper;

	public OperCacheEntry(String name, int oper) {
		super();
		this.name = name;
		this.oper = oper;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOper() {
		return oper;
	}

	public void setOper(int oper) {
		this.oper = oper;
	}

	@Override
	public String toString() {
		String str ="";
		if(oper == 0){
			str = "OperCacheEntry [name=" + name + ", oper=" + "DELETE" + "]";
		}else{
			str = "OperCacheEntry [name=" + name + ", oper=" + "ADD" + "]";
		}
		return str;
	}
	
	
	
	
}
