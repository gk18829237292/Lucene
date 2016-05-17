package org.gk.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileName;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.gk.DAO.BookDAO;
import org.gk.DAO.OperDAO;
import org.gk.Entries.BookEntry;
import org.gk.Entries.EnvironmentEntry;
import org.gk.Entries.OperCacheEntry;
import org.gk.Entries.ResultEntry;
import org.gk.Exception.FileExistsEXception;
import org.gk.Utils.IndexUtils;
import org.quartz.ListenerManager;

/**
 * 业务逻辑实现
 * @author pc_home
 *
 */
public class IndexService {
	
	private static IndexUtils iu = new IndexUtils();

	
	/**
	 * 索引重构
	 */
	public static void makeFailedIndexAgain(){
		OperDAO operDAO = new OperDAO();
		List<OperCacheEntry> list = operDAO.getAll();
		for(OperCacheEntry entry:list){
			if(entry.getOper() == OperCacheEntry.ADD)
				iu.makeAllIndex(entry.getName());
			else 
				iu.deleteIndex(entry.getName());
		}
		iu.commit();
	}

	/**
	 * 索引重构，先删除所有索引
	 */
	public static void makeAllIndexAgain(){
		iu.deleteAll();
		iu.commit();
		BookDAO bookDAO = new BookDAO();
		List<BookEntry> list =  bookDAO.getAll();
		for(BookEntry entry:list){
			iu.makeAllIndex(entry.getBookName());
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @param copyFileToWorkSpace 是否把文件Copy到默认目录
	 * @throws FileExistsEXception 文件已经存在
	 */
	public static void addBookNotRepleace(String filePath,boolean copyFileToWorkSpace) throws FileExistsEXception{
		if(!copyFileToWorkSpace){
			iu.makeAllIndex(filePath);
			return;
		}
		File desFile = new File(EnvironmentEntry.BOOK_DEFAULT_DIR + "\\" + FilenameUtils.getName(filePath));
		String desFilePath = desFile.getAbsolutePath();
		System.out.println(desFilePath);
		if(desFile.exists()){
			throw new FileExistsEXception();
		}
		try {
			FileUtils.copyFile(new File(filePath), desFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		iu.makeAllIndex(desFilePath);
	}
	
	/**
	 * 在更新的时候 使用
	 * @param filePath
	 */
	public static void addBookRepleace(String filePath){
		File desFile = new File(EnvironmentEntry.BOOK_DEFAULT_DIR + "\\" + FilenameUtils.getName(filePath));
		try {
			FileUtils.copyFile(new File(filePath), desFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		iu.makeAllIndex(desFile.getAbsolutePath());
	}
	
	/**
	 * 如果是默认目录下的书，那么书也删除
	 * @param filePath
	 */
	public static void deleteBook(String filePath){
		iu.deleteIndex(filePath);
		
		File file = new File(EnvironmentEntry.BOOK_DEFAULT_DIR + "\\" + FilenameUtils.getName(filePath));
		//存在则删除，不用判断了。
		file.deleteOnExit();
	}
	
	
	public static void updateBook(String oldFilePath,String newFilePath){
		iu.update(oldFilePath, newFilePath);
	}
	
	/**
	 * 默认queryParse
	 * @param str
	 * @return
	 */
	public static List<ResultEntry> queryBooks(String str){
//		QueryParser parser = new QueryParser(Version.LUCENE_35, "", a)
//		IndexUtils iu1 = new IndexUtils();
		
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_35, new String[]{EnvironmentEntry.FILENAME,EnvironmentEntry.FILECONTENT}, iu.getMyAnalyzer());
		
		try {
			return iu.searchWithHighLigter(queryParser.parse(str), 10);
		} catch (ParseException e) {
			System.out.println("queryBooks error");
			e.printStackTrace();
		}
		return null;
	}
	
	
}
