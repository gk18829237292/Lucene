package org.gk.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;


/**
 * 1.Connection接口   JDBC连接表示接口
 * 		1.代表一个数据库的连接
 * 		2.可以用来获得数据库的 Statement()对象
 * 			1.
 * @author pc_home
 *
 */
public class TestJDBC {
	Connection conn = null;
	
	@Before
	public void init(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene","root","");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void show() throws SQLException{
		
		//
		Statement statement =  conn.createStatement();
		ResultSet set = statement.executeQuery("select * from test");
		while(set.next()){
			System.out.println(set.getString(2));
		}
		
	}

	@Test
	public void show1() throws SQLException{
		

		Statement stmt = conn.createStatement();

		stmt.execute("create database lucene_bak");
		stmt.execute("use lucene_bak");
		stmt.execute("create table a(id int,name varchar(20))");
		stmt.execute("insert into a values(1,'aaa')");
		stmt.execute("update a set name ='bbb' where id = 1");
		
		stmt.close();
		conn.close();
	}
}
