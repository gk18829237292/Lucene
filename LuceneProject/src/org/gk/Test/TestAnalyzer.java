package org.gk.Test;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.gk.Analyzer.MyAnalyzer;
import org.gk.Contenxt.MySameWordContext;
import org.gk.Utils.AnalyzerUtils;
import org.junit.Test;


public class TestAnalyzer {
	
	@Test
	public void test1(){
		Analyzer a2 = new MyAnalyzer(new MySameWordContext());
		String txt = "我你好啊";
		Directory dir = new RAMDirectory();
		IndexWriter writer;
		try {
			writer = new IndexWriter(dir,new IndexWriterConfig(Version.LUCENE_35, a2));
			Document doc = new Document();
			doc.add(new Field("content",txt,Field.Store.YES,Field.Index.ANALYZED));
			writer.addDocument(doc);
			writer.close();
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(dir));
			TopDocs tds = searcher.search(new TermQuery(new Term("content","咱")),10);
			for(ScoreDoc sd: tds.scoreDocs){
				Document doc1 = searcher.doc(sd.doc);
				System.out.println(doc1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
