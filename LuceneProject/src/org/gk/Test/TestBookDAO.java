package org.gk.Test;

import java.sql.Connection;
import java.util.List;

import org.gk.DAO.BookDAO;
import org.gk.Entries.BookEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBookDAO {
	
	BookDAO bookDAO = null;
	
	@Before
	public void init(){
		bookDAO = new BookDAO();
	}
	
	@Test
	public void testInsert(){
		bookDAO.insert("1");
	}
	
	@Test
	public void testDelete(){
		bookDAO.delete("1");
	}
	
	@Test
	public void testDeleteAll(){
		bookDAO.deleteAll();
	}
	
	@After
	public void show(){
		List<BookEntry> list = bookDAO.getAll();
		System.out.println("共有数据：" + list.size());
		int i = 1;
		for(BookEntry entry:list){
			System.out.println("第" +i +"条数据：" + entry);
			i++;
		}
	}

	@Test
	public void testContains(){
		System.out.println(bookDAO.contain("1"));
	}
}
