package org.gk.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.gk.Analyzer.MyAnalyzer;
import org.gk.Contenxt.MySameWordContext;
import org.gk.DAO.BookDAO;
import org.gk.DAO.OperDAO;
import org.gk.Entries.EnvironmentEntry;
import org.gk.Entries.OperCacheEntry;
import org.gk.Entries.ResultEntry;

import static org.gk.Utils.ProjectUtils.*;



public class IndexUtils {
	
	private static final String TAG ="IndexUtils";
	private SearcherManager mgr = null;
	private NRTManager nrtMgr = null;
	private IndexWriter writer = null;
	private Directory dir = null;
	private IndexWriterConfig iwc = null;
	
	private Tika tika = null;
	
	private static Analyzer analyzer = new MyAnalyzer(new MySameWordContext());
	
	private boolean debug = true;
	
	private OperDAO operDAO;
	
	private BookDAO bookDAO;
	
	public IndexUtils() {
		try {
			operDAO = new OperDAO();
			bookDAO = new BookDAO();
			dir = FSDirectory.open(new File(EnvironmentEntry.INDEXPATH));
			tika = new Tika();
			iwc = new IndexWriterConfig(Version.LUCENE_35, getMyAnalyzer());
			writer = new IndexWriter(dir, iwc);
			System.out.println(12);
			nrtMgr = new NRTManager(writer,new SearcherWarmer() {  
				
				public void warm(IndexSearcher s) throws IOException {
					logInfo("reopen()");
					commit();
				}
			});
			
			//启动NRTManager的Reopen线程
			NRTManagerReopenThread reopen = new NRTManagerReopenThread(nrtMgr, 5.0,0.025);
			reopen.setDaemon(true);
			reopen.setName("NrtManager Reopen Thread");
			reopen.start();
			mgr = nrtMgr.getSearcherManager(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2016年5月14日00:35:18
	 * 暂时使用标准分词器
	 * 2016年5月15日01:26:50
	 * 修改使用自定义分词器《支持中文分词》
	 */
	public Analyzer getMyAnalyzer(){
		return analyzer;
	}
	
	
	/**
	 * 添加文件到索引
	 * 	传入的可以是文件名，或者是文件目录，如果是文件目录，则去一直遍历文件目录
	 * @param filePaths
	 */
	public void makeAllIndex(String...filePaths){
		Path filePath = null;
		for(String path:filePaths){
			filePath = Paths.get(path);
			if(Files.isDirectory(filePath)){
				try {
					Files.walkFileTree(filePath, new SimpleFileVisitor<Path>(){
						@Override
						public FileVisitResult visitFile(Path file,
								BasicFileAttributes attrs) throws IOException {
							makeIndex(file.toAbsolutePath().toString());
							return FileVisitResult.CONTINUE;
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
//				logInfo(path);
				makeIndex( path);
			}
		}
	}
	
	
	/**
	 * 1.把文件的
	 * 			路径				filePath
	 * 			文件名			fileName
	 * 			文件内容			fileContent
	 * 			最后修改的时间		fileLastModlify
	 * 			读取出来。
	 * 2.因为 文件格式等原因 有好多无法识别的 所以切记设置单词最短长度。也就是LengthFilter.
	 * 
	 * @param filePath 传入的文件路径
	 */
	private void makeIndex(String filePath){
		try {
		
			String content = tika.parseToString(new File(filePath));
			
			Document doc = new Document();
			//添加文件路径 ，不分词，也不进行评分
			doc.add(new Field(EnvironmentEntry.FILEPATH, filePath, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			//添加文件名称
			doc.add(new Field(EnvironmentEntry.FILENAME,FilenameUtils.getName(filePath),Field.Store.YES,Field.Index.ANALYZED));
			//添加文件内容
			doc.add(new Field(EnvironmentEntry.FILECONTENT, content, Field.Store.NO, Field.Index.ANALYZED));
			//添加最后修改的时间
			doc.add(new NumericField(EnvironmentEntry.FILELASTMODIFY,Field.Store.YES,true).setLongValue(Files.getLastModifiedTime(Paths.get(filePath)).toMillis()));
			logInfo("Update :" + filePath);
			nrtMgr.addDocument(doc);
			//TODO 这里添加数据库添加操作，同步
			operDAO.insert(filePath, OperCacheEntry.ADD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deleteIndex(String filePath){
		try {
			logInfo("delete :" + filePath);
			nrtMgr.deleteDocuments(new Term(EnvironmentEntry.FILEPATH, filePath));
			//TODO 这里添加数据库添加操作，同步
			operDAO.insert(filePath, OperCacheEntry.DELETE);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void deleteAll(){
		try {
			logInfo("delete All");
			nrtMgr.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(String oldFilePath,String newFilePath){
		try {
			
			String content = tika.parseToString(new File(newFilePath));
			
			Document doc = new Document();
			//添加文件路径 ，不分词，也不进行评分
			doc.add(new Field(EnvironmentEntry.FILEPATH, newFilePath, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			//添加文件名称
			doc.add(new Field(EnvironmentEntry.FILENAME,FilenameUtils.getName(newFilePath),Field.Store.YES,Field.Index.ANALYZED));
			//添加文件内容
			doc.add(new Field(EnvironmentEntry.FILECONTENT, content, Field.Store.NO, Field.Index.ANALYZED));
			//添加最后修改的时间
			doc.add(new NumericField(EnvironmentEntry.FILELASTMODIFY,Field.Store.YES,true).setLongValue(Files.getLastModifiedTime(Paths.get(newFilePath)).toMillis()));
			logInfo("Update from: " + oldFilePath + " to : " + newFilePath);
			nrtMgr.updateDocument(new Term(EnvironmentEntry.FILEPATH, oldFilePath), doc);
			//TODO 这里添加数据库添加操作，同步
			operDAO.insert(oldFilePath, OperCacheEntry.DELETE);
			operDAO.insert(newFilePath, OperCacheEntry.ADD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 2016年5月15日02:38:22
	 * 排序还没有实现
	 * @param query
	 * @param num
	 * @return 查找结果
	 */
	public List<ResultEntry> search(Query query,int num){
		List<ResultEntry> results = new ArrayList<ResultEntry>();
		IndexSearcher searcher = mgr.acquire();
		try {
			TopDocs tds = searcher.search(query, num);
			logInfo(tds.scoreDocs.length+"");
			for(ScoreDoc sd:tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				results.add(new ResultEntry(doc));
				logInfo(new ResultEntry(doc).toString());
			}
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				mgr.release(searcher);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/*
	 * @param query
	 * @param num
	 * @return 查找结果
	 */
	public List<ResultEntry> searchWithHighLigter(Query query,int num){
		List<ResultEntry> results = new ArrayList<ResultEntry>();
		IndexSearcher searcher = mgr.acquire();
		try {
			TopDocs tds = searcher.search(query, num);
			logInfo(tds.scoreDocs.length+"");
			for(ScoreDoc sd:tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				ResultEntry entry = new ResultEntry(doc);
				
//				makeHighLight(query, fieldName, text)
				entry.setFileName(makeHighLight(query, EnvironmentEntry.FILENAME, entry.getFileName()));
				try {
					entry.setFileContent(makeHighLight(query, EnvironmentEntry.FILECONTENT, tika.parseToString(Paths.get(entry.getFilePath()))));
				} catch (TikaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				entry.setFileName(high);
				results.add(entry);
				logInfo(entry.toString());
				logInfo("");
				logInfo("***********************************");
			}
			return results;
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				mgr.release(searcher);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String makeHighLight(Query query,String fieldName,String text){
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b color =red>", "</b>");
		Highlighter highlighter = new Highlighter(formatter,scorer);
		
		highlighter.setTextFragmenter(fragmenter);
		
		String str = null;
		try {
			str = highlighter.getBestFragment(getMyAnalyzer(), fieldName, text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(str==null){
			if(text.length() < 150)
				str = text;
			else 
				str = text.substring(0, 150);
		}
		return str;
	}
	
	
	
	
	public void commit(){
		try {
			writer.commit();
			List<OperCacheEntry> list =  operDAO.getAll();
			operDAO.deleteAll();
			for(OperCacheEntry entry:list){
				if(entry.getOper() == OperCacheEntry.ADD)
					bookDAO.insert(entry.getName());
				else
					bookDAO.delete(entry.getName());
			}
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
