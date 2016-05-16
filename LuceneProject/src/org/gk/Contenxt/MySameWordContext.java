package org.gk.Contenxt;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.gk.Utils.GKMap;

/**
 *  自定义的同义词读取
 *  	1.可以读自文件 
 *  		文件一行是一组同义词，词与词之间用逗号分隔
 *  	2.可以用户自定义
 * @author pc_home
 *
 */
public class MySameWordContext implements SameWordContenxt{
	
	private final static String sameWordPathString = "data\\SameWord";
	
	GKMap<String> map = new GKMap<String>();
	
	public MySameWordContext(){
		this(true);
	}
	
	/**
	 * 是否加载默认的同义词字典
	 * @param addDefaultSameWord
	 */
	public MySameWordContext(boolean addDefaultSameWord){
		if(addDefaultSameWord)
			addSameWordFromFile(sameWordPathString);
	}
	
	
	
	/**
	 * 注意文件格式
	 * @param filePath
	 */
	public void addSameWordFromFile(String filePath){
		try {
			List<String> list = FileUtils.readLines(new File(filePath));
			for(String str:list){
				map.add(str.split(","));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 从数组中加载
	 * @param sameWords
	 */
	public void addSameWordFromStrings(String[] sameWords){
		map.add(sameWords);
	}
	
	/**
	 * 从集合中加载
	 * @param c
	 */
	public void addSameWordFromStrings(Collection<String> c){
		map.add(c);
	}
	
	@Override
	public HashSet<String> getSameWords(String name) {
		return map.get(name);
	}
	
	
	@Override
	public String toString() {
		return map.toString();
	}
	
}
