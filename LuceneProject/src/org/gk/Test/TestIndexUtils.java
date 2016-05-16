package org.gk.Test;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.apache.sis.xml.XLink.Show;
import org.gk.DAO.OperDAO;
import org.gk.Entries.EnvironmentEntry;
import org.gk.Entries.OperCacheEntry;
import org.gk.Utils.IndexUtils;
import org.junit.After;
import org.junit.Test;
public class TestIndexUtils {
	


	
	@Test
	public void testMakeIndex(){
		IndexUtils iu = new IndexUtils();
		
		iu.makeAllIndex("F:\\my_paper\\别人家的论文\\test\\1.pdf",
				"F:\\my_paper\\别人家的论文\\test\\4.pdf",
				"F:\\my_paper\\别人家的论文\\test\\5.pdf",
				"F:\\my_paper\\别人家的论文\\test\\6.pdf"
				);
		
		iu.commit();
	}
	
	@After
	public void Show(){
		OperDAO oper = new OperDAO();
		List<OperCacheEntry> list = oper.getAll();
		System.out.println("共有数据：" + list.size());
		int i = 1;
		for(OperCacheEntry entry:list){
			System.out.println("第"+i+"条数据 :"+entry);
			i++;
		}
	}
	
	@Test
	public void testSearch(){
		IndexUtils iu = new IndexUtils();
		System.out.println(121212);
		QueryParser queryParser = new QueryParser(Version.LUCENE_35, "", iu.getMyAnalyzer());
		iu.search(new TermQuery(new Term(EnvironmentEntry.FILENAME,"4")), 10);
	}
	
	@Test
	public void testSerachWithHighLigter(){
		IndexUtils iu = new IndexUtils();
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_35, new String[]{EnvironmentEntry.FILENAME,EnvironmentEntry.FILECONTENT}, iu.getMyAnalyzer());
		
		try {
			iu.searchWithHighLigter(queryParser.parse("日本"), 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
