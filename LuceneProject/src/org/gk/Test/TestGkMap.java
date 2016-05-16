package org.gk.Test;

import org.gk.Utils.GKMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TestGkMap {
	
	GKMap<String> map = new GKMap<String>();
	@Before
	public void doit(){
		map.add(new String[]{"32","12"});
		map.add(new String[]{"12"});		
	}
	
	@Test
	public void testAdd(){
		System.out.println(map);
	}
	
	@Test
	public void testGet(){

		System.out.println(map.get("12"));
	}
	
	@Test
	public void testDelete(){
		try {
			map.delete("12");
			System.out.println(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
