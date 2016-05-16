package org.gk.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


/**
 * 数据库的连接和关闭实现
 * 
 * what I learn form it
 * 
 * 		1.ResourceBundle 
 * @author pc_home
 *
 */
public class DBUtils {
	private static final String URL_STRING;
	private static final String ACCOUNT_STRING;
	private static final String PWD_STRING;
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("dbconfig");
		URL_STRING = bundle.getString("URL");
		ACCOUNT_STRING = bundle.getString("ACCOUNT");
		PWD_STRING = bundle.getString("PWD");
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL_STRING,ACCOUNT_STRING,PWD_STRING);
	}
	
	public static void close(ResultSet rs,Statement stmt,Connection conn){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
			}
			rs = null;
		}
		close(stmt, conn);
	}
	
	public static void close(Statement stmt,Connection conn){
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			stmt = null;
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
			}
			conn = null;
		}
	}
}
