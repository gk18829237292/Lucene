package org.gk.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class GKMap<T> {
	
	private HashMap<T, HashSet<T>> map = new HashMap<T, HashSet<T>>();
	private List<HashSet<T>> list = new ArrayList<HashSet<T>>();
	
	
	public void add(T[] c){
		if(c == null || c.length < 2) return;
		add(Arrays.asList(c));
	}
	
	public void add(Collection<T> c){
		if(c == null || c.size() < 2) return;
		HashSet<T> temp = new HashSet<T>();
		T t1 = null;
		for(T t:c){
			if(map.containsKey(t)){
				t1 = t;
			}
			//如果该词没有在 Collection中，那么便加入到 temp中。
			temp.add(t);
		}
		if(t1 == null){
			HashSet<T> set1 = new HashSet<T>();
			map.put(t1, set1);
			list.add(set1);
		}
		
		//获取存储同义词的Set
		HashSet<T> temp1 = map.get(t1);
		temp1.addAll(c);
		for(T t:temp){
			map.put(t, temp1);
		}
//		map.put(t1,map.get(t1).addAll(temp));
	}
	
	public HashSet<T> get(T key){
		if(!map.containsKey(key)) return null;
		HashSet<T> set = (HashSet<T>) map.get(key).clone();
		set.remove(key);
		return set;
	}
	
	public void clear(){
		map.clear();
		list.clear();
	}
	
	public void delete(T key) throws Exception{
		if(!map.containsKey(key))
			throw new Exception("Can't find the Key");
		HashSet<T> set = map.get(key);
		set.remove(key);
		map.remove(key);
		
	}
	
	@Override
	public String toString() {
		
//		map.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		for(HashSet<T> set:list){
			sb.append(set);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public int getGroup(){
		return list.size();
	}
}	
