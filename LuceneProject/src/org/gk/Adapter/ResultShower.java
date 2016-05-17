package org.gk.Adapter;

import java.util.Date;
import java.util.List;

import org.gk.Entries.ResultEntry;
import org.gk.Utils.ProjectUtils;

public class ResultShower {
	
	
	public static String showResult(List<ResultEntry> entries){
		
		StringBuffer sb = new StringBuffer();
		for(ResultEntry entry:entries){
			sb.append("<tr>");
				
			sb.append("<td>");
			sb.append(entry.getFileName());
			sb.append("</td>");
			
			
			sb.append("<td colspan='2'>");
			sb.append(entry.getFileContent());
			sb.append("</td>");
			
			
			sb.append("<td>");
			sb.append(entry.getFilePath());
			sb.append("</td>");
			
			sb.append("<td>");
			sb.append(ProjectUtils.simpleDateFormat.format(new Date(entry.getLastModilfy())));
			sb.append("</td>");
			
			sb.append("</tr>");
		}
		
		
		return sb.toString();
	}
	
}
