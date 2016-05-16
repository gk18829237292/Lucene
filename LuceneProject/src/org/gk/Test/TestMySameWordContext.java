package org.gk.Test;

import org.gk.Contenxt.MySameWordContext;
import org.junit.Test;

public class TestMySameWordContext {
	
	@Test
	public void testAdd(){
		MySameWordContext context = new MySameWordContext();
		context.addSameWordFromStrings(new String[]{"qw","12"});
		System.out.println(context);
	}
}
