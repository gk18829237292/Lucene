package org.gk.Entries;

import java.io.Serializable;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.gk.Utils.ProjectUtils;

public class ResultEntry implements Serializable{
	
	/**
	 * 不太懂 慢慢学
	 */
	private static final long serialVersionUID = 1L;
	
	private String filePath ="";
	private String fileName ="";
	private String fileContent ="";
	private long lastModilfy= 0;
	
	public ResultEntry(String filePath, String fileName, long lastModilfy) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.lastModilfy = lastModilfy;
	}
	
	public ResultEntry(Document doc){
		this.filePath = doc.get(EnvironmentEntry.FILEPATH);
		this.fileName = doc.get(EnvironmentEntry.FILENAME);
		lastModilfy = Long.parseLong(doc.get(EnvironmentEntry.FILELASTMODIFY));
	}

	
	
	public String getFilePath() {
		return filePath;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLastModilfy() {
		return lastModilfy;
	}


	@Override
	public String toString() {
		return "ResultEntry [filePath=" + filePath + ", fileName=" + fileName
				+", fileContent=" + fileContent +", lastModilfy=" + ProjectUtils.simpleDateFormat.format(new Date(lastModilfy)) + "]";
	}
	
	
}
