package org.gk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.gk.Entries.OperCacheEntry;
import org.gk.Utils.DBUtils;

import static org.gk.Utils.ProjectUtils.*;

public class OperDAO {
	
	/**
	 * 把当前内存中才操作写进数据库
	 * 	1.如果当前表中包含有 数据<name:a,oper:add>
	 * 	2.那么如果当前操作是写数据<name:a,oper:delete>
	 * 	3.那么则不往数据库中写该数据，而是删除前一条数据<name:a,oper:add>
	 * 
	 * 2016年5月15日22:20:44 add by gk
	 * 应该加入事物保证一致性
	 * 
	 * @param entry
	 */
	public int insert(OperCacheEntry entry){
		return insert(entry.getName(),entry.getOper());
	}
	
	public int insert(String name,int oper){
		//TODO 应该加入事物保证一致性，防止删除了，而没有添加。
		if(oper == OperCacheEntry.DELETE && contain(new OperCacheEntry(name, OperCacheEntry.ADD))){
			delete(name, OperCacheEntry.ADD);
			return 1;
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement("insert into "+OperCacheEntry.DBTABLE_STRING+" values(?,?);");
			stmt.setString(1, name);
			stmt.setInt(2, oper);
			stmt.addBatch();

			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logInfo("Insert error");
		}finally{
			DBUtils.close(stmt, conn);	
		}
		
		return 0;
	}

	/**
	 * 查询 返回
	 * @param entry
	 * @return
	 */
	public boolean contain(OperCacheEntry entry){
		return contain(entry.getName(),entry.getOper());
	}
	
	public boolean contain(String name,int oper){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			stmt =  conn.prepareStatement("select * from "+OperCacheEntry.DBTABLE_STRING+" where "+OperCacheEntry.NAME_STRING+" = ? and "+OperCacheEntry.OPER_STRING+" = ?;");
			stmt.setString(1, name);
			stmt.setInt(2, oper);
			stmt.addBatch();
			rs =  stmt.executeQuery();
			if(rs.next()){
				return true;
			}
			return false;
		} catch (SQLException e) {
			logInfo("contain error");
		}finally{
			DBUtils.close(rs, stmt, conn);
		}
		return false;
	}
	
	public int delete(String name){
		//TODO数据库同步事务加入，这种删除不保证 数据库合法。
		return delete(name, OperCacheEntry.ADD) + delete(name,OperCacheEntry.DELETE);
	}
	
	public int delete(String name,int oper){
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement("delete from "+OperCacheEntry.DBTABLE_STRING+" where "+OperCacheEntry.NAME_STRING+" = ? and "+OperCacheEntry.OPER_STRING+" = ?");
			stmt.setString(1, name);
			stmt.setInt(2, oper);
			stmt.addBatch();
			return stmt.executeUpdate();
		} catch (SQLException e) {
			logInfo("delete error");
		}finally{
			DBUtils.close(stmt, conn);			
		}
		
		return 0;
	}
	
	public int delete(OperCacheEntry entry){
		return delete(entry.getName(),entry.getOper());
	}
	
	
	public List<OperCacheEntry> getAll(){
		List<OperCacheEntry> list = new ArrayList<OperCacheEntry>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.createStatement();
			rs =  stmt.executeQuery("select * from " + OperCacheEntry.DBTABLE_STRING);
			while(rs.next()){
				OperCacheEntry entry = 
						new OperCacheEntry(rs.getString(OperCacheEntry.NAME_STRING), rs.getInt(OperCacheEntry.OPER_STRING));
				list.add(entry);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally{
			DBUtils.close(rs, stmt, conn);
		}
		
		
		
		return list;
	}
	
	public int deleteAll() {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtils.getConnection();
			stmt = conn.createStatement();
			
			return stmt.executeUpdate("delete from " +OperCacheEntry.DBTABLE_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.close(stmt, conn);
		}
		return 0;
	}
	
	public int query(String name){
		if(contain(name, OperCacheEntry.ADD)) return OperCacheEntry.ADD;
		else return OperCacheEntry.DELETE;
	}

}
