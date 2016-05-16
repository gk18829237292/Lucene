package org.gk.Entries;

import java.util.ResourceBundle;

import net.didion.jwnl.util.ResourceBundleSet;

public final class EnvironmentEntry {

	/**
	 * 索引存储路径
	 */
	public static final String INDEXPATH;
	
	public static final String BOOK_DEFAULT_DIR;
	
	static{
		ResourceBundle bundle = new ResourceBundleSet("indexConfig");
		INDEXPATH = bundle.getString("index");
		BOOK_DEFAULT_DIR = bundle.getString("bookDir");
	}
	
	/**
	 * doc 域 文件内容
	 */
	public static final String FILECONTENT = "fileContent";

	/**
	 * doc 域 文件路径
	 */
	public static final String FILEPATH = "filePath";
	
	/**
	 * doc 域 文件名字
	 */
	public static final String FILENAME = "fileName";
	
	/**
	 * doc 域 文件最后修改时间
	 */
	public static final String FILELASTMODIFY = "fileLastModify";
}
