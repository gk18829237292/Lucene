package org.gk.Test;

import java.util.List;

import org.gk.DAO.OperDAO;
import org.gk.Entries.OperCacheEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestOperDAO {
	OperDAO oper = null;
	
	@Before
	public void Init(){
		oper = new OperDAO();
	}
	
	/**
	 * 1.添加 <'1',ADD>
	 * 		共有数据：1
	 *		第1条数据 :OperCacheEntry [name=1, oper=ADD]
	 * 2.添加<'2',ADD>
	 * 		共有数据：2
	 *		第1条数据 :OperCacheEntry [name=1, oper=ADD]
	 *		第2条数据 :OperCacheEntry [name=2, oper=ADD]
	 *3.添加<1,DELETE>
	 *		共有数据：1
	 *		第1条数据 :OperCacheEntry [name=2, oper=ADD]
	 */
	@Test
	public void testInsert(){
		oper.insert("2", OperCacheEntry.ADD);

	
	}
	
	@Test
	public void testDelete(){
		oper.deleteAll();
	}
	
	@After
	public void show(){
		List<OperCacheEntry> list = oper.getAll();
		System.out.println("共有数据：" + list.size());
		int i = 1;
		for(OperCacheEntry entry:list){
			System.out.println("第"+i+"条数据 :"+entry);
			i++;
		}
	}
}
