package org.gk.Analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.gk.Contenxt.SameWordContenxt;
import org.gk.TokenFilter.MyTokenFilter;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import com.sun.xml.internal.bind.CycleRecoverable.Context;

public class MyAnalyzer extends Analyzer{

	private SameWordContenxt context = null;
	
	public MyAnalyzer(SameWordContenxt contenxt) {
		super();
		this.context = contenxt;
	}



	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		Dictionary dic = Dictionary.getInstance("");
		return new MyTokenFilter(
				new MMSegTokenizer(new MaxWordSeg(dic), reader),context);
	}
	
	
}
