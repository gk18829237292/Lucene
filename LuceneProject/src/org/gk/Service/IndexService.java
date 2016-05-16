package org.gk.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileName;
import org.gk.DAO.BookDAO;
import org.gk.DAO.OperDAO;
import org.gk.Entries.BookEntry;
import org.gk.Entries.EnvironmentEntry;
import org.gk.Entries.OperCacheEntry;
import org.gk.Exception.FileExistsEXception;
import org.gk.Utils.IndexUtils;

/**
 * 业务逻辑实现
 * @author pc_home
 *
 */
public class IndexService {
	
	IndexUtils iu = new IndexUtils();

	
	/**
	 * 索引重构
	 */
	public void makeFailedIndexAgain(){
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
	public void makeAllIndexAgain(){
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
	public void addBookNotRepleace(String filePath,boolean copyFileToWorkSpace) throws FileExistsEXception{
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
	public void addBookRepleace(String filePath){
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
	public void deleteBook(String filePath){
		iu.deleteIndex(filePath);
		
		File file = new File(EnvironmentEntry.BOOK_DEFAULT_DIR + "\\" + FilenameUtils.getName(filePath));
		//存在则删除，不用判断了。
		file.deleteOnExit();
	}
	
	
	public void updateBook(String oldFilePath,String newFilePath){
		iu.update(oldFilePath, newFilePath);
	}
	
	
}
