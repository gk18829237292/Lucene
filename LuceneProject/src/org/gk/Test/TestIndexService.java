package org.gk.Test;

import org.gk.Exception.FileExistsEXception;
import org.gk.Service.IndexService;
import org.junit.Test;

public class TestIndexService {
	
	@Test
	public void testAddBook() throws FileExistsEXception{
		IndexService service = new IndexService();
		service.addBookNotRepleace("D:\\sqsxfree_0.4_beta11.5.zip", true);
	}
	
	
	@Test
	public void testDeleteBook(){
		IndexService service = new IndexService();
		service.deleteBook("D:\\my_eclipse\\my_javaWEB1\\LuceneProject\\books\\sqsxfree_0.4_beta11.5.zip");
	}
}
