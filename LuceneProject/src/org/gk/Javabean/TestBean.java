package org.gk.Javabean;

public class TestBean {
	private String test ="";
	
	private static int id = 0;
	
	
	public TestBean() {
		super();
		System.out.println("it's me  :" + id);
		id++;
	}

	public String getTest() {
		
		return test;
	}

	public void setTest(String test) {
		this.test = test;
		System.out.println("test :" + test);
	}
	
	
}
