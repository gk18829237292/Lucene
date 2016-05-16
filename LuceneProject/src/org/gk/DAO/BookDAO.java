package org.gk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.gk.Entries.BookEntry;
import org.gk.Utils.DBUtils;

import static org.gk.Utils.ProjectUtils.*;

public class BookDAO {
	
	private static final String  TAG ="BookDAO";
	
	public int insert(BookEntry entry){
		return insert(entry.getBookName());
	}
	
	public int insert(String bookName){
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement("insert into " + BookEntry.DBTABLE_STRING + " values(?)");
			stmt.setString(1, bookName);
			stmt.addBatch();
			stmt.execute();
			return 1;
		} catch (SQLException e) {
			logInfo(TAG + " insert error");
			e.printStackTrace();
		}finally{
			DBUtils.close(stmt, conn);
		}
		
		return 0;
	}
	
	public int delete(BookEntry entry){
		return delete(entry.getBookName());
	}
	
	public int delete(String bookName){
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement("delete from " + BookEntry.DBTABLE_STRING + " where "+BookEntry.NAME_STRING+"=?");
			stmt.setString(1, bookName);
			stmt.addBatch();
			stmt.execute();
			return 1;
		} catch (SQLException e) {
			logInfo(TAG + " insert error");
		}finally{
			DBUtils.close(stmt, conn);
		}
		
		return 0;
	}

	public int deleteAll(){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtils.getConnection();
			stmt = conn.createStatement();
			return stmt.executeUpdate("delete from "+ BookEntry.DBTABLE_STRING);
		} catch (SQLException e) {
			logInfo(TAG + " insert error");
		}finally{
			DBUtils.close(stmt, conn);
		}
		
		return 0;
	}
	
	public List<BookEntry> getAll(){
		List<BookEntry> list = new ArrayList<BookEntry>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from "+ BookEntry.DBTABLE_STRING);
			while(rs.next()){
				BookEntry entry = new BookEntry(rs.getString(BookEntry.NAME_STRING));
				list.add(entry);
			}
			return list;
		} catch (SQLException e) {
			logInfo(TAG + " insert error");
		}finally{
			DBUtils.close(rs,stmt, conn);
		}
		
		return null;
	}
	
	public boolean contain(String bookName){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement("select * from " + BookEntry.DBTABLE_STRING + " where "+BookEntry.NAME_STRING+"=?");
			stmt.setString(1, bookName);
			stmt.addBatch();
			rs = stmt.executeQuery();
			if(rs.next()) return true;
		} catch (SQLException e) {
			logInfo(TAG + " insert error");
		}finally{
			DBUtils.close(rs,stmt, conn);
		}
		
		return false;
		
	}
	
	
	
}
